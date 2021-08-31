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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import jp.root42.indolently.function.ConsumerE;
import jp.root42.indolently.function.FunctionE;
import jp.root42.indolently.function.RunnableE;
import jp.root42.indolently.function.SupplierE;

import static java.util.Objects.*;
import static jp.root42.indolently.Indolently.*;


/**
 * The {@link Optional} alternative.
 *
 * @param <T> element type
 * @author takahashikzn
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class $<T>
    implements Serializable, Supplier<T> {

    @SuppressWarnings("PublicField")
    public final Optional<T> opt; // NOPMD

    public static <T> $<T> of(final T val) { return (val == null) ? none() : new $<>(val); }

    @SuppressWarnings("OptionalAssignedToNull")
    public static <T> $<T> of(final Optional<? extends T> val) {
        return (val == null) || val.isEmpty() ? none() : new $<>(cast(val));
    }

    private static final $<?> NONE = new $<>(Optional.empty());

    public static <T> $<T> none() { return cast(NONE); }

    private $(final T val) { this(Optional.ofNullable(val)); }

    private $(final Optional<T> opt) { this.opt = requireNonNull(opt); }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public T get() { return this.opt.get(); }

    public boolean empty() { return this == NONE || this.opt.isEmpty(); }

    public boolean present() { return !this.empty(); }

    public $<T> tap(final Consumer<? super T> f) { return this.tapTry(f::accept); }

    public $<T> tap(final Consumer<? super T> action, final Runnable orAction) {
        return this.tapTry(action::accept, orAction::run);
    }

    public $<T> when(final Predicate<? super T> f) {
        return this.empty() ? none() : this.opt.filter(f).map($::of).orElse(none());
    }

    // alias
    public $<T> filter(final Predicate<? super T> f) { return this.when(f); }

    private static final $<Boolean> T = of(true);

    private static final $<Boolean> F = of(false);

    public $<Boolean> exam(final Predicate<? super T> f) { return this.empty() ? none() : this.test(f) ? T : F; }

    public boolean test(final Predicate<? super T> f) { return !this.empty() && this.when(f).present(); }

    public <U> $<U> map(final Function<? super T, ? extends U> f) { return this.mapTry(f::apply); }

    public <U> $<U> fmap(final Function<? super T, ? extends $<? extends U>> f) { return this.fmapTry(f::apply); }

    public <S> $<$2<T, S>> and$(final Supplier<? extends $<? extends S>> f) { return this.and$Try(f::get); }

    public <S> $<$2<T, S>> and$(final $<? extends S> and) { return this.and$(() -> and); }

    public $<T> or$(final Supplier<? extends $<? extends T>> f) { return this.or$Try(f::get); }

    public $<T> or$(final $<? extends T> or) { return this.present() ? this : cast(or); }

    public Stream<T> stream() { return this.opt.stream(); }

    public T or(final T or) { return this.orElse(or); }

    public T orElse(final T or) { return this.opt.orElse(or); }

    public T orNull() { return this.orElse(null); }

    public T or(final Supplier<? extends T> f) { return this.orElseGet(f); }

    public T orElseGet(final Supplier<? extends T> f) { return this.opt.orElseGet(f); }

    public <E extends Exception> T orTry(final SupplierE<? extends T, E> f) throws E { return this.orElseTry(f); }

    public T orFail() { return this.opt.orElseThrow(); }

    public <X extends Throwable> T orFail(final Supplier<? extends X> f) throws X { return this.opt.orElseThrow(f); }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public <U, E extends Exception> $<T> tapTry(final ConsumerE<? super T, E> f) throws E {
        if (this.empty()) return none();
        f.accept(this.opt.get());
        return this;
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public <U, E extends Exception> $<T> tapTry(final ConsumerE<? super T, E> f, final RunnableE<E> orAction) throws E {
        if (this.empty()) {
            orAction.run();
            return none();
        } else {
            f.accept(this.opt.get());
            return this;
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public <U, E extends Exception> $<U> mapTry(final FunctionE<? super T, ? extends U, E> f) throws E {
        return this.empty() ? none() : of(f.apply(this.opt.get()));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public <U, E extends Exception> $<U> fmapTry(final FunctionE<? super T, ? extends $<? extends U>, E> f) throws E {
        if (this.empty()) return none();
        final var x = f.apply(this.opt.get());
        return (x == null) ? none() : cast(x);
    }

    public <E extends Exception> $<T> or$Try(final SupplierE<? extends $<? extends T>, E> f) throws E {
        if (this.present()) return this;
        final var x = f.get();
        return (x == null) ? none() : of(x.opt);
    }

    public <S, E extends Exception> $<$2<T, S>> and$Try(final SupplierE<? extends $<? extends S>, E> f) throws E {
        if (this.empty()) return none();
        final var x = f.get();
        return (x == null) ? none() : x.map(y -> tuple(this.get(), y));
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public <E extends Exception> T orElseTry(final SupplierE<? extends T, E> f) throws E {
        return this.present() ? this.opt.get() : f.get();
    }

    @Override
    public int hashCode() { return Objects.hash(this.getClass(), this.opt); }

    public boolean eq(final T that) { return this.test(x -> x.equals(that)); }

    public boolean equals(final $<? extends T> that) { return this.equals0(that); }

    @Deprecated
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof $)) return false;

        return this.equals0(cast(o));
    }

    private boolean equals0(final $<? extends T> that) {
        return (this == that) || ((that != null) && equiv(this.opt, that.opt));
    }

    @Override
    public String toString() { return this.present() ? "$(" + this.get() + ")" : "$<empty>"; }
}
