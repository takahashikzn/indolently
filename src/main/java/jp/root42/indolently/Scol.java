/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.Collection;
import java.util.Iterator;
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
     * get first value.
     *
     * @return first value
     * @throws NoSuchElementException if empty
     */
    default T first() {
        return this.iterator().next();
    }

    /**
     * get last value.
     *
     * @return first value
     * @throws NoSuchElementException if empty
     */
    default T last() {

        for (final Iterator<T> i = this.iterator(); i.hasNext();) {
            if (!i.hasNext()) {
                return i.next();
            }
        }

        throw new NoSuchElementException();
    }

    /**
     * get rest of this collection.
     *
     * @return collection values except for first value.
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
     * Map operation.
     * Convert values.
     * This operation is constructive.
     *
     * @param f function
     * @return newly constructed collection which contains converted values
     */
    default SELF map(final Function<? super T, ? extends T> f) {
        return this.map((idx, val) -> f.apply(val));
    }

    /**
     * Map operation.
     * Convert values.
     * This operation is constructive.
     *
     * @param f function. first argument is loop index.
     * @return newly constructed collection which contains converted values
     */
    SELF map(BiFunction<Integer, ? super T, ? extends T> f);

    /**
     * Filter operation.
     * Returns values which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition
     * @return new filtered collection
     */
    default SELF filter(final Predicate<? super T> f) {
        return this.filter((idx, val) -> f.test(val));
    }

    /**
     * Filter operation.
     * Returns values which satisfying condition.
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
     */
    default Optional<T> reduce(final BiFunction<? super T, ? super T, ? extends T> f) {
        return this.reduce(Optional.empty(), f);
    }

    /**
     * Reduce operation.
     *
     * @param initial initial value
     * @param f function
     * @return result value
     */
    default Optional<T> reduce(final T initial, final BiFunction<? super T, ? super T, ? extends T> f) {
        return this.reduce(Optional.of(initial), f);
    }

    /**
     * Reduce operation.
     *
     * @param initial initial value
     * @param f function
     * @return result value
     */
    default Optional<T> reduce(final Optional<? extends T> initial,
        final BiFunction<? super T, ? super T, ? extends T> f) {

        T memo = Indolently.empty(initial) ? null : initial.get();

        for (final T val : this) {
            memo = f.apply(memo, val);
        }

        return Optional.ofNullable(memo);
    }
}
