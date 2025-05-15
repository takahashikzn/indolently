// Copyright 2014 takahashikzn
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
package jp.root42.indolently.ref;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import jp.root42.indolently.Indolently;
import jp.root42.indolently.function.ConsumerE;
import jp.root42.indolently.function.FunctionE;
import jp.root42.indolently.function.RunnableE;
import jp.root42.indolently.function.SupplierE;

import static java.util.Objects.requireNonNull;


/**
 * The {@link Optional} alternative.
 *
 * @param <T> element type
 * @author takahashikzn
 */
public sealed interface $<T>
    extends Serializable, Supplier<T>
    permits $.Just, $.None {

    record Just<T>(T val)
        implements $<T> {

        @Deprecated
        public Just(final T val) { this.val = requireNonNull(val); }

        @Override
        public T get() { return this.val; }

        @Override
        public boolean empty() { return false; }

        @Override
        public T orElse(final T or) { return this.val; }

        @Override
        public T orElseGet(final Supplier<? extends T> f) { return this.val; }

        @Override
        public <X extends Throwable> T orFail(final Supplier<? extends X> f) throws X { return this.val; }

        @Override
        public <U, E extends Exception> $<T> doTry(final ConsumerE<? super T, E> f) throws E {
            f.accept(this.val);
            return this;
        }

        @Override
        public <U, E extends Exception> $<T> doTry(final ConsumerE<? super T, E> f, final RunnableE<E> orAction) throws E {
            f.accept(this.val);
            return this;
        }

        @Override
        public <U, E extends Exception> $<U> mapTry(final FunctionE<? super T, ? extends U, E> f) throws E { return $.of(f.apply(this.val)); }

        @Override
        public <U, E extends Exception> $<U> fmapTry(final FunctionE<? super T, ? extends $<? extends U>, E> f) throws E {
            final var x = f.apply(this.val);
            return (x == null) ? none() : Indolently.cast(x);
        }

        @Override
        public <E extends Exception> T orElseTry(final SupplierE<? extends T, E> f) throws E { return this.val; }

        @Override
        public boolean equals(final $<? extends T> that) { return this.equals0(that); }

        private boolean equals0(final $<?> that) { return (this == that) || (that instanceof $.Just<?> j && Indolently.equiv(this.val, j.val)); }

        @Override
        public int hashCode() { return Objects.hash(this.getClass(), this.val); }

        @Deprecated
        @Override
        public boolean equals(final Object o) { return this == o || (o instanceof $<?> that && this.equals0(that)); }

        @Override
        public String toString() { return "$(" + this.get() + ")"; }

        private static final $<Boolean> T = of(true);

        private static final $<Boolean> F = of(false);
    }

    final class None<T>
        implements $<T> {

        private static final None<?> NONE = new None<>();

        private None() { }

        @Deprecated
        @Override
        public T get() throws NoSuchElementException { throw new NoSuchElementException("No value present"); }

        @Override
        public boolean empty() { return true; }

        @Override
        public T orElse(final T or) { return or; }

        @Override
        public T orElseGet(final Supplier<? extends T> f) { return f.get(); }

        @Override
        public <X extends Throwable> T orFail(final Supplier<? extends X> f) throws X { throw f.get(); }

        @Override
        public <U, E extends Exception> $<T> doTry(final ConsumerE<? super T, E> f) throws E { return this; }

        @Override
        public <U, E extends Exception> $<T> doTry(final ConsumerE<? super T, E> f, final RunnableE<E> orAction) throws E {
            orAction.run();
            return this;
        }

        @Override
        public <U, E extends Exception> $<U> mapTry(final FunctionE<? super T, ? extends U, E> f) throws E { return none(); }

        @Override
        public <U, E extends Exception> $<U> fmapTry(final FunctionE<? super T, ? extends $<? extends U>, E> f) throws E { return none(); }

        @Override
        public <E extends Exception> T orElseTry(final SupplierE<? extends T, E> f) throws E { return f.get(); }

        @Override
        public boolean equals(final $<? extends T> that) {
            assert that == null || this == that;
            return this == that;
        }

        @Override
        public String toString() { return "$<empty>"; }
    }

    static <T> Just<T> just(final T val) { return new Just<>(val); }

    static <T> None<T> none() { return Indolently.cast(None.NONE); }

    static <T> $<T> of(final T val) { return (val == null) ? none() : just(val); }

    @SuppressWarnings({ "OptionalAssignedToNull", "OptionalUsedAsFieldOrParameterType" })
    static <T> $<T> of(final Optional<? extends T> val) { return (val == null) || val.isEmpty() ? none() : just(Indolently.cast(val)); }

    default Optional<T> unwrap() { return this.empty() ? Optional.empty() : Optional.of(this.get()); }

    @Override
    T get() throws NoSuchElementException;

    default T orFail() { return this.get(); }

    default <U> U done(final Function<? super T, ? extends U> f) { return this.doneTry(f::apply); }

    default <U, E extends Exception> U doneTry(final FunctionE<? super T, ? extends U, E> f) throws E { return this.mapTry(f).get(); }

    boolean empty();

    default boolean present() { return !this.empty(); }

    default $<T> if_(final Predicate<? super T> f) { return this.test(f) ? this : none(); }

    // alias
    default $<T> when(final Predicate<? super T> f) { return this.if_(f); }

    // alias
    @Deprecated
    default $<T> filter(final Predicate<? super T> f) { return this.if_(f); }

    default $<T> do_(final Consumer<? super T> f) { return this.doTry(f::accept); }

    default $<T> do_(final Consumer<? super T> action, final Runnable orAction) { return this.doTry(action::accept, orAction::run); }

    // alias
    default $<T> tap(final Consumer<? super T> f) { return this.do_(f); }

    // alias
    default $<T> tap(final Consumer<? super T> action, final Runnable orAction) { return this.do_(action, orAction); }

    // alias
    default $<T> then(final Consumer<? super T> f) { return this.tap(f); }

    // alias
    default $<T> then(final Consumer<? super T> action, final Runnable orAction) { return this.tap(action, orAction); }

    default $<Boolean> test$(final Predicate<? super T> f) { return this.empty() ? none() : this.test(f) ? Just.T : Just.F; }

    default boolean test(final Predicate<? super T> f) { return this.present() && f.test(this.get()); }

    default <U> $<U> cast(final Class<U> type) { return this.if_(type::isInstance).map(type::cast); }

    default <U> $<U> map(final Function<? super T, ? extends U> f) { return this.mapTry(f::apply); }

    default <U> $<U> fmap(final Function<? super T, ? extends $<? extends U>> f) { return this.fmapTry(f::apply); }

    default $<T> fold(final Function<? super T, ? extends $<? extends T>> f) { return this.foldTry(f::apply); }

    default <S> $<$2<T, S>> and$(final Supplier<? extends $<? extends S>> f) { return this.and$Try(f::get); }

    default <S> $<$2<T, S>> and$(final $<? extends S> and) { return this.and$(() -> and); }

    default $<T> or$(final Supplier<? extends $<? extends T>> f) { return this.or$Try(f::get); }

    default $<T> or$(final $<? extends T> or) { return this.present() ? this : Indolently.cast(or); }

    default Stream<T> stream() { return this.empty() ? Stream.empty() : Stream.of(this.get()); }

    default T or(final T or) { return this.orElse(or); }

    T orElse(final T or);

    default T orNull() { return this.orElse(null); }

    default T or(final Supplier<? extends T> f) { return this.orElseGet(f); }

    T orElseGet(final Supplier<? extends T> f);

    default <E extends Exception> T orTry(final SupplierE<? extends T, E> f) throws E { return this.orElseTry(f); }

    <X extends Throwable> T orFail(final Supplier<? extends X> f) throws X;

    <U, E extends Exception> $<T> doTry(final ConsumerE<? super T, E> f) throws E;

    <U, E extends Exception> $<T> doTry(final ConsumerE<? super T, E> f, final RunnableE<E> orAction) throws E;

    <U, E extends Exception> $<U> mapTry(final FunctionE<? super T, ? extends U, E> f) throws E;

    <U, E extends Exception> $<U> fmapTry(final FunctionE<? super T, ? extends $<? extends U>, E> f) throws E;

    default <E extends Exception> $<T> foldTry(final FunctionE<? super T, ? extends $<? extends T>, E> f) throws E {
        final var ret = this.fmapTry(f::apply);
        return ret.empty() ? this : Indolently.cast(ret);
    }

    default <E extends Exception> $<T> or$Try(final SupplierE<? extends $<? extends T>, E> f) throws E {
        if (this.present()) return this;
        final var x = f.get();
        return (x == null || x.empty()) ? none() : Indolently.cast(x);
    }

    default <S, E extends Exception> $<$2<T, S>> and$Try(final SupplierE<? extends $<? extends S>, E> f) throws E {
        if (this.empty()) return none();
        final var x = f.get();
        return (x == null) ? none() : x.map(y -> Indolently.tuple(this.get(), y));
    }

    <E extends Exception> T orElseTry(final SupplierE<? extends T, E> f) throws E;

    default boolean eq(final T that) { return this.test(x -> x.equals(that)); }

    boolean equals($<? extends T> that);

    @Override
    @Deprecated
    boolean equals(Object that);
}
