// Copyright 2024 takahashikzn
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


/**
 * @author takahashikzn
 */
public class PrioritySemaphore {

    private final Semaphore sem;

    private final long interval;

    private final BlockingQueue<Ticket> waitingThreads = new PriorityBlockingQueue<>();

    public PrioritySemaphore(final int capacity) { this(capacity, 50); }

    public PrioritySemaphore(final int capacity, final long interval) {
        if (interval <= 0) throw new IllegalArgumentException("interval must be positive");

        this.sem = new Semaphore(capacity, true);
        this.interval = interval;
    }

    public void acquire() throws InterruptedException { this.sem.acquire(); }

    private static final long FOREVER = Long.MAX_VALUE;

    public boolean acquire(final int priority) throws InterruptedException { return this.acquire(FOREVER, priority); }

    private record Ticket(int priority, long timestamp)
        implements Comparable<Ticket> {

        @Override
        public int compareTo(final Ticket that) { return Comparator.comparingInt(Ticket::priority).thenComparingLong(Ticket::timestamp).compare(this, that); }
    }

    public boolean acquire(final long timeout, final int priority) throws InterruptedException {

        final var ticket = new Ticket(priority, now());

        if (!this.waitingThreads.offer(ticket)) return false;

        try {
            var waitUntil = now() + timeout;
            if (waitUntil < 0) waitUntil = FOREVER;

            for (var wait = this.interval; //
                 0 < (wait = Math.min(waitUntil - now(), wait)); )
                //
                if (this.sem.tryAcquire(wait, TimeUnit.MILLISECONDS)) //
                    if (this.waitingThreads.peek() == ticket) return true;
                    else this.sem.release();

            return false;
        } finally {
            this.waitingThreads.remove(ticket);
        }
    }

    public void release() { this.sem.release(); }

    private static long now() { return System.currentTimeMillis(); }
}
