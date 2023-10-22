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
import java.util.function.Supplier;

import jp.root42.indolently.Functional;


/**
 * @param <X> argument type
 * @param <R> return value type
 * @author takahashikzn
 */
public class SFunc<X, R>
    implements Serializable, Function<X, R>, SLambda<SFunc<X, R>> {

    private static final long serialVersionUID = -8241959687855651918L;

    private final BiFunction<? super Function<X, R>, ? super X, ? extends R> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public SFunc(final BiFunction<? super Function<X, R>, ? super X, ? extends R> body) { this.body = Objects.requireNonNull(body); }

    @Override
    public R apply(final X x) { return this.body.apply(this, x); }

    /**
     * bind parameter to this function.
     *
     * @param x argument to bind
     * @return curried function
     */
    public SSuppl<R> bind(final X x) { return this.bind(() -> x); }

    /**
     * bind parameter to this function.
     *
     * @param x argument to bind
     * @return curried function
     */
    public SSuppl<R> bind(final Supplier<? extends X> x) { return new SSuppl<>(self -> this.apply(x.get())); }

    /**
     * return function body.
     *
     * @return function body
     */
    public BiFunction<? super Function<X, R>, ? super X, ? extends R> body() { return this.body; }

    @Override
    public SFunc<X, R> memoize() { return new SFunc<>(Functional.memoize(this.body)); }

    @Override
    public String toString() { return this.body.toString(); }

    private int hashCode = -1;

    @Override
    public int hashCode() { return (this.hashCode != -1) ? this.hashCode : (this.hashCode = this.body.hashCode()); }

    @Override
    public boolean equals(final Object o) { return this == o || o instanceof SFunc<?, ?> that && this.body.equals(that.body); }
}
