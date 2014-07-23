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

import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * Extended Map class for indolent person.
 * It's name comes from "Sugared map".
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
        this.keySet().removeAll(Indolently.set(keys));
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
     * An alias of {@link Map#values()} then newly constructed (detached) value view.
     * Equivalent to {@code Indolently.list(map.values())}.
     *
     * @return values
     */
    default Slist<V> vals() {
        return Indolently.list(this.values());
    }

    /**
     * construct new map which having keys you specify.
     * a key which does not exist is ignored.
     *
     * @param keys keys to extract
     * @return extracted new map
     */
    default Smap<K, V> slice(final Iterable<? extends K> keys) {
        return Indolently.map(this).delete(this.keys().delete(keys));
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
    default boolean some(final Predicate<? super V> f) {
        return this.some((key, val) -> f.test(val));
    }

    /**
     * Test whether all values satisfy condition.
     *
     * @param f condition
     * @return test result
     */
    default boolean every(final Predicate<? super V> f) {
        return this.every((key, val) -> f.test(val));
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
     * Map value to another type value.
     * This operation is constructive.
     *
     * @param <M> mapping target type
     * @param f function
     * @return new converted map
     */
    default <M> Smap<K, M> map(final Function<? super V, ? extends M> f) {
        return this.map((key, val) -> f.apply(val));
    }

    /**
     * Map operation.
     * Map value to another type value.
     * This operation is constructive.
     *
     * @param <M> mapping target type
     * @param f function
     * @return new converted map
     */
    default <M> Smap<K, M> map(final BiFunction<? super K, ? super V, ? extends M> f) {

        final Smap<K, M> rslt = Indolently.map();

        for (final Entry<K, V> e : Indolently.set(this.entrySet())) {

            final K key = e.getKey();
            final V val = e.getValue();

            rslt.put(key, f.apply(key, val));
        }

        return rslt;
    }
}
