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

import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;
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

    /**
     * Express a method is destructive operation.
     *
     * @author takahashikzn
     */
    @Retention(RetentionPolicy.SOURCE)
    @Target(ElementType.METHOD)
    @Documented
    @Inherited
    public @interface Destructive {
    }

    /**
     * @param <SELF> self type
     * @author takahashikzn
     */
    protected interface Freezable<SELF extends Freezable<SELF>> {

        /**
         * construct freezed new {@link Collections#unmodifiableList(List) List} /
         * {@link Collections#unmodifiableMap(Map) Map} / {@link Collections#unmodifiableSet(Set) Set} instance.
         * Circular structure is not supported yet.
         *
         * @return freezed new instance
         * @see Collections#unmodifiableList(List)
         * @see Collections#unmodifiableMap(Map)
         * @see Collections#unmodifiableSet(Set)
         */
        SELF freeze();
    }

    /**
     * @param <SELF> self type
     * @author takahashikzn
     */
    protected interface Identical<SELF extends Identical<SELF>> {

        /**
         * return this instance.
         *
         * @return {@code this} instance.
         */
        default SELF identity() {
            @SuppressWarnings("unchecked")
            final SELF self = (SELF) this;
            return self;
        }
    }

    /**
     * Extended Map class for indolent person.
     *
     * @param <K> key type
     * @param <V> value type
     * @author takahashikzn
     */
    public interface Smap<K, V>
        extends Map<K, V>, Freezable<Smap<K, V>>, Identical<Smap<K, V>> {

        @Override
        default Smap<K, V> freeze() {
            return Indolently.freeze(this);
        }

        /**
         * put key/value pair then return this instance.
         *
         * @param key key to put
         * @param value value to put
         * @return {@code this} instance
         */
        @Destructive
        default Smap<K, V> push(final K key, final V value) {
            this.put(key, value);
            return this;
        }

        /**
         * put all key/value pairs then return this instance.
         *
         * @param map map to put
         * @return {@code this} instance
         */
        @Destructive
        default Smap<K, V> pushAll(final Map<? extends K, ? extends V> map) {
            this.putAll(map);
            return this;
        }

        /**
         * put key/value pair then return this instance only if value exists.
         * otherwise, do nothing.
         *
         * @param key key to put
         * @param value nullable value to put
         * @return {@code this} instance
         */
        @Destructive
        default Smap<K, V> push(final K key, final Optional<? extends V> value) {
            return Indolently.empty(value) ? this : this.push(key, value.get());
        }

        /**
         * put all key/value pairs then return this instance only if map exists.
         * otherwise, do nothing.
         *
         * @param map nullable map to put
         * @return {@code this} instance
         */
        @Destructive
        default Smap<K, V> pushAll(final Optional<? extends Map<? extends K, ? extends V>> map) {
            return Indolently.empty(map) ? this : this.pushAll(map.get());
        }

        /**
         * remove keys then return this instance.
         *
         * @param keys keys to remove
         * @return {@code this} instance
         */
        @Destructive
        default Smap<K, V> delete(final Iterable<? extends K> keys) {

            if (keys != null) {
                for (final K key : keys) {
                    this.remove(key);
                }
            }

            return this;
        }

        /**
         * An alias of {@link Map#keySet()} then newly constructed (detached) key view.
         * Equivalent to {@code Indolently.set(map.keySet())}.
         *
         * @return keys
         */
        default Sset<K> keys() {
            return Indolently.set(this.keySet());
        }

        /**
         * construct new map which having keys you specify.
         * a key which does not exist is ignored.
         *
         * @return extracted new map
         */
        default Smap<K, V> slice(final Iterable<? extends K> keys) {

            final Smap<K, V> map = Indolently.map();

            for (final K key : keys) {
                if (this.containsKey(key)) {
                    map.put(key, this.get(key));
                }
            }

            return map;
        }

        /**
         * internal iterator.
         *
         * @param f function
         * @return {@code this} instance
         */
        default Smap<K, V> each(final Consumer<? super V> f) {
            return this.each((key, val) -> f.accept(val));
        }

        /**
         * internal iterator.
         *
         * @param f function
         * @return {@code this} instance
         */
        default Smap<K, V> each(final BiConsumer<? super K, ? super V> f) {
            this.forEach(f);
            return this;
        }

        /**
         * Test whether at least one value satisfy condition.
         *
         * @param f condition
         * @return test result
         */
        default boolean anyMatch(final Predicate<? super V> f) {
            return !this.filter((key, val) -> f.test(val)).isEmpty();
        }

        /**
         * Test whether all values satisfy condition.
         *
         * @param f condition
         * @return test result
         */
        default boolean allMatch(final Predicate<? super V> f) {
            return this.filter((key, val) -> f.test(val)).size() == this.size();
        }

        /**
         * Test whether at least one key/value pair satisfy condition.
         *
         * @param f condition
         * @return test result
         */
        default boolean anyMatch(final BiPredicate<? super K, ? super V> f) {
            return !this.filter(f).isEmpty();
        }

        /**
         * Test whether all key/value pairs satisfy condition.
         *
         * @param f condition
         * @return test result
         */
        default boolean allMatch(final BiPredicate<? super K, ? super V> f) {
            return this.filter(f).size() == this.size();
        }

        /**
         * Filter operation.
         * Returns entries as a map which satisfying condition.
         * This operation is constructive.
         *
         * @param f function
         * @return new filtered map
         */
        default Smap<K, V> filter(final Predicate<? super V> f) {
            return this.filter((key, val) -> f.test(val));
        }

        /**
         * Filter operation.
         * Returns entries as a map which satisfying condition.
         * This operation is constructive.
         *
         * @param f condition
         * @return new filtered map
         */
        default Smap<K, V> filter(final BiPredicate<? super K, ? super V> f) {

            final Smap<K, V> rslt = Indolently.map();

            for (final Entry<K, V> e : Indolently.set(this.entrySet())) {

                final K key = e.getKey();
                final V val = e.getValue();

                if (f.test(key, val)) {
                    rslt.put(key, val);
                }
            }

            return rslt;
        }

        /**
         * Map operation.
         * Convert values.
         * This operation is constructive.
         *
         * @param f function
         * @return new converted map
         */
        default Smap<K, V> map(final Function<? super V, ? extends V> f) {
            return this.map((key, val) -> f.apply(val));
        }

        /**
         * Map operation.
         * Convert values.
         * This operation is constructive.
         *
         * @param f function
         * @return new converted map
         */
        default Smap<K, V> map(final BiFunction<? super K, ? super V, ? extends V> f) {

            final Smap<K, V> rslt = Indolently.map();

            for (final Entry<K, V> e : Indolently.set(this.entrySet())) {

                final K key = e.getKey();
                final V val = e.getValue();

                rslt.put(key, f.apply(key, val));
            }

            return rslt;
        }
    }

    /**
     * common method definition for {@link Scol}/{@link Sset}/{@link Slist}.
     *
     * @param <T> value type
     * @param <SELF> self type
     * @author takahashikzn
     */
    protected interface Scol<T, SELF extends Scol<T, SELF>>
        extends Collection<T>, Identical<SELF> {

        /**
         * add value then return this instance.
         *
         * @param value value to add
         * @return {@code this} instance
         */
        @Destructive
        default SELF push(final T value) {
            this.add(value);
            return this.identity();
        }

        /**
         * add all values then return this instance.
         *
         * @param values values to add
         * @return {@code this} instance
         */
        @Destructive
        default SELF pushAll(final Iterable<? extends T> values) {
            for (final T val : values) {
                this.add(val);
            }

            return this.identity();
        }

        /**
         * add value then return this instance only if value exists.
         * otherwise, do nothing.
         *
         * @param value nullable value to add
         * @return {@code this} instance
         */
        @Destructive
        default SELF push(final Optional<? extends T> value) {
            return Indolently.empty(value) ? this.identity() : this.push(value.get());
        }

        /**
         * add all values then return this instance only if values exists.
         * otherwise, do nothing.
         *
         * @param values nullable values to add
         * @return {@code this} instance
         */
        @Destructive
        default SELF pushAll(final Optional<? extends Iterable<? extends T>> values) {
            return Indolently.empty(values) ? this.identity() : this.pushAll(values.get());
        }

        /**
         * remove values then return this instance.
         *
         * @param values values to remove
         * @return {@code this} instance
         * @see #removeAll(Collection)
         */
        @Destructive
        default SELF delete(final Iterable<? extends T> values) {

            // optimization
            final Collection<?> vals =
                (values instanceof Collection) ? (Collection<? extends T>) values : Indolently.list(values);

            this.removeAll(vals);

            return this.identity();
        }

        /**
         * get first value.
         *
         * @return first value
         * @throws NoSuchElementException if empty
         */
        default T first() {
            return this.iterator().next();
        }

        /**
         * get last value.
         *
         * @return first value
         * @throws NoSuchElementException if empty
         */
        default T last() {

            for (final Iterator<T> i = this.iterator(); i.hasNext();) {
                if (!i.hasNext()) {
                    return i.next();
                }
            }

            throw new NoSuchElementException();
        }

        /**
         * get rest of this collection.
         *
         * @return collection values except for first value.
         * if this instance is empty, i.e. {@code col.isEmpty()} returns true, return empty collection.
         */
        SELF tail();

        /**
         * internal iterator.
         *
         * @param f function
         * @return {@code this} instance
         */
        default SELF each(final Consumer<? super T> f) {
            return this.each((idx, val) -> f.accept(val));
        }

        /**
         * internal iterator.
         *
         * @param f function. first argument is loop index.
         * @return {@code this} instance
         */
        default SELF each(final BiConsumer<Integer, ? super T> f) {

            int i = 0;
            for (final T val : this) {
                f.accept(i++, val);
            }

            return this.identity();
        }

        /**
         * Test whether at least one value satisfy condition.
         *
         * @param f condition
         * @return test result
         */
        default boolean anyMatch(final Predicate<? super T> f) {
            return !this.filter(f).isEmpty();
        }

        /**
         * Test whether all values satisfy condition.
         *
         * @param f condition
         * @return test result
         */
        default boolean allMatch(final Predicate<? super T> f) {
            return this.filter(f).size() == this.size();
        }

        /**
         * Map operation.
         * Convert values.
         * This operation is constructive.
         *
         * @param f function
         * @return newly constructed collection which contains converted values
         */
        default SELF map(final Function<? super T, ? extends T> f) {
            return this.map((idx, val) -> f.apply(val));
        }

        /**
         * Map operation.
         * Convert values.
         * This operation is constructive.
         *
         * @param f function. first argument is loop index.
         * @return newly constructed collection which contains converted values
         */
        SELF map(BiFunction<Integer, ? super T, ? extends T> f);

        /**
         * Filter operation.
         * Returns values which satisfying condition.
         * This operation is constructive.
         *
         * @param f condition
         * @return new filtered collection
         */
        default SELF filter(final Predicate<? super T> f) {
            return this.filter((idx, val) -> f.test(val));
        }

        /**
         * Filter operation.
         * Returns values which satisfying condition.
         * This operation is constructive.
         *
         * @param f condition. first argument is loop index.
         * @return new filtered collection
         */
        SELF filter(BiPredicate<Integer, ? super T> f);

        /**
         * Reduce operation.
         *
         * @param f function
         * @return result value
         */
        default Optional<T> reduce(final BiFunction<? super T, ? super T, ? extends T> f) {
            return this.reduce(Optional.empty(), f);
        }

        /**
         * Reduce operation.
         *
         * @param initial initial value
         * @param f function
         * @return result value
         */
        default Optional<T> reduce(final T initial, final BiFunction<? super T, ? super T, ? extends T> f) {
            return this.reduce(Optional.of(initial), f);
        }

        /**
         * Reduce operation.
         *
         * @param initial initial value
         * @param f function
         * @return result value
         */
        default Optional<T> reduce(final Optional<? extends T> initial,
            final BiFunction<? super T, ? super T, ? extends T> f) {

            T memo = Indolently.empty(initial) ? null : initial.get();

            for (final T val : this) {
                memo = f.apply(memo, val);
            }

            return Optional.ofNullable(memo);
        }
    }

    /**
     * Extended {@link List} class for indolent person.
     *
     * @param <T> value type
     * @author takahashikzn
     */
    public interface Slist<T>
        extends Scol<T, Slist<T>>, List<T>, Freezable<Slist<T>> {

        @Override
        default Slist<T> freeze() {
            return Indolently.freeze(this);
        }

        // for optimization
        @Override
        default T first() {
            return this.get(0);
        }

        /**
         * Equivalent to {@code list.subList(idx, list.size())}.
         *
         * @param from from index. negative index also acceptable.
         * @return sub list
         */
        default List<T> subList(final int from) {
            return this.subList(Indolently.idx(this, from), this.size());
        }

        @Override
        default Slist<T> tail() {
            return (this.size() <= 1) ? Indolently.list() : Indolently.list(this.subList(1));
        }

        // for optimization
        @Override
        default T last() {
            return this.get(-1);
        }

        /**
         * convert this list to {@link Sset}.
         *
         * @return a set constructed from this instance.
         */
        default Sset<T> set() {
            return Indolently.set(this);
        }

        /**
         * insert value at specified index then return this instance.
         *
         * @param idx insertion position.
         * negative index also acceptable. for example, {@code slist.push(-1, "x")} means
         * {@code slist.push(slist.size() - 1, "x")}
         * @param value value to add
         * @return {@code this} instance
         */
        @Destructive
        default Slist<T> push(final int idx, final T value) {
            this.add(Indolently.idx(this, idx), value);
            return this;
        }

        /**
         * insert all values at specified index then return this instance.
         *
         * @param idx insertion position.
         * negative index also acceptable. for example, {@code slist.pushAll(-1, list("x", "y"))} means
         * {@code slist.pushAll(slist.size() - 1,  list("x", "y"))}
         * @param values values to add
         * @return {@code this} instance
         */
        @Destructive
        default Slist<T> pushAll(final int idx, final Iterable<? extends T> values) {

            // optimization
            final Collection<? extends T> vals =
                (values instanceof Collection) ? (Collection<? extends T>) values : Indolently.list(values);

            this.addAll(Indolently.idx(this, idx), vals);

            return this;
        }

        /**
         * insert value at specified index then return this instance only if value exists.
         * otherwise, do nothing.
         *
         * @param idx insertion position.
         * negative index also acceptable. for example, {@code slist.push(-1, "x")} means
         * {@code slist.push(slist.size() - 1, "x")}
         * @param value nullable value to add
         * @return {@code this} instance
         */
        @Destructive
        default Slist<T> push(final int idx, final Optional<? extends T> value) {
            return Indolently.empty(value) ? this : this.push(idx, value.get());
        }

        /**
         * insert all values at specified index then return this instance only if values exist.
         * otherwise, do nothing.
         *
         * @param idx insertion position.
         * negative index also acceptable. for example, {@code slist.pushAll(-1, list("x", "y"))} means
         * {@code slist.pushAll(slist.size() - 1,  list("x", "y"))}
         * @param values nullable values to add
         * @return {@code this} instance
         */
        @Destructive
        default Slist<T> pushAll(final int idx, final Optional<? extends Iterable<? extends T>> values) {
            return Indolently.empty(values) ? this : this.pushAll(idx, values.get());
        }

        /**
         * an alias of {@link #subList(int, int)} but newly constructed (detached) view.
         *
         * @return detached sub list
         */
        default Slist<T> slice(final int from, final int to) {
            return Indolently.list(this.subList(from, to));
        }

        @Override
        default Slist<T> map(final BiFunction<Integer, ? super T, ? extends T> f) {

            final Slist<T> rslt = Indolently.list();

            int i = 0;
            for (final T val : this) {
                rslt.add(f.apply(i++, val));
            }

            return rslt;
        }

        @Override
        default Slist<T> filter(final BiPredicate<Integer, ? super T> f) {

            final Slist<T> rslt = Indolently.list();

            int i = 0;
            for (final T val : this) {
                if (f.test(i++, val)) {
                    rslt.add(val);
                }
            }

            return this;
        }
    }

    private static int idx(final List<?> list, final int idx) {
        return 0 <= idx ? idx : list.size() + idx;
    }

    /**
     * Extended {@link Set} class for indolent person.
     *
     * @param <T> value type
     * @author takahashikzn
     */
    public interface Sset<T>
        extends Scol<T, Sset<T>>, Set<T>, Freezable<Sset<T>> {

        @Override
        default Sset<T> freeze() {
            return Indolently.freeze(this);
        }

        @Override
        default Sset<T> tail() {
            return Indolently.set(this.list().tail());
        }

        /**
         * convert this set to {@link Slist}.
         *
         * @return a list newly constructed from this instance.
         */
        default Slist<T> list() {
            return Indolently.list(this);
        }

        @Override
        default Sset<T> map(final BiFunction<Integer, ? super T, ? extends T> f) {

            final Sset<T> rslt = Indolently.set();

            int i = 0;
            for (final T val : this) {
                rslt.add(f.apply(i++, val));
            }

            return rslt;
        }

        @Override
        default Sset<T> filter(final BiPredicate<Integer, ? super T> f) {

            final Sset<T> rslt = Indolently.set();

            int i = 0;
            for (final T val : this) {
                if (!f.test(i++, val)) {
                    rslt.add(val);
                }
            }

            return this;
        }
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
        @SuppressWarnings("unchecked")
        final Smap<K, V> rslt = new SmapImpl<>(Collections.unmodifiableMap(map(map).map(freezer())));
        return rslt;
    }

    public static <T> Sset<T> freeze(final Set<? extends T> values) {
        @SuppressWarnings("unchecked")
        final Sset<T> rslt = new SsetImpl<>(Collections.unmodifiableSet(set(values).map(freezer())));
        return rslt;
    }

    public static <T> Slist<T> freeze(final List<? extends T> values) {
        @SuppressWarnings("unchecked")
        final Slist<T> rslt = new SlistImpl<>(Collections.unmodifiableList(list(values).map(freezer())));
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
        return minmax(true, values);
    }

    @SafeVarargs
    public static <T extends Comparable<T>> T min(final T... values) {
        return minmax(false, values);
    }

    private static <T extends Comparable<T>> T minmax(final boolean max, final T[] values) {

        if (empty(values)) {
            return null;
        }

        T rslt = values[0];

        for (final T value : values) {
            rslt = max ? max(value, rslt) : min(value, rslt);
        }

        return rslt;
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

    private static final class SmapImpl<K, V>
        extends AbstractMap<K, V>
        implements Smap<K, V>, Serializable {

        private static final long serialVersionUID = 8705188807596442213L;

        private Map<K, V> store;

        SmapImpl() {
            this(new HashMap<>());
        }

        SmapImpl(final Map<K, V> store) {
            this.store = store;
        }

        @Override
        public V put(final K key, final V value) {
            return this.store.put(key, value);
        }

        @Override
        public Set<Entry<K, V>> entrySet() {
            return this.store.entrySet();
        }

        @Override
        public String toString() {
            return this.store.toString();
        }

        @Override
        public int hashCode() {
            return this.getClass().hashCode() ^ this.store.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            return (this == o) || this.store.equals(o);
        }
    }

    private static final class SlistImpl<T>
        extends AbstractList<T>
        implements Slist<T>, Serializable {

        private static final long serialVersionUID = 8705188807596442213L;

        private final List<T> store;

        SlistImpl() {
            this(new ArrayList<>());
        }

        SlistImpl(final List<T> store) {
            this.store = store;
        }

        // keep original order
        @Override
        public Sset<T> set() {
            return new SsetImpl<>(new LinkedHashSet<>(this));
        }

        @Override
        public T set(final int i, final T val) {
            return this.store.set(Indolently.idx(this, i), val);
        }

        @Override
        public void add(final int i, final T val) {
            this.store.add(Indolently.idx(this, i), val);
        }

        @Override
        public T remove(final int i) {
            return this.store.remove(Indolently.idx(this, i));
        }

        @Override
        public T get(final int i) {
            return this.store.get(Indolently.idx(this, i));
        }

        @Override
        public List<T> subList(final int from, final int to) {

            final int actFrom = Indolently.idx(this, from);
            final int actTo = Indolently.idx(this, to);

            return this.store.subList(actFrom, ((from < 0) && (actTo == 0)) ? this.size() : actTo);
        }

        @Override
        public ListIterator<T> listIterator(final int i) {
            return this.store.listIterator(Indolently.idx(this, i));
        }

        @Override
        public int size() {
            return this.store.size();
        }

        @Override
        public String toString() {
            return this.store.toString();
        }

        @Override
        public int hashCode() {
            return this.getClass().hashCode() ^ this.store.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            return (this == o) || this.store.equals(o);
        }
    }

    private static final class SsetImpl<T>
        extends AbstractSet<T>
        implements Sset<T>, Serializable {

        private static final long serialVersionUID = 8705188807596442213L;

        private final Set<T> store;

        SsetImpl() {
            this(new HashSet<>());
        }

        SsetImpl(final Set<T> store) {
            this.store = store;
        }

        @Override
        public boolean add(final T element) {
            return this.store.add(element);
        }

        @Override
        public int size() {
            return this.store.size();
        }

        @Override
        public Iterator<T> iterator() {
            return this.store.iterator();
        }

        @Override
        public String toString() {
            return this.store.toString();
        }

        @Override
        public int hashCode() {
            return this.getClass().hashCode() ^ this.store.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            return (this == o) || this.store.equals(o);
        }
    }
}
