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
package jp.root42.indolently.io;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.assertj.core.api.Assertions.assertThat;


public class LargeBufferTest {

    @Rule
    public final TemporaryFolder dir = new TemporaryFolder();

    @Before
    @After
    public void resetConfiguration() {
        LargeBuffer.compressionThreshold(4 * 1024 * 1024);
        LargeBuffer.offloadThreshold(8 * 1024 * 1024);
        LargeBuffer.compressionCodec(LargeBuffer.Codec.GZIP);
    }

    @Test
    public void cleanupFollowsMovedFile() throws Exception {
        try (var buffer = LargeBuffer.raw(new byte[9 * 1024 * 1024])) {
            final var original = buffer.file().get();
            final var moved = this.dir.getRoot().toPath().resolve("moved");
            buffer.moveTo(moved);
            buffer.close();
            assertThat(original).doesNotExist();
            assertThat(moved).doesNotExist();
        }
    }

    @Test
    public void rawFileStaysOpenUntilStreamCloses() throws Exception {
        final var body = new byte[17 * 1024 * 1024];
        body[body.length - 1] = 42;
        try (var buffer = LargeBuffer.raw(body)) {
            final var file = buffer.file().get();
            try (var in = buffer.open()) {
                assertThat(in.readAllBytes()).isEqualTo(body);
                assertThat(file).exists();
            }
            assertThat(file).doesNotExist();
        }
    }

    @Test
    public void compressedFileCanBeReadCompletely() throws Exception {
        final var body = new byte[17 * 1024 * 1024];
        new Random(42).nextBytes(body);
        try (var buffer = LargeBuffer.of(body)) {
            final var file = buffer.file().get();
            try (var in = buffer.open()) { assertThat(in.readAllBytes()).isEqualTo(body); }
            assertThat(file).doesNotExist();
        }
    }

    @Test
    public void repeatableFileSurvivesStreamClose() throws Exception {
        try (var buffer = LargeBuffer.raw(new byte[17 * 1024 * 1024]).repeatable()) {
            final var file = buffer.file().get();
            for (int i = 0; i < 2; i++) {
                try (var in = buffer.open()) { assertThat(in.read()).isZero(); }
                assertThat(file).exists();
            }
        }
    }

    @Test
    public void cleanerDoesNotRetainClosedOwner() throws Exception {
        awaitCleanup(closedBuffer());
    }

    @Test
    public void cleanerReclaimsAbandonedFile() throws Exception {
        awaitCleanup(abandonedBuffer());
    }

    @Test
    public void smallBufferOpenDoesNotDeadlock() throws Exception {
        final var task = new FutureTask<>(() -> {
            try (var buffer = LargeBuffer.raw(new byte[] { 1, 2, 3 }); var in = buffer.open()) {
                return in.readAllBytes();
            }
        });
        Thread.startVirtualThread(task);
        assertThat(task.get(5, TimeUnit.SECONDS)).containsExactly((byte) 1, (byte) 2, (byte) 3);
    }

    @Test
    public void existingBufferRetainsInjectedCodec() throws Exception {
        final var codec = new XorCodec();
        LargeBuffer.compressionThreshold(0);
        LargeBuffer.compressionCodec(codec);
        final var body = new byte[] { 1, 2, 3 };

        try (var buffer = LargeBuffer.of(body).repeatable()) {
            LargeBuffer.compressionCodec(LargeBuffer.Codec.NONE);
            assertThat(buffer.read()).containsExactly(body);
            assertThat(buffer.read()).containsExactly(body);
            assertThat(codec.compressions).isEqualTo(1);
            assertThat(codec.decompressions).isEqualTo(2);
        }
    }

    @Test
    public void rawBufferBypassesInjectedCodec() throws Exception {
        final var codec = new XorCodec();
        LargeBuffer.compressionThreshold(0);
        LargeBuffer.compressionCodec(codec);
        final var body = new byte[] { 1, 2, 3 };

        try (var buffer = LargeBuffer.raw(body)) { assertThat(buffer.read()).containsExactly(body); }
        assertThat(codec.compressions).isZero();
        assertThat(codec.decompressions).isZero();
    }

    @Test
    public void injectedCodecStreamDoesNotDependOnClosedSource() throws Exception {
        LargeBuffer.compressionThreshold(0);
        LargeBuffer.offloadThreshold(32);
        LargeBuffer.compressionCodec(new XorCodec());
        final var body = new byte[256];
        new Random(42).nextBytes(body);

        try (var buffer = LargeBuffer.of(body)) {
            final var file = buffer.file().get();
            assertThat(Files.readAllBytes(file)).isNotEqualTo(body);
            try (var in = buffer.open()) { assertThat(in.readAllBytes()).containsExactly(body); }
            assertThat(file).doesNotExist();
        }
    }

    @Test
    public void offloadUsesCompressedSize() throws Exception {
        LargeBuffer.compressionThreshold(0);
        LargeBuffer.offloadThreshold(64);
        final var body = new byte[1024];

        try (var buffer = LargeBuffer.of(body)) {
            assertThat(buffer.file().present()).isFalse();
            assertThat(buffer.length()).isEqualTo(body.length);
            try (var in = buffer.open()) { assertThat(in.readAllBytes()).containsExactly(body); }
        }
    }

    @Test
    public void writeLeavesCallerOutputOpen() throws Exception {
        LargeBuffer.offloadThreshold(32);
        final var body = new byte[256];
        new Random(42).nextBytes(body);
        final var out = new ByteArrayOutputStream() {

            private boolean closed;

            @Override
            public void close() { this.closed = true; }
        };

        try (var buffer = LargeBuffer.raw(body)) { buffer.write(out); }
        assertThat(out.toByteArray()).containsExactly(body);
        assertThat(out.closed).isFalse();
    }

    private static final class XorCodec
        implements LargeBuffer.Codec {

        private int compressions;

        private int decompressions;

        @Override
        public byte[] compress(final byte[] raw) {
            this.compressions++;
            return transform(raw);
        }

        @Override
        public byte[] decompress(final byte[] stored) {
            this.decompressions++;
            return transform(stored);
        }

        private static byte[] transform(final byte[] bytes) {
            final var result = bytes.clone();
            for (int i = 0; i < result.length; i++) result[i] ^= 0x5a;
            return result;
        }
    }

    private static Abandoned abandonedBuffer() throws Exception {
        final var buffer = LargeBuffer.raw(new byte[9 * 1024 * 1024]).repeatable();
        return new Abandoned(new WeakReference<>(buffer), buffer.file().get());
    }

    private record Abandoned(WeakReference<LargeBuffer> owner, Path file) { }

    private static Abandoned closedBuffer() throws Exception {
        final var buffer = LargeBuffer.raw(new byte[9 * 1024 * 1024]);
        final var abandoned = new Abandoned(new WeakReference<>(buffer), buffer.file().get());
        buffer.close();
        return abandoned;
    }

    private static void awaitCleanup(final Abandoned abandoned) throws Exception {
        final long deadline = System.nanoTime() + Duration.ofSeconds(5).toNanos();
        while (System.nanoTime() < deadline && (abandoned.owner().get() != null || Files.exists(abandoned.file()))) {
            System.gc();
            Thread.sleep(20);
        }
        assertThat(abandoned.owner().get()).isNull();
        assertThat(abandoned.file()).doesNotExist();
    }
}
