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

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.util.Objects.requireNonNull;


/**
 * @author takahashikzn
 */
public class PriorityRateLimitSemaphore
    implements PrioritySemaphore {

    private final PrioritySemaphore window;

    public PriorityRateLimitSemaphore(final int limit, final TimeUnit unit) {
        if (limit <= 0) throw new IllegalArgumentException("limit must be positive");

        this.window = PrioritySemaphore.of(limit);
        this.setLimit0(limit, unit);
    }

    private volatile long holdNanos;

    private volatile int limit;

    private void setLimit0(final int newLimit, final TimeUnit unit) {
        this.holdNanos = requireNonNull(unit, "unit is null").toNanos(1);
        this.limit = newLimit;
    }

    public synchronized boolean rateLimit(final int newLimit, final TimeUnit unit, final Duration timeout) {

        if (newLimit <= 0) throw new IllegalArgumentException("newLimit must be positive");

        final int oldLimit = this.limit;
        var success = true;

        if (oldLimit < newLimit) {
            this.window.release(newLimit - oldLimit);
            this.setLimit0(newLimit, unit);
        } else if (newLimit < oldLimit) {

            final int target = oldLimit - newLimit;
            int drained = 0;

            for (
                final long timeoutNs = timeout.toNanos(), startAt = System.nanoTime();
                drained < target && (System.nanoTime() - startAt) < timeoutNs; Promissory.onSpinWait()) //
                drained += this.window.drainPermits();

            if (target <= drained) {
                this.window.release(drained - target);
                this.setLimit0(newLimit, unit);
            } else {
                success = false;
                this.setLimit0(oldLimit - drained, unit);
            }
        }

        return success;
    }

    @Override
    public int drainPermits() { return this.window.drainPermits(); }

    @Override
    public int availablePermits() { return this.window.availablePermits(); }

    @Override
    public void acquire() throws InterruptedException { this.window.acquire(); }

    @Override
    public boolean acquire(final long timeoutMs) throws InterruptedException { return this.window.acquire(timeoutMs); }

    @Override
    public void acquire(final int nice) throws InterruptedException {
        if (nice == NO_NICE) this.window.acquire();
        else this.window.acquire(nice);
    }

    @Override
    public boolean acquire(final int nice, final long timeoutMs) throws InterruptedException {
        return (nice == NO_NICE) ? this.window.acquire(timeoutMs) : this.window.acquire(nice, timeoutMs);
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void release() {
        try {
            final var delay = this.holdNanos;
            Thread.startVirtualThread(() -> {
                LockSupport.parkNanos(delay);
                this.window.release();
            });
        } catch (Exception e) {
            this.window.release();
            e.printStackTrace();
        }
    }

    @Deprecated
    @Override
    public void release(final int permits) { throw new UnsupportedOperationException(); }
}
