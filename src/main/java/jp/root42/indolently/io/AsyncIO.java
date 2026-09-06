// Copyright 2025 takahashikzn
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;

import static java.lang.System.nanoTime;


/**
 * @author takahashikzn
 */
public final class AsyncIO {

    private AsyncIO() { }

    public static void transfer(final InputStream in, final OutputStream out, final BooleanSupplier cancelled)
        throws IOException, CancellationException { transfer(IOExchange.of(in, out), cancelled); }

    public static void transfer(final InputStream in, final OutputStream out, final BiPredicate<Long, Long> cancelled)
        throws IOException, CancellationException { transfer(IOExchange.of(in, out), cancelled); }

    public static void transfer(final InputStream in, final OutputStream out, final BiPredicate<Long, Long> cancelled, final int pollIntervalMs,
        final long ioTimeoutMs) throws IOException, CancellationException { transfer(IOExchange.of(in, out), cancelled, pollIntervalMs, ioTimeoutMs); }

    public interface IOExchange {

        int read() throws IOException;

        void write() throws IOException;

        void finish() throws IOException;

        static IOExchange of(final InputStream in, final OutputStream out) {
            return new IOExchange() {

                private int len;

                private final byte[] buf = new byte[1024 * 256];

                @Override
                public int read() throws IOException { return this.len = in.read(this.buf); }

                @Override
                public void write() throws IOException { out.write(this.buf, 0, this.len); }

                @Override
                public void finish() throws IOException { out.flush(); }
            };
        }
    }

    public static void transfer(final IOExchange ex, final BooleanSupplier cancelled) throws IOException, CancellationException {
        transfer(ex, (_1, _2) -> cancelled.getAsBoolean(), 25, 5000);
    }

    public static void transfer(final IOExchange ex, final BiPredicate<Long, Long> cancelled) throws IOException, CancellationException {
        transfer(ex, cancelled, 25, 5000);
    }

    private static final Semaphore sem = new Semaphore(2048, true);

    /** Cancellation interrupts pending I/O; callers retain ownership of streams and any required transport abort. */
    public static void transfer(final IOExchange ex, final BiPredicate<Long, Long> cancelled, final int pollIntervalMs, final long ioTimeoutMs)
        throws IOException, CancellationException {
        run(new Transfer(ex, cancelled, pollIntervalMs, ioTimeoutMs));
    }

    private static final class Transfer {

        private final IOExchange exchange;

        private final BiPredicate<Long, Long> cancelled;

        private final Duration pollingInterval;

        private final long ioTimeoutNano;

        private final long started = nanoTime();

        private final AtomicLong progress = new AtomicLong(this.started);

        private final AtomicReference<Throwable> failure = new AtomicReference<>();

        private Transfer(final IOExchange exchange, final BiPredicate<Long, Long> cancelled, final int pollIntervalMs, final long ioTimeoutMs) {
            this.exchange = exchange;
            this.cancelled = cancelled;
            this.pollingInterval = Duration.ofMillis(Math.max(1, pollIntervalMs));
            this.ioTimeoutNano = ioTimeoutMs < 0 ? Long.MAX_VALUE : TimeUnit.MILLISECONDS.toNanos(ioTimeoutMs);
        }
    }

    private static void run(final Transfer state) throws IOException {

        Thread pump = null;
        try {
            acquire(state);
            pump = startPump(state);
            await(state, pump);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            final var interrupted = new CancellationException("interrupted");
            interrupted.initCause(e);
            state.failure.compareAndSet(null, interrupted);
            throw interrupted;
        } catch (IOException | RuntimeException | Error e) {
            state.failure.compareAndSet(null, e);
            throw e;
        } finally {
            if (pump != null && pump.isAlive()) {
                // Publish the stop before waking I/O that may return EOF on interruption.
                state.failure.compareAndSet(null, new CancellationException("transfer stopped"));
                pump.interrupt();
            }
        }
    }

    private static void acquire(final Transfer state) throws InterruptedException {

        final var queued = nanoTime();
        while (true) {
            checkCancelled(state);
            final long remaining = Math.max(0L, state.ioTimeoutNano - (nanoTime() - queued));
            final long wait = Math.min(state.pollingInterval.toNanos(), remaining);
            if (sem.tryAcquire(wait, TimeUnit.NANOSECONDS)) return;
            if (remaining == 0 || state.ioTimeoutNano <= nanoTime() - queued) //
                throw new CancellationException("io pool saturated (semaphore timeout)");
        }
    }

    private static Thread startPump(final Transfer state) throws InterruptedException {

        var started = false;
        try {
            checkCancelled(state);
            state.progress.set(nanoTime());
            final var pump = Thread.ofVirtual().name("async-io-", 0).unstarted(() -> copy(state));
            pump.start();
            started = true;
            return pump;
        } finally {
            // Once started, only the pump may release its permit.
            if (!started) sem.release();
        }
    }

    private static void copy(final Transfer state) {

        try {
            while (true) {
                checkRunning(state);
                final int n = state.exchange.read();
                checkRunning(state);
                if (n < 0) break;
                if (n > 0) {
                    state.progress.set(nanoTime());
                    checkRunning(state);
                    state.exchange.write();
                    state.progress.set(nanoTime());
                }
            }

            checkRunning(state);
            state.exchange.finish();
        } catch (Throwable t) {
            state.failure.compareAndSet(null, t);
        } finally { sem.release(); }
    }

    private static void await(final Transfer state, final Thread pump) throws IOException, InterruptedException {

        while (!pump.join(state.pollingInterval)) {
            checkCancelled(state);
            raise(state.failure);
            final long lastProgress = state.progress.get();
            if (state.ioTimeoutNano < nanoTime() - lastProgress) //
                throw new CancellationException("io timeout (no progress)");
        }

        checkCancelled(state);
        raise(state.failure);
    }

    private static void checkCancelled(final Transfer state) throws InterruptedException {

        if (Thread.currentThread().isInterrupted()) throw new InterruptedException();

        final var lastProgress = state.progress.get();
        final var now = nanoTime();
        if (state.cancelled.test(now - state.started, now - lastProgress)) throw new CancellationException("cancel requested");
    }

    private static void checkRunning(final Transfer state) throws IOException {
        raise(state.failure);
        if (Thread.currentThread().isInterrupted()) throw new CancellationException("interrupted");
    }

    private static void raise(final AtomicReference<Throwable> failure) throws IOException {

        switch (failure.get()) {
            case RuntimeException e when e.getCause() instanceof IOException io -> throw io;
            case RuntimeException e when e.getCause() instanceof CancellationException co -> throw co;
            case RuntimeException e -> throw e;
            case IOException e -> throw e;
            case Error e -> throw e;
            case null -> { }
            default -> throw new IOException(failure.get());
        }
    }
}
