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

import java.util.Comparator;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * Extended {@link Set} class for indolent person.
 * The name is came from "Sugared Set".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface $set<T>
    extends Set<T>, $collection<T, $set<T>>, Cloneable {

    /**
     * Clone this instance.
     *
     * @return clone of this instance
     * @see Cloneable
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default $set<T> clone() { return set((Iterable<T>) this); }

    /**
     * Wrap a set.
     * This method is an alias of {@link Indolently#$(Set)}.
     *
     * @param set set to wrap
     * @return wrapped set
     */
    static <T> $set<T> of(final Set<T> set) { return $(set); }

    /**
     * @see Indolently#freeze(Set)
     */
    @Override
    default $set<T> freeze() { return Indolently.freeze(this); }

    @Override
    default $set<T> tail() { return set(this.list().tail()); }

    /**
     * convert this set to {@link $list}.
     *
     * @return a list newly constructed from this instance.
     */
    default $list<T> list() { return Indolently.list(this); }

    /**
     * Map operation.
     * Map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed set which contains converted values
     */
    default <R> $set<R> map(final Function<? super T, ? extends R> f) { return this.map((idx, val) -> f.apply(val)); }

    /**
     * Map operation.
     * Map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function. first argument is loop index.
     * @return newly constructed set which contains converted values
     */
    default <R> $set<R> map(final BiFunction<Integer, ? super T, ? extends R> f) {

        final $set<R> rslt = set();

        int i = 0;
        for (final T val: this) {
            rslt.add(f.apply(i++, val));
        }

        return rslt;
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed list which contains converted values
     */
    default <R> $set<R> flatMap(final Function<? super T, $<? extends R>> f) {
        return this.reduce(set(), (x, y) -> x.push(f.apply(y)));
    }

    default <R> $set<R> fmap(final Function<? super T, $<? extends R>> f) { return this.flatMap(f); }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function. first argument is element index, second one is element value
     * @return newly constructed list which contains converted values
     */
    default <R> $set<R> flatMap(final BiFunction<Integer, ? super T, $<? extends R>> f) {

        final var i = ref(0);
        return this.flatMap(x -> f.apply(i.$++, x));
    }

    default <R> $set<R> fmap(final BiFunction<Integer, ? super T, $<? extends R>> f) { return this.flatMap(f); }

    @Override
    default $set<T> take(final Predicate<? super T> f) {
        return this.reduce(set(), (x, y) -> f.test(y) ? x.push(y) : x);
    }

    /**
     * compute union of set.
     *
     * @param values values
     * @return newly constructed set as a computed union
     */
    default $set<T> union(final Iterable<? extends T> values) { return this.clone().pushAll(values); }

    /**
     * compute intersection of set.
     *
     * @param values values
     * @return newly constructed set as a computed intersection.
     */
    default $set<T> intersect(final Iterable<? extends T> values) {
        return this.union(values).delete(this.diff(values));
    }

    /**
     * compute difference of set.
     *
     * @param values values
     * @return newly constructed set as a computed difference.
     */
    @SuppressWarnings("unchecked")
    default $set<T> diff(final Iterable<? extends T> values) {
        return this.clone().delete(values).union(set(values).delete((Set) this));
    }

    /**
     * Flatten this set.
     *
     * @param f value generator
     * @return newly constructed flatten set
     */
    default <R> $set<R> flat(final Function<? super T, ? extends Iterable<? extends R>> f) {
        return set(this.iterator().flat(f));
    }

    @Deprecated
    default <R> $set<R> flatten(final Function<? super T, ? extends Iterable<? extends R>> f) { return this.flat(f); }

    /**
     * Return this instance if not empty, otherwise return {@code other}.
     *
     * @param other alternative value
     * @return this instance or other
     */
    default $set<T> orElse(final Set<? extends T> other) { return this.orElseGet(() -> other); }

    /**
     * Return this instance if not empty, otherwise return the invocation result of {@code other}.
     *
     * @param other alternative value supplier
     * @return this instance or other
     */
    default $set<T> orElseGet(final Supplier<? extends Set<? extends T>> other) {
        return this.isEmpty() ? set(other.get()) : this;
    }

    @Override
    default <K> $map<K, $set<T>> group(final Function<? super T, ? extends K> fkey) {

        return Expressive.eval( //
            this.list().group(fkey), //
            (final $map<K, $list<T>> grp) -> grp.map((k, v) -> v.set()));
    }

    @Override
    default $set<T> order(final Comparator<? super T> comp) { return sort(this, comp); }

    @Override
    default String join(final Function<T, ? extends CharSequence> f, final String sep) {
        return Indolently.join(this.map(f), sep);
    }

    default <U extends T> $set<U> only(final Class<U> type) { return this.take(type::isInstance).map(type::cast); }
}
