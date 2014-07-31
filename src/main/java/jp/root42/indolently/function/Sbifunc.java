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
package jp.root42.indolently.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import jp.root42.indolently.Functional;


/**
 * @param <T> first argument type
 * @param <U> second argument type
 * @param <R> return value type
 * @author takahashikzn
 */
public class Sbifunc<T, U, R>
    implements Serializable, BiFunction<T, U, R>, Slambda<Sbifunc<T, U, R>> {

    private static final long serialVersionUID = 5099015871546179565L;

    private final TriFunction<? super BiFunction<T, U, R>, ? super T, ? super U, ? extends R> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public Sbifunc(final TriFunction<? super BiFunction<T, U, R>, ? super T, ? super U, ? extends R> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public R apply(final T x, final U y) {
        return this.body.apply(this, x, y);
    }

    /**
     * currying this function.
     *
     * @param x argument to bind
     * @return curried function
     */
    public Sfunc<U, R> curry(final T x) {
        return new Sfunc<>((self, y) -> this.apply(x, y));
    }

    /**
     * currying this function.
     *
     * @param x 1st argument to bind
     * @param y 2nd argument to bind
     * @return curried function
     */
    public Sspplr<R> curry(final T x, final U y) {
        return this.curry(x).curry(y);
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public TriFunction<? super BiFunction<T, U, R>, ? super T, ? super U, ? extends R> body() {
        return this.body;
    }

    @Override
    public Sbifunc<T, U, R> memoize() {
        return new Sbifunc<>(Functional.memoize(this.body));
    }

    @Override
    public String toString() {
        return this.body.toString();
    }

    @Override
    public int hashCode() {
        return this.body.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof Sbifunc)) {
            return false;
        }

        final Sbifunc<?, ?, ?> that = (Sbifunc<?, ?, ?>) o;
        return this.body.equals(that.body);
    }
}
