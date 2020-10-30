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

import java.util.function.BiPredicate;

import static jp.root42.indolently.Expressive.*;


/**
 * @author takahashikzn
 */
@FunctionalInterface
public interface Predicate2E<T1, T2, E extends Exception> {

    default BiPredicate<T1, T2> as() { return (t1, t2) -> eval(() -> this.test(t1, t2)); }

    boolean test(T1 t1, T2 t2) throws E;
}
