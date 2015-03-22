// Copyright 2015 takahashikzn
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
package jp.root42.indolently.ref;

/**
 * Numeral value reference.
 *
 * @param <T> numeral type.
 * @param <S> self type.
 * @author takahashikzn
 * @version $Id$
 */
public interface NumberRef<T extends Number, S extends NumberRef<T, S>>
    extends ValueReference<T, S> {

    /**
     * add operation.
     *
     * @param val value to add
     * @return {@code this} instance.
     */
    S add(T val);

    /**
     * add operation.
     *
     * @param val value to multiply
     * @return {@code this} instance.
     */
    S mul(T val);

    /**
     * add operation.
     *
     * @param val value to divide
     * @return {@code this} instance.
     */
    S div(T val);

    /**
     * negate operation.
     *
     * @return {@code this} instance.
     */
    S negate();
}
