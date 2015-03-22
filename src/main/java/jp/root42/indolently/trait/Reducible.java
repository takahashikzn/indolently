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

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import jp.root42.indolently.function.TriFunction;


/**
 * @param <T> -
 * @author takahashikzn
 */
public interface Reducible<T> {

    /**
     * Map then Reduce operation with initial value.
     *
     * @param <R> mapping target type
     * @param initial initial value
     * @param f function
     * @return result value
     * @throws NoSuchElementException if the result not present
     */
    default <R> R reduce(final R initial, final BiFunction<? super R, ? super T, ? extends R> f) {
        return this.reduce(initial, (x, y, z) -> f.apply(y, z));
    }

    /**
     * Reduce operation.
     *
     * @param f function
     * @return result value
     * @throws NoSuchElementException if this collection is empty
     * @see #mapred(Function, BiFunction)
     */
    default Optional<T> reduce(final BiFunction<? super T, ? super T, ? extends T> f) {
        return this.reduce((x, y, z) -> f.apply(y, z));
    }

    /**
     * Map then Reduce operation.
     *
     * @param <R> mapping target type
     * @param fm mapper function
     * @param fr reducer function
     * @return result value
     * @throws NoSuchElementException if this collection is empty
     */
    default <R> Optional<R> mapred(final Function<? super T, ? extends R> fm,
        final BiFunction<? super R, ? super R, ? extends R> fr) {

        return this.mapred(fm, (x, y, z) -> fr.apply(y, z));
    }

    /**
     * Map then Reduce operation with initial value.
     *
     * @param <R> mapping target type
     * @param initial initial value
     * @param f function
     * @return result value
     * @throws NoSuchElementException if the result not present
     */
    default <R> R reduce(final R initial, final TriFunction<Integer, ? super R, ? super T, ? extends R> f) {
        return this.reduce(Optional.of(initial), f).get();
    }

    /**
     * Map then Reduce operation with initial value.
     *
     * @param <R> mapping target type
     * @param initial initial value
     * @param f function
     * @return result value
     */
    <R> Optional<R> reduce(Optional<? extends R> initial, TriFunction<Integer, ? super R, ? super T, ? extends R> f);

    /**
     * Reduce operation.
     *
     * @param f function
     * @return result value
     * @throws NoSuchElementException if this collection is empty
     * @see #mapred(Function, BiFunction)
     */
    default Optional<T> reduce(final TriFunction<Integer, ? super T, ? super T, ? extends T> f) {

        // "x -> x" lambda literal occurs compilation error on OracleJDK compiler
        return this.mapred(Function.identity(), f);
    }

    /**
     * Map then Reduce operation.
     *
     * @param <R> mapping target type
     * @param fm mapper function
     * @param fr reducer function
     * @return result value
     * @throws NoSuchElementException if this collection is empty
     */
    <R> Optional<R> mapred(Function<? super T, ? extends R> fm,
        TriFunction<Integer, ? super R, ? super R, ? extends R> fr);
}
