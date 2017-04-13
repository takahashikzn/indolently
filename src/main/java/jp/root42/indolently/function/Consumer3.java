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


/**
 * @author takahashikzn
 */
@FunctionalInterface
public interface Consumer3<X0, X1, X2> {

    void accept(X0 x0, X1 x1, X2 x2);

    default Consumer3<X0, X1, X2> andThen(final Consumer3<? super X0, ? super X1, ? super X2> after) {
        Objects.requireNonNull(after);

        return (x0, x1, x2) -> {
            this.accept(x0, x1, x2);
            after.accept(x0, x1, x2);
        };
    }
}
