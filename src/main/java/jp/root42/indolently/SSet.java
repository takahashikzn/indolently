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
package jp.root42.indolently;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * Extended {@link Set} class for indolent person.
 * The name is came from "Sugared Set".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface SSet<T>
    extends Set<T>, SCol<T, SSet<T>>, Cloneable {

    /**
     * Clone this instance.
     *
     * @return clone of this instance
     * @see Object#clone()
     * @see Cloneable
     */
    default SSet<T> clone() {
        return Indolently.set(this);
    }

    /**
     * Wrap a set.
     * This method is an alias of {@link Indolently#wrap(Set)}.
     *
     * @param set set to wrap
     * @return wrapped set
     */
    public static <T> SSet<T> of(final Set<T> set) {
        return Indolently.wrap(set);
    }

    /**
     * {@inheritDoc}
     *
     * @see Indolently#freeze(Set)
     */
    @Override
    default SSet<T> freeze() {
        return Indolently.freeze(this);
    }

    @Override
    default SSet<T> tail() {
        return Indolently.set(this.list().tail());
    }

    /**
     * convert this set to {@link SList}.
     *
     * @return a list newly constructed from this instance.
     */
    default SList<T> list() {
        return Indolently.list(this);
    }

    /**
     * Map operation.
     * Map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed set which contains converted values
     */
    default <R> SSet<R> map(final Function<? super T, ? extends R> f) {
        return this.map((idx, val) -> f.apply(val));
    }

    /**
     * Map operation.
     * Map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function. first argument is loop index.
     * @return newly constructed set which contains converted values
     */
    default <R> SSet<R> map(final BiFunction<Integer, ? super T, ? extends R> f) {

        final SSet<R> rslt = Indolently.set();

        int i = 0;
        for (final T val : this) {
            rslt.add(f.apply(i++, val));
        }

        return rslt;
    }

    @Override
    default SSet<T> filter(final Predicate<? super T> f) {

        final SSet<T> rslt = Indolently.set();

        for (final T val : this) {
            if (f.test(val)) {
                rslt.add(val);
            }
        }

        return rslt;
    }

    /**
     * compute union of set.
     *
     * @param values
     * @return newly constructed set as a computed union
     */
    default SSet<T> union(final Iterable<? extends T> values) {
        return Indolently.set(this).pushAll(values);
    }

    /**
     * compute intersection of set.
     *
     * @param values
     * @return newly constructed set as a computed intersection.
     */
    default SSet<T> intersect(final Iterable<? extends T> values) {
        return this.union(values).delete(this.diff(values));
    }

    /**
     * compute difference of set.
     *
     * @param values
     * @return newly constructed set as a computed difference.
     */
    default SSet<T> diff(final Iterable<? extends T> values) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final SSet<T> rslt = Indolently.set(this).delete(values).union(Indolently.set(values).delete((Set) this));
        return rslt;
    }

    @SuppressWarnings("javadoc")
    default <R> SList<R> flatten(final Function<? super T, ? extends Iterable<? extends R>> f) {
        return Indolently.list(this.iterator().flatten(f));
    }

    /**
     * Return this instance if not empty, otherwise return {@code other}.
     *
     * @param other alternative value
     * @return this instance or other
     */
    default SSet<T> orElse(final Set<? extends T> other) {
        return this.orElseGet(() -> other);
    }

    /**
     * Return this instance if not empty, otherwise return the invocation result of {@code other}.
     *
     * @param other alternative value supplier
     * @return this instance or other
     */
    default SSet<T> orElseGet(final Supplier<? extends Set<? extends T>> other) {

        if (this.isEmpty()) {
            return Indolently.set(other.get());
        } else {
            return this;
        }
    }
}
