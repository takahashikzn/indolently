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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Supplier;


/**
 * The Java syntax sugar collection for indolent person (like you).
 *
 * @author takahashikzn
 */
@SuppressWarnings("javadoc")
public class Indolently {

    @SuppressWarnings("rawtypes")
    private static final Class<? extends Map> MAP_TYPE = LinkedHashMap.class;

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
     * @param value value
     * @return Optional representation of value
     */
    public static <T> Optional<T> nonNull(final T value) {
        return optional(value);
    }

    /**
     * An alias of {@link Optional#ofNullable(T)}.
     *
     * @param value value
     * @return Optional representation of value
     */
    public static <T> Optional<T> optional(final T value) {
        return Optional.ofNullable(value);
    }

    /**
     * construct new list which contains specified value.
     *
     * @param value to add
     * @return new list
     */
    public static <T> Slist<T> list(final Optional<? extends T> value) {
        return new SlistImpl<T>().push(value);
    }

    /**
     * construct new list which contains specified values.
     *
     * @param values to add
     * @return new list
     */
    public static <T> Slist<T> list(final Iterable<? extends T> values) {
        return new SlistImpl<T>().pushAll(optional(values));
    }

    /**
     * construct new list which contains specified values.
     *
     * @param values to add
     * @return new list
     */
    @SafeVarargs
    public static <T> Slist<T> list(final T... values) {

        final Slist<T> list = new SlistImpl<>();

        if (values != null) {
            for (final T v : values) {
                list.add(v);
            }
        }

        return list;
    }

    /**
     * Generate infinite integer sequence.
     *
     * @param from the value start from (inclusive).
     * @return infinite integer sequence.
     */
    public static Iterable<Integer> sequence(final int from) {
        return sequence(from, 1);
    }

    /**
     * Generate infinite integer sequence.
     *
     * @param from the value start from (inclusive).
     * @param step count stepping
     * @return infinite integer sequence.
     */
    public static Iterable<Integer> sequence(final int from, final int step) {
        if (step <= 0) {
            throw new IllegalArgumentException(String.format("(step = %d) <= 0", step));
        }

        return () -> new Iterator<Integer>() {

            private long i = from;

            @Override
            public boolean hasNext() {
                return this.i <= Integer.MAX_VALUE;
            }

            @Override
            public Integer next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }

                final int val = (int) this.i;
                this.i += step;
                return val;
            }
        };
    }

    /**
     * Generate integer list.
     * <p>
     * Examples
     * <ul>
     * <li>{@code range(1, 3)} -&gt; {@code [1, 2, 3]}</li>
     * <li>{@code range(3, 1)} -&gt; {@code [3, 2, 1]}</li>
     * <li>{@code range(-3, 1)} -&gt; {@code [-3, -2, -1, 0, 1]}</li>
     * </ul>
     * </p>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @return integer list.
     */
    public static Slist<Integer> range(final int from, final int to) {
        return range(from, to, 1);
    }

    /**
     * Generate integer list.
     * <p>
     * Examples
     * <ul>
     * <li>{@code range(1, 6, 2)} -&gt; {@code [1, 3, 5]}</li>
     * </ul>
     * </p>
     *
     * @param from the value start from (inclusive).
     * @param to the value end to (inclusive).
     * @param step count stepping
     * @return integer list.
     */
    public static Slist<Integer> range(final int from, final int to, final int step) {
        if (step <= 0) {
            throw new IllegalArgumentException(String.format("(step = %d) <= 0", step));
        }

        final Slist<Integer> list = list();

        if (from < to) {
            for (int i = from; i <= to; i += step) {
                list.add(i);
            }
        } else if (to < from) {
            for (int i = from; to <= i; i -= step) {
                list.add(i);
            }
        } else {
            assert from == to;
            list.add(from);
        }

        return list;
    }

    public static <T> T[] array(final Iterable<? extends T> values) {
        if (empty(values)) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        // pseudo typing. actually this type wouldn't be T[].
        @SuppressWarnings("unchecked")
        final T[] pseudoTyped = (T[]) list(values).toArray();

        return array(pseudoTyped);
    }

    @SafeVarargs
    public static <T> T[] array(final T... values) {
        if (empty(values)) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        @SuppressWarnings("unchecked")
        final T[] ary = (T[]) Array.newInstance(values[0].getClass(), values.length);

        return list(values).toArray(ary);
    }

    @SafeVarargs
    public static <T, V extends T> T[] array(final Class<T> type, final V... values) {

        if (empty(values)) {
            @SuppressWarnings("unchecked")
            final T[] ary = (T[]) Array.newInstance(type, 0);
            return ary;
        }

        @SuppressWarnings("unchecked")
        final T[] ary = (T[]) Array.newInstance(type, values.length);

        return list(values).toArray(ary);
    }

    public static Object[] oarray(final Object... values) {
        return values;
    }

    public static char[] parray(final char... values) {
        return values;
    }

    public static int[] parray(final int... values) {
        return values;
    }

    public static long[] parray(final long... values) {
        return values;
    }

    public static float[] parray(final float... values) {
        return values;
    }

    public static byte[] parray(final byte... values) {
        return values;
    }

    public static double[] parray(final double... values) {
        return values;
    }

    public static boolean[] parray(final boolean... values) {
        return values;
    }

    public static <T> Sset<T> set(final Optional<? extends T> value) {
        return new SsetImpl<T>().push(value);
    }

    public static <T> Sset<T> set(final Iterable<? extends T> values) {
        return new SsetImpl<T>().pushAll(optional(values));
    }

    @SafeVarargs
    public static <T> Sset<T> set(final T... values) {

        final Sset<T> set = new SsetImpl<>();

        if (values != null) {
            set.addAll(list(values));
        }

        return set;
    }

    public static <K, V> Smap<K, V> sort(final Map<K, V> map) {
        return new SmapImpl<>(new TreeMap<>(map));
    }

    public static <T extends Comparable<T>> Sset<T> sort(final Set<? extends T> values) {
        return new SsetImpl<>(new TreeSet<>(values));
    }

    public static <T extends Comparable<T>> Slist<T> sort(final List<? extends T> values) {
        return list(new TreeSet<>(values));
    }

    public static <K, V> Smap<K, V> freeze(final Map<? extends K, ? extends V> map) {

        final Smap<? extends K, ? extends V> smap =
            (map instanceof Smap) ? (Smap<? extends K, ? extends V>) map : map(map);

        @SuppressWarnings("unchecked")
        final Smap<K, V> rslt = new SmapImpl<>(Collections.unmodifiableMap(smap.map(freezer())));

        return rslt;
    }

    public static <T> Sset<T> freeze(final Set<? extends T> values) {

        final Sset<? extends T> sset = values instanceof Sset ? (Sset<? extends T>) values : set(values);

        @SuppressWarnings("unchecked")
        final Sset<T> rslt = new SsetImpl<>(Collections.unmodifiableSet(sset.map(freezer())));

        return rslt;
    }

    public static <T> Slist<T> freeze(final List<? extends T> values) {

        final Slist<? extends T> slist = (values instanceof Slist) ? (Slist<? extends T>) values : list(values);

        @SuppressWarnings("unchecked")
        final Slist<T> rslt = new SlistImpl<>(Collections.unmodifiableList(slist.map(freezer())));

        return rslt;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Function freezer() {

        return (x) -> (x instanceof List) ? freeze((List) x) //
            : (x instanceof Set) ? freeze((Set) x) //
                : (x instanceof Map) ? freeze((Map) x) //
                    : x;
    }

    public static boolean empty(final Iterable<?> i) {
        if (i == null) {
            return true;
        }

        if (i instanceof Collection) { // for optimization
            return ((Collection<?>) i).isEmpty();
        } else {
            return i.iterator().hasNext();
        }
    }

    public static boolean empty(final Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }

    public static boolean empty(final Optional<?> opt) {
        return (opt == null) || !opt.isPresent();
    }

    public static boolean empty(final CharSequence cs) {
        return (cs == null) || (cs.length() == 0);
    }

    public static boolean blank(final CharSequence cs) {
        return empty(cs) || cs.chars().allMatch(Character::isWhitespace);
    }

    public static boolean empty(final Object[] ary) {
        return (ary == null) || (ary.length == 0);
    }

    @SafeVarargs
    public static <T> T choose(final T... values) {

        for (final T val : values) {
            if (val != null) {
                return val;
            }
        }

        throw new IllegalArgumentException("all values are null");
    }

    @SafeVarargs
    public static <T> T choose(final Supplier<? extends T>... suppliers) {

        for (final Supplier<? extends T> s : suppliers) {

            if (s != null) {
                final T val = s.get();

                if (val != null) {
                    return val;
                }
            }
        }

        throw new IllegalArgumentException("all suppliers return null");
    }

    public static <T extends Comparable<T>> T max(final T l, final T r) {
        return (0 <= l.compareTo(r)) ? r : l;
    }

    public static <T extends Comparable<T>> T min(final T l, final T r) {
        return (l.compareTo(r) < 0) ? r : l;
    }

    @SafeVarargs
    public static <T extends Comparable<T>> T max(final T... values) {
        return list(values).reduce(Indolently::max).get();
    }

    @SafeVarargs
    public static <T extends Comparable<T>> T min(final T... values) {
        return list(values).reduce(Indolently::min).get();
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

    public static <K, V> Smap<K, V> map(@SuppressWarnings("rawtypes")
    final Class<? extends Map> clazz, final K key, final V val) {

        try {
            return new SmapImpl<K, V>(clazz.newInstance()).push(key, val);
        } catch (final ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <K, V> Smap<K, V> map(@SuppressWarnings("rawtypes")
    final Class<? extends Map> clazz, final K key, final Optional<? extends V> val) {

        try {
            return new SmapImpl<K, V>(clazz.newInstance()).push(key, val);
        } catch (final ReflectiveOperationException e) {
            throw new IllegalArgumentException(e);
        }
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

    // CHECKSTYLE:OFF

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1) {
        return map(MAP_TYPE, k0, v0).push(k1, v1);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26).push(k27, v27);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26).push(k27, v27)
            .push(k28, v28);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26).push(k27, v27)
            .push(k28, v28).push(k29, v29);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26).push(k27, v27)
            .push(k28, v28).push(k29, v29).push(k30, v30);
    }

    public static <K, V> Smap<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2,
        final K k3, final V v3, final K k4, final V v4, final K k5, final V v5, final K k6, final V v6, final K k7,
        final V v7, final K k8, final V v8, final K k9, final V v9, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30, final K k31, final V v31) {
        return map(MAP_TYPE, k0, v0).push(k1, v1).push(k2, v2).push(k3, v3).push(k4, v4).push(k5, v5).push(k6, v6)
            .push(k7, v7).push(k8, v8).push(k9, v9).push(k10, v10).push(k11, v11).push(k12, v12).push(k13, v13)
            .push(k14, v14).push(k15, v15).push(k16, v16).push(k17, v17).push(k18, v18).push(k19, v19).push(k20, v20)
            .push(k21, v21).push(k22, v22).push(k23, v23).push(k24, v24).push(k25, v25).push(k26, v26).push(k27, v27)
            .push(k28, v28).push(k29, v29).push(k30, v30).push(k31, v31);
    }
    // CHECKSTYLE:ON
}
