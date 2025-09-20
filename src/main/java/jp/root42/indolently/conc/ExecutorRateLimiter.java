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

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.util.Objects.requireNonNull;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class ExecutorRateLimiter
    implements Executor {

    private final Deque<Long> recentRequests = new ConcurrentLinkedDeque<>();

    private final Deque<Object> waitingQueue = new ConcurrentLinkedDeque<>();

    private final Executor executor;

    private volatile int limit;

    private volatile TimeUnit unit;

    public ExecutorRateLimiter(final Executor executor, final int limit, final TimeUnit unit) {
        this.executor = requireNonNull(executor);
        this.setLimit(limit, unit);
    }

    public void setLimit(final int x, final TimeUnit unit) {
        if (x <= 0) throw new IllegalArgumentException("limit must be positive: " + x);
        this.limit = x;
        this.unit = requireNonNull(unit);
    }

    @Override
    public void execute(final Runnable run) {

        final var ticket = new Object();
        this.waitingQueue.addLast(ticket);

        try {
            spin:
            for (int spins = 0; ; spins++) {
                if (!this.myTurn(ticket)) {
                    if (spins < 50) Thread.onSpinWait();
                    else LockSupport.parkNanos(200_000L);
                    continue;
                } else spins = 0;

                final var base = this.baseLine();
                this.clean(base);

                final var window = this.unit.toMillis(1);

                int last1MinCount = 0;
                Long firstActive = null;
                for (final var ts: this.recentRequests)
                    if (base <= ts) {
                        if (firstActive == null) firstActive = ts;
                        if (this.limit <= ++last1MinCount) {
                            Thread.sleep(narrow(10, firstActive + window - now(), 100));
                            continue spin;
                        }
                    }

                break;
            }

            this.execute0(run);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } finally {
            this.waitingQueue.removeFirstOccurrence(ticket);
        }
    }

    private void execute0(final Runnable run) {
        final var stamp = now();

        this.recentRequests.addLast(stamp);
        try { this.executor.execute(run); } //
        catch (RejectedExecutionException e) {
            this.recentRequests.removeLastOccurrence(stamp);
            throw e;
        }
    }

    private long baseLine() { return now() - this.unit.toMillis(1); }

    private boolean myTurn(final Object ticket) { return this.waitingQueue.peekFirst() == ticket; }

    private void clean(final long base) {
        while (true) {
            final var h = this.recentRequests.peekFirst();
            if (h == null || base <= h) break;
            this.recentRequests.pollFirst();
        }
    }
}
