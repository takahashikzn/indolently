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

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.root42.indolently.ref.IntRef;
import jp.root42.indolently.trait.EdgeAwareIterable;
import jp.root42.indolently.trait.Filterable;
import jp.root42.indolently.trait.Freezable;
import jp.root42.indolently.trait.Identical;
import jp.root42.indolently.trait.Loopable;
import jp.root42.indolently.trait.Matchable;
import jp.root42.indolently.trait.ReducibleIterable;

import static jp.root42.indolently.Indolently.*;


/**
 * common method definition for {@link SSet} / {@link SList}.
 * The name is came from "Sugared Collection".
 *
 * @param <T> value type
 * @param <SELF> self type
 * @author takahashikzn
 * @see SList
 * @see SSet
 */
public interface SCol<T, SELF extends SCol<T, SELF>>
    extends Collection<T>, EdgeAwareIterable<T>, ReducibleIterable<T>, Freezable<SELF>, Identical<SELF>,
    Loopable<T, SELF>, Filterable<T, SELF>, Matchable<T> {

    /**
     * add value then return this instance.
     *
     * @param value value to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF push(final T value) {
        return this.push(value, x -> true);
    }

    /**
     * add all values then return this instance.
     *
     * @param values values to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF pushAll(final Iterable<? extends T> values) {
        return this.pushAll(values, x -> true);
    }

    /**
     * add value then return this instance only if condition satisfied.
     *
     * @param value value to add
     * @param f condition
     * @return {@code this} instance
     */
    @Destructive
    default SELF push(final T value, final Predicate<? super T> f) {
        if (f.test(value)) {
            this.add(value);
        }

        return this.identity();
    }

    /**
     * add all values then return this instance only if condition satisfied.
     *
     * @param values values to add
     * @param f condition
     * @return {@code this} instance
     */
    @Destructive
    default SELF pushAll(final Iterable<? extends T> values, final Predicate<? super Iterable<? extends T>> f) {
        if (f.test(values)) {
            for (final T val : values) {
                this.add(val);
            }
        }

        return this.identity();
    }

    /**
     * add value then return this instance only if value exists.
     * otherwise, do nothing.
     *
     * @param value nullable value to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF push(final Optional<? extends T> value) {
        return Indolently.empty(value) ? this.identity() : this.push(value.get());
    }

    /**
     * add all values then return this instance only if values exists.
     * otherwise, do nothing.
     *
     * @param values nullable values to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF pushAll(final Optional<? extends Iterable<? extends T>> values) {
        return Indolently.empty(values) ? this.identity() : this.pushAll(values.get());
    }

    /**
     * remove values then return this instance.
     *
     * @param values values to remove
     * @return {@code this} instance
     * @see #removeAll(Collection)
     */
    @Destructive
    default SELF delete(final Iterable<? extends T> values) {

        // optimization
        final Collection<? extends T> vals =
            (values instanceof Collection) ? (Collection<? extends T>) values : Indolently.list(values);

        this.removeAll(vals);

        return this.identity();
    }

    /**
     * remove values then return this instance.
     *
     * @param f condition
     * @return {@code this} instance
     * @see #removeAll(Collection)
     */
    @Destructive
    default SELF delete(final Predicate<? super T> f) {
        return this.delete((i, x) -> f.test(x));
    }

    /**
     * remove values then return this instance.
     *
     * @param f condition
     * @return {@code this} instance
     * @see #removeAll(Collection)
     */
    @Destructive
    default SELF delete(final BiPredicate<Integer, ? super T> f) {
        return this.delete(this.filter(f));
    }

    @Override
    SIter<T> iterator();

    /**
     * get rest elements of this collection.
     *
     * @return collection elements except for first element.
     * if this instance is empty, i.e. {@code col.isEmpty()} returns true, return empty collection.
     */
    SELF tail();

    @Override
    default SELF each(final Consumer<? super T> f) {

        for (final T val : this) {
            f.accept(val);
        }

        return this.identity();
    }

    /**
     * internal iterator.
     *
     * @param f function. first argument is loop index.
     * @return {@code this} instance
     */
    default SELF each(final BiConsumer<Integer, ? super T> f) {

        final IntRef i = ref(0);

        return this.each(x -> f.accept(i.val++, x));
    }

    @Override
    default boolean some(final Predicate<? super T> f) {
        return !this.filter(f).isEmpty();
    }

    /**
     * Count elements which satisfying condition.
     *
     * @param f condition
     * @return the number of elements which satisfying condition
     */
    default int count(final Predicate<? super T> f) {
        return this.filter(f).size();
    }

    /**
     * Filter operation: returns values which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition. first argument is loop index.
     * @return new filtered collection
     */
    default SELF filter(final BiPredicate<Integer, ? super T> f) {

        final IntRef i = ref(0);

        return this.filter(x -> f.test(i.val++, x));
    }

    @Override
    default SStream<T> stream() {
        return Indolently.wrap(Collection.super.stream());
    }

    @Override
    default SStream<T> parallelStream() {
        return Indolently.wrap(Collection.super.parallelStream());
    }

    /**
     * Convert this collection to map.
     *
     * @param <K> key type
     * @param <V> value type
     * @param fkey a function which convert element to map key
     * @param fval a function which convert element to map value
     * @return map instance.
     */
    default <K, V> SMap<K, V> mapmap(final Function<? super T, ? extends K> fkey,
        final Function<? super T, ? extends V> fval) {

        return this.reduce(Indolently.map(), (rslt, e) -> rslt.push(fkey.apply(e), fval.apply(e)));
    }

    /**
     * Just an alias of {@link #contains(Object)}
     *
     * @param val value
     * @return the result of {@link #contains(Object)}
     */
    default boolean has(final T val) {
        return this.contains(val);
    }

    /**
     * Just an alias of {@link #containsAll(Collection)}
     *
     * @param vals values
     * @return the result of {@link #containsAll(Collection)}
     */
    default boolean hasAll(final Collection<? extends T> vals) {
        return this.containsAll(vals);
    }

    /**
     * 'Group By' operation: returns grouped elements as {@link SMap} form.
     *
     * @param fkey convert element to grouping key
     * @return grouped elements
     */
    <K> SMap<K, SELF> group(Function<? super T, ? extends K> fkey);

    /**
     * Return newly constructed sorted collection using comparator.
     *
     * @param comp comparator
     * @return sorted collection
     */
    SELF sortWith(final Comparator<? super T> comp);
}
