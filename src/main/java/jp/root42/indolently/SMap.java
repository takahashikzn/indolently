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

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.root42.indolently.SMap.SEntry;
import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.trait.EdgeAwareIterable;
import jp.root42.indolently.trait.Filterable;
import jp.root42.indolently.trait.Freezable;
import jp.root42.indolently.trait.Identical;
import jp.root42.indolently.trait.Loopable;
import jp.root42.indolently.trait.Matchable;

import static java.util.Objects.*;


/**
 * Extended Map class for indolent person.
 * The name is came from "Sugared Map".
 *
 * @param <K> key type
 * @param <V> value type
 * @author takahashikzn
 */
public interface SMap<K, V>
    extends Map<K, V>, Freezable<SMap<K, V>>, Identical<SMap<K, V>>, Loopable<V, SMap<K, V>>, Filterable<V, SMap<K, V>>,
    EdgeAwareIterable<SEntry<K, V>>, Matchable<V>, Cloneable {

    /**
     * Create a new fifo Map instance which containing all entries which this instance contains.
     *
     * @return a new fifo Map instance which containing all entries which this instance contains
     */
    default SMap<K, V> fifo() {
        return of(ObjFactory.getInstance().<K, V> newFifoMap()).pushAll(this);
    }

    /**
     * Clone this instance.
     *
     * @return clone of this instance
     * @see Object#clone()
     * @see Cloneable
     */
    default SMap<K, V> clone() {
        return Indolently.<K, V> map().pushAll(this);
    }

    /**
     * Wrap a map.
     * This method is an alias of {@link Indolently#wrap(Map)}.
     *
     * @param map map to wrap
     * @return wrapped map
     */
    public static <K, V> SMap<K, V> of(final Map<K, V> map) {
        return Indolently.wrap(map);
    }

    /**
     * {@inheritDoc}
     *
     * @see Indolently#freeze(Map)
     */
    @Override
    default SMap<K, V> freeze() {
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
    default SMap<K, V> push(final K key, final V value) {
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
    default SMap<K, V> pushAll(final Map<? extends K, ? extends V> map) {
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
    default SMap<K, V> push(final K key, final Optional<? extends V> value) {
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
    default SMap<K, V> pushAll(final Optional<? extends Map<? extends K, ? extends V>> map) {
        return Indolently.empty(map) ? this : this.pushAll(map.get());
    }

    /**
     * remove keys then return this instance.
     *
     * @param keys keys to remove
     * @return {@code this} instance
     */
    @Destructive
    default SMap<K, V> delete(final Iterable<? extends K> keys) {
        this.keySet().removeAll(Indolently.set(keys));
        return this;
    }

    /**
     * remove keys then return this instance.
     *
     * @param f entry filter to remove
     * @return {@code this} instance
     */
    @Destructive
    default SMap<K, V> delete(final BiPredicate<? super K, ? super V> f) {
        return this.delete(this.keys().filter(x -> f.test(x, this.get(x))));
    }

    /**
     * An alias of {@link Map#keySet()} but newly constructed, detached one.
     * Any modification don't effect to this map.
     * Equivalent to {@code Indolently.set(map.keySet())}.
     *
     * @return keys
     */
    default SSet<K> keys() {
        return Indolently.set(this.keySet());
    }

    /**
     * An alias of {@link Map#values()} but newly constructed, detached one.
     * Any modification don't effect to this map.
     * Equivalent to {@code Indolently.list(map.values())}.
     *
     * @return values
     */
    default SList<V> vals() {
        return Indolently.list(this.values());
    }

    /**
     * Extended {@link java.util.Map.Entry} class for indolent person.
     * The name is came from "Sugared Entry".
     *
     * @param <K> key type
     * @param <V> value type
     * @author takahashikzn.
     */
    class SEntry<K, V>
        implements Map.Entry<K, V> {

        private final Entry<K, V> e;

        /** key of this entry. */
        public final K key;

        /** value of this entry. */
        public final V val;

        /**
         * Constructor.
         *
         * @param e entry
         */
        public SEntry(final Entry<K, V> e) {
            this.e = requireNonNull(e, "entry");
            this.key = e.getKey();
            this.val = e.getValue();
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.val;
        }

        /**
         * Use {@link #update(Object)} instead of.
         * This method is unsupported.
         *
         * @throws UnsupportedOperationException always thrown
         */
        @Deprecated
        @Override
        public final V setValue(final V value) throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        /**
         * Update the value corresponded to the key of this entry.
         * <p>
         * Important: the field {@link #val} never changed by calling this method.
         * </p>
         *
         * @param value new value
         * @return {@code this} instance
         * @see #setValue(Object)
         */
        public V update(final V value) {
            return this.e.setValue(value);
        }

        @Override
        public int hashCode() {
            return this.e.hashCode();
        }

        @Override
        public boolean equals(final Object o) {
            return this.e.equals(o);
        }

        @Override
        public String toString() {
            return this.e.toString();
        }
    }

    /**
     * An alias of {@link Map#entrySet()} but newly constructed, detached one.
     * Any modification don't effect to this map.
     * Equivalent to {@code Indolently.set(map.entrySet())}.
     *
     * @return entries
     */
    default SSet<SEntry<K, V>> entries() {
        return Indolently.set(this.entrySet()).map(SEntry<K, V>::new);
    }

    @Override
    default SIter<SEntry<K, V>> iterator() {
        return Indolently.wrap(this.entrySet().iterator()).map(SEntry<K, V>::new);
    }

    /**
     * construct new map which having keys you specify.
     * any keys which does not contained by this instance are ignored.
     *
     * @param keys keys to extract
     * @return extracted new map
     */
    default SMap<K, V> slice(final Iterable<? extends K> keys) {
        return this.delete(this.keys().delete(keys));
    }

    /**
     * internal iterator.
     *
     * @param f function
     * @return {@code this} instance
     */
    @Override
    default SMap<K, V> each(final Consumer<? super V> f) {
        return this.each((key, val) -> f.accept(val));
    }

    /**
     * internal iterator.
     *
     * @param f function
     * @return {@code this} instance
     */
    default SMap<K, V> each(final BiConsumer<? super K, ? super V> f) {
        this.forEach(f);
        return this;
    }

    @Override
    default boolean some(final Predicate<? super V> f) {
        return this.some((key, val) -> f.test(val));
    }

    /**
     * Test whether at least one key/value pair satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean some(final BiPredicate<? super K, ? super V> f) {
        return !this.filter(f).isEmpty();
    }

    /**
     * Test whether all key/value pairs satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean every(final BiPredicate<? super K, ? super V> f) {
        return this.filter(f).size() == this.size();
    }

    /**
     * Count key/value pairs which satisfying condition.
     *
     * @param f condition
     * @return the number of key/value pairs which satisfying condition
     */
    default int count(final BiPredicate<? super K, ? super V> f) {
        return this.filter(f).size();
    }

    /**
     * Filter operation: returns entries as a map which satisfying condition.
     * This operation is constructive.
     *
     * @param f function
     * @return new filtered map
     */
    @Override
    default SMap<K, V> filter(final Predicate<? super V> f) {
        return this.filter((key, val) -> f.test(val));
    }

    /**
     * Filter operation: returns entries as a map which satisfying condition.
     * This operation is constructive.
     *
     * @param f condition
     * @return new filtered map
     */
    default SMap<K, V> filter(final BiPredicate<? super K, ? super V> f) {

        return this //
            .entries() //
            .filter(e -> f.test(e.key, e.val)) //
            .reduce( //
                Indolently.map(), //
                (map, e) -> map.push(e.key, e.val));
    }

    /**
     * Map operation: map value to another type value.
     * This operation is constructive.
     *
     * @param <R> mapping target type
     * @param f function
     * @return new converted map
     */
    default <R> SMap<K, R> map(final Function<? super V, ? extends R> f) {
        return this.map((key, val) -> f.apply(val));
    }

    /**
     * Map operation: map value to another type value.
     * This operation is constructive.
     *
     * @param <R> mapping target type
     * @param f function
     * @return new converted map
     */
    default <R> SMap<K, R> map(final BiFunction<? super K, ? super V, ? extends R> f) {

        return this //
            .entries() //
            .reduce( //
                Indolently.map(), //
                (map, e) -> map.push( //
                    e.key, //
                    f.apply(e.key, e.val)));
    }

    /**
     * Just an alias of {@link #containsKey(Object)}
     *
     * @param key key of map
     * @return the result of {@link #containsKey(Object)}
     */
    default boolean has(final K key) {
        return this.containsKey(key);
    }

    /**
     * Return value as optional representation.
     *
     * @param key key of the value
     * @return optional representation of the value
     */
    default Optional<V> opt(final K key) {
        return Indolently.optional(this.get(key));
    }

    /**
     * Return newly constructed sorted map using comparator.
     *
     * @param comp comparator
     * @return sorted map
     */
    default SMap<K, V> sortWith(final Comparator<? super K> comp) {
        return Indolently.sort(this, comp);
    }
}
