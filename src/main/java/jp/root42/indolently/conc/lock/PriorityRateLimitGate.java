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

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import jp.root42.indolently.conc.Concurrentive;

import static java.util.Objects.requireNonNull;


/**
 * @author takahashikzn
 */
public class PriorityRateLimitGate
    extends Gate.Delegate<PriorityGate>
    implements PriorityGate, RateLimitGate {

    public PriorityRateLimitGate(final int limit, final TimeUnit unit) {
        super(PriorityGate.of(limit));
        this.setLimit0(limit, unit);
    }

    private volatile long holdNanos;

    private volatile int limit;

    private void setLimit0(final int newLimit, final TimeUnit unit) {
        this.holdNanos = requireNonNull(unit, "unit is null").toNanos(1);
        this.limit = newLimit;
    }

    @Override
    public synchronized boolean rateLimit(final int newLimit, final TimeUnit unit, final Duration timeout) {
        return Concurrentive.setPermits(this.limit, newLimit, timeout, this.gate, l -> this.setLimit0(l, unit));
    }

    @Override
    public void acquire(final Nice nice) throws InterruptedException {
        if (no_nice(nice)) this.gate.acquire();
        else this.gate.acquire(nice);
    }

    @Override
    public boolean acquire(final Nice nice, final long timeout, final TimeUnit unit) throws InterruptedException {
        return no_nice(nice) ? this.gate.tryAcquire(timeout, unit) : this.gate.acquire(nice, timeout, unit);
    }

    private static boolean no_nice(final Nice nice) { return nice == null || NO_NICE.equals(nice); }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void release(final int permits) {
        try {
            final var delay = this.holdNanos;
            Thread.startVirtualThread(() -> {
                LockSupport.parkNanos(delay);
                this.gate.release(permits);
            });
        } catch (Exception e) {
            this.gate.release(permits);
            e.printStackTrace();
        }
    }
}
