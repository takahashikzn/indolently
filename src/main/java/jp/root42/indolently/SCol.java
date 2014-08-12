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
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
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
import jp.root42.indolently.trait.Reducible;

import static jp.root42.indolently.Indolently.*;


/**
 * common method definition for {@link SSet} / {@link SList}.
 * It's name comes from "Sugared collection".
 *
 * @param <T> value type
 * @param <SELF> self type
 * @author takahashikzn
 * @see SList
 * @see SSet
 */
public interface SCol<T, SELF extends SCol<T, SELF>>
    extends Collection<T>, EdgeAwareIterable<T>, Freezable<SELF>, Identical<SELF>, Loopable<T, SELF>,
    Filterable<T, SELF>, Reducible<T>, Matchable<T> {

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
    default <R> Optional<R> mapred(final Function<? super T, ? extends R> fm,
        final BiFunction<? super R, ? super R, ? extends R> fr) {

        return this.tail().mapfold( //
            fm.apply(this.first()), //
            (rem, val) -> fr.apply(rem, fm.apply(val)));
    }

    @Override
    default <R> Optional<R> mapfold(final Optional<? extends R> initial,
        final BiFunction<? super R, ? super T, ? extends R> f) {

        R rem = Indolently.empty(initial) ? null : initial.get();

        for (final T val : this) {
            rem = f.apply(rem, val);
        }

        return Optional.ofNullable(rem);
    }

    @Override
    default SStream<T> stream() {
        return Indolently.wrap(Collection.super.stream());
    }

    @Override
    default SStream<T> parallelStream() {
        return Indolently.wrap(Collection.super.parallelStream());
    }
}
