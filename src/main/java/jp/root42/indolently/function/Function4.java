// Copyright 2016 takahashikzn
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
import java.util.function.Function;


/**
 * @author takahashikzn
 */
@FunctionalInterface
@SuppressWarnings("javadoc")
public interface Function4<X0, X1, X2, X3, Y> {

    Y apply(X0 x0, X1 x1, X2 x2, X3 x3);

    default <Z> Function4<X0, X1, X2, X3, Z> andThen(final Function<? super Y, ? extends Z> after) {
        Objects.requireNonNull(after);
        return (x0, x1, x2, x3) -> after.apply(this.apply(x0, x1, x2, x3));
    }
}
