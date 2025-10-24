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
import java.util.concurrent.TimeUnit;

import jp.root42.indolently.conc.Concurrentive;
import jp.root42.indolently.conc.lock.PriorityGate.Nice;
import jp.root42.indolently.conc.lock.PriorityRateLimitGate;


/**
 * @author takahashikzn
 */
public interface PriorityRateLimitExecutor
    extends PriorityExecutor, RateLimitExecutor {

    static PriorityRateLimitExecutor of(final Executor exec) { return of(exec, RateLimitBase.TIMEUNIT); }

    static PriorityRateLimitExecutor of(final Executor exec, final RateLimitBase base) {

        final var window = new PriorityRateLimitGate(60, TimeUnit.MINUTES);

        return new PriorityRateLimitExecutor() {

            @Override
            public boolean rateLimit(final int limit, final TimeUnit unit, final Duration timeout) { return window.rateLimit(limit, unit, timeout); }

            @Override
            public void execute(final Runnable task, final Nice nice) {

                Concurrentive.execute( //
                    () -> window.acquire(nice) //
                    , () -> exec.execute(() -> {
                        switch (base) {
                            case TIMEUNIT -> {
                                window.release();
                                task.run();
                            }

                            case COMPLETION -> {
                                try { task.run(); } //
                                finally { window.release(); }
                            }
                        }
                    }) //
                    , window::release);
            }
        };
    }
}
