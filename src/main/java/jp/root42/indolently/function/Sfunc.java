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
import java.util.function.Function;

import jp.root42.indolently.Functional;


/**
 * @param <T> argument type
 * @param <R> return value type
 * @author takahashikzn
 */
public class Sfunc<T, R>
    implements Serializable, Function<T, R>, Slambda<Sfunc<T, R>> {

    private static final long serialVersionUID = -8241959687855651918L;

    private final BiFunction<? super Function<T, R>, ? super T, ? extends R> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public Sfunc(final BiFunction<? super Function<T, R>, ? super T, ? extends R> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public R apply(final T x) {
        return this.body.apply(this, x);
    }

    /**
     * currying this function.
     *
     * @param x argument to bind
     * @return curried function
     */
    public Sspplr<R> curry(final T x) {
        return new Sspplr<>(self -> this.apply(x));
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public BiFunction<? super Function<T, R>, ? super T, ? extends R> body() {
        return this.body;
    }

    @Override
    public Sfunc<T, R> memoize() {
        return new Sfunc<>(Functional.memoize(this.body));
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
        } else if (!(o instanceof Sfunc)) {
            return false;
        }

        final Sfunc<?, ?> that = (Sfunc<?, ?>) o;
        return this.body.equals(that.body);
    }
}
