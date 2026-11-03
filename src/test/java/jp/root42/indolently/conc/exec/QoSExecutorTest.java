// Copyright 2026 takahashikzn
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
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * @author takahashikzn
 */
public class QoSExecutorTest {

    record Wait(Duration rate, Duration concurrency) { }

    @Test(timeout = 10_000)
    public void onWait() throws Exception {

        final List<Wait> waits = new CopyOnWriteArrayList<>();
        final var done = new CountDownLatch(3);

        try (var exec = new QoSExecutor(es -> ConcurrencyLimitExecutor.of(es, 1)).onWait((rate, conc) -> waits.add(new Wait(rate, conc)))) {
            exec.rateLimit(2, TimeUnit.SECONDS);

            for (var i = 0; i < 3; i++)
                exec.execute(() -> {
                    LockSupport.parkNanos(Duration.ofMillis(300).toNanos());
                    done.countDown();
                });

            done.await();
        }

        // 1st runs at once, 2nd waits for the 1st to finish, 3rd waits for the 1st permit to come back a second after it was taken
        assertThat(waits).hasSize(3);
        assertThat(waits.get(0).rate().plus(waits.get(0).concurrency())).isLessThan(Duration.ofMillis(100));
        assertThat(waits.get(1).concurrency()).isGreaterThan(Duration.ofMillis(200));
        assertThat(waits.get(2).rate()).isGreaterThan(Duration.ofMillis(500));
    }

    @Test(timeout = 10_000)
    public void onWait_interrupted() throws Exception {

        final List<Wait> waits = new CopyOnWriteArrayList<>();

        try (var exec = new QoSExecutor(es -> ConcurrencyLimitExecutor.of(es, 1)).onWait((rate, conc) -> waits.add(new Wait(rate, conc)))) {
            exec.rateLimit(1, TimeUnit.MINUTES);
            exec.execute(() -> { });

            final var self = Thread.currentThread();
            Thread.startVirtualThread(() -> {
                LockSupport.parkNanos(Duration.ofMillis(300).toNanos());
                self.interrupt();
            });

            assertThatThrownBy(() -> exec.execute(() -> { })).hasCauseInstanceOf(InterruptedException.class);
            Thread.interrupted();
        }

        assertThat(waits).hasSize(2);
        assertThat(waits.get(1).rate()).isGreaterThan(Duration.ofMillis(200));
    }
}
