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
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.Functional;


/**
 * @param <T> argument type
 * @author takahashikzn
 */
public class SPred<T>
    implements Serializable, Predicate<T>, SLambda<SPred<T>> {

    private static final long serialVersionUID = 9028625532191422275L;

    private final BiPredicate<? super Predicate<T>, ? super T> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public SPred(final BiPredicate<? super Predicate<T>, ? super T> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public boolean test(final T x) {
        return this.body.test(this, x);
    }

    /**
     * currying this function.
     *
     * @param x argument to bind
     * @return curried function
     */
    public SBoolSuppl curry(final T x) {
        return this.curry(() -> x);
    }

    /**
     * currying this function.
     *
     * @param x argument to bind
     * @return curried function
     */
    public SBoolSuppl curry(final Supplier<? extends T> x) {
        return new SBoolSuppl(self -> this.test(x.get()));
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public BiPredicate<? super Predicate<T>, ? super T> body() {
        return this.body;
    }

    @Override
    public SPred<T> memoize() {
        return new SPred<>(Functional.memoize(this.body));
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
        } else if (!(o instanceof SPred)) {
            return false;
        }

        final SPred<?> that = (SPred<?>) o;
        return this.body.equals(that.body);
    }
}
