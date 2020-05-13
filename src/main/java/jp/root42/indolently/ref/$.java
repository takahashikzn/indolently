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

import static jp.root42.indolently.Indolently.*;


/**
 * The {@link Optional} alternative.
 *
 * @param <T> element type
 * @author takahashikzn
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class $<T>
    implements Serializable, Supplier<T> {

    @SuppressWarnings("PublicField")
    public final Optional<T> opt; // NOPMD

    public static <T> $<T> of(final T val) { return new $<>(val); }

    @SuppressWarnings("OptionalAssignedToNull")
    public static <T> $<T> of(final Optional<T> val) { return val == null ? none() : new $<>(val); }

    private static final $<?> NONE = new $<>(Optional.empty());

    public static <T> $<T> none() { return cast(NONE); }

    private $(final T val) { this(Optional.ofNullable(val)); }

    private $(final Optional<T> opt) { this.opt = opt; }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public T get() { return this.opt.get(); }

    public boolean empty() { return this.opt.isEmpty(); }

    public boolean present() { return this.opt.isPresent(); }

    public $<T> tap(final Consumer<? super T> action) {
        this.opt.ifPresent(action);
        return this;
    }

    public $<T> tap(final Consumer<? super T> action, final Runnable emptyAction) {
        this.opt.ifPresentOrElse(action, emptyAction);
        return this;
    }

    public $<T> filter(final Predicate<? super T> predicate) {
        return this.opt.filter(predicate).map($::of).orElse(none());
    }

    public <U> $<U> map(final Function<? super T, ? extends U> mapper) {
        return cast(this.opt.map(mapper).map($::of).orElse(none()));
    }

    public <U> $<U> fmap(final Function<? super T, ? extends $<? extends U>> mapper) {
        return cast(this.opt.flatMap(x -> mapper.apply(x).opt).map($::of).orElse(none()));
    }

    public $<T> otherwise(final Supplier<? extends $<? extends T>> supplier) {
        return this.present() ? this : of(supplier.get().get());
    }

    public $<T> otherwise(final $<? extends T> supplier) { return this.present() ? this : cast(of(supplier.get())); }

    public Stream<T> stream() { return this.opt.stream(); }

    public T or(final T other) { return this.orElse(other); }

    public T orElse(final T other) { return this.opt.orElse(other); }

    public T orNull() { return this.orElse(null); }

    public T or(final Supplier<? extends T> supplier) { return this.orElseGet(supplier); }

    public T orElseGet(final Supplier<? extends T> supplier) { return this.opt.orElseGet(supplier); }

    public T orFail() { return this.opt.orElseThrow(); }

    public <X extends Throwable> T orFail(final Supplier<? extends X> exceptionSupplier) throws X {
        return this.opt.orElseThrow(exceptionSupplier);
    }

    @Override
    public int hashCode() { return Objects.hash(this.getClass(), this.opt); }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof $)) return false;

        final $<?> that = cast(o);

        return equiv(this.opt, that.opt);
    }

    @Override
    public String toString() { return this.opt.isEmpty() ? "$.empty" : "$[" + this.opt.get() + "]"; }
}