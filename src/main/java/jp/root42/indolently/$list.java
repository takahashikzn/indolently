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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * Extended {@link List} class for indolent person.
 * The name is came from "Sugared List".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface $list<T>
    extends List<T>, $collection<T, $list<T>>, Cloneable {

    /**
     * Clone this instance.
     *
     * @return clone of this instance
     * @see Cloneable
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    default $list<T> clone() { return list((Iterable<T>) this); }

    /**
     * Wrap a list.
     * This method is an alias of {@link Indolently#$(List)}.
     *
     * @param list list to wrap
     * @return wrapped list
     */
    static <T> $list<T> of(final List<T> list) { return $(list); }

    /**
     * @see Indolently#freeze(List)
     */
    @Override
    default $list<T> freeze() { return Indolently.freeze(this); }

    @Override
    default $<T> head$() { return this.opt(0); } // for optimization

    @Override
    default T head() { return this.get(0); } // for optimization

    /**
     * Return element at the position if exists.
     * This method never throws {@link IndexOutOfBoundsException}.
     *
     * @param index index of the element
     * @return the element if exists
     */
    default $<T> opt(final int index) { // for optimization
        final int i = idx(this, index);
        return (0 <= i) && (i < this.size()) ? Indolently.opt(this.get(i)) : $.none();
    }

    /**
     * Equivalent to {@code list.subList(idx, list.size())}.
     *
     * @param from from index. negative index also acceptable.
     * @return sub list
     */
    default $list<T> subList(final int from) { return this.subList(idx(this, from), this.size()); }

    @Override
    $list<T> subList(int from, int to);

    @Override
    default $list<T> tail() { return (this.size() <= 1) ? list() : this.subList(1).clone(); }

    @Override
    default $<T> last$() { return this.opt(-1); } // for optimization

    @SuppressWarnings("ConstantConditions")
    @Override
    default T last() { return this.get(-1); } // for optimization

    /**
     * convert this list to {@link $set}. original order is reserved.
     *
     * @return a set constructed from this instance.
     */
    default $set<T> set() { return $(ObjFactory.getInstance().<T> newFifoSet()).pushAll(this); }

    /**
     * convert this list to sorted{@link $set}.
     *
     * @param comp a {@link Comparator}
     * @return a set constructed from this instance.
     */
    default $set<T> set(final Comparator<T> comp) {
        return $(ObjFactory.getInstance().newSortedSet(comp)).pushAll(this);
    }

    /**
     * insert value at given index then return this instance.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.push(-1, "x")} means
     * {@code slist.push(slist.size() - 1, "x")}
     * @param value value to add
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> push(final int idx, final T value) {
        this.add(idx(this, idx), value);
        return this;
    }

    /**
     * insert all values at given index then return this instance.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.pushAll(-1, list("x", "y"))} means
     * {@code slist.pushAll(slist.size() - 1,  list("x", "y"))}
     * @param values values to add
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> pushAll(final int idx, final Iterable<? extends T> values) {

        // optimization
        final Collection<? extends T> vals = (values instanceof Collection) ? cast(values) : list(values);

        this.addAll(idx(this, idx), vals);

        return this;
    }

    /**
     * insert value at given index then return this instance only if value exists.
     * otherwise, do nothing.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.push(-1, "x")} means
     * {@code slist.push(slist.size() - 1, "x")}
     * @param value nullable value to add
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> push(final int idx, final $<? extends T> value) {
        return Indolently.empty(value) ? this : this.push(idx, value.get());
    }

    /**
     * insert all values at given index then return this instance only if values exist.
     * otherwise, do nothing.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.pushAll(-1, list("x", "y"))} means
     * {@code slist.pushAll(slist.size() - 1,  list("x", "y"))}
     * @param values nullable values to add
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> pushAll(final int idx, final $<? extends Iterable<? extends T>> values) {
        return Indolently.empty(values) ? this : this.pushAll(idx, values.get());
    }

    /**
     * Almost same as {@link #narrow(int)} but returns newly constructed (detached) view.
     *
     * @param from from index (inclusive)
     * @return detached sub list
     */
    default $list<T> slice(final int from) { return this.narrow(from).clone(); }

    /**
     * Almost same as {@link #narrow(int, int)} but returns newly constructed (detached) view.
     *
     * @param from from index (inclusive)
     * @param to to index (exclusive)
     * @return detached sub list
     */
    default $list<T> slice(final int from, final int to) { return this.narrow(from, to).clone(); }

    /**
     * Almost same as {@link #subList(int, int)} but never throw {@link IllegalArgumentException} and
     * {@link IndexOutOfBoundsException}.
     *
     * @param from from index (inclusive)
     * @return the narrowed view of this list
     */
    default $list<T> narrow(final int from) { return this.narrow(from, this.size()); }

    /**
     * Almost same as {@link #subList(int, int)} but never throw {@link IllegalArgumentException} and
     * {@link IndexOutOfBoundsException}.
     *
     * @param from from index (inclusive)
     * @param to to index (exclusive)
     * @return the narrowed view of this list
     */
    default $list<T> narrow(final int from, final int to) {

        int fromIndex = idx(this, from);

        if (fromIndex < 0) fromIndex = 0;

        int toIndex = idx(this, to);

        if (((from < 0) && (toIndex == 0)) || (this.size() < toIndex)) toIndex = this.size();

        if (toIndex < fromIndex) return list();

        return this.subList(from, toIndex);
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed list which contains converted values
     */
    default <R> $list<R> map(final Function<? super T, ? extends R> f) {
        return this.reduce(list(), (x, y) -> x.push(f.apply(y)));
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function. first argument is element index, second one is element value
     * @return newly constructed list which contains converted values
     */
    default <R> $list<R> map(final BiFunction<Integer, ? super T, ? extends R> f) {
        final var i = ref(0);
        return this.map(x -> f.apply(i.$++, x));
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed list which contains converted values
     */
    default <R> $list<R> flatMap(final Function<? super T, $<? extends R>> f) {
        return this.reduce(list(), (x, y) -> x.push(f.apply(y)));
    }

    default <R> $list<R> fmap(final Function<? super T, $<? extends R>> f) { return this.flatMap(f); }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function. first argument is element index, second one is element value
     * @return newly constructed list which contains converted values
     */
    default <R> $list<R> flatMap(final BiFunction<Integer, ? super T, $<? extends R>> f) {
        final var i = ref(0);
        return this.flatMap(x -> f.apply(i.$++, x));
    }

    default <R> $list<R> fmap(final BiFunction<Integer, ? super T, $<? extends R>> f) { return this.flatMap(f); }

    @Override
    default $list<T> take(final Predicate<? super T> f) {
        return this.reduce(list(), (x, y) -> f.test(y) ? x.push(y) : x);
    }

    /**
     * Reverse this list.
     *
     * @return newly constructed reversed list
     */
    default $list<T> reverse() {
        final var rslt = this.clone();
        Collections.reverse(rslt);
        return rslt;
    }

    /**
     * Flatten this list.
     *
     * @param f value generator
     * @return newly constructed flatten list
     */
    default <R> $list<R> flat(final Function<? super T, ? extends Iterable<? extends R>> f) {
        return list(this.iterator().flat(f));
    }

    @Deprecated
    default <R> $list<R> flatten(final Function<? super T, ? extends Iterable<? extends R>> f) { return this.flat(f); }

    /**
     * Return this instance if not empty, otherwise return {@code other}.
     *
     * @param other alternative value
     * @return this instance or other
     */
    default $list<T> orElse(final List<? extends T> other) { return this.orElseGet(() -> other); }

    /**
     * Return this instance if not empty, otherwise return the invocation result of {@code other}.
     *
     * @param other alternative value supplier
     * @return {@code this} instance or other
     */
    default $list<T> orElseGet(final Supplier<? extends List<? extends T>> other) {
        return this.present$().or(() -> list(other.get()));
    }

    @Override
    default <K> $map<K, $list<T>> group(final Function<? super T, ? extends K> fkey) {

        return this.reduce($(ObjFactory.getInstance().newFifoMap()), (x, y) -> {
            x.computeIfAbsent(fkey.apply(y), z -> list()).add(y);
            return x;
        });
    }

    /**
     * Split this list into chunks the length of {@code size}.
     *
     * @param size chunk size
     * @return the list of chunks
     */
    default $list<$list<T>> chunk(final int size) {
        if (size <= 0) throw new IllegalArgumentException("size should greater than 0");

        final $list<$list<T>> ret = list();

        for (int i = 0, Z = (int) Math.ceil((double) this.size() / size); i < Z; i++)
            ret.add(this.slice(i * size, (i + 1) * size));

        return ret;
    }

    @Override
    default $list<T> order(final Comparator<? super T> comp) { return Indolently.sort(this, comp); }

    @Override
    default String join(final Function<T, ? extends CharSequence> f, final String sep) {
        return Indolently.join(this.map(f), sep);
    }

    default $list<T> uniq() { return Indolently.uniq(this); }

    default $list<T> uniq(final BiPredicate<? super T, ? super T> f) { return Indolently.uniq(this, f); }

    /**
     * Replace value at the position if exists.
     *
     * @param idx index of the element
     * @param val replacement value
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> update(final int idx, final T val) { return this.update(idx, x -> val); }

    /**
     * Replace value at the position if exists.
     *
     * @param idx index of the element
     * @param f function
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> update(final int idx, final Function<? super T, ? extends T> f) {
        this.opt(idx).tap(x -> this.set(idx(this, idx), f.apply(x)));
        return this;
    }

    /**
     * Replace value at the position if exists.
     *
     * @param f function
     * @return {@code this} instance
     */
    @Destructive
    default $list<T> update(final Function<? super T, ? extends T> f) {

        for (int i = 0, Z = this.size(); i < Z; i++) this.update(i, f);

        return this;
    }

    /**
     * Replace value at the position if exists.
     *
     * @param idx index of the element
     * @param f function
     * @return newly constructed list
     */
    default $list<T> map(final int idx, final Function<? super T, ? extends T> f) {
        return this.clone().update(idx, f);
    }

    /**
     * Test this list starts with given elements or not.
     *
     * @param col elements
     * @return {@code true} when this list starts with given elements
     */
    default boolean startsWith(final Collection<T> col) {
        return (col != null) && this.narrow(0, col.size()).equals(col);
    }

    /**
     * Test this list ends with given elements or not.
     *
     * @param col elements
     * @return {@code true} when this list ends with given elements
     */
    default boolean endsWith(final Collection<T> col) {
        return (col != null) && this.narrow(-col.size(), 0).equals(col);
    }

    /**
     * Find first index of the element which satisfies given predication.
     *
     * @param f predication
     * @return found index
     */
    default OptionalInt indexOf(final Predicate<T> f) {
        return this.head(f).map(this::indexOf).map(OptionalInt::of).or(OptionalInt::empty);
    }

    /**
     * Find last index of the element which satisfies given predication.
     *
     * @param f predication
     * @return found index
     */
    default OptionalInt lastIndexOf(final Predicate<T> f) {
        return this.last(f).map(this::lastIndexOf).map(OptionalInt::of).or(OptionalInt::empty);
    }

    default <U extends T> $list<U> only(final Class<U> type) { return this.take(type::isInstance).map(type::cast); }

    default boolean randomAccessible() { return true; }
}
