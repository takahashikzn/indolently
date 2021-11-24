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
package jp.root42.indolently.trait;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * @param <T> -
 * @author takahashikzn
 */
public interface EdgeAwareIterable<T>
    extends Iterable<T> {

    /**
     * get first element.
     *
     * @return first element
     */
    default $<T> head$() { return this.head(vrai()); }

    /**
     * get first element.
     *
     * @return first element
     * @throws NoSuchElementException if empty
     */
    default T head() { return this.head$().get(); }

    /**
     * get first element which satisfy the condition.
     *
     * @param f condition
     * @return first element as optional representation
     * @throws NullPointerException if the value which satisfy the condition is null
     */
    default $<T> head(final Predicate<? super T> f) {

        for (final T val: this)
            if (f.test(val)) return $.of(val);

        return $.none();
    }

    /**
     * get first element if exists, otherwise return alternative value.
     *
     * @param other alternative value.
     * @return first element or alternative value
     */
    default T head(final Supplier<? extends T> other) { return this.head(x -> true, other); }

    /**
     * get first element which satisfy the condition, otherwise return alternative value.
     *
     * @param f condition
     * @param other alternative value.
     * @return first element or alternative value
     */
    default T head(final Predicate<? super T> f, final Supplier<? extends T> other) { return this.head(f).or(other); }

    /**
     * get last element of this iterator.
     *
     * @return last element
     */
    default $<T> last$() { return this.last(vrai()); }

    /**
     * get last element of this iterator.
     *
     * @return last element
     * @throws NoSuchElementException if this iterator is empty
     */
    default T last() { return this.last$().get(); }

    /**
     * get last element which satisfy the condition.
     *
     * @param f condition
     * @return last element as optional representation
     * @throws NullPointerException if the value which satisfy the condition is null
     */
    default $<T> last(final Predicate<? super T> f) {

        T rslt = null;

        for (final T val: this)
            if (f.test(val)) rslt = val;

        return $.of(rslt);
    }

    /**
     * get last element if exists, otherwise return alternative value.
     *
     * @param other alternative value.
     * @return last element or alternative value
     */
    default T last(final Supplier<? extends T> other) { return this.last(x -> true, other); }

    /**
     * get last element which satisfy the condition, otherwise return alternative value.
     *
     * @param f condition
     * @param other alternative value
     * @return last element or alternative value
     */
    default T last(final Predicate<? super T> f, final Supplier<? extends T> other) { return this.last(f).or(other); }

    default <R> $<R> fhead(final Function<T, $<R>> f) {

        for (final var val: this) {
            final var ret = f.apply(val);
            if (ret.present()) return ret;
        }

        return $.none();
    }

    default <R> $<R> flast(final Function<T, $<R>> f) {

        $<R> ret = $.none();

        for (final var val: this) {
            final var x = f.apply(val);
            if (x.present()) ret = x;
        }

        return ret;
    }
}
