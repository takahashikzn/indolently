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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
public interface SIter<T>
    extends Iterator<T>, Supplier<T>, EdgeAwareIterable<T>, Loopable<T, SIter<T>>, Filterable<T, SIter<T>>,
    ReducibleIterable<T>, Matchable<T> {

    @Override
    default T get() {
        return this.next();
    }

    @Override
    default Iterator<T> iterator() {
        return this;
    }

    @Override
    default boolean some(final Predicate<? super T> f) {

        for (final T val : this) {
            if (f.test(val)) {
                return true;
            }
        }

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
    static <E, T> SIter<T> of(final E env, final Predicate<? super E> hasNext,
        final Function<? super E, ? extends T> next) {

        Objects.requireNonNull(next);

        return new SIter<T>() {

            @Override
            public boolean hasNext() {
                return hasNext.test(env);
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                return next.apply(env);
            }
        };
    }

    @Override
    default SIter<T> each(final Consumer<? super T> f) {

        return this.map(x -> {
            f.accept(x);
            return x;
        });
    }

    @Override
    default SIter<T> filter(final Predicate<? super T> f) {

        return new SIter<T>() {

            private Optional<T> cur;

            @Override
            public boolean hasNext() {
                if (!Indolently.isNull(this.cur)) {
                    return true;
                } else if (!SIter.this.hasNext()) {
                    return false;
                }

                final T val = SIter.this.next();

                if (f.test(val)) {
                    this.cur = Optional.ofNullable(val);
                    return true;
                }

                return this.hasNext();
            }

            @Override
            public T next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                final T val = this.cur.get();
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
    default <R> SIter<R> map(final Function<? super T, ? extends R> f) {
        return of(this, x -> x.hasNext(), x -> f.apply(x.next()));
    }

    /**
     * construct new list which contains all elements contained by this instance.
     *
     * @return a list
     */
    default SList<T> list() {
        return Indolently.list(this);
    }

    /**
     * create a {@link Stream} view of this iterator.
     *
     * @return {@link Stream} view of this iterator
     * @see Collection#stream()
     */
    default SStream<T> stream() {
        return Indolently.wrap(StreamSupport.stream(this.spliterator(), false));
    }

    /**
     * create a parallelized {@link Stream} view of this iterator.
     *
     * @return parallelized {@link Stream} view of this iterator
     * @see Collection#parallelStream()
     */
    default SStream<T> parallelStream() {
        return Indolently.wrap(StreamSupport.stream(this.spliterator(), true));
    }

    @SuppressWarnings("javadoc")
    default <R> SIter<R> aggregate(final Function<? super Iterable<? extends T>, ? extends Iterable<? extends R>> f) {
        return Indolently.wrap(f.apply(this).iterator());
    }

    @SuppressWarnings("javadoc")
    default <R> SIter<R> flatten(final Function<? super T, ? extends Iterable<? extends R>> f) {
        Objects.requireNonNull(f);

        return new SIter<R>() {

            private Iterator<? extends R> cur = Iterative.iterator();

            @Override
            public boolean hasNext() {

                if (!this.cur.hasNext() && SIter.this.hasNext()) {
                    this.cur = f.apply(SIter.this.next()).iterator();
                }

                return this.cur.hasNext();
            }

            @Override
            public R next() {
                return this.hasNext() ? this.cur.next() : Expressive.raise(() -> new NoSuchElementException());
            }
        };
    }
}
