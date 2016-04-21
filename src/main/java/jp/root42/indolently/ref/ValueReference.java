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

import java.util.Optional;
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
public interface ValueReference<T, S extends ValueReference<T, S>>
    extends Supplier<T>, Consumer<T>, Identical<S> {

    /**
     * get value then do something with this instance.
     *
     * @param f any operation
     * @return the value
     */
    default T getThen(final Consumer<? super S> f) {
        return this.optThen(f).get();
    }

    /**
     * get value if exists, then do something with this instance.
     *
     * @param f any operation
     * @return optional representation of the value
     */
    default Optional<T> optThen(final Consumer<? super S> f) {
        final Optional<T> curr = this.opt();
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
    default Optional<T> opt() {
        return Indolently.opt(this.get());
    }

    /**
     * get value if exists, otherwise use the value from supplier as this reference's value.
     *
     * @param f value supplier
     * @return the value
     */
    default T orAccept(final Expression<? extends T> f) {

        return this.opt().orElseGet(() -> {
            final T val = f.get();
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
    static BoolRef of(final boolean val) {
        return new BoolRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static IntRef of(final int val) {
        return new IntRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static LongRef of(final long val) {
        return new LongRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static DoubleRef of(final double val) {
        return new DoubleRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static FloatRef of(final float val) {
        return new FloatRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static ShortRef of(final short val) { // NOPMD
        return new ShortRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static ByteRef of(final byte val) {
        return new ByteRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static CharRef of(final char val) {
        return new CharRef(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static <T> Ref<T> of(final T val) {
        return new Ref<>(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    static <T extends Comparable<T>> CmpRef<T> of(final T val) {
        return new CmpRef<>(val);
    }
}
