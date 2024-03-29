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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jp.root42.indolently.ref.$;
import jp.root42.indolently.trait.EdgeAwareIterable;
import jp.root42.indolently.trait.Filterable;
import jp.root42.indolently.trait.Loopable;
import jp.root42.indolently.trait.Matchable;
import jp.root42.indolently.trait.ReducibleIterable;


/**
 * Extended {@link Iterable}/{@link Iterator} class for indolent person.
 * The name is came from "Sugared iterator".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface $iter<T>
    extends Iterator<T>, Supplier<T>, EdgeAwareIterable<T>, Loopable<T, $iter<T>>, Filterable<T, $iter<T>>, ReducibleIterable<T>, Matchable<T> {

    @Override
    default T get() { return this.next(); }

    @Override
    default Iterator<T> iterator() { return this; }

    @Override
    default boolean any(final Predicate<? super T> f) {

        for (final var val: this)
            if (f.test(val)) return true;

        return false;
    }

    /**
     * constructor.
     *
     * @param env iteration environment
     * @param hasNext {@link Iterator#hasNext()} implementation
     * @param next {@link Iterator#next()} implementation
     * @return a instance of this class.
     */
    static <E, T> $iter<T> of(final E env, final Predicate<? super E> hasNext, final Function<? super E, ? extends T> next) {

        Objects.requireNonNull(next);

        return new $iter<>() {

            @Override
            public boolean hasNext() { return hasNext.test(env); }

            @SuppressWarnings("IteratorNextCanNotThrowNoSuchElementException")
            @Override
            public T next() { return this.hasNext() ? next.apply(env) : Expressive.raise(NoSuchElementException::new); }
        };
    }

    @Override
    default $iter<T> each(final Consumer<? super T> f) {

        return this.map(x -> {
            f.accept(x);
            return x;
        });
    }

    @Override
    default $iter<T> take(final Predicate<? super T> f) {

        return new $iter<>() {

            private $<T> cur;

            @Override
            public boolean hasNext() {

                while (true) {
                    if (!Indolently.isNull(this.cur)) return true;
                    if (!$iter.this.hasNext()) return false;

                    final var val = $iter.this.next();

                    if (f.test(val)) {
                        this.cur = $.of(val);
                        return true;
                    }
                }
            }

            @Override
            public T next() {
                if (!this.hasNext()) throw new NoSuchElementException();

                final var val = this.cur.get();
                this.cur = null;
                return val;
            }
        };
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed iterator which iterates converted values
     */
    default <R> $iter<R> map(final Function<? super T, ? extends R> f) { return of(this, x -> x.hasNext(), x -> f.apply(x.next())); }

    /**
     * construct new list which contains all elements contained by this instance.
     *
     * @return a list
     */
    default $list<T> list() { return Indolently.list(this); }

    /**
     * create a {@link Stream} view of this iterator.
     *
     * @return {@link Stream} view of this iterator
     * @see Collection#stream()
     */
    default $stream<T> stream() { return Indolently.$(StreamSupport.stream(this.spliterator(), false)); }

    /**
     * create a parallelized {@link Stream} view of this iterator.
     *
     * @return parallelized {@link Stream} view of this iterator
     * @see Collection#parallelStream()
     */
    default $stream<T> parallelStream() { return Indolently.$(StreamSupport.stream(this.spliterator(), true)); }

    default <R> $iter<R> aggregate(final Function<? super Iterable<? extends T>, ? extends Iterable<? extends R>> f) {
        return Indolently.$(f.apply(this).iterator());
    }

    default <R> $iter<R> flat(final Function<? super T, ? extends Iterable<? extends R>> f) {
        Objects.requireNonNull(f);

        return new $iter<>() {

            private Iterator<? extends R> cur = Iterative.iterator();

            @Override
            public boolean hasNext() {

                if (this.cur.hasNext()) return true;

                while ($iter.this.hasNext()) {
                    this.cur = f.apply($iter.this.next()).iterator();

                    if (this.cur.hasNext()) return true;
                }

                return false;
            }

            @Override
            public R next() { return this.hasNext() ? this.cur.next() : Expressive.raise(NoSuchElementException::new); }
        };
    }
}
