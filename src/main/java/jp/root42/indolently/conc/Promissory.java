// Copyright 2021 takahashikzn
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

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jp.root42.indolently.function.RunnableE;
import jp.root42.indolently.ref.$$;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class Promissory {

    /** non private for subtyping. */
    protected Promissory() { }

    private static boolean useVirtualThread = true;

    private static Executor virtualThreadExecutor;

    private static Executor platformThreadExecutor = ForkJoinPool.commonPool();

    static {
        try {
            final var builderCls = Class.forName("java.lang.Thread$Builder");
            var builder = Thread.class.getDeclaredMethod("ofVirtual").invoke(null);
            builder = builderCls.getMethod("name", String.class, long.class).invoke(builder, "virtual-", 0L);
            final var factory = builderCls.getMethod("factory").invoke(builder);

            virtualThreadExecutor = (Executor) Executors.class.getDeclaredMethod("newThreadPerTaskExecutor", ThreadFactory.class).invoke(null, factory);
        } //
        catch (final ClassNotFoundException | NoSuchMethodException ignored) { } //
        catch (final InvocationTargetException | IllegalAccessException e) {
            if (!e.getCause().getMessage().contains("--enable-preview")) e.printStackTrace();
        }
    }

    public static boolean virtualThreadAvailable() { return virtualThreadExecutor != null; }

    public static boolean useVirtualThread() { return useVirtualThread; }

    public static void useVirtualThread(final boolean x) { useVirtualThread = x; }

    public static Executor executor() {
        return virtualThreadAvailable() && useVirtualThread ? virtualThreadExecutor : platformThreadExecutor;
    }

    public static void executor(final Executor x) { platformThreadExecutor = Objects.requireNonNull(x); }

    public static Promise<Void> async(final RunnableE<? super Exception> run) { return async(run, executor()); }

    public static Promise<Void> async(final RunnableE<? super Exception> run, final Executor exec) {
        return async(() -> {
            run.run();
            return null;
        }, exec);
    }

    public static <T> Promise<T> async(final Callable<? extends T> run) { return async(run, executor()); }

    public static <T> Promise<T> async(final Callable<? extends T> run, final Executor exec) {
        return new PromiseCFuture<T>(CompletableFuture.supplyAsync(() -> {
            try { return run.call(); } //
            catch (final Exception e) { return raise(e); }
        }, exec));
    }

    public static <T> T await(final Promise<? extends T> promise) { return promise.resolve(); }

    public static <T> $$<T, $$.None> await(final Promise<? extends T> promise, final long timeout) {
        return cast(promise.resolve(timeout));
    }

    public static <T> T await(final Future<? extends T> promise) {
        try { return promise.get(); } //
        catch (final InterruptedException e) { return raise(e); } //
        catch (final ExecutionException e) { return raise(e.getCause()); }
    }

    public static <T> $$<T, $$.None> await(final Future<? extends T> promise, final long timeout) {
        try { return left(promise.get(timeout, TimeUnit.MILLISECONDS)); } //
        catch (final TimeoutException e) { return right(NONE); } //
        catch (final InterruptedException e) { return raise(e); } //
        catch (final ExecutionException e) { return raise(e.getCause()); }
    }
}
