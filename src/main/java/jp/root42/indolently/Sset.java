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


/**
 * Extended {@link Set} class for indolent person.
 * It's name comes from "Sugared set".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface Sset<T>
    extends Scol<T, Sset<T>>, Set<T> {

    /**
     * Wrap a set.
     * This method is an alias of {@link Indolently#wrap(Set)}.
     *
     * @param set set to wrap
     * @return wrapped set
     */
    public static <T> Sset<T> of(final Set<T> set) {
        return Indolently.wrap(set);
    }

    /**
     * {@inheritDoc}
     *
     * @see Indolently#freeze(Set)
     */
    @Override
    default Sset<T> freeze() {
        return Indolently.freeze(this);
    }

    @Override
    default Sset<T> tail() {
        return Indolently.set(this.list().tail());
    }

    /**
     * convert this set to {@link Slist}.
     *
     * @return a list newly constructed from this instance.
     */
    default Slist<T> list() {
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
    default <R> Sset<R> map(final Function<? super T, ? extends R> f) {
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
    default <R> Sset<R> map(final BiFunction<Integer, ? super T, ? extends R> f) {

        final Sset<R> rslt = Indolently.set();

        int i = 0;
        for (final T val : this) {
            rslt.add(f.apply(i++, val));
        }

        return rslt;
    }

    @Override
    default Sset<T> filter(final Predicate<? super T> f) {

        final Sset<T> rslt = Indolently.set();

        for (final T val : this) {
            if (f.test(val)) {
                rslt.add(val);
            }
        }

        return this;
    }

    /**
     * compute union of set.
     *
     * @param values
     * @return newly constructed set as a computed union
     */
    default Sset<T> union(final Iterable<? extends T> values) {
        return Indolently.set(this).pushAll(values);
    }

    /**
     * compute intersection of set.
     *
     * @param values
     * @return newly constructed set as a computed intersection.
     */
    default Sset<T> intersect(final Iterable<? extends T> values) {
        return this.union(values).delete(this.diff(values));
    }

    /**
     * compute difference of set.
     *
     * @param values
     * @return newly constructed set as a computed difference.
     */
    default Sset<T> diff(final Iterable<? extends T> values) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Sset<T> rslt = Indolently.set(this).delete(values).union(Indolently.set(values).delete((Set) this));
        return rslt;
    }
}
