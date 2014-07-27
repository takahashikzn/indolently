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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * @param <T> value type
 * @author takahashikzn
 */
public interface Iter<T>
    extends Supplier<T>, Iterable<T>, Iterator<T> {

    /**
     * constructor.
     *
     * @param env iteration environment
     * @param hasNext {@link Iterator#hasNext()} implementation
     * @param next {@link Iterator#next()} implementation
     * @return a instance of this class.
     */
    static <E, T> Iter<T> of(final E env, final Predicate<? super E> hasNext,
        final Function<? super E, ? extends T> next) {

        Objects.requireNonNull(next);

        final Iterator<T> i = new Iterator<T>() {

            @Override
            public boolean hasNext() {
                return hasNext.test(env);
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                return next.apply(env);
            }
        };

        return new Iter<T>() {

            @Override
            public T get() {
                return this.next();
            }

            @Override
            public Iterator<T> iterator() {
                return i;
            }

            @Override
            public boolean hasNext() {
                return i.hasNext();
            }

            @Override
            public T next() {
                return i.next();
            }
        };
    }

    /**
     * construct new list which contains all elements contained by this instance.
     *
     * @return a list
     */
    default Slist<T> list() {
        return Indolently.list(this);
    }
}
