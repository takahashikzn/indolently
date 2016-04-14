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
import java.util.function.BiPredicate;
import java.util.function.Supplier;

import jp.root42.indolently.Functional;


/**
 * @param <X0> argument type
 * @param <X1> return value type
 * @author takahashikzn
 */
public class SPred2<X0, X1>
    implements Serializable, BiPredicate<X0, X1>, SLambda<SPred2<X0, X1>> {

    private static final long serialVersionUID = 2715052895955591445L;

    private final Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public SPred2(final Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public boolean test(final X0 x0, final X1 x1) {
        return this.body.test(this, x0, x1);
    }

    /**
     * currying this function.
     *
     * @param x0 argument to bind
     * @return curried function
     */
    public SPred<X1> curry(final X0 x0) {
        return this.curry(() -> x0);
    }

    /**
     * currying this function.
     *
     * @param x0 argument to bind
     * @return curried function
     */
    public SPred<X1> curry(final Supplier<? extends X0> x0) {
        return new SPred<>((self, x1) -> this.test(x0.get(), x1));
    }

    /**
     * currying this function.
     *
     * @param x0 1st argument to bind
     * @param x1 2nd argument to bind
     * @return curried function
     */
    public SBoolSuppl curry(final X0 x0, final X1 x1) {
        return this.curry(() -> x0, () -> x1);
    }

    /**
     * currying this function.
     *
     * @param x0 1st argument to bind
     * @param x1 2nd argument to bind
     * @return curried function
     */
    public SBoolSuppl curry(final Supplier<? extends X0> x0, final Supplier<? extends X1> x1) {
        return this.curry(x0).curry(x1);
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body() {
        return this.body;
    }

    @Override
    public SPred2<X0, X1> memoize() {
        return new SPred2<>(Functional.memoize(this.body));
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
        } else if (!(o instanceof SPred2)) {
            return false;
        }

        final SPred2<?, ?> that = (SPred2<?, ?>) o;
        return this.body.equals(that.body);
    }
}
