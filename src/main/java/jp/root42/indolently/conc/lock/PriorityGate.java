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
package jp.root42.indolently.conc.lock;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import jp.root42.indolently.conc.Concurrentive;
import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface PriorityGate
    extends Gate {

    Nice NO_NICE = new Nice(0);

    record Nice(int val) {

        public static Nice of(final int val) { return val == 0 ? NO_NICE : new Nice(val); }
    }

    default void acquire(final Nice nice) throws InterruptedException { this.acquire(nice, Long.MAX_VALUE, TimeUnit.SECONDS); }

    boolean acquire(Nice nice, long timeout, TimeUnit unit) throws InterruptedException;

    default boolean tryAcquire(final Nice nice) {
        try { return this.acquire(nice, 0L, TimeUnit.SECONDS); } //
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    default Permit lease(final Nice nice) throws InterruptedException {
        this.acquire(nice);
        return this::release;
    }

    default $<Permit> lease(final Nice nice, final long timeout, final TimeUnit unit) throws InterruptedException {
        return this.acquire(nice, timeout, unit) ? just(this::release) : none();
    }

    default $<Permit> tryLease(final Nice nice) { return this.tryAcquire(nice) ? just(this::release) : none(); }

    static PriorityGate of(final int capacity) { return of(capacity, 50); }

    static PriorityGate of(final int capacity, final long intervalMs) {

        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        if (intervalMs <= 0) throw new IllegalArgumentException("interval must be positive");

        final class Impl
            extends Gate.Delegate<Gate>
            implements PriorityGate {

            private final long intervalNs;

            private final BlockingQueue<Ticket> waitingThreads;

            public Impl(final int capacity, final long intervalMs) {
                super(Gate.of(capacity));
                this.intervalNs = TimeUnit.MILLISECONDS.toNanos(intervalMs);
                this.waitingThreads = new PriorityBlockingQueue<>();
                this.ticketSeq = new AtomicLong();
            }

            private final AtomicLong ticketSeq;

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
            public boolean acquire(final Nice nice, final long timeout, final TimeUnit unit) throws InterruptedException {

                final var ticket = new Ticket(this.nextTicketSeq(), narrow(-20, nice.val(), 19));

                if (this.waitingThreads.offer(ticket)) {
                    var waitUntilNs = System.nanoTime() + unit.toNanos(timeout);
                    if (waitUntilNs < 0) waitUntilNs = Long.MAX_VALUE; // forever

                    try {
                        for (var waitNs = this.intervalNs; //
                             0 < (waitNs = Math.min(waitUntilNs - System.nanoTime(), waitNs)); Concurrentive.onSpinWait()) //
                            if (this.gate.tryAcquire() || (this.waitingThreads.peek() == ticket && this.gate.tryAcquire(waitNs, TimeUnit.NANOSECONDS)))
                                return true;
                    } finally { this.waitingThreads.remove(ticket); }
                }

                return false;
            }
        }

        return new Impl(capacity, intervalMs);
    }
}
