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
package jp.root42.indolently.trait;

import java.util.function.Predicate;


/**
 * @param <T> -
 * @author takahashikzn
 */
public interface Matchable<T> {

    /**
     * Test whether is there any value which satisfies condition.
     *
     * @param f condition
     * @return test result
     */
    boolean some(Predicate<? super T> f);

    /**
     * Test whether all values satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean every(final Predicate<? super T> f) {
        return !this.some(f.negate());
    }

    /**
     * Test whether no value satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean none(final Predicate<? super T> f) {
        return !this.some(f);
    }
}
