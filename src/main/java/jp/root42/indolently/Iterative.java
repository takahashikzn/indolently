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
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.ref.$long;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class Iterative {

    /** non private for subtyping. */
    protected Iterative() {}

    /**
     * Generate infinite integer sequence.
     *
     * @param from the value start from (inclusive).
     * @return infinite integer sequence.
     */
    public static $iter<Integer> sequence(final int from) {
        return sequence(from, 1);
    }

    /**
     * Generate infinite integer sequence.
     *
     * @param from the value start from (inclusive).
     * @param step count stepping
     * @return infinite integer sequence.
     */
    public static $iter<Integer> sequence(final int from, final int step) {
        return range(from, Integer.MAX_VALUE, step);
    }

    /**
     * Generate infinite long sequence.
     *
     * @param from the value start from (inclusive).
     * @return infinite integer sequence.
     */
    public static $iter<Long> sequence(final long from) {
        return sequence(from, 1);
    }

    /**
     * Generate infinite long sequence.
     *
     * @param from the value start from (inclusive).
     * @param step count stepping
     * @return infinite integer sequence.
     */
    public static $iter<Long> sequence(final long from, final int step) {
        return range(from, Long.MAX_VALUE, step);
    }

    // @formatter:off
    /**
     * Create iterator which simulates <a
     * href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">generator</a> function.
     *
     * <div>
     * Example
     *
     * {@code
     * // print timestamp every 10 seconds forever.
     * generator(System::currentTimeMillis).forEach(
     *     consumerOf((final Long x) -> System.out.println(Instant.ofEpochMilli(x))).andThen(x -> {
     *         try {
     *             Thread.sleep(10000);
     *         } catch (final InterruptedException e) {
     *             Generator.breaks(); // stop generator gently
     *     }
     * }   ));
     * }
     * </div>
     *
     * @param <T> value type
     * @param f value generating function
     * @return generator as {@link $iter}
     * @see <a href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">Generator_(computer_programming)</a>
     */
    // @formatter:on
    public static <T> Generator<T> generator(final Supplier<? extends T> f) {
        return Generator.of(null, x -> f.get());
    }

    // @formatter:off
    /**
     * Create iterator which simulates <a
     * href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">generator</a> function.
     *
     * <div>
     * Example
     *
     * {@code
     * // print timestamp every 10 seconds forever.
     * generator(
     *     list(),
     *     env -> {
     *         final long now = System.currentTimeMillis();
     *         env.push(now);
     *         return now;
     *     })
     *     .forEach(consumerOf((final Long x) -> {
     *         System.out.println(Instant.ofEpochMilli(x));
     *     })
     *     .andThen(x -> {
     *         try {
     *             Thread.sleep(10000);
     *         } catch (final InterruptedException e) {
     *             Generator.breaks(); // stop generator gently
     *         }
     *     }
     * ));
     * }
     * </div>
     *
     * @param <E> environment type
     * @param <T> value type
     * @param env iteration environment
     * @param f value generating function
     * @return generator as {@link $iter}
     * @see <a href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">Generator_(computer_programming)</a>
     */
    // @formatter:on
    public static <E, T> Generator<T> generator(final E env, final Function<? super E, ? extends T> f) {
        return Generator.of(env, f);
    }

    /**
     * shortcut notation of iterator.
     * This is shortcut notation of creating {@code Iterable<T>}.
     *
     * @param <T> value type
     * @param values lazy evaluated values which {@link Iterator#next} returns
     * @return iterator as {@link $iter}
     */
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    @SafeVarargs
    public static <T> $iter<T> iterator(final Supplier<? extends T>... values) {

        final Iterator<Supplier<? extends T>> i = list(values).iterator();

        return iterator(i::hasNext, () -> i.next().get());
    }

    /**
     * shortcut notation of iterator.
     * This is shortcut notation of creating {@code Iterable<T>}.
     *
     * @param <T> value type
     * @param hasNext {@link Iterator#hasNext} implementation
     * @param next {@link Iterator#next} implementation
     * @return iterator as {@link $iter}
     */
    public static <T> $iter<T> iterator(final BooleanSupplier hasNext, final Supplier<? extends T> next) {

        return iterator(null, x -> hasNext.getAsBoolean(), x -> next.get());
    }

    /**
     * shortcut notation of iterator.
     * This is shortcut notation of creating {@code Iterable<T>}.
     *
     * @param <E> environment type
     * @param <T> value type
     * @param env iteration environment
     * @param hasNext {@link Iterator#hasNext} implementation
     * @param next {@link Iterator#next} implementation
     * @return iterator as {@link $iter}
     */
    public static <E, T> $iter<T> iterator(final E env, final Predicate<? super E> hasNext,
        final Function<? super E, ? extends T> next) {

        return $iter.of(env, hasNext, next);
    }

    /**
     * Generate integer iterator.
     *
     * <div>
     * Examples
     *
     * <ul>
     * <li>{@code range(1, 3).list()} -&gt; {@code [1, 2, 3]}</li>
     * <li>{@code range(3, 1).list()} -&gt; {@code [3, 2, 1]}</li>
     * <li>{@code range(-3, 1).list()} -&gt; {@code [-3, -2, -1, 0, 1]}</li>
     * </ul>
     * </div>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @return integer iterator.
     */
    public static $iter<Integer> range(final int from, final int to) {
        return range(from, to, 1);
    }

    /**
     * Generate integer iterator.
     *
     * <div>
     * Examples
     *
     * <ul>
     * <li>{@code range(1, 6, 2).list()} -&gt; {@code [1, 3, 5]}</li>
     * </ul>
     * </div>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @param step count stepping
     * @return integer iterator.
     */
    public static $iter<Integer> range(final int from, final int to, final int step) {
        //noinspection
        return range((long) from, to, step).map(x -> x.intValue());
    }

    /**
     * Generate long iterator.
     *
     * <div>
     * Examples
     *
     * <ul>
     * <li>{@code range(1, 3).list()} -&gt; {@code [1, 2, 3]}</li>
     * <li>{@code range(3, 1).list()} -&gt; {@code [3, 2, 1]}</li>
     * <li>{@code range(-3, 1).list()} -&gt; {@code [-3, -2, -1, 0, 1]}</li>
     * </ul>
     * </div>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @return integer iterator.
     */
    public static $iter<Long> range(final long from, final long to) {
        return range(from, to, 1);
    }

    /**
     * Generate long iterator.
     *
     * <div>
     * Examples
     *
     * <ul>
     * <li>{@code range(1, 6, 2).list()} -&gt; {@code [1, 3, 5]}</li>
     * </ul>
     * </div>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @param step count stepping
     * @return integer iterator.
     */
    public static $iter<Long> range(final long from, final long to, final int step) {
        if (step <= 0) {
            throw new IllegalArgumentException(String.format("(step = %d) <= 0", step));
        }

        final Predicate<$long> pred = //
            env -> when(from < to).then(() -> env.$ <= to) //
                .when(to < from).then(() -> to <= env.$) //
                .none(() -> env.$ == from);

        return iterator(ref(from), pred, env -> match(env) //
            .when(pred).then(x -> x.getThen(y -> y.$ += (from <= to ? step : -step))) //
            .raise(x -> new NoSuchElementException()));
    }

    public static class ResourceIterable<T extends AutoCloseable>
        implements Iterable<T>, AutoCloseable {

        private final Iterable<T> iter;

        private final $list<T> list = list();

        public ResourceIterable(final Iterable<T> iter) { this.iter = iter; }

        @Override
        public void close() throws Exception {
            Exception last = null;
            for (final var r: this.list.pushAll(list(this.iter)))
                try {
                    r.close();
                } catch (final Exception e) {
                    (last = e).printStackTrace();
                }

            if (last != null) throw last;
        }

        @Override
        public Iterator<T> iterator() {

            return new Iterator<>() {

                private final Iterator<T> i = ResourceIterable.this.iter.iterator();

                @Override
                public boolean hasNext() { return this.i.hasNext(); }

                @Override
                public T next() {
                    final var x = this.i.next();
                    ResourceIterable.this.list.add(x);
                    return x;
                }
            };
        }
    }
}
