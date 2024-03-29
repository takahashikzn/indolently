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
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.root42.indolently.function.Consumer2E;
import jp.root42.indolently.function.ConsumerE;
import jp.root42.indolently.ref.$;
import jp.root42.indolently.trait.EdgeAwareIterable;
import jp.root42.indolently.trait.Filterable;
import jp.root42.indolently.trait.Freezable;
import jp.root42.indolently.trait.Identical;
import jp.root42.indolently.trait.Loopable;
import jp.root42.indolently.trait.Matchable;
import jp.root42.indolently.trait.ReducibleIterable;

import static jp.root42.indolently.Indolently.*;


/**
 * common method definition for {@link $set} / {@link $list}.
 * The name is came from "Sugared Collection".
 *
 * @param <T> value type
 * @param <SELF> self type
 * @author takahashikzn
 * @see $list
 * @see $set
 */
public interface $collection<T, SELF extends $collection<T, SELF>>
    extends Collection<T>, EdgeAwareIterable<T>, ReducibleIterable<T>, Freezable<SELF>, Identical<SELF>, Loopable<T, SELF>, Filterable<T, SELF>, Matchable<T> {

    /**
     * add value then return this instance.
     *
     * @param value value to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF push(final T value) { return this.push(value, x -> true); }

    /**
     * add all values then return this instance.
     *
     * @param values values to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF pushAll(final Iterable<? extends T> values) {
        if (values instanceof Collection) //
            this.addAll(cast(values));
        else //
            values.forEach(this::add);

        return this.identity();
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

        if (f.test(value)) this.add(value);

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
    default SELF push(final $<? extends T> value) {
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
    default SELF pushAll(final $<? extends Iterable<? extends T>> values) {
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
        final Collection<? extends T> vals = (values instanceof Collection) ? cast(values) : list(values);

        this.removeAll(vals);

        return this.identity();
    }

    /**
     * remove value then return this instance.
     *
     * @param val value to remove
     * @return {@code this} instance
     * @see #removeAll(Collection)
     */
    @Destructive
    default SELF delete(final T val) { return this.delete(list(val)); }

    /**
     * remove values then return this instance.
     *
     * @param f condition
     * @return {@code this} instance
     * @see #removeAll(Collection)
     */
    @Destructive
    default SELF delete(final Predicate<? super T> f) { return this.delete((i, x) -> f.test(x)); }

    /**
     * remove values then return this instance.
     *
     * @param f condition
     * @return {@code this} instance
     * @see #removeAll(Collection)
     */
    @Destructive
    default SELF delete(final BiPredicate<Integer, ? super T> f) { return this.delete(this.filter(f)); }

    @Override
    $iter<T> iterator();

    /**
     * get rest elements of this collection.
     *
     * @return collection elements except for first element.
     * if this instance is empty, i.e. {@code col.isEmpty()} returns true, return empty collection.
     */
    SELF tail();

    @Override
    default SELF each(final Consumer<? super T> f) { return this.eachTry(f::accept); }

    default <E extends Exception> SELF eachTry(final ConsumerE<? super T, E> f) throws E {
        if (!this.isEmpty()) //
            for (final T val: this)
                f.accept(val);

        return this.identity();
    }

    /**
     * internal iterator.
     *
     * @param f function. first argument is loop index.
     * @return {@code this} instance
     */
    default SELF each(final BiConsumer<Integer, ? super T> f) { return this.eachTry(f::accept); }

    default <E extends Exception> SELF eachTry(final Consumer2E<Integer, ? super T, E> f) throws E {
        final var i = ref(0);
        return this.eachTry(x -> f.accept(i.$++, x));
    }

    @Override
    default boolean any(final Predicate<? super T> f) {
        if (!this.isEmpty()) //
            for (final var x: this)
                if (f.test(x)) return true;

        return false;
    }

    /**
     * Count elements which satisfying condition.
     *
     * @param f condition
     * @return the number of elements which satisfying condition
     */
    default int count(final Predicate<? super T> f) { return this.take(f).size(); }

    /**
     * Filter operation: returns values which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition. first argument is loop index.
     * @return new filtered collection
     */
    default SELF filter(final BiPredicate<Integer, ? super T> f) {
        final var i = ref(0);
        return this.take(x -> f.test(i.$++, x));
    }

    @Override
    default $stream<T> stream() { return Indolently.$(Collection.super.stream()); }

    @Override
    default $stream<T> parallelStream() { return Indolently.$(Collection.super.parallelStream()); }

    /**
     * Convert this collection to map.
     *
     * @param <K> key type
     * @param <V> value type
     * @param fkey a function which convert element to map key
     * @param fval a function which convert element to map value
     * @return map instance.
     */
    default <K, V> $map<K, V> mapmap(final Function<? super T, ? extends K> fkey, final Function<? super T, ? extends V> fval) {

        return this.reduce(map(), (rslt, e) -> rslt.push(fkey.apply(e), fval.apply(e)));
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
    default <K, V> $map<K, V> flatMapMap(final Function<? super T, ? extends K> fkey, final Function<? super T, $<? extends V>> fval) {

        return this.reduce(map(), (rslt, e) -> rslt.push(fkey.apply(e), fval.apply(e)));
    }

    /**
     * Just an alias of {@link #contains(Object)}
     *
     * @param val value
     * @return the result of {@link #contains(Object)}
     */
    default boolean has(final T val) { return this.contains(val); }

    /**
     * Just an alias of {@link #containsAll(Collection)}
     *
     * @param vals values
     * @return the result of {@link #containsAll(Collection)}
     */
    default boolean hasAll(final Collection<? extends T> vals) { return this.containsAll(vals); }

    /**
     * 'Group By' operation: returns grouped elements as {@link $map} form.
     *
     * @param fkey convert element to grouping key
     * @return grouped elements
     */
    <K> $map<K, SELF> group(Function<? super T, ? extends K> fkey);

    /**
     * Return newly constructed sorted collection using comparator.
     *
     * @param comp comparator
     * @return sorted collection
     */
    SELF order(Comparator<? super T> comp);

    default <C extends Comparable<? super C>> SELF order(final Function<? super T, C> f) {
        return this.order(equal(f, it()) ? cast(Comparator.naturalOrder()) : Comparator.comparing(f));
    }

    default String join() { return this.join((String) null); }

    default String join(final String sep) { return this.join(x -> (x == null) ? "" : "" + x, sep); }

    default String join(final Function<T, ? extends CharSequence> f) { return this.join(f, null); }

    default String join(final Function<T, ? extends CharSequence> f, final String sep) { return Indolently.join(this.iterator().map(f), sep); }

    default $<String> join$() { return this.empty() ? Indolently.none() : Indolently.opt(this.join()); }

    default $<String> join$(final String sep) { return this.empty() ? Indolently.none() : Indolently.opt(this.join(sep)); }

    default $<String> join$(final Function<T, ? extends CharSequence> f) { return this.empty() ? Indolently.none() : Indolently.opt(this.join(f)); }

    default SELF tap(final Consumer<SELF> f) {
        f.accept(this.identity());
        return this.identity();
    }

    // alias
    default boolean empty() { return this.isEmpty(); }

    default boolean present() { return !this.isEmpty(); }

    default $<SELF> present$() { return this.empty() ? Indolently.none() : Indolently.opt(this.identity()); }
}
