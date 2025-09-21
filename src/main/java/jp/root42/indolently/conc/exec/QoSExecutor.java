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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import jp.root42.indolently.conc.PriorityRateLimitSemaphore;


/**
 * @author takahashikzn
 */
public class QoSExecutor
    implements PriorityExecutor, ConcurrencyLimitExecutor, RateLimitExecutor, AutoCloseable {

    private static final int HARD_LIMIT = 256;

    private final ConcurrencyLimitExecutor concLimit;

    private interface PriorityRateLimitExecutor
        extends PriorityExecutor, RateLimitExecutor { }

    private final PriorityRateLimitExecutor rateLimit;

    private final Runnable closeAction;

    public QoSExecutor(final Function<ExecutorService, ConcurrencyLimitExecutor> factory) { this(chooseES(), factory); }

    private static ExecutorService chooseES() {

        return Runtime.version().feature() >= 25
            ? Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("qos-vexec-", 0L).factory())
            : Executors.newCachedThreadPool(r -> {
                interface $static {

                    AtomicLong seq = new AtomicLong(0);
                }

                final var t = new Thread(r);
                t.setName("qos-exec-" + $static.seq.incrementAndGet());
                t.setDaemon(true);
                return t;
            });
    }

    private interface CloseableConcurrencyLimitExecutor
        extends ConcurrencyLimitExecutor, AutoCloseable { }

    public QoSExecutor(final ExecutorService es, final Function<ExecutorService, ConcurrencyLimitExecutor> concLimitFactory) {
        this(new CloseableConcurrencyLimitExecutor() {

            private final ConcurrencyLimitExecutor delegate = concLimitFactory.apply(es);

            @Override
            public void concurrency(final int x) { this.delegate.concurrency(x); }

            @Override
            public void execute(final Runnable command) { this.delegate.execute(command); }

            @Override
            public void close() {
                if (this.delegate instanceof AutoCloseable c) try { c.close(); } catch (Exception e) { e.printStackTrace(); }
                es.close();
            }
        });
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public QoSExecutor(final ConcurrencyLimitExecutor concLimit) {
        this.concLimit = concLimit;
        this.rateLimit = new PriorityRateLimitExecutor() {

            private final PriorityRateLimitSemaphore window = new PriorityRateLimitSemaphore(60, TimeUnit.MINUTES);

            @Override
            public boolean rateLimit(final int newLimit, final TimeUnit unit, final Duration timeout) { return this.window.rateLimit(newLimit, unit, timeout); }

            @Override
            public void execute(final Runnable task, final int nice) {

                try { this.window.acquire(nice); } //
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }

                try {
                    concLimit.execute(() -> {
                        this.window.release();
                        task.run();
                    });
                } catch (Exception e) {
                    this.window.release();
                    throw e;
                }
            }
        };

        this.closeAction = () -> {
            if (this.concLimit instanceof AutoCloseable c) try { c.close(); } catch (Exception e) { e.printStackTrace(); }
        };
    }

    @Override
    public void concurrency(final int x) {
        if (x <= 0) throw new IllegalArgumentException("concurrency must be positive: " + x);
        if (HARD_LIMIT < x) throw new IllegalArgumentException("concurrency must be <= " + HARD_LIMIT + ": " + x);
        this.concLimit.concurrency(x);
    }

    @Override
    public boolean rateLimit(final int limit, final TimeUnit unit, final Duration timeout) { return this.rateLimit.rateLimit(limit, unit, timeout); }

    @Override
    public void execute(final Runnable task, final int nice) { this.rateLimit.execute(task, nice); }

    @Override
    public void execute(final Runnable task) { this.rateLimit.execute(task); }

    @Override
    public void close() throws Exception { this.closeAction.run(); }
}
