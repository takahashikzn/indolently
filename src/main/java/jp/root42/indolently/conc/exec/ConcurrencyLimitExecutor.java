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
package jp.root42.indolently.conc.exec;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

import jp.root42.indolently.conc.Concurrentive;
import jp.root42.indolently.conc.lock.Gate;


/**
 * @author takahashikzn
 */
public interface ConcurrencyLimitExecutor
    extends Executor {

    default void concurrency(final int limit) { this.concurrency(limit, Duration.ofNanos(Long.MAX_VALUE)); }

    boolean concurrency(int limit, Duration timeout);

    static ConcurrencyLimitExecutor of(final Executor exec, final int limit) {

        final var gate = Gate.of(limit);
        final var lastLimit = new AtomicInteger(limit);

        return new ConcurrencyLimitExecutor() {

            @Override
            public boolean concurrency(final int newLimit, final Duration timeout) {
                return Concurrentive.setPermits(lastLimit.get(), newLimit, timeout, gate, lastLimit::set);
            }

            @Override
            public void execute(final Runnable task) {

                Concurrentive.execute( //
                    gate::acquire //
                    , () -> exec.execute(() -> {
                        try { task.run(); } //
                        finally { gate.release(); }
                    }) //
                    , gate::release);
            }
        };
    }
}
