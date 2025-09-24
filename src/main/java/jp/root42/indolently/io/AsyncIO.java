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
package jp.root42.indolently.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static java.lang.System.nanoTime;
import static jp.root42.indolently.Expressive.if_;


/**
 * @author takahashikzn
 */
public final class AsyncIO {

    private AsyncIO() { }

    public static void transfer(final InputStream in, final OutputStream out, final Predicate<Long> cancelled)
        throws IOException, CancellationException { transfer(in, out, cancelled, 25, 5000); }

    private static final Semaphore sem = new Semaphore(2048, true);

    @SuppressWarnings("CallToPrintStackTrace")
    public static void transfer(final InputStream in, final OutputStream out, final Predicate<Long> cancelled, final int pollIntervalMs, final long ioTimeoutMs)
        throws IOException, CancellationException {

        final var pollingInterval = Duration.ofMillis(Math.max(1, pollIntervalMs));
        final var ioTimeoutNano = ioTimeoutMs < 0 ? Long.MAX_VALUE : TimeUnit.MILLISECONDS.toNanos(ioTimeoutMs);

        try {
            if (!sem.tryAcquire(ioTimeoutNano, TimeUnit.NANOSECONDS)) throw new CancellationException("io pool saturated (semaphore timeout)");

            final var startAt = nanoTime();
            final var progress = new AtomicLong(startAt);
            final var failure = new AtomicReference<Throwable>();

            try {
                final var pump = Thread.ofVirtual() //
                    .name("async-io") //
                    .uncaughtExceptionHandler((__, t) -> if_(!failure.compareAndSet(null, t), () -> t.printStackTrace())) //
                    .start(() -> {
                        final var buf = new byte[1024 * 256];

                        try {
                            for (int n; (n = in.read(buf)) != -1; ) {
                                if (failure.get() != null) return;
                                if (0 < n) {
                                    progress.set(nanoTime());
                                    out.write(buf, 0, n);
                                    progress.set(nanoTime());
                                }
                                if (failure.get() != null) return;
                            }

                            out.flush();
                        } catch (Throwable t) {
                            if_(!failure.compareAndSet(null, t), () -> t.printStackTrace());
                        }
                    });

                while (!pump.join(pollingInterval)) {
                    final var tick = nanoTime();

                    if (cancelled.test(tick - startAt)) {
                        pump.interrupt();
                        failure.compareAndSet(null, new CancellationException("cancel requested"));
                    } else if (ioTimeoutNano < tick - progress.get()) {
                        pump.interrupt();
                        failure.compareAndSet(null, new CancellationException("io timeout (no progress)"));
                    }

                    raise(failure);
                }

                raise(failure);
            } finally { sem.release(); }
        } catch (InterruptedException __) {
            Thread.currentThread().interrupt();
            throw new CancellationException("interrupted");
        }
    }

    private static void raise(final AtomicReference<Throwable> failure) throws IOException {

        switch (failure.get()) {
            case RuntimeException e when e.getCause() instanceof IOException io -> throw io;
            case RuntimeException e -> throw e;
            case IOException e -> throw e;
            case Error e -> throw e;
            case null -> { }
            default -> throw new IOException(failure.get());
        }
    }
}
