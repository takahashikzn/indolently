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

import jp.root42.indolently.Functional;


/**
 * @param <T> argument type
 * @param <U> return value type
 * @author takahashikzn
 */
public class Sbipred<T, U>
    implements Serializable, BiPredicate<T, U>, Sfunctor<Sbipred<T, U>> {

    private static final long serialVersionUID = 2715052895955591445L;

    private final TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public Sbipred(final TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public boolean test(final T x, final U y) {
        return this.body.test(this, x, y);
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> body() {
        return this.body;
    }

    @Override
    public Sbipred<T, U> memoize() {
        return new Sbipred<>(Functional.memoize(this.body));
    }

    @Override
    public String toString() {
        return this.body.toString();
    }
}
