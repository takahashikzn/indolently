// Copyright 2026 takahashikzn
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package jp.root42.indolently.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


public class AsyncIOTest {

    @Test(timeout = 5000)
    public void callerInterruptionStopsPumpAndPreservesInterrupt() throws Exception {
        final var ex = new BlockedExchange(false);
        final var call = new TransferCall(ex, (total, idle) -> false, 5000);

        try {
            ex.awaitRead();
            call.thread.interrupt();
            call.awaitCancellation();

            assertThat(call.interruptPreserved.get()).as("caller interrupt flag was cleared").isTrue();
            assertThat(ex.interrupted.await(1, TimeUnit.SECONDS)).as("pump did not receive the interrupt").isTrue();
            ex.joinPump();
            assertThat(ex.finishes.get()).isZero();
        } finally {
            ex.release.countDown();
            call.close();
            ex.joinPump();
        }
    }

    @Test(timeout = 5000)
    public void idleTimeoutRetainsPermitUntilBlockedPumpExits() throws Exception {
        assertPermitHeldUntilPumpExits(false);
    }

    @Test(timeout = 5000)
    public void cancellationRetainsPermitUntilBlockedPumpExits() throws Exception {
        assertPermitHeldUntilPumpExits(true);
    }

    @Test(timeout = 5000)
    public void cancelledReadReachingEofDoesNotFinish() throws Exception {
        final var cancelled = new AtomicBoolean();
        final var ex = new BlockedExchange(true);
        final var call = new TransferCall(ex, (total, idle) -> cancelled.get(), 5000);

        try {
            ex.awaitRead();
            cancelled.set(true);
            call.awaitCancellation();
        } finally {
            ex.release.countDown();
            call.close();
            ex.joinPump();
        }

        assertThat(ex.finishes.get()).as("EOF after cancellation must not finish the response").isZero();
    }

    private static void assertPermitHeldUntilPumpExits(final boolean explicitCancellation) throws Exception {
        final var sem = semaphore();
        final var permits = sem.availablePermits();
        final var cancelled = new AtomicBoolean();
        final var ex = new BlockedExchange(true);
        final var call = new TransferCall(ex, (total, idle) -> cancelled.get(), explicitCancellation ? 5000 : 100);

        try {
            ex.awaitRead();
            if (explicitCancellation) cancelled.set(true);
            call.awaitCancellation();

            assertThat(ex.interrupted.await(1, TimeUnit.SECONDS)).as("pump did not receive the interrupt").isTrue();
            assertThat(ex.pump.get().isAlive()).as("fixture must remain blocked after interrupt").isTrue();
            assertThat(sem.availablePermits()).as("an active pump must keep its permit").isEqualTo(permits - 1);
        } finally {
            ex.release.countDown();
            call.close();
            ex.joinPump();
        }

        assertThat(sem.availablePermits()).as("permit was not restored after pump exited").isEqualTo(permits);
        assertThat(ex.finishes.get()).as("EOF after cancellation must not finish the response").isZero();
    }

    @Test(timeout = 5000)
    public void alreadyCancelledTransferDoesNotTouchExchange() {
        final var ex = new CountingExchange();

        assertThatThrownBy(() -> AsyncIO.transfer(ex, (total, idle) -> true, 1000, 5000))
            .isInstanceOf(CancellationException.class);

        ex.assertUntouched();
    }

    @Test(timeout = 5000)
    public void cancellationWhileWaitingForPermitIsPrompt() throws Exception {
        assertCancelledWhileWaitingForPermit(30000);
    }

    @Test(timeout = 5000)
    public void disabledIdleTimeoutStillAllowsCancellationWhileWaitingForPermit() throws Exception {
        assertCancelledWhileWaitingForPermit(-1);
    }

    private static void assertCancelledWhileWaitingForPermit(final long timeoutMs) throws Exception {
        final var sem = semaphore();
        final var permits = sem.availablePermits();
        assertThat(permits).as("previous transfer leaked all permits").isPositive();
        sem.acquire(permits);

        final var cancelled = new AtomicBoolean();
        final var ex = new CountingExchange();
        final var call = new TransferCall(ex, (total, idle) -> cancelled.get(), timeoutMs);

        try {
            final var deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(1);
            while (!sem.hasQueuedThreads() && System.nanoTime() < deadline) Thread.sleep(1);
            assertThat(sem.hasQueuedThreads()).as("transfer did not wait for a permit").isTrue();

            cancelled.set(true);
            call.awaitCancellation();
            ex.assertUntouched();
            assertThat(sem.availablePermits()).isZero();
        } finally {
            sem.release(permits);
            call.close();
        }

        assertThat(sem.availablePermits()).as("queued cancellation changed permit count").isEqualTo(permits);
    }

    @Test(timeout = 5000)
    public void successfulTransferCopiesAllBytesAndFlushes() throws Exception {
        final var bytes = new byte[600_000];
        for (var i = 0; i < bytes.length; i++) bytes[i] = (byte) (i * 31);
        final var flushes = new AtomicInteger();
        final var out = new ByteArrayOutputStream() {

            @Override
            public void flush() { flushes.incrementAndGet(); }
        };

        AsyncIO.transfer(new ByteArrayInputStream(bytes), out, () -> false);

        assertThat(out.toByteArray()).containsExactly(bytes);
        assertThat(flushes.get()).isEqualTo(1);
    }

    @Test(timeout = 5000)
    public void ioFailuresFromReadWriteAndFinishArePropagated() throws Exception {
        final var sem = semaphore();
        final var permits = sem.availablePermits();

        for (var step = 0; step < 3; step++) {
            final var failingStep = step;
            final var failure = new IOException("step " + step);
            final var ex = new AsyncIO.IOExchange() {

                private boolean read;

                @Override
                public int read() throws IOException {
                    if (failingStep == 0) throw failure;
                    if (this.read) return -1;
                    this.read = true;
                    return 1;
                }

                @Override
                public void write() throws IOException {
                    if (failingStep == 1) throw failure;
                }

                @Override
                public void finish() throws IOException {
                    if (failingStep == 2) throw failure;
                }
            };

            assertThatThrownBy(() -> AsyncIO.transfer(ex, () -> false)).isSameAs(failure);
            assertThat(sem.availablePermits()).as("failed pump leaked a permit").isEqualTo(permits);
        }
    }

    private static Semaphore semaphore() throws ReflectiveOperationException {
        final var field = AsyncIO.class.getDeclaredField("sem");
        field.setAccessible(true);
        return (Semaphore) field.get(null);
    }

    private static final class TransferCall
        implements AutoCloseable {

        private final AtomicReference<Throwable> failure = new AtomicReference<>();

        private final AtomicBoolean interruptPreserved = new AtomicBoolean();

        private final CountDownLatch returned = new CountDownLatch(1);

        private final Thread thread;

        private TransferCall(final AsyncIO.IOExchange ex, final BiPredicate<Long, Long> cancelled, final long timeoutMs) {
            this.thread = Thread.ofVirtual().start(() -> {
                try { AsyncIO.transfer(ex, cancelled, 5, timeoutMs); } catch (Throwable t) { this.failure.set(t); } finally {
                    this.interruptPreserved.set(Thread.currentThread().isInterrupted());
                    this.returned.countDown();
                }
            });
        }

        private void awaitCancellation() throws InterruptedException {
            assertThat(this.returned.await(1, TimeUnit.SECONDS)).as("transfer did not return promptly").isTrue();
            assertThat(this.failure.get()).isInstanceOf(CancellationException.class);
        }

        @Override
        public void close() throws InterruptedException {
            this.thread.interrupt();
            this.thread.join(1000);
            assertThat(this.thread.isAlive()).as("caller cleanup failed").isFalse();
        }
    }

    private static final class BlockedExchange
        implements AsyncIO.IOExchange {

        private final boolean ignoreInterrupt;

        private final CountDownLatch entered = new CountDownLatch(1);

        private final CountDownLatch interrupted = new CountDownLatch(1);

        private final CountDownLatch release = new CountDownLatch(1);

        private final AtomicReference<Thread> pump = new AtomicReference<>();

        private final AtomicInteger finishes = new AtomicInteger();

        private BlockedExchange(final boolean ignoreInterrupt) { this.ignoreInterrupt = ignoreInterrupt; }

        @Override
        public int read() throws IOException {
            this.pump.set(Thread.currentThread());
            this.entered.countDown();
            final var deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);

            while (true) {
                try {
                    final var remaining = deadline - System.nanoTime();
                    if (remaining <= 0 || !this.release.await(remaining, TimeUnit.NANOSECONDS)) throw new IOException("test cleanup deadline");
                    return -1;
                } catch (InterruptedException e) {
                    this.interrupted.countDown();
                    if (!this.ignoreInterrupt) throw new IOException(e);
                }
            }
        }

        @Override
        public void write() { fail("EOF must not be written"); }

        @Override
        public void finish() { this.finishes.incrementAndGet(); }

        private void awaitRead() throws InterruptedException {
            assertThat(this.entered.await(1, TimeUnit.SECONDS)).as("pump did not start").isTrue();
        }

        private void joinPump() throws InterruptedException {
            final var thread = this.pump.get();
            if (thread == null) return;
            thread.join(1000);
            assertThat(thread.isAlive()).as("pump cleanup failed").isFalse();
        }
    }

    private static final class CountingExchange
        implements AsyncIO.IOExchange {

        private final AtomicInteger reads = new AtomicInteger();

        private final AtomicInteger writes = new AtomicInteger();

        private final AtomicInteger finishes = new AtomicInteger();

        @Override
        public int read() { return this.reads.getAndIncrement() == 0 ? 1 : -1; }

        @Override
        public void write() { this.writes.incrementAndGet(); }

        @Override
        public void finish() { this.finishes.incrementAndGet(); }

        private void assertUntouched() {
            assertThat(this.reads.get()).isZero();
            assertThat(this.writes.get()).isZero();
            assertThat(this.finishes.get()).isZero();
        }
    }
}
