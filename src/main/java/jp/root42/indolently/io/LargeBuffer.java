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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.ref.Cleaner;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Predicate;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import jp.root42.indolently.$set;
import jp.root42.indolently.ref.$;

import static java.lang.System.Logger.Level.*;
import static java.util.Objects.requireNonNull;
import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * A byte buffer that compresses large payloads and spills large stored representations to temporary files.
 * <p>
 * Buffers are consumed by default: {@link #read()} closes the buffer, and the stream returned by {@link #open()}
 * releases it when closed. Call {@link #repeatable()} before reading to retain it for subsequent reads, and close
 * the buffer explicitly when its owner is finished. A {@link Cleaner} also removes abandoned temporary files.
 * <p>
 * Compression uses {@link Codec#GZIP} unless a codec is configured at application startup. On Linux, available
 * memory is monitored and temporary files can be moved to a secondary directory; see {@link #setSecondaryDir(Path, long)}.
 * Configuration is shared by all buffers in this class loader and should be completed before concurrent use.
 * Individual buffers are not safe for concurrent reads, writes, or changes to their lifetime.
 *
 * @author takahashikzn
 */
public class LargeBuffer
    implements Closeable {

    static final Logger LOG = System.getLogger(LargeBuffer.class.getSimpleName());

    final Logger log = System.getLogger(this.getClass().getSimpleName());

    private static long compressionThreshold = 1024 * 1024 * 4;

    /**
     * Sets the uncompressed size above which compression is considered; the default is 4 MiB.
     *
     * @param x threshold in bytes; a payload equal to the threshold is not compressed
     */
    public static void compressionThreshold(final int x) { compressionThreshold = x; }

    private static long offloadThreshold = compressionThreshold * 2;

    /**
     * Sets the stored size at which a buffer is moved to a temporary file; the default is 8 MiB.
     * This threshold is applied after compression and is independent of the compression threshold.
     *
     * @param x threshold in bytes
     */
    public static void offloadThreshold(final int x) { offloadThreshold = x; }

    private static Codec compressionCodec = Codec.GZIP;

    /**
     * Selects the codec for subsequently created buffers. Existing buffers retain their original codec.
     *
     * @param x codec to use; {@link Codec#NONE} disables compression
     */
    public static void compressionCodec(final Codec x) { compressionCodec = requireNonNull(x); }

    private static boolean useLog;

    /**
     * Enables diagnostic logging for file allocation and deletion.
     *
     * @param x whether to log these operations
     */
    public static void useLog(final boolean x) { useLog = x; }

    private static final $set<Predicate<byte[]>> compHint = set(vrai());

    /**
     * Adds a compression eligibility predicate. A payload is compressed only if every predicate accepts it
     * and its uncompressed size exceeds the compression threshold.
     *
     * @param compressible predicate that returns true when compression is useful
     */
    public static void compHint(final Predicate<byte[]> compressible) { compHint.addAll(list(compressible)); }

    private final String name;

    private final Codec codec;

    private final long length;

    private Path file;

    private byte[] buf;

    /**
     * Configures the Linux directory used when available memory falls below the given threshold.
     * Existing temporary files are also considered for relocation every five seconds. Choose a directory on
     * disk when {@code java.io.tmpdir} is backed by memory; otherwise relocation cannot relieve memory pressure.
     * By default, the directory is {@code .indolently-lb} under {@code java.io.tmpdir}, with a 1 GiB threshold.
     * On systems without {@code /proc/meminfo}, this method logs a warning and has no effect.
     *
     * @param dir secondary temporary directory
     * @param threshold available-memory threshold in bytes, as reported by the host's {@code /proc/meminfo}
     * @throws IOException if the directory cannot be prepared
     */
    public static void setSecondaryDir(final Path dir, final long threshold) throws IOException {
        if (!LinuxLargeBuffer.available()) {
            LOG.log(WARNING, "LinuxLargeBuffer not available");
            return;
        }

        LinuxLargeBuffer.lowMemTmpDir(dir);
        LinuxLargeBuffer.lowMemThreshold(threshold);
        LOG.log(INFO, "LinuxLargeBuffer available: memInfo = \n{0}", LinuxMemInfo.get());
    }

    /**
     * Creates a buffer, compressing and spilling it to a file according to the configured thresholds.
     * The input array may be retained without copying; callers must not modify it while the buffer is in use.
     *
     * @param buf payload to retain
     * @return a buffer consumed by its first read unless made repeatable
     * @throws IOException if compression or temporary-file creation fails
     */
    public static LargeBuffer of(final byte[] buf) throws IOException {
        return LinuxLargeBuffer.available() ? new LinuxLargeBuffer("LargeBuffer", buf, true) : new LargeBuffer("LargeBuffer", buf, true);
    }

    /**
     * Creates a buffer with a diagnostic name used as its temporary-file prefix.
     *
     * @param buf payload to retain without further modification by the caller
     * @param name temporary-file prefix
     * @return a buffer with the same ownership rules as {@link #of(byte[])}
     * @throws IOException if compression or temporary-file creation fails
     */
    public static LargeBuffer of(final byte[] buf, final String name) throws IOException {
        return LinuxLargeBuffer.available() ? new LinuxLargeBuffer(name, buf, true) : new LargeBuffer(name, buf, true);
    }

    /**
     * Creates an uncompressed buffer, spilling it to a file if it reaches the offload threshold.
     *
     * @param buf payload to retain without further modification by the caller
     * @return a buffer with the same ownership rules as {@link #of(byte[])}
     * @throws IOException if temporary-file creation fails
     */
    public static LargeBuffer raw(final byte[] buf) throws IOException {
        return LinuxLargeBuffer.available() ? new LinuxLargeBuffer("LargeBuffer", buf, false) : new LargeBuffer("LargeBuffer", buf, false);
    }

    /**
     * Creates a named, uncompressed buffer.
     *
     * @param buf payload to retain without further modification by the caller
     * @param name temporary-file prefix
     * @return a buffer with the same ownership rules as {@link #raw(byte[])}
     * @throws IOException if temporary-file creation fails
     */
    public static LargeBuffer raw(final byte[] buf, final String name) throws IOException {
        return LinuxLargeBuffer.available() ? new LinuxLargeBuffer(name, buf, false) : new LargeBuffer(name, buf, false);
    }

    private static final Cleaner cleaner = Cleaner.create();

    private FileCleanup cleanup;

    private Cleaner.Cleanable cleanable;

    // Keep only the path: a cleanup action must not retain its owning buffer.
    private static final class FileCleanup
        implements Runnable {

        volatile Path file;

        FileCleanup(final Path file) { this.file = file; }

        @Override
        public void run() { delete(this.file); }
    }

    /**
     * Initializes the stored representation and registers cleanup when a temporary file is allocated.
     *
     * @param name temporary-file prefix
     * @param raw original payload
     * @param useComp whether the configured compression policy applies
     * @throws IOException if compression or storage allocation fails
     */
    protected LargeBuffer(final String name, final byte[] raw, final boolean useComp) throws IOException {
        this.name = name;
        this.length = raw.length;
        this.codec = useComp && compressionThreshold < raw.length && compHint.all(x -> x.test(raw)) //
            ? compressionCodec : Codec.NONE;

        this.buf = this.codec.compress(raw);

        if (offloadThreshold <= this.buf.length) {
            final var now = now();
            this.file = this.alloc0(name);
            this.buf = null;

            if (useLog) //
                this.log.log(INFO,
                    "buffer offloaded: len = %.2fkb, time = %d, file = %s".formatted(this.length / 1024d, now() - now, this.file.toAbsolutePath()));
        }
    }

    private Path alloc0(final String name) throws IOException {
        final var store = this.allocate(name, this.buf.length);
        this.registerCleaner(store);
        Files.write(store, this.buf);
        return store;
    }

    void moveTo(final Path to) throws IOException {
        this.registerCleaner(this.file = Files.move(this.file, to));
    }

    private void registerCleaner(final Path file) {
        if (this.cleanup == null) {
            this.cleanup = new FileCleanup(file);
            this.cleanable = cleaner.register(this, this.cleanup);
        } else { this.cleanup.file = file; }
    }

    /**
     * Allocates a path for the stored representation. Subclasses may choose a different filesystem.
     *
     * @param name temporary-file prefix
     * @param len stored size in bytes
     * @return path to write
     * @throws IOException if the path cannot be allocated
     */
    protected Path allocate(final String name, final long len) throws IOException {
        return Files.createTempFile(name + "-", ".tmp");
    }

    private boolean repeatable;

    /**
     * Retains the buffer after reads. The owner must call {@link #close()} when all readers have finished.
     *
     * @return this buffer
     * @throws IllegalStateException if the buffer has already been closed
     */
    public LargeBuffer repeatable() {
        this.checkState();
        this.repeatable = true;
        return this;
    }

    /**
     * Returns the current backing-file path, if any. The path can change during Linux memory-pressure handling.
     * Callers must not delete or modify the backing file.
     *
     * @return the current file, or an empty reference for an in-memory or closed buffer
     */
    public $<Path> file() { return opt(this.file); }

    /**
     * Returns the original, uncompressed payload size, including after the buffer has been closed.
     *
     * @return payload size in bytes
     */
    public long length() { return this.length; }

    private boolean useMemory() { return this.length <= (offloadThreshold * 2); }

    /**
     * Reads the entire uncompressed payload and closes a non-repeatable buffer, even if reading fails.
     * An uncompressed in-memory buffer may return its original array without copying.
     *
     * @return uncompressed bytes
     * @throws IOException if reading or decompression fails
     * @throws IllegalStateException if the buffer has already been closed
     */
    public byte[] read() throws IOException {
        this.checkState();

        try {
            return this.codec.decompress((this.buf != null) ? this.buf : Files.readAllBytes(this.file));
        } finally {
            if (!this.repeatable) this.close();
        }
    }

    /**
     * Writes the uncompressed payload to a path. Large streamed writes require a destination that does not exist;
     * small buffered writes may replace an existing file. Partial output can remain if a write fails.
     *
     * @param out destination path
     * @throws IOException if reading, decompression, or writing fails
     */
    public void write(final Path out) throws IOException {
        this.checkState();

        if (this.useMemory()) //
            Files.write(out, this.read());
        else  //
            try (var in = this.open()) { Files.copy(in, out); }
    }

    /**
     * Writes the uncompressed payload without closing or flushing the caller's output stream.
     * A failed write can leave partial output.
     *
     * @param out caller-owned destination
     * @throws IOException if reading, decompression, or writing fails
     */
    public void write(final OutputStream out) throws IOException {
        this.checkState();

        if (this.useMemory()) //
            out.write(this.read());
        else  //
            IO_(this.open(), in -> in.transferTo(out));
    }

    /**
     * Opens the uncompressed payload. The caller must close the returned stream before closing this buffer.
     * Small payloads are read in full; compressed file payloads are also fully decoded before this method returns.
     * A non-repeatable buffer is released either during this call or when the returned stream is closed.
     *
     * @return a caller-owned input stream
     * @throws IOException if reading or decompression fails
     * @throws IllegalStateException if the buffer has already been closed
     */
    public InputStream open() throws IOException {
        this.checkState();

        if (this.useMemory()) return bytesIn(this.read());

        if (this.buf != null) {
            try { return this.codec.decompress(bytesIn(this.buf)); } //
            finally { if (!this.repeatable) this.close(); }
        } else {
            return new FilterInputStream(this.openFile()) {

                @Override
                public byte[] readAllBytes() throws IOException { return this.in.readAllBytes(); }

                @Override
                public byte[] readNBytes(final int len) throws IOException { return this.in.readNBytes(len); }

                @Override
                public int readNBytes(final byte[] b, final int off, final int len) throws IOException {
                    return this.in.readNBytes(b, off, len);
                }

                @Override
                public long transferTo(final OutputStream out) throws IOException { return this.in.transferTo(out); }

                @Override
                public void close() throws IOException {
                    super.close();
                    if (!LargeBuffer.this.repeatable) LargeBuffer.this.close();
                }
            };
        }
    }

    private InputStream openFile() throws IOException {
        if (this.codec == Codec.NONE) return openRead(this.file);

        // A decoded stream owns its bytes, so the source file can be closed immediately.
        try (var in = openRead(this.file)) { return this.codec.decompress(in); }
    }

    private void checkState() { if (this.buf == null && this.file == null) throw new IllegalStateException("already closed"); }

    /**
     * Releases memory and deletes the backing file, if present. Repeated calls have no effect.
     * File-deletion failures are logged because cleanup may also be invoked by a {@link Cleaner}.
     */
    @Override
    public void close() {
        if (this.cleanable != null) this.cleanable.clean();
        this.buf = null;
        this.file = null;
    }

    private static void delete(final Path f) {
        if (f == null || !Files.exists(f)) return;

        if (useLog) LOG.log(WARNING, "delete uncleaned file: " + f.toAbsolutePath());

        try { Files.delete(f); } //
        catch (NoSuchFileException e) { /* Already removed. */ } //
        catch (Exception e) { LOG.log(ERROR, "can't delete: " + f.toAbsolutePath(), e); }
    }

    /**
     * Encodes a stored representation and restores its original bytes.
     * Implementations configured globally must support concurrent calls from different buffers.
     */
    public interface Codec {

        /** Stores bytes unchanged without copying them. */
        Codec NONE = new Codec() {

            @Override
            public byte[] compress(final byte[] raw) { return raw; }

            @Override
            public byte[] decompress(final byte[] stored) { return stored; }

            @Override
            public InputStream decompress(final InputStream in) { return in; }
        };

        /** JDK gzip compression with {@link Deflater#BEST_SPEED}, requiring no optional libraries. */
        Codec GZIP = new Codec() {

            @SuppressWarnings("DoubleBraceInitialization")
            @Override
            public byte[] compress(final byte[] raw) throws IOException {
                final var out = new ByteArrayOutputStream();
                try (var gzip = new GZIPOutputStream(out) {

                    { this.def.setLevel(Deflater.BEST_SPEED); }
                }) { gzip.write(raw); }
                return out.toByteArray();
            }

            @Override
            public byte[] decompress(final byte[] stored) throws IOException {
                try (var in = new GZIPInputStream(new ByteArrayInputStream(stored))) { return in.readAllBytes(); }
            }
        };

        /**
         * Encodes a payload without modifying the supplied array.
         *
         * @param raw uncompressed payload
         * @return stored bytes
         * @throws IOException if encoding fails
         */
        byte[] compress(byte[] raw) throws IOException;

        /**
         * Restores the original bytes without modifying the stored representation.
         *
         * @param stored encoded payload
         * @return uncompressed bytes
         * @throws IOException if decoding fails
         */
        byte[] decompress(byte[] stored) throws IOException;

        /**
         * Fully decodes the input before returning a stream independent of it.
         * The caller retains ownership of the input and may close it as soon as this method returns.
         * The identity codec {@link #NONE} is handled separately and returns the input itself.
         *
         * @param in caller-owned encoded input
         * @return a stream containing the decoded bytes
         * @throws IOException if reading or decoding fails
         */
        default InputStream decompress(final InputStream in) throws IOException {
            return new ByteArrayInputStream(this.decompress(in.readAllBytes()));
        }
    }
}

/** Moves temporary files away from memory-backed storage when Linux reports low available memory. */
final class LinuxLargeBuffer
    extends LargeBuffer {

    LinuxLargeBuffer(final String name, final byte[] raw, final boolean useComp) throws IOException { super(name, raw, useComp); }

    @SuppressWarnings("StaticVariableMayNotBeInitialized")
    private static Path lowMemTmpDir;

    private static void initDir(final Path dir) throws IOException {
        if (Files.isRegularFile(dir)) Files.delete(dir);
        if (!Files.exists(dir)) Files.createDirectory(dir);
    }

    private static final boolean AVAIL = LinuxMemInfo.available();

    static boolean available() { return AVAIL; }

    static void lowMemTmpDir(final Path x) throws IOException {
        LOG.log(INFO, "lowMemTmpDir: " + x);
        initDir(lowMemTmpDir = requireNonNull(x));
    }

    private static long lowMemThreshold = 1024 * 1024 * 1024;

    static void lowMemThreshold(final long x) throws IOException {
        LOG.log(INFO, "lowMemThreshold: " + x);
        lowMemThreshold = x;
    }

    @Override
    protected Path allocate(final String name, final long len) throws IOException {

        buffers.put(this, len);

        final var memInfo = LinuxMemInfo.get();
        if (lowMemThreshold <= memInfo.memAvailable) return super.allocate(name, len);

        LOG.log(WARNING, "LOW-MEMORY DETECTED: \n{0}", memInfo);
        return lowMemTmpDir.resolve("%s-%d.tmp".formatted(name, Math.abs(ThreadLocalRandom.current().nextLong())));
    }

    private static final Timer timer;

    // Monitoring must not keep an otherwise unreachable buffer alive.
    private static final Map<LinuxLargeBuffer, Long> buffers = Collections.synchronizedMap(new WeakHashMap<>());

    private final ReentrantLock lock = new ReentrantLock();

    private static final boolean verbose = false;

    private static void resolveLowMem() {

        final var memInfo = LinuxMemInfo.get();
        if (lowMemThreshold < memInfo.memAvailable) {
            if (verbose) LOG.log(INFO, "lowMemThreshold = {0} \n{1}", lowMemThreshold, memInfo);
            return;
        }

        if (verbose) //
            LOG.log(WARNING, "LOW-MEMORY DETECTED: buffers = {0} \n{1}", buffersInfo(), memInfo);
        else //
            LOG.log(WARNING, "LOW-MEMORY DETECTED: \n{0}", memInfo);

        await($(buffersSnapshot()).entries() //
            .take(e -> e.key.file().present()) // Exclude buffers whose initial file write has not completed.
            .map(e -> tuple(e.key, e.val)).list() //
            .order(_2()) //
            .map(_1()) //
            .reverse() //
            .map(lb -> async(() -> if_try(LinuxMemInfo.get().memAvailable < lowMemThreshold, () -> lb.realloc()))));
    }

    private static Map<LinuxLargeBuffer, Long> buffersSnapshot() {
        // Copy under the registry lock; keep candidates alive without holding that lock during I/O.
        synchronized (buffers) { return new HashMap<>(buffers); }
    }

    private static String buffersInfo() { return $(buffersSnapshot()).entries().map(e -> e.key.file().get().toAbsolutePath() + ", " + e.val).join("\n"); }

    private void realloc() throws IOException {

        final var from = this.file().get();

        if (!Files.exists(from)) {
            if (verbose) LOG.log(INFO, "realloc: file not ready %s, %.1fmb".formatted(from.toAbsolutePath(), MiB(from)));
            return;
        }

        final var to = lowMemTmpDir.resolve(from.getFileName());
        if (from.equals(to)) {
            LOG.log(WARNING, "already realloc: %s, %.1fmb".formatted(from.toAbsolutePath(), MiB(from)));
            buffers.remove(this);
            return;
        }

        this.lock.lock();
        try {
            LOG.log(WARNING, "realloc: move %s to %s, %.1fmb".formatted(from.toAbsolutePath(), to.toAbsolutePath(), MiB(from)));
            this.moveTo(to);
            buffers.remove(this);
        } catch (IOException e) {
            LOG.log(ERROR, "realloc failed", e);
        } finally { this.lock.unlock(); }
    }

    private static double MiB(final Path f) throws IOException { return l2d(Files.size(f)) / 1024 / 1024; }

    @Override
    public InputStream open() throws IOException {
        this.lock.lock();
        try { return super.open(); } //
        finally { this.lock.unlock(); }
    }

    @Override
    public void close() {
        this.lock.lock();
        try {
            buffers.remove(this);
            super.close();
        } finally { this.lock.unlock(); }
    }

    static {
        if (available()) {
            let(() -> initDir(lowMemTmpDir = Path.of(System.getProperty("java.io.tmpdir"), ".indolently-lb")));

            final var name = LinuxLargeBuffer.class.getSimpleName() + "-lowMemMonitor";
            (timer = new Timer(name, true)).schedule(new TimerTask() {

                @Override
                public void run() { resolveLowMem(); }
            }, 1000, 1000 * 5);
            LOG.log(INFO, "start {0}", name);
        } else //
            timer = null;
    }
}
