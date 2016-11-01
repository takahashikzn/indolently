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

import java.util.function.Supplier;

import jp.root42.indolently.Expressive;


/**
 * Represents an arbitrary operation that doesn't accept any argument.
 *
 * @param <T> type of this expression
 * @author takahashikzn
 */
@FunctionalInterface
public interface Expression<T> {

    /**
     * evaluate this expression.
     *
     * @return evaluation result
     * @throws Exception any exception which this expression would throw
     */
    T eval() throws Exception;

    /**
     * evaluate this expression.
     *
     * @return evaluation result
     * @throws RuntimeException if anything wrong.
     */
    default T get() {
        try {
            return this.eval();
        } catch (final Exception e) {
            return Expressive.raise(e);
        }
    }

    /**
     * adapt this expression to {@link Supplier} interface.
     *
     * @return Supplier instance
     */
    default Supplier<T> asSupplier() {
        return this::get;
    }
}
