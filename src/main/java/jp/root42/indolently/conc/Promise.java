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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.*;
import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface Promise<T> {

    CompletableFuture<T> cfuture();

    default Future<T> future() { return this.cfuture(); }

    default T resolve() { return this.cfuture().join(); }

    default boolean cancel() { return this.future().cancel(true); }

    default boolean cancelGracefully() { return this.future().cancel(false); }

    default boolean cancelled() { return this.future().isCancelled(); }

    default boolean done() { return this.future().isDone(); }

    default boolean failed() { return this.cfuture().isCompletedExceptionally(); }

    <U> Promise<U> thenAsync(Function<? super T, ? extends U> f);

    <U> Promise<U> then(Function<? super T, ? extends U> f);

    Promise<T> fail(Function<? super Exception, ? extends T> f);

    <U> Promise<U> last(BiFunction<? super T, Exception, ? extends U> f);

    Promise<T> last(BiConsumer<? super T, Exception> f);

    static <T> Promise<T> resolve(final T val) { return of(CompletableFuture.completedFuture(val)); }

    static <T> Promise<T> reject(final Exception e) { return of(CompletableFuture.failedFuture(requireNonNull(e))); }

    static <T> Promise<List<T>> all(final Iterable<? extends Promise<? extends T>> promises) {
        return of(CompletableFuture.supplyAsync(() -> list(promises).map(x -> x.resolve())));
    }

    @SuppressWarnings("ConstantConditions")
    static <T> Promise<T> any(final Iterable<? extends Promise<? extends T>> promises) {
        return cast(of(CompletableFuture.anyOf(cast(promises))));
    }

    static <T> Promise<T> of(final CompletableFuture<T> f) { return new PromiseImpl<>(f); }
}

class PromiseImpl<T>
    implements Promise<T> {

    private final CompletableFuture<T> cfuture;

    PromiseImpl(final CompletableFuture<T> cfuture) { this.cfuture = cfuture; }

    @Override
    public CompletableFuture<T> cfuture() { return this.cfuture; }

    @Override
    public <U> Promise<U> thenAsync(final Function<? super T, ? extends U> f) {
        return new PromiseImpl<>(this.cfuture.thenApplyAsync(f));
    }

    @Override
    public <U> Promise<U> then(final Function<? super T, ? extends U> f) {
        return new PromiseImpl<>(this.cfuture.thenApply(f));
    }

    @Override
    public Promise<T> fail(final Function<? super Exception, ? extends T> f) {
        return new PromiseImpl<>(
            this.cfuture.exceptionally(e -> e instanceof Exception ? f.apply((Exception) e) : raise(e)));
    }

    @Override
    public <U> Promise<U> last(final BiFunction<? super T, Exception, ? extends U> f) {
        return new PromiseImpl<>(
            this.cfuture.handle((v, e) -> e instanceof Exception ? f.apply(v, cast(e)) : raise(e)));
    }

    @Override
    public Promise<T> last(final BiConsumer<? super T, Exception> f) {
        return new PromiseImpl<>(this.cfuture.whenComplete((v, e) -> {
            if (e instanceof Exception) f.accept(v, cast(e));
            else raise(e);
        }));
    }

    @Override
    public String toString() { return "Promise(" + this.cfuture + ")"; }
}
