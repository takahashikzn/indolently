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
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.root42.indolently.ref.IntRef;

import static jp.root42.indolently.Indolently.*;


/**
 * Extended {@link List} class for indolent person.
 * It's name comes from "Sugared list".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface Slist<T>
    extends Scol<T, Slist<T>>, List<T> {

    /**
     * Wrap a list.
     * This method is an alias of {@link Indolently#wrap(List)}.
     *
     * @param list list to wrap
     * @return wrapped list
     */
    public static <T> Slist<T> of(final List<T> list) {
        return Indolently.wrap(list);
    }

    /**
     * {@inheritDoc}
     *
     * @see Indolently#freeze(List)
     */
    @Override
    default Slist<T> freeze() {
        return Indolently.freeze(this);
    }

    // for optimization
    @Override
    default T first() {
        return this.get(0);
    }

    /**
     * Equivalent to {@code list.subList(idx, list.size())}.
     *
     * @param from from index. negative index also acceptable.
     * @return sub list
     */
    default List<T> subList(final int from) {
        return this.subList(Indolently.idx(this, from), this.size());
    }

    @Override
    default Slist<T> tail() {
        return (this.size() <= 1) ? Indolently.list() : Indolently.list(this.subList(1));
    }

    // for optimization
    @Override
    default T last() {
        return this.get(-1);
    }

    /**
     * convert this list to {@link Sset}.
     *
     * @return a set constructed from this instance.
     */
    default Sset<T> set() {
        return Indolently.set(this);
    }

    /**
     * insert value at specified index then return this instance.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.push(-1, "x")} means
     * {@code slist.push(slist.size() - 1, "x")}
     * @param value value to add
     * @return {@code this} instance
     */
    @Destructive
    default Slist<T> push(final int idx, final T value) {
        this.add(Indolently.idx(this, idx), value);
        return this;
    }

    /**
     * insert all values at specified index then return this instance.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.pushAll(-1, list("x", "y"))} means
     * {@code slist.pushAll(slist.size() - 1,  list("x", "y"))}
     * @param values values to add
     * @return {@code this} instance
     */
    @Destructive
    default Slist<T> pushAll(final int idx, final Iterable<? extends T> values) {

        // optimization
        final Collection<? extends T> vals =
            (values instanceof Collection) ? (Collection<? extends T>) values : Indolently.list(values);

        this.addAll(Indolently.idx(this, idx), vals);

        return this;
    }

    /**
     * insert value at specified index then return this instance only if value exists.
     * otherwise, do nothing.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.push(-1, "x")} means
     * {@code slist.push(slist.size() - 1, "x")}
     * @param value nullable value to add
     * @return {@code this} instance
     */
    @Destructive
    default Slist<T> push(final int idx, final Optional<? extends T> value) {
        return Indolently.empty(value) ? this : this.push(idx, value.get());
    }

    /**
     * insert all values at specified index then return this instance only if values exist.
     * otherwise, do nothing.
     *
     * @param idx insertion position.
     * negative index also acceptable. for example, {@code slist.pushAll(-1, list("x", "y"))} means
     * {@code slist.pushAll(slist.size() - 1,  list("x", "y"))}
     * @param values nullable values to add
     * @return {@code this} instance
     */
    @Destructive
    default Slist<T> pushAll(final int idx, final Optional<? extends Iterable<? extends T>> values) {
        return Indolently.empty(values) ? this : this.pushAll(idx, values.get());
    }

    /**
     * an alias of {@link #subList(int, int)} but newly constructed (detached) view.
     *
     * @param from from index (inclusive)
     * @param to to index (exclusive)
     * @return detached sub list
     */
    default Slist<T> slice(final int from, final int to) {
        return Indolently.list(this.subList(from, to));
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed list which contains converted values
     */
    default <R> Slist<R> map(final Function<? super T, ? extends R> f) {

        final Slist<R> rslt = Indolently.list();

        for (final T val : this) {
            rslt.add(f.apply(val));
        }

        return rslt;
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function. first argument is loop index.
     * @return newly constructed list which contains converted values
     */
    default <R> Slist<R> map(final BiFunction<Integer, ? super T, ? extends R> f) {

        final IntRef i = ref(0);

        return this.map(x -> f.apply(i.val++, x));
    }

    @Override
    default Slist<T> filter(final Predicate<? super T> f) {

        final Slist<T> list = list();

        for (final T val : this) {
            if (f.test(val)) {
                list.add(val);
            }
        }

        return list;
    }

    /**
     * Reverse this list.
     *
     * @return newly constructed reversed list
     */
    default Slist<T> reverse() {
        final Slist<T> rslt = Indolently.list(this);
        Collections.reverse(rslt);
        return rslt;
    }
}
