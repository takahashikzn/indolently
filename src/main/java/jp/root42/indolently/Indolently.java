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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.Match.When;
import jp.root42.indolently.ref.BoolRef;
import jp.root42.indolently.ref.ByteRef;
import jp.root42.indolently.ref.CharRef;
import jp.root42.indolently.ref.CmpRef;
import jp.root42.indolently.ref.DoubleRef;
import jp.root42.indolently.ref.FloatRef;
import jp.root42.indolently.ref.IntRef;
import jp.root42.indolently.ref.LongRef;
import jp.root42.indolently.ref.Ref;
import jp.root42.indolently.ref.ShortRef;
import jp.root42.indolently.ref.Tuple2;
import jp.root42.indolently.ref.Tuple3;
import jp.root42.indolently.ref.ValueReference;


/**
 * The Java syntax sugar collection for indolent person (like you).
 *
 * @author takahashikzn
 */
@SuppressWarnings("javadoc")
public class Indolently {

    /** non private for subtyping. */
    protected Indolently() {
    }

    static int idx(final List<?> list, final int idx) {
        return 0 <= idx ? idx : list.size() + idx;
    }

    /**
     * An alias of {@link #optionalEmpty(CharSequence)}.
     *
     * @param value string value
     * @return Optional representation of string
     */
    public static <T extends CharSequence> Optional<T> nonEmpty(final T value) {
        return optionalEmpty(value);
    }

    /**
     * {@link Optional} representation of string.
     * Equivalent to {@code empty(str) ? Optional.empty() : Optional.of(str)}.
     *
     * @param value string value
     * @return Optional representation of string
     * @see #empty(CharSequence)
     */
    public static <T extends CharSequence> Optional<T> optionalEmpty(final T value) {
        return empty(value) ? Optional.empty() : Optional.of(value);
    }

    /**
     * An alias of {@link #optionalBlank(CharSequence)}.
     *
     * @param value string value
     * @return Optional representation of string
     */
    public static <T extends CharSequence> Optional<T> nonBlank(final T value) {
        return optionalBlank(value);
    }

    /**
     * {@link Optional} representation of string.
     * Equivalent to {@code blank(str) ? Optional.empty() : Optional.of(str)}.
     *
     * @param value string value
     * @return Optional representation of string
     * @see #blank(CharSequence)
     */
    public static <T extends CharSequence> Optional<T> optionalBlank(final T value) {
        return blank(value) ? Optional.empty() : Optional.of(value);
    }

    /**
     * An alias of {@link #optional(T)}.
     *
     * @param value the value
     * @return Optional representation of value
     */
    public static <T> Optional<T> nonNull(final T value) {
        return optional(value);
    }

    /**
     * An alias of {@link Optional#ofNullable(T)}.
     *
     * @param value the value
     * @return Optional representation of value
     */
    public static <T> Optional<T> optional(final T value) {
        return Optional.ofNullable(value);
    }

    /**
     * Just an alias of {@link #list(Object...)} but not overloaded one.
     * If compiler fail to do type inference on {@link #list(Object...)}, for example such a
     * {@code List<List<Integer>> nested = list(list(42))}, you can use this method instead of.
     *
     * @param elems elements of list
     * @return new list
     * @see #list(Object...)
     */
    @SafeVarargs
    public static <T> Slist<T> listof(final T... elems) {
        return list(elems);
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param elem element of the list
     * @return new list
     */
    public static <T> Slist<T> list(final Optional<? extends T> elem) {
        return new SlistImpl<T>().push(elem);
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param elems elements of the list
     * @return new list
     */
    public static <T> Slist<T> list(final Iterable<? extends T> elems) {
        return new SlistImpl<T>().pushAll(optional(elems));
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param elems elements of the list
     * @return new list
     */
    @SafeVarargs
    public static <T> Slist<T> list(final T... elems) {

        final Slist<T> list = new SlistImpl<>();

        if (elems != null) {
            for (final T v : elems) {
                list.add(v);
            }
        }

        return list;
    }

    public static <V, T> Slist<T> list(final Iterable<? extends V> input, final Predicate<? super V> pred,
        final Function<? super V, ? extends T> expr) {

        final Slist<T> list = list();

        for (final V in : input) {
            if (pred.test(in)) {
                list.add(expr.apply(in));
            }
        }

        return list;
    }

    public static <T> Supplier<? extends T> def(final Supplier<? extends T> f) {
        return f;
    }

    public static <T> Supplier<? extends T> supplierOf(final Supplier<? extends T> f) {
        return f;
    }

    public static <T> Consumer<? super T> def(final Consumer<? super T> f) {
        return f;
    }

    public static <T> Consumer<? super T> consumerOf(final Consumer<? super T> f) {
        return f;
    }

    public static <T, U> BiConsumer<? super T, ? super U> def(final BiConsumer<? super T, ? super U> f) {
        return f;
    }

    public static <T, U> BiConsumer<? super T, ? super U> biConsumerOf(final BiConsumer<? super T, ? super U> f) {
        return f;
    }

    public static <T, R> Function<? super T, ? extends R> def(final Function<? super T, ? extends R> f) {
        return f;
    }

    public static <T, R> Function<? super T, ? extends R> functionOf(final Function<? super T, ? extends R> f) {
        return f;
    }

    public static <T, U, R> BiFunction<? super T, ? super U, ? extends R> def(
        final BiFunction<? super T, ? super U, ? extends R> f) {
        return f;
    }

    public static <T, U, R> BiFunction<? super T, ? super U, ? extends R> biFunctionOf(
        final BiFunction<? super T, ? super U, ? extends R> f) {
        return f;
    }

    public static <T> Predicate<? super T> def(final Predicate<? super T> f) {
        return f;
    }

    public static <T> Predicate<? super T> predicateOf(final Predicate<? super T> f) {
        return f;
    }

    public static <T, U> BiPredicate<? super T, ? super U> def(final BiPredicate<? super T, ? super U> f) {
        return f;
    }

    public static <T, U> BiPredicate<? super T, ? super U> biPredicateOf(final BiPredicate<? super T, ? super U> f) {
        return f;
    }

    /**
     * create iterator which simulates <a
     * href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">generator</a> function.
     * <p>
     * Example
     *
     * <pre>
     * <code>
     * // print timestamp every 10 seconds forever.
     * generator(System::currentTimeMillis).forEach(
     *     def((final Long x) -> System.out.println(Instant.ofEpochMilli(x))).andThen(x -> {
     *         try {
     *             Thread.sleep(10000);
     *         } catch (final Exception e) {
     *         }
     *     }));
     * </code>
     * </pre>
     *
     * </p>
     *
     * @param <T> value type
     * @param f value generating function
     * @return generator as {@link Iterable}
     * @see <a href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">Generator_(computer_programming)</a>
     */
    public static <T> Generator<T> generator(final Supplier<? extends T> f) {
        return Generator.of(null, x -> f.get());
    }

    /**
     * create iterator which simulates <a
     * href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">generator</a> function.
     * <p>
     * Example
     *
     * <pre>
     * <code>
     * // print timestamp every 10 seconds forever.
     * generator(System::currentTimeMillis).forEach(
     *     def((final Long x) -> System.out.println(Instant.ofEpochMilli(x))).andThen(x -> {
     *         try {
     *             Thread.sleep(10000);
     *         } catch (final Exception e) {
     *         }
     *     }));
     * </code>
     * </pre>
     *
     * </p>
     *
     * @param <E> environment type
     * @param <T> value type
     * @param env iteration environment
     * @param f value generating function
     * @return generator as {@link Iterable}
     * @see <a href="http://en.wikipedia.org/wiki/Generator_(computer_programming)">Generator_(computer_programming)</a>
     */
    public static <E, T> Generator<T> generator(final E env, final Function<? super E, ? extends T> f) {
        return Generator.of(env, f);
    }

    /**
     * shortcut notation of iterator.
     * This is shortcut notation of creating {@code Iterable<T>}.
     *
     * @param <T> value type
     * @param values lazy evaluated values which {@link Iterator#next} returns
     * @return iterator as {@link Iterable}
     */
    @SafeVarargs
    public static <T> Iter<T> iterator(final Supplier<? extends T>... values) {

        final Iterator<Supplier<? extends T>> i = list(values).iterator();

        return iterator(i::hasNext,
        // avoid compilation error on OracleJDK
            () -> {
                final T val = i.next().get();
                return val;
            });
    }

    /**
     * shortcut notation of iterator.
     * This is shortcut notation of creating {@code Iterable<T>}.
     *
     * @param <T> value type
     * @param hasNext {@link Iterator#hasNext} implementation
     * @param next {@link Iterator#next} implementation
     * @return iterator as {@link Iterable}
     */
    public static <T> Iter<T> iterator(final Supplier<Boolean> hasNext, final Supplier<? extends T> next) {

        return iterator(null, x -> hasNext.get(), x -> next.get());
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
     * @return iterator as {@link Iterable}
     */
    public static <E, T> Iter<T> iterator(final E env, final Predicate<? super E> hasNext,
        final Function<? super E, ? extends T> next) {

        return Iter.of(env, hasNext, next);
    }

    /**
     * evaluate following forms then return evaluation result of first expressions.
     *
     * @param first evaluation result of this expression
     * @param forms evaluation target forms. argument is evaluation result of {@code first}.
     * @return first expression evaluation result
     */
    @SafeVarargs
    public static <T> T prog1(final Supplier<? extends T> first, final Consumer<? super T>... forms) {

        final T val = first.get();

        list(forms).each(f -> f.accept(val));

        return val;
    }

    /**
     * evaluate following forms then return first expression evaluation result.
     *
     * @param first evaluation result of this expression
     * @param forms evaluation target forms
     * @return first expression evaluation result
     */
    public static <T> T prog1(final Supplier<? extends T> first, final Closure... forms) {

        final T val = first.get();

        list(forms).each(f -> f.perform());

        return val;
    }

    public static <C, V> When<C, V> whenNull(final V val) {
        return when(x -> x == null, val);
    }

    public static <C, V> When<C, V> whenEq(final C pred, final V val) {
        return when(x -> equiv(x, pred), () -> val);
    }

    public static <C, V> When<C, V> whenEq(final C pred, final Supplier<? extends V> expr) {
        return when(x -> equiv(x, pred), expr);
    }

    public static <C, V> When<C, V> whenEq(final C pred, final Function<? super C, ? extends V> expr) {
        return when(x -> equiv(x, pred), expr);
    }

    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final V val) {
        return when(pred, () -> val);
    }

    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final Supplier<? extends V> expr) {
        return When.of(pred, x -> expr.get());
    }

    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final Function<? super C, ? extends V> expr) {
        return When.of(pred, expr);
    }

    @SafeVarargs
    public static <C, V> Match<C, V> match(final When<C, V>... cases) {
        return Match.of(cases);
    }

    @SafeVarargs
    public static <V, T extends Predicate<V>> Optional<T> find(final V cond, final T... preds) {

        for (final T p : preds) {
            if (p.test(cond)) {
                return Optional.of(p);
            }
        }

        return Optional.empty();
    }

    /**
     * Generate infinite integer sequence.
     *
     * @param from the value start from (inclusive).
     * @return infinite integer sequence.
     */
    public static Iter<Integer> sequence(final int from) {
        return sequence(from, 1);
    }

    /**
     * Generate infinite integer sequence.
     *
     * @param from the value start from (inclusive).
     * @param step count stepping
     * @return infinite integer sequence.
     */
    public static Iter<Integer> sequence(final int from, final int step) {
        return range(from, Integer.MAX_VALUE, step);
    }

    /**
     * Generate integer iterator.
     * <p>
     * Examples
     * <ul>
     * <li>{@code range(1, 3).list()} -&gt; {@code [1, 2, 3]}</li>
     * <li>{@code range(3, 1).list()} -&gt; {@code [3, 2, 1]}</li>
     * <li>{@code range(-3, 1).list()} -&gt; {@code [-3, -2, -1, 0, 1]}</li>
     * </ul>
     * </p>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @return integer iterator.
     */
    public static Iter<Integer> range(final int from, final int to) {
        return range(from, to, 1);
    }

    /**
     * Generate integer iterator.
     * <p>
     * Examples
     * <ul>
     * <li>{@code range(1, 6, 2).list()} -&gt; {@code [1, 3, 5]}</li>
     * </ul>
     * </p>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @param step count stepping
     * @return integer iterator.
     */
    public static Iter<Integer> range(final int from, final int to, final int step) {
        if (step <= 0) {
            throw new IllegalArgumentException(String.format("(step = %d) <= 0", step));
        }

        return iterator(ref((long) from), env -> {
            return (from < to) ? (env.val <= to) //
                : (to < from) ? (to <= env.val) //
                    : (env.val == from);
        }, env -> prog1(() -> (int) env.val, () -> {
            if (from < to) {
                env.val += step;
            } else {
                env.val -= step;
            }
        }));
    }

    /**
     * Convert {@link Iterable} to typed array.
     * The array type is same as first element type.
     *
     * @param elems elements
     * @return typed array
     * @throws IllegalArgumentException iterable don't contain any element.
     */
    public static <T> T[] array(final Iterable<? extends T> elems) {
        if (empty(elems)) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        final Slist<? extends T> list = list(elems);

        // pseudo typing. actually this type wouldn't be T[].
        @SuppressWarnings("unchecked")
        final T[] pseudoTyped = (T[]) list.tail().toArray();

        return array(list.first(), pseudoTyped);
    }

    /**
     * Convert arguments to typed array.
     * The array type is same as first element type.
     *
     * @param elems first element
     * @param elems rest elements
     * @return typed array
     * @throws IllegalArgumentException first element is null..
     */
    @SafeVarargs
    public static <T> T[] array(final T first, final T... rest) {
        if (first == null) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        final int len = 1 + ((rest == null) ? 0 : rest.length);
        @SuppressWarnings("unchecked")
        final T[] ary = (T[]) Array.newInstance(first.getClass(), len);

        return list(first).pushAll(list(rest)).toArray(ary);
    }

    /**
     * Convert arguments to typed array.
     *
     * @param type array type
     * @param elems first element
     * @param elems rest elements
     * @return typed array
     */
    @SafeVarargs
    public static <T, V extends T> T[] array(final Class<T> type, final V first, final V... rest) {

        if (first == null) {
            @SuppressWarnings("unchecked")
            final T[] ary = (T[]) Array.newInstance(type, 0);
            return ary;
        }

        final int len = 1 + ((rest == null) ? 0 : rest.length);
        @SuppressWarnings("unchecked")
        final T[] ary = (T[]) Array.newInstance(type, len);

        return list(first).pushAll(list(rest)).toArray(ary);
    }

    /**
     * The shortcut notation of <code>new Object[] { ... }</code>.
     *
     * @param elems
     * @return {@link Object} array
     */
    public static Object[] oarray(final Object... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new char[] { ... }</code>.
     *
     * @param elems
     * @return char array
     */
    public static char[] parray(final char... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new int[] { ... }</code>.
     *
     * @param elems
     * @return int array
     */
    public static int[] parray(final int... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new long[] { ... }</code>.
     *
     * @param elems
     * @return long array
     */
    public static long[] parray(final long... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new float[] { ... }</code>.
     *
     * @param elems
     * @return float array
     */
    public static float[] parray(final float... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new byte[] { ... }</code>.
     *
     * @param elems
     * @return byte array
     */
    public static byte[] parray(final byte... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new double[] { ... }</code>.
     *
     * @param elems
     * @return double array
     */
    public static double[] parray(final double... elems) {
        return elems;
    }

    /**
     * The shortcut notation of <code>new boolean[] { ... }</code>.
     *
     * @param elems
     * @return boolean array
     */
    public static boolean[] parray(final boolean... elems) {
        return elems;
    }

    public static <T> Sset<T> set(final Optional<? extends T> elem) {
        return new SsetImpl<T>().push(elem);
    }

    public static <T> Sset<T> set(final Iterable<? extends T> elems) {
        return new SsetImpl<T>().pushAll(optional(elems));
    }

    /**
     * Just an alias of {@link #set(Object...)} but not overloaded one.
     * If compiler fail to do type inference on {@link #set(Object...)}, for example such a
     * {@code Set<List<Integer>> nested = set(list(42))}, you can use this method instead of.
     *
     * @param elems elements of set
     * @return new set
     * @see #set(Object...)
     */
    @SafeVarargs
    public static <T> Sset<T> setof(final T... elems) {
        return set(elems);
    }

    @SafeVarargs
    public static <T> Sset<T> set(final T... elems) {

        final Sset<T> set = new SsetImpl<>();

        if (elems != null) {
            set.addAll(list(elems));
        }

        return set;
    }

    public static <K, V> Smap<K, V> sort(final Map<K, V> map) {
        return wrap(new TreeMap<>(map));
    }

    public static <T extends Comparable<T>> Sset<T> sort(final Set<? extends T> elems) {
        return wrap(new TreeSet<>(elems));
    }

    public static <T extends Comparable<T>> Slist<T> sort(final List<? extends T> elems) {
        return list(new TreeSet<>(elems));
    }

    public static <K, V> Smap<K, V> freeze(final Map<? extends K, ? extends V> map) {

        @SuppressWarnings("unchecked")
        final Smap<K, V> rslt = wrap(Collections.unmodifiableMap(wrap(map).map(freezer())));

        return rslt;
    }

    public static <T> Sset<T> freeze(final Set<? extends T> elems) {

        @SuppressWarnings("unchecked")
        final Sset<T> rslt = wrap(Collections.unmodifiableSet(wrap(elems).map(freezer())));

        return rslt;
    }

    public static <T> Slist<T> freeze(final List<? extends T> elems) {

        @SuppressWarnings("unchecked")
        final Slist<T> rslt = wrap(Collections.unmodifiableList(wrap(elems).map(freezer())));

        return rslt;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Function freezer() {

        return match( //
            when(x -> (x instanceof List), x -> freeze((List) x)) //
            , when(x -> (x instanceof Set), x -> freeze((Set) x))//
            , when(x -> (x instanceof Map), x -> freeze((Map) x))) //
            .defaults((Function) Function.identity());
    }

    /**
     * test whether the argument is empty or not.
     *
     * @param i test target
     * @return test result
     */
    public static boolean empty(final Iterable<?> i) {
        if (i == null) {
            return true;
        } else if (i instanceof Collection) { // for optimization
            return ((Collection<?>) i).isEmpty();
        } else {
            return !i.iterator().hasNext();
        }
    }

    /**
     * test whether the argument is empty or not.
     *
     * @param map test target
     * @return test result
     */
    public static boolean empty(final Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }

    /**
     * test whether the argument is present or not.
     *
     * @param opt test target
     * @return test result
     */
    public static boolean empty(final Optional<?> opt) {
        return (opt == null) || !opt.isPresent();
    }

    /**
     * test whether the argument is empty string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean empty(final CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }

    /**
     * test whether the argument is blank string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean blank(final CharSequence cs) {
        return empty(cs) || cs.chars().allMatch(Character::isWhitespace);
    }

    public static boolean empty(final Object[] ary) {
        return (ary == null) || (ary.length == 0);
    }

    @SafeVarargs
    public static <T> T choose(final T... elems) {

        return list(elems) //
            .reduce((rem, val) -> rem != null ? rem : val) //
            .orElseThrow(() -> new IllegalArgumentException("all elements are null"));
    }

    @SafeVarargs
    public static <T> T choose(final Supplier<? extends T>... suppliers) {
        return choose(null, suppliers);
    }

    @SafeVarargs
    public static <T> T choose(final T initial, final Supplier<? extends T>... suppliers) {

        if (initial != null) {
            return initial;
        }

        if (suppliers != null) {
            for (final Supplier<? extends T> s : suppliers) {
                final T val = s.get();
                if (val != null) {
                    return val;
                }
            }
        }

        throw new IllegalArgumentException("all elements are null");
    }

    /**
     * Test that the value is gerater than lower and less than upper.
     * i.e. this method tests {@code lower < val < upper}.
     *
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gtlt(final T lower, final T val, final T upper) {
        return (lower.compareTo(val) < 0) && (val.compareTo(upper) < 0);
    }

    /**
     * Test that the value is gerater equal lower and less than upper.
     * i.e. this method tests {@code lower <= val < upper}.
     *
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gelt(final T lower, final T val, final T upper) {
        return (lower.compareTo(val) <= 0) && (val.compareTo(upper) < 0);
    }

    /**
     * Test that the value is gerater than lower and less equal upper.
     * i.e. this method tests {@code lower < val <= upper}.
     *
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gtle(final T lower, final T val, final T upper) {
        return (lower.compareTo(val) < 0) && (val.compareTo(upper) <= 0);
    }

    /**
     * Test that the value is gerater equal lower and less equal upper.
     * i.e. this method tests {@code lower <= val <= upper}.
     *
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gele(final T lower, final T val, final T upper) {
        return (lower.compareTo(val) <= 0) && (val.compareTo(upper) <= 0);
    }

    /**
     * An alias of {@link #gele(Comparable, Comparable, Comparable)}.
     *
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean between(final T lower, final T val, final T upper) {
        return gele(lower, val, upper);
    }

    @SafeVarargs
    public static <T extends Comparable<T>> boolean equal(final T l, final T r, final T... rest) {
        return list(r).pushAll(list(rest)).every(x -> equal(l, x));
    }

    public static boolean equal(final Object l, final Object r, final Object... rest) {
        return list(r).pushAll(list(rest)).every(x -> equal(l, x));
    }

    public static <T extends Comparable<T>> boolean equal(final T l, final T r) {
        return (l == null) ? (r == null) : (r == l) || (l.compareTo(r) == 0);
    }

    public static boolean equal(final Object l, final Object r) {
        return (l == null) ? (r == null) : (l == r) || l.equals(r);
    }

    @TypeUnsafe
    public static <T> boolean equiv(final T l, final T r) {

        if ((l instanceof Comparable) && (r instanceof Comparable)) {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            final boolean rslt = equal((Comparable) l, (Comparable) r);
            return rslt;
        } else {
            return equal(l, r);
        }
    }

    @TypeUnsafe
    @SafeVarargs
    public static <T> boolean equiv(final T l, final T r, final T... rest) {
        return list(r).pushAll(list(rest)).every(x -> equiv(l, x));
    }

    public static <T extends Comparable<T>> T max(final T l, final T r) {
        return (0 <= l.compareTo(r)) ? l : r;
    }

    public static <T extends Comparable<T>> T min(final T l, final T r) {
        return (l.compareTo(r) <= 0) ? l : r;
    }

    @SafeVarargs
    public static <T extends Comparable<T>> T max(final T first, final T second, final T... rest) {
        return list(first, second).pushAll(list(rest)).reduce(Indolently::max).get();
    }

    @SafeVarargs
    public static <T extends Comparable<T>> T min(final T first, final T second, final T... rest) {
        return list(first, second).pushAll(list(rest)).reduce(Indolently::min).get();
    }

    public static <T extends Comparable<T>> T max(final Iterable<? extends T> values) {
        return list(values).reduce((l, r) -> max(l, r)).get();
    }

    public static <T extends Comparable<T>> T min(final Iterable<? extends T> values) {
        return list(values).reduce((l, r) -> min(l, r)).get();
    }

    public static Class<?> typed(@SuppressWarnings("rawtypes")
    final Class cls) {
        return cls;
    }

    public static Map<?, ?> typed(@SuppressWarnings("rawtypes")
    final Map raw) {
        return raw;
    }

    public static List<?> typed(@SuppressWarnings("rawtypes")
    final List raw) {
        return raw;
    }

    public static Set<?> typed(@SuppressWarnings("rawtypes")
    final Set raw) {
        return raw;
    }

    public static <K, V> Smap<K, V> map(final Map<? extends K, ? extends V> map) {
        return new SmapImpl<K, V>().pushAll(optional(map));
    }

    public static <K, V> Smap<K, V> map() {
        return new SmapImpl<>();
    }

    /**
     * Wrap a map.
     *
     * @param map map to wrap
     * @return wrapped map
     */
    public static <K, V> Smap<K, V> wrap(final Map<K, V> map) {
        return (map == null) ? null //
            : (map instanceof Smap) ? (Smap<K, V>) map //
                : new SmapImpl<>(map);
    }

    /**
     * Wrap a list.
     *
     * @param list list to wrap
     * @return wrapped list
     */
    public static <T> Slist<T> wrap(final List<T> list) {
        return (list == null) ? null //
            : (list instanceof Slist) ? (Slist<T>) list //
                : new SlistImpl<>(list);
    }

    /**
     * Wrap a set.
     *
     * @param set set to wrap
     * @return wrapped set
     */
    public static <T> Sset<T> wrap(final Set<T> set) {
        return (set == null) ? null //
            : (set instanceof Sset) ? (Sset<T>) set //
                : new SsetImpl<>(set);
    }

    /**
     * Wrap a map.
     *
     * @param map map to wrap
     * @param key key to put
     * @param val value to put
     * @return wrapped map
     */
    public static <K, V> Smap<K, V> wrap(final Map<K, V> map, final K key, final V val) {
        return wrap(Objects.requireNonNull(map, "map")).push(key, val);
    }

    /**
     * Wrap a list.
     *
     * @param list list to wrap
     * @param elems elements to add
     * @return wrapped list
     */
    @SafeVarargs
    public static <T> Slist<T> wrap(final List<T> list, final T... elems) {
        return wrap(Objects.requireNonNull(list, "list")).pushAll(list(elems));
    }

    /**
     * Wrap a set.
     *
     * @param set set to wrap
     * @param elems elements to add
     * @return wrapped set
     */
    @SafeVarargs
    public static <T> Sset<T> wrap(final Set<T> set, final T... elems) {
        return wrap(Objects.requireNonNull(set, "set")).pushAll(list(elems));
    }

    public static <K, V> Smap<K, V> map(final K key, final V val) {

        @SuppressWarnings("unchecked")
        final Smap<K, V> map = (Smap<K, V>) map().push(key, val);

        return map;
    }

    public static <K, V> Smap<K, V> map(final K key, final Optional<? extends V> val) {

        @SuppressWarnings("unchecked")
        final Smap<K, V> map = (Smap<K, V>) map().push(key, val);

        return map;
    }

    /**
     * create two element tuple.
     *
     * @param fst 1st element
     * @param snd 2nd element
     * @return tuple
     */
    public static <F, S> Tuple2<F, S> tuple(final F fst, final S snd) {
        return new Tuple2<F, S>().fst(fst).snd(snd);
    }

    /**
     * create three element tuple.
     *
     * @param fst 1st element
     * @param snd 2nd element
     * @param trd 3rd element
     * @return tuple
     */
    public static <F, S, T> Tuple3<F, S, T> tuple(final F fst, final S snd, final T trd) {
        return new Tuple3<F, S, T>().fst(fst).snd(snd).trd(trd);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static BoolRef ref(final boolean val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static IntRef ref(final int val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static LongRef ref(final long val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static DoubleRef ref(final double val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static FloatRef ref(final float val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static ShortRef ref(final short val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static ByteRef ref(final byte val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static CharRef ref(final char val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static <T> Ref<T> ref(final T val) {
        return ValueReference.of(val);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static <T extends Comparable<T>> CmpRef<T> ref(final T val) {
        return ValueReference.of(val);
    }

    // CHECKSTYLE:OFF

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25)
            .push(k26, v26);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25)
            .push(k26, v26).push(k27, v27);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25)
            .push(k26, v26).push(k27, v27).push(k28, v28);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25)
            .push(k26, v26).push(k27, v27).push(k28, v28).push(k29, v29);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25)
            .push(k26, v26).push(k27, v27).push(k28, v28).push(k29, v29).push(k30, v30);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30, final K k31, final V v31) {

        return wrap(new LinkedHashMap<K, V>()).push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4)
            .push(k5, v5).push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11)
            .push(k12, v12).push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18)
            .push(k19, v19).push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25)
            .push(k26, v26).push(k27, v27).push(k28, v28).push(k29, v29).push(k30, v30).push(k31, v31);
    }
    // CHECKSTYLE:ON
}
