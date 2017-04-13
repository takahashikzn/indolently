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

import jp.root42.indolently.Expressive;


/**
 * Represents an arbitrary operation that doesn't accept any argument.
 *
 * @author takahashikzn
 */
@FunctionalInterface
public interface Statement {

    /**
     * Perform this operation.
     *
     * @throws Exception any exception which this statement would throw
     */
    void exec() throws Exception;

    /**
     * Perform this operation.
     */
    default void execute() {
        try {
            this.exec();
        } catch (final Exception e) {
            Expressive.raise(e);
        }
    }

    /**
     * Returns composed statement which execute this statement then execute {@code after}.
     *
     * @param after statement which execute after this statement
     * @return composed statement
     */
    default Statement andThen(final Statement after) {

        Objects.requireNonNull(after);

        return () -> {
            this.execute();
            after.execute();
        };
    }
}
