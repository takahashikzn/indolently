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
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import jp.root42.indolently.Functional;


/**
 * @param <T> argument type
 * @author takahashikzn
 */
public class Spred<T>
    implements Predicate<T>, Sfunctor<Spred<T>> {

    private final BiPredicate<? super Predicate<T>, ? super T> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public Spred(final BiPredicate<? super Predicate<T>, ? super T> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public boolean test(final T x) {
        return this.body.test(this, x);
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
    public Spred<T> memoize() {
        return new Spred<>(Functional.memoize(this.body));
    }

    @Override
    public String toString() {
        return this.body.toString();
    }
}
