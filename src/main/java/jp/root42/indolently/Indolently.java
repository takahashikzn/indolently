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
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.ref.BoolRef;
import jp.root42.indolently.ref.ByteRef;
import jp.root42.indolently.ref.CharRef;
import jp.root42.indolently.ref.CmpRef;
import jp.root42.indolently.ref.DoubleRef;
import jp.root42.indolently.ref.Duo;
import jp.root42.indolently.ref.FloatRef;
import jp.root42.indolently.ref.IntRef;
import jp.root42.indolently.ref.LongRef;
import jp.root42.indolently.ref.Ref;
import jp.root42.indolently.ref.ShortRef;
import jp.root42.indolently.ref.Trio;
import jp.root42.indolently.ref.ValueReference;

import static jp.root42.indolently.Expressive.*;


/**
 * The Java Syntactic sugar collection for indolent person (like you).
 *
 * @author takahashikzn
 */
@SuppressWarnings("javadoc")
public class Indolently {

    /** non private for subtyping. */
    protected Indolently() {}

    static int idx(final List<?> list, final int idx) {
        return 0 <= idx ? idx : list.size() + idx;
    }

    public static <T> T fatal() {
        throw new AssertionError();
    }

    public static <T> T fatal(final Object msg) {
        throw new AssertionError(msg);
    }

    /**
     * An alias of {@link #optionalEmpty(Map)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     */
    public static <T extends Map<?, ?>> Optional<T> nonEmpty(final T value) {
        return optionalEmpty(value);
    }

    /**
     * {@link Optional} representation of collection.
     * Equivalent to {@code empty(col) ? Optional.empty() : Optional.of(col)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     * @see #empty(Map)
     */
    public static <T extends Map<?, ?>> Optional<T> optionalEmpty(final T value) {
        return empty(value) ? Optional.empty() : Optional.of(value);
    }

    /**
     * An alias of {@link #optionalEmpty(Iterable)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     */
    public static <T extends Iterable<?>> Optional<T> nonEmpty(final T value) {
        return optionalEmpty(value);
    }

    /**
     * {@link Optional} representation of collection.
     * Equivalent to {@code empty(col) ? Optional.empty() : Optional.of(col)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     * @see #empty(Iterable)
     */
    public static <T extends Iterable<?>> Optional<T> optionalEmpty(final T value) {
        return empty(value) ? Optional.empty() : Optional.of(value);
    }

    /**
     * An alias of {@link #optionalEmpty(CharSequence)}.
     *
     * @param <T> type of value
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
     * @param <T> type of value
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
     * @param <T> type of value
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
     * @param <T> type of value
     * @param value string value
     * @return Optional representation of string
     * @see #blank(CharSequence)
     */
    public static <T extends CharSequence> Optional<T> optionalBlank(final T value) {
        return blank(value) ? Optional.empty() : Optional.of(value);
    }

    /**
     * An alias of {@link #optional(Object)}.
     *
     * @param <T> type of value
     * @param value the value
     * @return Optional representation of value
     */
    public static <T> Optional<T> nonNull(final T value) {
        return optional(value);
    }

    /**
     * An alias of {@link Optional#ofNullable(Object)}.
     *
     * @param <T> type of value
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
     * @param <T> type of value
     * @param elems elements of list
     * @return new list
     * @see #list(Object...)
     */
    @SafeVarargs
    public static <T> SList<T> listOf(final T... elems) {
        return list(elems);
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param <T> type of value
     * @param elem element of the list
     * @return new list
     */
    public static <T> SList<T> list(final Optional<? extends T> elem) {
        return new SListImpl<T>().push(elem);
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param <T> type of value
     * @param elems elements of the list
     * @return new list
     */
    public static <T> SList<T> list(final Iterable<? extends T> elems) {
        return new SListImpl<T>().pushAll(optional(elems));
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param <T> type of value
     * @param elems elements of the list
     * @return new list
     */
    @SafeVarargs
    public static <T> SList<T> list(final T... elems) {

        final SList<T> list = new SListImpl<>();

        if (elems != null) {
            for (final T v : elems) {
                list.add(v);
            }
        }

        return list;
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
     * Convert {@link Iterable} to typed array.
     * The array type is same as first element type.
     *
     * @param <T> type of value
     * @param elems elements
     * @return typed array
     * @throws IllegalArgumentException iterable don't contain any element.
     */
    public static <T> T[] array(final Iterable<? extends T> elems) {
        if (empty(elems)) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        final SList<? extends T> list = list(elems);

        // pseudo typing. actually this type wouldn't be T[].
        @SuppressWarnings("unchecked")
        final T[] pseudoTyped = (T[]) list.tail().toArray();

        return array(list.head(), pseudoTyped);
    }

    /**
     * Convert arguments to typed array.
     * The array type is same as first element type.
     *
     * @param <T> type of value
     * @param first first element
     * @param rest rest elements
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
     * @param <T> type of array
     * @param <V> type of value
     * @param type array type
     * @param first first element
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

    /**
     * Create a list of <code>char</code>.
     *
     * @param elems
     * @return char list
     */
    public static SList<Character> plist(final char... elems) {
        final SList<Character> list = list();
        for (final char e : elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of <code>int</code>.
     *
     * @param elems
     * @return int list
     */
    public static SList<Integer> plist(final int... elems) {
        final SList<Integer> list = list();
        for (final int e : elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of <code>long</code>.
     *
     * @param elems
     * @return long list
     */
    public static SList<Long> plist(final long... elems) {
        final SList<Long> list = list();
        for (final long e : elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of <code>float</code>.
     *
     * @param elems
     * @return float list
     */
    public static SList<Float> plist(final float... elems) {
        final SList<Float> list = list();
        for (final float e : elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of <code>short</code>.
     *
     * @param elems
     * @return short list
     */
    public static SList<Short> plist(final short... elems) {
        final SList<Short> list = list();
        for (final short e : elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of <code>double</code>.
     *
     * @param elems
     * @return double list
     */
    public static SList<Double> plist(final double... elems) {
        final SList<Double> list = list();
        for (final double e : elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of <code>boolean</code>.
     *
     * @param elems
     * @return boolean list
     */
    public static SList<Boolean> plist(final boolean... elems) {
        final SList<Boolean> list = list();
        for (final boolean e : elems) {
            list.add(e);
        }
        return list;
    }

    public static <T> SSet<T> set(final Optional<? extends T> elem) {
        return new SSetImpl<T>().push(elem);
    }

    public static <T> SSet<T> set(final Iterable<? extends T> elems) {
        return new SSetImpl<T>().pushAll(optional(elems));
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
    public static <T> SSet<T> setOf(final T... elems) {
        return set(elems);
    }

    @SafeVarargs
    public static <T> SSet<T> set(final T... elems) {

        final SSet<T> set = new SSetImpl<>();

        if (elems != null) {
            set.addAll(list(elems));
        }

        return set;
    }

    public static <K extends Comparable<K>, V> SMap<K, V> sort(final Map<K, V> map) {
        return sort(map, Comparator.naturalOrder());
    }

    public static <K, V> SMap<K, V> sort(final Map<K, V> map, final Comparator<? super K> comp) {
        return wrap(ObjFactory.getInstance().<K, V> newSortedMap(Objects.requireNonNull(comp, "comparator"))).pushAll(
            map);
    }

    public static <T extends Comparable<T>> SSet<T> sort(final Set<? extends T> elems) {
        return sort(elems, Comparator.naturalOrder());
    }

    public static <T> SSet<T> sort(final Set<? extends T> elems, final Comparator<? super T> comp) {
        return wrap(ObjFactory.getInstance().<T> newSortedSet(Objects.requireNonNull(comp, "comparator"))).pushAll(
            elems);
    }

    public static <T extends Comparable<T>> SList<T> sort(final List<? extends T> elems) {
        return sort(elems, Comparator.naturalOrder());
    }

    public static <T> SList<T> sort(final List<? extends T> elems, final Comparator<? super T> comp) {
        final SList<T> rslt = list(elems);
        Collections.sort(rslt, Objects.requireNonNull(comp, "comparator"));
        return rslt;
    }

    public static <K, V> SMap<K, V> freeze(final Map<? extends K, ? extends V> map) {

        @SuppressWarnings("unchecked")
        final SMap<K, V> rslt = wrap(Collections.unmodifiableMap(wrap(map).map(freezer())));

        return rslt;
    }

    public static <T> SSet<T> freeze(final Set<? extends T> elems) {

        @SuppressWarnings("unchecked")
        final SSet<T> rslt = wrap(Collections.unmodifiableSet(wrap(elems).map(freezer())));

        return rslt;
    }

    public static <T> SList<T> freeze(final List<? extends T> elems) {

        @SuppressWarnings("unchecked")
        final SList<T> rslt = wrap(Collections.unmodifiableList(wrap(elems).map(freezer())));

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
     * @param i test target
     * @return test result
     */
    public static boolean empty(final Iterable<?>... i) {
        return (i == null) || (i.length == 0) || list(i).every(Indolently::empty);
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
     * test whether the argument is empty or not.
     *
     * @param map test target
     * @return test result
     */
    public static boolean empty(final Map<?, ?>... map) {
        return (map == null) || (map.length == 0) || list(map).every(Indolently::empty);
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
     * test whether the argument is present or not.
     *
     * @param opt test target
     * @return test result
     */
    public static boolean empty(final Optional<?>... opt) {
        return (opt == null) || (opt.length == 0) || list(opt).every(Indolently::empty);
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
     * test whether the argument is empty string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean empty(final CharSequence... cs) {
        return (cs == null) || (cs.length == 0) || list(cs).every(Indolently::empty);
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

    /**
     * test whether the argument is blank string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean blank(final CharSequence... cs) {
        return (cs == null) || (cs.length == 0) || list(cs).every(Indolently::blank);
    }

    public static boolean empty(final Object[] ary) {
        return (ary == null) || (ary.length == 0);
    }

    public static String join(final Collection<? extends CharSequence> col) {
        return join(col, "");
    }

    public static String join(final Collection<? extends CharSequence> col, final String sep) {
        return list(col).reduce((x, y) -> x + sep + y).map(x -> x.toString()).orElse("");
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
     * Test that the lower is less than upper.
     * i.e. this method tests {@code lower < upper}.
     *
     * @param <T> value type
     * @param lower lower value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean lt(final T lower, final T upper) {
        return (lower.compareTo(upper) < 0);
    }

    /**
     * Test that the lower is less equal than upper.
     * i.e. this method tests {@code lower <= upper}.
     *
     * @param <T> value type
     * @param lower lower value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean le(final T lower, final T upper) {
        return (lower.compareTo(upper) <= 0);
    }

    /**
     * Test that the value is gerater than lower and less than upper.
     * i.e. this method tests {@code lower < val < upper}.
     *
     * @param <T> value type
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gtlt(final T lower, final T val, final T upper) {
        return lt(lower, val) && lt(val, upper);
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
    public static boolean gtlt(final long lower, final long val, final long upper) {
        return (lower < val) && (val < upper);
    }

    /**
     * Test that the value is gerater equal lower and less than upper.
     * i.e. this method tests {@code lower <= val < upper}.
     *
     * @param <T> value type
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gelt(final T lower, final T val, final T upper) {
        return le(lower, val) && lt(val, upper);
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
    public static boolean gelt(final long lower, final long val, final long upper) {
        return (lower <= val) && (val < upper);
    }

    /**
     * Test that the value is gerater than lower and less equal upper.
     * i.e. this method tests {@code lower < val <= upper}.
     *
     * @param <T> value type
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gtle(final T lower, final T val, final T upper) {
        return lt(lower, val) && le(val, upper);
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
    public static boolean gtle(final long lower, final long val, final long upper) {
        return (lower < val) && (val <= upper);
    }

    /**
     * Test that the value is gerater equal lower and less equal upper.
     * i.e. this method tests {@code lower <= val <= upper}.
     *
     * @param <T> value type
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gele(final T lower, final T val, final T upper) {
        return le(lower, val) && le(val, upper);
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
    public static boolean gele(final long lower, final long val, final long upper) {
        return (lower <= val) && (val <= upper);
    }

    /**
     * An alias of {@link #gele(long, long, long)}.
     *
     * @param lower lower value
     * @param val value
     * @param upper upper value
     * @return test result
     */
    public static boolean between(final long lower, final long val, final long upper) {
        return gele(lower, val, upper);
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

    public static <K, V> SMap<K, V> map(final Map<? extends K, ? extends V> map) {
        return new SMapImpl<K, V>().pushAll(optional(map));
    }

    public static <K, V> SMap<K, V> map() {
        return new SMapImpl<>();
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <T> SList<T> wrap(final SList<T> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <T> SSet<T> wrap(final SSet<T> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <K, V> SMap<K, V> wrap(final SMap<K, V> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return clone of the argument
     * @deprecated Use {@link SList#clone()} instead of to get more intentional.
     */
    @Deprecated
    public static <T> SList<T> list(final SList<T> x) {
        return x.clone();
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return clone of the argument
     * @deprecated Use {@link SSet#clone()} instead of to get more intentional.
     */
    @Deprecated
    public static <T> SSet<T> set(final SSet<T> x) {
        return x.clone();
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return clone of the argument
     * @deprecated Use {@link SMap#clone()} instead of to get more intentional.
     */
    @Deprecated
    public static <K, V> SMap<K, V> map(final SMap<K, V> x) {
        return x.clone();
    }

    /**
     * Wrap a map.
     *
     * @param map map to wrap
     * @return wrapped map
     */
    public static <K, V> SMap<K, V> wrap(final Map<K, V> map) {
        return (map == null) ? null //
            : (map instanceof SMap) ? (SMap<K, V>) map //
                : new SMapImpl<>(map);
    }

    /**
     * Wrap a list.
     *
     * @param list list to wrap
     * @return wrapped list
     */
    public static <T> SList<T> wrap(final List<T> list) {
        return (list == null) ? null //
            : (list instanceof SList) ? (SList<T>) list //
                : new SListImpl<>(list);
    }

    /**
     * Wrap a set.
     *
     * @param set set to wrap
     * @return wrapped set
     */
    public static <T> SSet<T> wrap(final Set<T> set) {
        return (set == null) ? null //
            : (set instanceof SSet) ? (SSet<T>) set //
                : new SSetImpl<>(set);
    }

    /**
     * Wrap a map.
     *
     * @param map map to wrap
     * @param key key to put
     * @param val value to put
     * @return wrapped map
     */
    public static <K, V> SMap<K, V> wrap(final Map<K, V> map, final K key, final V val) {
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
    public static <T> SList<T> wrap(final List<T> list, final T... elems) {
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
    public static <T> SSet<T> wrap(final Set<T> set, final T... elems) {
        return wrap(Objects.requireNonNull(set, "set")).pushAll(list(elems));
    }

    /**
     * Wrap a iterator.
     *
     * @param iter iterator to wrap
     * @return wrapped iterator
     */
    public static <T> SIter<T> wrap(final Iterator<? extends T> iter) {
        if (iter == null) {
            return null;
        }

        if (iter instanceof SIter) {
            @SuppressWarnings("unchecked")
            final SIter<T> i = (SIter<T>) iter;
            return i;
        }

        return new SIter<T>() {

            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public T next() {
                return iter.next();
            }

            @Override
            public void remove() {
                iter.remove();
            }
        };
    }

    /**
     * Wrap a stream.
     *
     * @param stream stream to wrap
     * @return wrapped stream
     */
    public static <T> SStream<T> wrap(final Stream<T> stream) {
        return (stream == null) ? null //
            : (stream instanceof SStream) ? (SStream<T>) stream //
                : new SStreamImpl<>(stream);
    }

    public static <K, V> SMap<K, V> map(final K key, final V val) {

        @SuppressWarnings("unchecked")
        final SMap<K, V> map = (SMap<K, V>) map().push(key, val);

        return map;
    }

    public static <K, V> SMap<K, V> map(final K key, final Optional<? extends V> val) {

        @SuppressWarnings("unchecked")
        final SMap<K, V> map = (SMap<K, V>) map().push(key, val);

        return map;
    }

    /**
     * create two element tuple.
     *
     * @param fst 1st element
     * @param snd 2nd element
     * @return tuple
     */
    public static <F, S> Duo<F, S> tuple(final F fst, final S snd) {
        return new Duo<F, S>().fst(fst).snd(snd);
    }

    /**
     * create three element tuple.
     *
     * @param fst 1st element
     * @param snd 2nd element
     * @param trd 3rd element
     * @return tuple
     */
    public static <F, S, T> Trio<F, S, T> tuple(final F fst, final S snd, final T trd) {
        return new Trio<F, S, T>().fst(fst).snd(snd).trd(trd);
    }

    /**
     * Convert tuple to list.
     *
     * @param tuple two element tuple
     * @return list of tuple elements
     */
    public static <T> SList<T> list(final Duo<? extends T, ? extends T> tuple) {
        return list(tuple.fst, tuple.snd);
    }

    /**
     * Convert tuple to list.
     *
     * @param tuple three element tuple
     * @return list of tuple elements
     */
    public static <T> SList<T> list(final Trio<? extends T, ? extends T, ? extends T> tuple) {
        return list(tuple.fst, tuple.snd, tuple.trd);
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
     * create an empty reference.
     *
     * @return empty reference
     */
    public static <T> Ref<T> ref() {
        return ValueReference.of((T) null);
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
    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26)
            .push(k27, v27);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26)
            .push(k27, v27).push(k28, v28);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26)
            .push(k27, v27).push(k28, v28).push(k29, v29);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26)
            .push(k27, v27).push(k28, v28).push(k29, v29).push(k30, v30);
    }

    public static <K, V> SMap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30, final K k31, final V v31) {

        return Indolently.<K, V> map().push(k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5)
            .push(k6, v6).push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12)
            .push(k13, v13).push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19)
            .push(k20, v20).push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26)
            .push(k27, v27).push(k28, v28).push(k29, v29).push(k30, v30).push(k31, v31);
    }
    // CHECKSTYLE:ON
}
