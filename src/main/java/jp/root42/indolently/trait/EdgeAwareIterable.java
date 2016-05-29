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
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

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
     * @throws NoSuchElementException if empty
     */
    default T head() {
        return this.head(vrai()).get();
    }

    /**
     * get first element which satisfy the condition.
     *
     * @param f condition
     * @return first element as optional representation
     * @throws NullPointerException if the value which satisfy the condition is null
     */
    default Optional<T> head(final Predicate<? super T> f) {

        for (final T val : this) {
            if (f.test(val)) {
                return Optional.of(val);
            }
        }

        return Optional.empty();
    }

    /**
     * get first element if exists, otherwise return alternative value.
     *
     * @param other alternative value.
     * @return first element or alternative value
     */
    default T head(final Supplier<? extends T> other) {
        return this.head(x -> true, other);
    }

    /**
     * get first element which satisfy the condition, otherwise return alternative value.
     *
     * @param f condition
     * @param other alternative value.
     * @return first element or alternative value
     */
    default T head(final Predicate<? super T> f, final Supplier<? extends T> other) {

        for (final T val : this) {
            if (f.test(val)) {
                return val;
            }
        }

        return other.get();
    }

    /**
     * get last element of this iterator.
     *
     * @return last element
     * @throws NoSuchElementException if this iterator is empty
     */
    default T last() {
        return this.last(x -> true).get();
    }

    /**
     * get last element which satisfy the condition.
     *
     * @param f condition
     * @return last element as optional representation
     * @throws NullPointerException if the value which satisfy the condition is null
     */
    default Optional<T> last(final Predicate<? super T> f) {

        T rslt = null;

        for (final T val : this) {
            if (f.test(val)) {
                rslt = val;
            }
        }

        return Optional.ofNullable(rslt);
    }

    /**
     * get last element if exists, otherwise return alternative value.
     *
     * @param other alternative value.
     * @return last element or alternative value
     */
    default T last(final Supplier<? extends T> other) {
        return this.last(x -> true, other);
    }

    /**
     * get last element which satisfy the condition, otherwise return alternative value.
     *
     * @param f condition
     * @param other alternative value
     * @return last element or alternative value
     */
    default T last(final Predicate<? super T> f, final Supplier<? extends T> other) {

        boolean found = false;
        T rslt = null;

        for (final T val : this) {
            if (f.test(val)) {
                found = true;
                rslt = val;
            }
        }

        return found ? rslt : other.get();
    }
}
