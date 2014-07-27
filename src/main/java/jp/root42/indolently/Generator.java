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

import jp.root42.indolently.ref.BoolRef;
import jp.root42.indolently.ref.Ref;


/**
 * @param <T> value type
 * @author takahashikzn
 */
public interface Generator<T>
    extends Iter<T> {

    /**
     * send stop signal.
     *
     * @return actually never return any value.
     */
    static <T> T stop() {
        throw new Stop();
    }

    /**
     * @author takahashikzn
     */
    final class Stop
        extends RuntimeException {

        private static final long serialVersionUID = -7710521845711826670L;

        private Stop() {
        }
    }

    /**
     * constructor.
     *
     * @param env iteration environment
     * @param hasNext {@link Iterator#hasNext()} implementation
     * @param next {@link Iterator#next()} implementation
     * @return a instance of this class.
     */
    static <E, T> Generator<T> of(final E env, final Function<? super E, ? extends T> next) {
        Objects.requireNonNull(next);

        final BoolRef stopped = Indolently.ref(false);
        final Ref<T> nextVal = Indolently.<T> ref(null);

        final Iterator<T> i = new Iterator<T>() {

            @Override
            public boolean hasNext() {
                if (stopped.val) {
                    return false;
                }

                try {
                    nextVal.val = next.apply(env);
                    return true;
                } catch (final Stop | NoSuchElementException s) {
                    stopped.val = true;
                    return false;
                }
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                return nextVal.val;
            }
        };

        return new Generator<T>() {

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
}
