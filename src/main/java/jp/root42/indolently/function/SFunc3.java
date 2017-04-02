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
import java.util.function.Supplier;

import jp.root42.indolently.Functional;


/**
 * @param <X0> first argument type
 * @param <X1> second argument type
 * @param <X2> third argument type
 * @param <Y> return value type
 * @author takahashikzn
 */
public class SFunc3<X0, X1, X2, Y>
    implements Serializable, Function3<X0, X1, X2, Y>, SLambda<SFunc3<X0, X1, X2, Y>> {

    private static final long serialVersionUID = 5099015871546179565L;

    private final Function4<? super Function3<X0, X1, X2, Y>, ? super X0, ? super X1, ? super X2, ? extends Y> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public SFunc3(
        final Function4<? super Function3<X0, X1, X2, Y>, ? super X0, ? super X1, ? super X2, ? extends Y> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public Y apply(final X0 x0, final X1 x1, final X2 x2) {
        return this.body.apply(this, x0, x1, x2);
    }

    /**
     * bind parameter to this function.
     *
     * @param x0 argument to bind
     * @return curried function
     */
    public SFunc2<X1, X2, Y> bind(final X0 x0) {
        return this.bind(() -> x0);
    }

    /**
     * bind parameter to this function.
     *
     * @param x0 argument to bind
     * @return curried function
     */
    public SFunc2<X1, X2, Y> bind(final Supplier<? extends X0> x0) {
        return new SFunc2<>((self, y, z) -> this.apply(x0.get(), y, z));
    }

    /**
     * bind parameter to this function.
     *
     * @param x0 1st argument to bind
     * @param x1 2nd argument to bind
     * @return curried function
     */
    public SFunc<X2, Y> bind(final X0 x0, final X1 x1) {
        return this.bind(() -> x0, () -> x1);
    }

    /**
     * bind parameter to this function.
     *
     * @param x0 1st argument to bind
     * @param x1 2nd argument to bind
     * @return curried function
     */
    public SFunc<X2, Y> bind(final Supplier<? extends X0> x0, final Supplier<? extends X1> x1) {
        return this.bind(x0).bind(x1);
    }

    /**
     * bind parameter to this function.
     *
     * @param x 1st argument to bind
     * @param y 2nd argument to bind
     * @param z 3rd argument to bind
     * @return curried function
     */
    public SSuppl<Y> bind(final X0 x, final X1 y, final X2 z) {
        return this.bind(() -> x, () -> y, () -> z);
    }

    /**
     * bind parameter to this function.
     *
     * @param x0 1st argument to bind
     * @param x1 2nd argument to bind
     * @param x2 3rd argument to bind
     * @return curried function
     */
    public SSuppl<Y> bind(final Supplier<? extends X0> x0, final Supplier<? extends X1> x1,
        final Supplier<? extends X2> x2) {
        return this.bind(x0).bind(x1).bind(x2);
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public Function4<? super Function3<X0, X1, X2, Y>, ? super X0, ? super X1, ? super X2, ? extends Y> body() {
        return this.body;
    }

    @Override
    public SFunc3<X0, X1, X2, Y> memoize() {
        return new SFunc3<>(Functional.memoize(this.body));
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
        } else if (!(o instanceof SFunc3)) {
            return false;
        }

        final SFunc3<?, ?, ?, ?> that = (SFunc3<?, ?, ?, ?>) o;
        return this.body.equals(that.body);
    }
}
