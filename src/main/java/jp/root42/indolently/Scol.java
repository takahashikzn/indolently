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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * common method definition for {@link Sset} / {@link Slist}.
 * It's name comes from "Sugared collection".
 *
 * @param <T> value type
 * @param <SELF> self type
 * @author takahashikzn
 * @see Slist
 * @see Sset
 */
public interface Scol<T, SELF extends Scol<T, SELF>>
    extends Collection<T>, Freezable<SELF>, Identical<SELF> {

    /**
     * add value then return this instance.
     *
     * @param value value to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF push(final T value) {
        this.add(value);
        return this.identity();
    }

    /**
     * add all values then return this instance.
     *
     * @param values values to add
     * @return {@code this} instance
     */
    @Destructive
    default SELF pushAll(final Iterable<? extends T> values) {
        for (final T val : values) {
            this.add(val);
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
     * get first element.
     *
     * @return first element
     * @throws NoSuchElementException if empty
     */
    default T first() {
        return this.iterator().next();
    }

    /**
     * get last element.
     *
     * @return first element
     * @throws NoSuchElementException if empty
     */
    default T last() {
        return this.reduce((rem, val) -> val).get();
    }

    /**
     * get rest elements of this collection.
     *
     * @return collection elements except for first element.
     * if this instance is empty, i.e. {@code col.isEmpty()} returns true, return empty collection.
     */
    SELF tail();

    /**
     * internal iterator.
     *
     * @param f function
     * @return {@code this} instance
     */
    default SELF each(final Consumer<? super T> f) {
        return this.each((idx, val) -> f.accept(val));
    }

    /**
     * internal iterator.
     *
     * @param f function. first argument is loop index.
     * @return {@code this} instance
     */
    default SELF each(final BiConsumer<Integer, ? super T> f) {

        int i = 0;
        for (final T val : this) {
            f.accept(i++, val);
        }

        return this.identity();
    }

    /**
     * Test whether at least one value satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean some(final Predicate<? super T> f) {
        return !this.filter(f).isEmpty();
    }

    /**
     * Test whether all values satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean every(final Predicate<? super T> f) {
        return this.filter(f).size() == this.size();
    }

    /**
     * Filter operation: returns values which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition
     * @return new filtered collection
     */
    default SELF filter(final Predicate<? super T> f) {
        return this.filter((idx, val) -> f.test(val));
    }

    /**
     * Filter operation: returns values which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition. first argument is loop index.
     * @return new filtered collection
     */
    SELF filter(BiPredicate<Integer, ? super T> f);

    /**
     * Reduce operation.
     *
     * @param f function
     * @return result value
     * @throws NoSuchElementException this collection is empty
     * @see #mapred(Function, BiFunction)
     */
    default Optional<T> reduce(final BiFunction<? super T, ? super T, ? extends T> f) {

        // "(x) -> x" lambda literal occurs compilation error on OracleJDK compiler
        return this.mapred(Function.identity(), f);
    }

    /**
     * Reduce operation.
     *
     * @param initial initial value
     * @param f function
     * @return result value
     * @see #mapred(Object, BiFunction)
     */
    default Optional<T> reduce(final T initial, final BiFunction<? super T, ? super T, ? extends T> f) {
        return this.mapred(initial, f);
    }

    /**
     * Reduce operation.
     *
     * @param initial initial value
     * @param f function
     * @return result value
     * @see #mapred(Optional, BiFunction)
     */
    default Optional<T> reduce(final Optional<? extends T> initial,
        final BiFunction<? super T, ? super T, ? extends T> f) {

        return this.mapred(initial, f);
    }

    /**
     * Map then Reduce operation.
     *
     * @param <M> mapping target type
     * @param fm mapper function
     * @param fr reducer function
     * @return result value
     * @throws NoSuchElementException this collection is empty
     */
    default <M> Optional<M> mapred(final Function<? super T, ? extends M> fm,
        final BiFunction<? super M, ? super M, ? extends M> fr) {

        return this.tail().mapred( //
            fm.apply(this.first()), //
            (rem, val) -> fr.apply(rem, fm.apply(val)));
    }

    /**
     * Map then Reduce operation.
     *
     * @param <M> mapping target type
     * @param initial initial value
     * @param f function
     * @return result value
     */
    default <M> Optional<M> mapred(final M initial, final BiFunction<? super M, ? super T, ? extends M> f) {
        return this.mapred(Optional.ofNullable(initial), f);
    }

    /**
     * Map then Reduce operation.
     *
     * @param <M> mapping target type
     * @param initial initial value
     * @param f function
     * @return result value
     */
    default <M> Optional<M> mapred(final Optional<? extends M> initial,
        final BiFunction<? super M, ? super T, ? extends M> f) {

        M rem = Indolently.empty(initial) ? null : initial.get();

        for (final T val : this) {
            rem = f.apply(rem, val);
        }

        return Optional.ofNullable(rem);
    }
}
