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
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import jp.root42.indolently.conc.lock.PriorityGate.Nice;


/**
 * @author takahashikzn
 */
public class QoSExecutor
    implements PriorityExecutor, ConcurrencyLimitExecutor, RateLimitExecutor, AutoCloseable {

    private static final int HARD_LIMIT = 256;

    private final ConcurrencyLimitExecutor concLimit;

    private final PriorityRateLimitExecutor rateLimit;

    private final Runnable closeAction;

    private static ExecutorService chooseES() {

        return Runtime.version().feature() >= 25
            ? Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("qos-vexec-", 0L).factory())
            : new ThreadPoolExecutor(1, HARD_LIMIT, 60, TimeUnit.SECONDS, new SynchronousQueue<>(),
                Thread.ofPlatform().name("qos-exec-", 0L).daemon(true).factory());
    }

    private static ConcurrencyLimitExecutor asCloseableExecutor(final ExecutorService es,
        final Function<ExecutorService, ConcurrencyLimitExecutor> concLimitFactory) {

        interface CloseableConcurrencyLimitExecutor
            extends ConcurrencyLimitExecutor, AutoCloseable { }

        return new CloseableConcurrencyLimitExecutor() {

            private final ConcurrencyLimitExecutor delegate = concLimitFactory.apply(es);

            @Override
            public boolean concurrency(final int limit, final Duration timeout) { return this.delegate.concurrency(limit, timeout); }

            @Override
            public void execute(final Runnable command) { this.delegate.execute(command); }

            @Override
            public void close() {
                if (this.delegate instanceof AutoCloseable c) try { c.close(); } catch (Exception e) { e.printStackTrace(); }
                es.close();
            }
        };
    }

    public QoSExecutor(final Function<ExecutorService, ConcurrencyLimitExecutor> factory) { this(chooseES(), factory); }

    public QoSExecutor(final ExecutorService es, final Function<ExecutorService, ConcurrencyLimitExecutor> factory) { this(asCloseableExecutor(es, factory)); }

    @SuppressWarnings("CallToPrintStackTrace")
    public QoSExecutor(final ConcurrencyLimitExecutor concLimit) {
        this.rateLimit = PriorityRateLimitExecutor.of(this.concLimit = concLimit);

        this.closeAction = () -> {
            if (this.concLimit instanceof AutoCloseable c) try { c.close(); } catch (Exception e) { e.printStackTrace(); }
        };
    }

    @Override
    public boolean concurrency(final int limit, final Duration timeout) {
        if (limit <= 0) throw new IllegalArgumentException("concurrency must be positive: " + limit);
        if (HARD_LIMIT < limit) throw new IllegalArgumentException("concurrency must be <= " + HARD_LIMIT + ": " + limit);
        return this.concLimit.concurrency(limit, timeout);
    }

    @Override
    public boolean rateLimit(final int limit, final TimeUnit unit, final Duration timeout) { return this.rateLimit.rateLimit(limit, unit, timeout); }

    @Override
    public void execute(final Runnable task, final Nice nice) { this.rateLimit.execute(task, nice); }

    @Override
    public void execute(final Runnable task) { this.rateLimit.execute(task); }

    @Override
    public void close() throws Exception { this.closeAction.run(); }
}
