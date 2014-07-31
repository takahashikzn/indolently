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

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.trait.EdgeAwareIterable;
import jp.root42.indolently.trait.Filterable;
import jp.root42.indolently.trait.Loopable;
import jp.root42.indolently.trait.Matchable;
import jp.root42.indolently.trait.Reducible;


/**
 * Extended {@link Iterable}/{@link Iterator} class for indolent person.
 * It's name comes from "Sugared iterator".
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface Siter<T>
    extends Supplier<T>, Iterator<T>, EdgeAwareIterable<T>, Loopable<T, Siter<T>>, Filterable<T, Siter<T>>,
    Reducible<T>, Matchable<T> {

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
    static <E, T> Siter<T> of(final E env, final Predicate<? super E> hasNext,
        final Function<? super E, ? extends T> next) {

        Objects.requireNonNull(next);

        return new Siter<T>() {

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
    default Siter<T> each(final Consumer<? super T> f) {

        return this.map(x -> {
            f.accept(x);
            return x;
        });
    }

    @Override
    default Siter<T> filter(final Predicate<? super T> f) {

        return new Siter<T>() {

            private Optional<T> cur;

            @Override
            public boolean hasNext() {
                if (this.cur != null) {
                    return true;
                } else if (!Siter.this.hasNext()) {
                    return false;
                }

                final T val = Siter.this.next();

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

    @Override
    default <R> Optional<R> mapred(final Function<? super T, ? extends R> fm,
        final BiFunction<? super R, ? super R, ? extends R> fr) {

        if (!this.hasNext()) {
            return Optional.empty();
        }

        return this.mapred( //
            Optional.ofNullable(fm.apply(this.next())), //
            (final R rem, final T val) -> fr.apply(rem, fm.apply(val)));
    }

    @Override
    default <R> Optional<R> mapred(final Optional<? extends R> initial,
        final BiFunction<? super R, ? super T, ? extends R> f) {

        R rem = initial.orElse(null);

        for (final T val : this) {
            rem = f.apply(rem, val);
        }

        return Optional.ofNullable(rem);
    }

    /**
     * Map operation: map value to another type value.
     *
     * @param <R> mapped value type
     * @param f function
     * @return newly constructed iterator which iterates converted values
     */
    default <R> Siter<R> map(final Function<? super T, ? extends R> f) {
        return of(this, x -> x.hasNext(), x -> f.apply(x.next()));
    }

    /**
     * construct new list which contains all elements contained by this instance.
     *
     * @return a list
     */
    default Slist<T> list() {
        return Indolently.list(this);
    }
}
