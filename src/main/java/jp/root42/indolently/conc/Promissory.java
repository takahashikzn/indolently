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

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Function;

import jp.root42.indolently.function.RunnableE;
import jp.root42.indolently.function.SupplierE;

import static jp.root42.indolently.Expressive.*;


/**
 * @author takahashikzn
 */
public class Promissory {

    /** non private for subtyping. */
    protected Promissory() {}

    public static CompletableFuture<Void> async(final Runnable run) { return CompletableFuture.runAsync(run); }

    public static CompletableFuture<Void> asyncE(final RunnableE<Exception> run) {
        return async(() -> {
            try { run.run(); } //
            catch (final Exception e) { raise(e); }
        });
    }

    public static <T> CompletableFuture<T> asyncE(final SupplierE<T, Exception> run) {
        return async(() -> {
            try { return run.get(); } //
            catch (final Exception e) { return raise(e); }
        });
    }

    public static <T> CompletableFuture<T> async(final Callable<T> run) { return asyncE(run::call); }

    public static <T> T await(final Future<T> promise) {
        try { return promise.get(); } //
        catch (final InterruptedException e) { return raise(e); } //
        catch (final ExecutionException e) { return raise(e.getCause()); }
    }

    public static <T> T await(final CompletableFuture<T> promise, final Function<Exception, T> onfail) {
        return await(promise.exceptionally(e -> (e instanceof Exception) ? onfail.apply((Exception) e) : raise(e)));
    }
}
