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
import java.util.function.IntConsumer;

import jp.root42.indolently.conc.lock.Gate;
import jp.root42.indolently.function.RunnableE;


/**
 * @author takahashikzn
 */
public final class Concurrentive {

    private Concurrentive() { }

    private static final long SPIN_PARK = TimeUnit.MILLISECONDS.toNanos(10L);

    public static void onSpinWait() {
        //Thread.onSpinWait();
        LockSupport.parkNanos(SPIN_PARK);
    }

    public static void execute(final RunnableE<InterruptedException> acquire, final Runnable execute, final Runnable rejected) {

        try { acquire.run(); } //
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        try { execute.run(); } //
        catch (Exception e) {
            rejected.run();
            throw e;
        }
    }

    public static boolean setPermits(final int oldLimit, final int newLimit, final Duration timeout, final Gate gate, final IntConsumer updateLimit) {

        if (newLimit <= 0) throw new IllegalArgumentException("newLimit must be positive");

        var success = true;

        if (oldLimit < newLimit) {
            gate.release(newLimit - oldLimit);
            updateLimit.accept(newLimit);
        } else if (newLimit < oldLimit) {

            final int target = oldLimit - newLimit;
            int drained = 0;

            for (
                final long timeoutNs = timeout.toNanos(), startAt = System.nanoTime();
                drained < target && (System.nanoTime() - startAt) < timeoutNs; onSpinWait()) //
                drained += gate.drainPermits();

            if (target <= drained) {
                gate.release(drained - target);
                updateLimit.accept(newLimit);
            } else {
                success = false;
                updateLimit.accept(oldLimit - drained);
            }
        }

        return success;
    }
}
