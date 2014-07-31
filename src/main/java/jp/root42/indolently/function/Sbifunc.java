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
    implements BiFunction<T, U, R>, Sfunctor<Sbifunc<T, U, R>> {

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
}
