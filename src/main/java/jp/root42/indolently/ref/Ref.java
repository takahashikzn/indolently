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

import java.util.function.Consumer;
import java.util.function.Supplier;

import jp.root42.indolently.Indolently;
import jp.root42.indolently.function.Expression;
import jp.root42.indolently.trait.Identical;


/**
 * @param <T> value type
 * @param <S> self type
 * @author takahashikzn
 */
public interface Ref<T, S extends Ref<T, S>>
    extends Supplier<T>, Consumer<T>, Identical<S> {

    /**
     * get value then do something with this instance.
     *
     * @param f any operation
     * @return the value
     */
    default T getThen(final Consumer<? super S> f) { return this.optThen(f).get(); }

    /**
     * get value if exists, then do something with this instance.
     *
     * @param f any operation
     * @return optional representation of the value
     */
    default $<T> optThen(final Consumer<? super S> f) {
        final var curr = this.opt();
        f.accept(this.identity());
        return curr;
    }

    /**
     * do something with this instance, then get value if exists.
     *
     * @param f any operation
     * @return optional representation of the value
     */
    default S init(final Consumer<? super S> f) {
        if (this.get() == null) {
            f.accept(this.identity());
        }

        return this.identity();
    }

    /**
     * get value as optional representation.
     *
     * @return optional representation of the value
     */
    default $<T> opt() { return Indolently.opt(this.get()); }

    /**
     * get value if exists, otherwise use the value from supplier as this reference's value.
     *
     * @param f value supplier
     * @return the value
     */
    default T orAccept(final Expression<? extends T> f) {

        return this.opt().or(() -> {
            final var val = f.get();
            this.accept(val);
            return val;
        });
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $bool of(final boolean val) { return new $bool(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $int of(final int val) { return new $int(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $long of(final long val) { return new $long(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $double of(final double val) { return new $double(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $float of(final float val) { return new $float(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $short of(final short val) { /* NOPMD*/return new $short(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $byte of(final byte val) { return new $byte(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static $char of(final char val) { return new $char(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static <T> $void<T> of(final T val) { return new $void<>(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static <T extends Comparable<T>> $voidc<T> of(final T val) { return new $voidc<>(val); }
}
