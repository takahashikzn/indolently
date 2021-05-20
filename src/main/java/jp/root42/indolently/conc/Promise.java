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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import jp.root42.indolently.ref.$$;
import jp.root42.indolently.ref.$$.None;

import static java.util.Objects.*;
import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface Promise<T> {

    T resolve();

    $$<T, None> resolve(long timeout);

    boolean cancel();

    boolean cancelled();

    boolean done();

    boolean failed();

    Future<T> future();

    CompletableFuture<T> cfuture();

    <U> Promise<U> thenAsync(Function<? super T, ? extends U> f);

    default Promise<T> thenAsync(final Consumer<? super T> f) {
        return this.thenAsync(x -> {
            f.accept(x);
            return x;
        });
    }

    <U> Promise<U> then(Function<? super T, ? extends U> f);

    default Promise<T> then(final Consumer<? super T> f) {
        return this.then(x -> {
            f.accept(x);
            return x;
        });
    }

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

    static <T> Promise<T> none() { return resolve(null); }
}

class PromiseImpl<T>
    implements Promise<T> {

    private final CompletableFuture<T> delegate;

    PromiseImpl(final CompletableFuture<T> delegate) { this.delegate = delegate; }

    @Override
    public T resolve() { return this.delegate.join(); }

    @Override
    public $$<T, None> resolve(final long timeout) {
        try { return left(this.delegate.get(timeout, TimeUnit.MILLISECONDS)); } //
        catch (final TimeoutException e) { return right(NONE); } //
        catch (final InterruptedException e) { return raise(e); } //
        catch (final ExecutionException e) { return raise(e.getCause()); } //
    }

    @Override
    public boolean cancel() { return this.delegate.cancel(true); }

    @Override
    public boolean cancelled() { return this.delegate.isCancelled(); }

    @Override
    public boolean done() { return this.delegate.isDone(); }

    @Override
    public boolean failed() { return this.delegate.isCompletedExceptionally(); }

    @Override
    public Future<T> future() { return this.delegate; }

    @Override
    public CompletableFuture<T> cfuture() { return this.delegate; }

    @Override
    public <U> Promise<U> thenAsync(final Function<? super T, ? extends U> f) {
        return new PromiseImpl<>(this.delegate.thenApplyAsync(f));
    }

    @Override
    public <U> Promise<U> then(final Function<? super T, ? extends U> f) {
        return new PromiseImpl<>(this.delegate.thenApply(f));
    }

    @Override
    public Promise<T> fail(final Function<? super Exception, ? extends T> f) {
        return new PromiseImpl<>(
            this.delegate.exceptionally(e -> e instanceof Exception ? f.apply((Exception) e) : raise(e)));
    }

    @Override
    public <U> Promise<U> last(final BiFunction<? super T, Exception, ? extends U> f) {
        return new PromiseImpl<>(
            this.delegate.handle((v, e) -> e instanceof Exception ? f.apply(v, cast(e)) : raise(e)));
    }

    @Override
    public Promise<T> last(final BiConsumer<? super T, Exception> f) {
        return new PromiseImpl<>(this.delegate.whenComplete((v, e) -> {
            if (e instanceof Exception) f.accept(v, cast(e));
            else raise(e);
        }));
    }

    @Override
    public String toString() { return "Promise(" + this.delegate + ")"; }
}