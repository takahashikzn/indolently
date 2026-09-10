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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import jp.root42.indolently.regex.Regex;

import static java.lang.System.Logger.Level.ERROR;
import static jp.root42.indolently.Indolently.*;


/**
 * A snapshot of Linux host memory counters from {@code /proc/meminfo}.
 * All sizes are expressed in bytes; missing counters have the value {@code -1}.
 * These are host counters, not a container's cgroup memory limits.
 *
 * @author takahashikzn
 */
public final class LinuxMemInfo {

    private static final System.Logger log = System.getLogger(LinuxMemInfo.class.getSimpleName());

    private static final Path FILE = Paths.get("/proc/meminfo");

    /**
     * Checks whether the Linux memory-information file exists.
     *
     * @return whether memory counters can be read on this system
     */
    public static boolean available() { return Files.exists(FILE); }

    private final List<String> content;

    /** Value of {@code MemTotal}, in bytes; -1 when absent. */
    public final long memTotal;

    /** Value of {@code MemFree}, in bytes; -1 when absent. */
    public final long memFree;

    /** Value of {@code MemAvailable}, in bytes; -1 when absent. */
    public final long memAvailable;

    /** Value of {@code Buffers}, in bytes; -1 when absent. */
    public final long buffers;

    /** Value of {@code Cached}, in bytes; -1 when absent. */
    public final long cached;

    /** Value of {@code SwapCached}, in bytes; -1 when absent. */
    public final long swapCached;

    /** Value of {@code Active}, in bytes; -1 when absent. */
    public final long active;

    /** Value of {@code Inactive}, in bytes; -1 when absent. */
    public final long inactive;

    /** Value of {@code Active(anon)}, in bytes; -1 when absent. */
    public final long activeAnon;

    /** Value of {@code Inactive(anon)}, in bytes; -1 when absent. */
    public final long inactiveAnon;

    /** Value of {@code Active(file)}, in bytes; -1 when absent. */
    public final long activeFile;

    /** Value of {@code Inactive(file)}, in bytes; -1 when absent. */
    public final long inactiveFile;

    /** Value of {@code SwapTotal}, in bytes; -1 when absent. */
    public final long swapTotal;

    /** Value of {@code SwapFree}, in bytes; -1 when absent. */
    public final long swapFree;

    /** Value of {@code AnonPages}, in bytes; -1 when absent. */
    public final long anonPages;

    /** Value of {@code Mapped}, in bytes; -1 when absent. */
    public final long mapped;

    /** Value of {@code Shmem}, in bytes; -1 when absent. */
    public final long shmem;

    /** Value of {@code Slab}, in bytes; -1 when absent. */
    public final long slab;

    /** Value of {@code SReclaimable}, in bytes; -1 when absent. */
    public final long sReclaimable;

    /** Value of {@code SUnreclaim}, in bytes; -1 when absent. */
    public final long sUnreclaim;

    /** Value of {@code KernelStack}, in bytes; -1 when absent. */
    public final long kernelStack;

    /** Value of {@code PageTables}, in bytes; -1 when absent. */
    public final long pageTables;

    /** Value of {@code CommitLimit}, in bytes; -1 when absent. */
    public final long commitLimit;

    /** Value of {@code Committed_AS}, in bytes; -1 when absent. */
    public final long committedAs;

    /** Value of {@code VmallocTotal}, in bytes; -1 when absent. */
    public final long vmallocTotal;

    /** Value of {@code VmallocUsed}, in bytes; -1 when absent. */
    public final long vmallocUsed;

    /** Value of {@code VmallocChunk}, in bytes; -1 when absent. */
    public final long vmallocChunk;

    /** Value of {@code AnonHugePages}, in bytes; -1 when absent. */
    public final long anonHugePages;

    // Instances are assembled from one read of /proc/meminfo.
    private LinuxMemInfo(final List<String> content, final long memTotal, final long memFree, final long memAvailable, final long buffers, final long cached,
        final long swapCached, final long active, final long inactive, final long activeAnon, final long inactiveAnon, final long activeFile,
        final long inactiveFile, final long swapTotal, final long swapFree, final long anonPages, final long mapped, final long shmem, final long slab,
        final long sReclaimable, final long sUnreclaim, final long kernelStack, final long pageTables, final long commitLimit, final long committedAs,
        final long vmallocTotal, final long vmallocUsed, final long vmallocChunk, final long anonHugePages) {

        this.content = content;
        this.memTotal = memTotal;
        this.memFree = memFree;
        this.memAvailable = memAvailable;
        this.buffers = buffers;
        this.cached = cached;
        this.swapCached = swapCached;
        this.active = active;
        this.inactive = inactive;
        this.activeAnon = activeAnon;
        this.inactiveAnon = inactiveAnon;
        this.activeFile = activeFile;
        this.inactiveFile = inactiveFile;
        this.swapTotal = swapTotal;
        this.swapFree = swapFree;
        this.anonPages = anonPages;
        this.mapped = mapped;
        this.shmem = shmem;
        this.slab = slab;
        this.sReclaimable = sReclaimable;
        this.sUnreclaim = sUnreclaim;
        this.kernelStack = kernelStack;
        this.pageTables = pageTables;
        this.commitLimit = commitLimit;
        this.committedAs = committedAs;
        this.vmallocTotal = vmallocTotal;
        this.vmallocUsed = vmallocUsed;
        this.vmallocChunk = vmallocChunk;
        this.anonHugePages = anonHugePages;
    }

    private static final Regex ptrn = re("(?i)^\\s*(.+):\\s*(\\d+)\\s*kB$");

    /**
     * Reads the current memory counters. Check {@link #available()} before calling on a non-Linux system.
     *
     * @return a snapshot with a nonnegative available-memory counter
     */
    public static LinuxMemInfo get() {

        final List<String> lines = read();

        long memTotal = -1;
        long memFree = -1;
        long memAvailable = -1;
        long buffers = -1;
        long cached = -1;
        long swapCached = -1;
        long active = -1;
        long inactive = -1;
        long activeAnon = -1;
        long inactiveAnon = -1;
        long activeFile = -1;
        long inactiveFile = -1;
        long swapTotal = -1;
        long swapFree = -1;
        long anonPages = -1;
        long mapped = -1;
        long shmem = -1;
        long slab = -1;
        long sReclaimable = -1;
        long sUnreclaim = -1;
        long kernelStack = -1;
        long pageTables = -1;
        long commitLimit = -1;
        long committedAs = -1;
        long vmallocTotal = -1;
        long vmallocUsed = -1;
        long vmallocChunk = -1;
        long anonHugePages = -1;

        for (final var line: lines) {
            final var match = ptrn.matcher(line);
            if (!match.matches()) continue;
            final var name = match.group(1);
            final var val = match.group(2);

            switch (name) {
                case "MemTotal" -> memTotal = toVal(val);
                case "MemFree" -> memFree = toVal(val);
                case "MemAvailable" -> memAvailable = toVal(val);
                case "Buffers" -> buffers = toVal(val);
                case "Cached" -> cached = toVal(val);
                case "SwapCached" -> swapCached = toVal(val);
                case "Active" -> active = toVal(val);
                case "Inactive" -> inactive = toVal(val);
                case "Active(anon)" -> activeAnon = toVal(val);
                case "Inactive(anon)" -> inactiveAnon = toVal(val);
                case "Active(file)" -> activeFile = toVal(val);
                case "Inactive(file)" -> inactiveFile = toVal(val);
                case "SwapTotal" -> swapTotal = toVal(val);
                case "SwapFree" -> swapFree = toVal(val);
                case "AnonPages" -> anonPages = toVal(val);
                case "Mapped" -> mapped = toVal(val);
                case "Shmem" -> shmem = toVal(val);
                case "Slab" -> slab = toVal(val);
                case "SReclaimable" -> sReclaimable = toVal(val);
                case "SUnreclaim" -> sUnreclaim = toVal(val);
                case "KernelStack" -> kernelStack = toVal(val);
                case "PageTables" -> pageTables = toVal(val);
                case "CommitLimit" -> commitLimit = toVal(val);
                case "Committed_AS" -> committedAs = toVal(val);
                case "VmallocTotal" -> vmallocTotal = toVal(val);
                case "VmallocUsed" -> vmallocUsed = toVal(val);
                case "VmallocChunk" -> vmallocChunk = toVal(val);
                case "AnonHugePages" -> anonHugePages = toVal(val);
            }
        }

        ASSERT(0 <= memAvailable);

        return new LinuxMemInfo(lines, memTotal, memFree, memAvailable, buffers, cached, swapCached, active, inactive, activeAnon, inactiveAnon, activeFile,
            inactiveFile, swapTotal, swapFree, anonPages, mapped, shmem, slab, sReclaimable, sUnreclaim, kernelStack, pageTables, commitLimit, committedAs,
            vmallocTotal, vmallocUsed, vmallocChunk, anonHugePages);
    }

    private static long toVal(final String val) { return Long.parseLong(val) * 1024; }

    private static List<String> read() {
        try {
            return Files.readAllLines(FILE);
        } catch (IOException e) {
            log.log(ERROR, "", e);
            return list();
        }
    }

    @Override
    public String toString() { return $(this.content).join("\n"); }
}
