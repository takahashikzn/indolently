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
package jp.root42.indolently.conc;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface PrioritySemaphore {

    int drainPermits();

    int availablePermits();

    default void acquire() throws InterruptedException { this.acquire(Long.MAX_VALUE); }

    int NO_NICE = 0;

    default boolean tryAcquire() {
        try { return this.acquire(0L); } //
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    default boolean tryAcquire(final int nice) {
        try { return this.acquire(nice, 0L); } //
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    default boolean acquire(final long timeoutMs) throws InterruptedException { return this.acquire(NO_NICE, timeoutMs); }

    default void acquire(final int nice) throws InterruptedException { this.acquire(nice, Long.MAX_VALUE); }

    boolean acquire(int nice, long timeoutMs) throws InterruptedException;

    interface _Permit
        extends AutoCloseable {

        @Override
        void close();
    }

    default _Permit lease() throws InterruptedException {
        this.acquire();
        return this::release;
    }

    default $<_Permit> lease(final long timeoutMs) throws InterruptedException { return this.acquire(timeoutMs) ? just(this::release) : none(); }

    default _Permit lease(final int nice) throws InterruptedException {
        this.acquire(nice);
        return this::release;
    }

    default $<_Permit> lease(final int nice, final long timeoutMs) throws InterruptedException {
        return this.acquire(nice, timeoutMs) ? just(this::release) : none();
    }

    default $<_Permit> tryLease() { return this.tryAcquire() ? just(this::release) : none(); }

    default $<_Permit> tryLease(final int nice) { return this.tryAcquire(nice) ? just(this::release) : none(); }

    void release();

    void release(int permits);

    static PrioritySemaphore of(final int capacity) { return of(capacity, 50); }

    static PrioritySemaphore of(final int capacity, final long intervalMs) {

        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        if (intervalMs <= 0) throw new IllegalArgumentException("interval must be positive");

        return new PrioritySemaphore() {

            private final Semaphore sem = new Semaphore(capacity, true);

            private final long intervalNs = TimeUnit.MILLISECONDS.toNanos(intervalMs);

            private final BlockingQueue<Ticket> waitingThreads = new PriorityBlockingQueue<>();

            @Override
            public int drainPermits() { return this.sem.drainPermits(); }

            @Override
            public int availablePermits() { return this.sem.availablePermits(); }

            @Override
            public void acquire() throws InterruptedException { this.sem.acquire(); }

            @Override
            public boolean tryAcquire() { return this.sem.tryAcquire(); }

            @Override
            public boolean acquire(final long timeoutMs) throws InterruptedException { return this.sem.tryAcquire(timeoutMs, TimeUnit.MILLISECONDS); }

            private static final long FOREVER = Long.MAX_VALUE;

            @Override
            public void acquire(final int nice) throws InterruptedException { this.acquire(nice, FOREVER); }

            private final AtomicLong ticketSeq = new AtomicLong();

            private long nextTicketSeq() {
                final var seq = this.ticketSeq.getAndIncrement();
                if (seq < 0) throw new AssertionError("overflow");
                return seq;
            }

            private record Ticket(long seq, int nice)
                implements Comparable<Ticket> {

                @Override
                public int compareTo(final Ticket that) { return Comparator.comparingInt(Ticket::nice).thenComparingLong(Ticket::seq).compare(this, that); }
            }

            @Override
            public boolean acquire(final int nice, final long timeoutMs) throws InterruptedException {

                final var ticket = new Ticket(this.nextTicketSeq(), narrow(-20, nice, 19));

                if (this.waitingThreads.offer(ticket)) {
                    var waitUntilNs = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(timeoutMs);
                    if (waitUntilNs < 0) waitUntilNs = FOREVER;

                    try {
                        for (var waitNs = this.intervalNs; //
                             0 < (waitNs = Math.min(waitUntilNs - System.nanoTime(), waitNs)); Promissory.onSpinWait()) //
                            if (this.sem.tryAcquire() || (this.waitingThreads.peek() == ticket && this.sem.tryAcquire(waitNs, TimeUnit.NANOSECONDS)))
                                return true;
                    } finally { this.waitingThreads.remove(ticket); }
                }

                return false;
            }

            @Override
            public void release() { this.sem.release(); }

            @Override
            public void release(final int permits) { this.sem.release(permits); }
        };
    }
}
