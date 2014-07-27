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
package jp.root42.indolently;

import java.util.function.Predicate;


/**
 * @param <T> -
 * @param <SELF> -
 * @author takahashikzn
 */
public interface Filterable<T, SELF extends Filterable<T, SELF>> {

    /**
     * Filter operation: returns values which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition
     * @return new filtered collection
     */
    SELF filter(Predicate<? super T> f);
}
