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

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import jp.root42.indolently.trait.EdgeAwareIterable;
import jp.root42.indolently.trait.Filterable;
import jp.root42.indolently.trait.Loopable;
import jp.root42.indolently.trait.Matchable;
import jp.root42.indolently.trait.ReducibleIterable;


/**
 * Extended {@link Stream} class for indolent person.
 * The name is came from "Sugared Stream".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface $stream<T>
    extends Stream<T>, EdgeAwareIterable<T>, Loopable<T, $stream<T>>, Filterable<T, $stream<T>>, ReducibleIterable<T>, Matchable<T> {

    @Deprecated
    @Override
    default $stream<T> filter(final Predicate<? super T> f) { return this.take(f); }

    @Override
    default void forEach(final Consumer<? super T> action) { ReducibleIterable.super.forEach(action); }

    @Override
    default $stream<T> each(final Consumer<? super T> f) { return Indolently.$(this.peek(f)); }

    @Override
    default boolean any(final Predicate<? super T> f) { return this.anyMatch(f); }

    // optimization
    @Override
    default boolean all(final Predicate<? super T> f) { return this.allMatch(f); }

    // optimization
    @Override
    default boolean non(final Predicate<? super T> f) { return this.noneMatch(f); }

    @Override
    default Spliterator<T> spliterator() { return Spliterators.spliteratorUnknownSize(this.iterator(), 0); }
}
