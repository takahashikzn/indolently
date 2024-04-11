// Copyright 2024 takahashikzn
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
package jp.root42.indolently.trait;

import java.util.function.Function;
import java.util.function.Predicate;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface FilterableWhile<T, SELF extends Filterable<T, SELF>>
    extends Filterable<T, SELF> {

    default SELF takeWhile(final Predicate<? super T> f) { return this.doWhile(f, this::take); }

    default SELF dropWhile(final Predicate<? super T> f) { return this.doWhile(f, this::drop); }

    private SELF doWhile(final Predicate<? super T> cond, final Function<Predicate<? super T>, SELF> func) {
        final var state = ref(true);
        return func.apply(x -> state.$ && (state.$ = cond.test(x)));
    }
}
