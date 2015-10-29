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
import java.util.Optional;
import java.util.function.Function;

import static jp.root42.indolently.Indolently.*;


/**
 * @param <T> value type
 * @author takahashikzn
 */
public interface Generator<T>
    extends SIter<T> {

    /**
     * Notify this generator reaches the end of generation.
     *
     * @return actually never return any value.
     */
    static <T> T breaks() {
        throw new Break();
    }

    /**
     * @author takahashikzn
     */
    final class Break
        extends NoSuchElementException {

        private static final long serialVersionUID = -7710521845711826670L;

        private Break() {}
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

        return new Generator<T>() {

            private boolean stopped; // NOPMD

            private Optional<T> cur; // NOPMD

            @Override
            public boolean hasNext() {
                if (this.stopped) {
                    return false;
                } else if (this.cur != null) {
                    return true;
                }

                try {
                    this.cur = optional(next.apply(env));
                    return true;
                } catch (@SuppressWarnings("unused") final Break s) {
                    this.stopped = true;
                    return false;
                }
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                final T val = this.cur.get();
                this.cur = null;
                return val;
            }
        };
    }
}
