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

import jp.root42.indolently.Expressive;


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
        return this.iterator().next();
    }

    /**
     * get first element if exists, otherwise return alternative value.
     *
     * @param other alternative value.
     * @return first element or alternative value
     */
    default T head(final Supplier<? extends T> other) {
        return Expressive.ifelse(this.iterator(), i -> i.hasNext(), i -> i.next(), i -> other.get());
    }

    /**
     * get first element which satisfy the condition.
     *
     * @param f condition
     * @return first element
     */
    default Optional<T> head(final Predicate<? super T> f) {

        for (final T val : this) {
            if (f.test(val)) {
                return Optional.ofNullable(val);
            }
        }

        return Optional.empty();
    }

    /**
     * get last element of this iterator.
     *
     * @return last element
     * @throws NoSuchElementException if this iterator is empty
     */
    default T last() {
        if (!this.iterator().hasNext()) {
            throw new NoSuchElementException();
        }

        return this.last(x -> true).get();
    }

    /**
     * get last element which satisfy the condition.
     *
     * @param f condition
     * @return first element
     */
    default Optional<T> last(final Predicate<? super T> f) {

        Optional<T> rslt = Optional.empty();

        for (final T val : this) {
            if (f.test(val)) {
                rslt = Optional.ofNullable(val);
            }
        }

        return rslt;
    }
}
