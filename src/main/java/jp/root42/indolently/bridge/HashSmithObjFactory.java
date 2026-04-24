// Copyright 2026 takahashikzn
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
package jp.root42.indolently.bridge;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import io.github.bluuewhale.hashsmith.SwissMap;
import io.github.bluuewhale.hashsmith.SwissSet;


/**
 * Implementation of {@link ObjFactory} using <a href="https://github.com/bluuewhale/hash-smith">HashSmith</a>.
 *
 * @author takahashikzn
 */
public final class HashSmithObjFactory
    extends JdkObjFactory {

    static final boolean swissset_available;

    static {
        boolean b;
        try {
            new SwissSet<>();
            b = true;
        } catch (NoClassDefFoundError e) {
            if (!e.getMessage().contains("jdk/incubator/vector/ByteVector")) throw e;
            else b = false;
        }

        swissset_available = b;
    }

    /**
     * @throws UnsupportedOperationException if Hash Smith isn't available.
     */
    public HashSmithObjFactory() throws UnsupportedOperationException {
        if (!isPresent("io.github.bluuewhale.hashsmith.SwissMap")) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <K, V> Map<K, V> newMap() { return this.newMap(INITIAL_CAPACITY); }

    public <K, V> Map<K, V> newMap(final int size) { return new NullSupportedSwissMap<>(size); }

    @Override
    public <V> Set<V> newSet() { return swissset_available ? new NullSupportedSwissSet<>(INITIAL_CAPACITY) : new HashSet<>(INITIAL_CAPACITY); }

    @Override
    public <K, V> Map<K, V> optimize(final Map<K, V> map) {
        if (map instanceof NullSupportedSwissMap) return map;
        final Map<K, V> ret = this.newMap(map.size());
        ret.putAll(map);
        return ret;
    }
}

final class NullSupportedSwissMap<K, V>
    extends AbstractMap<K, V>
    implements Serializable {

    private static final Object NULL = new Object();

    private transient SwissMap<K, V> map;

    private transient Object nullVal = NULL;

    public NullSupportedSwissMap(final int size) { this.map = new SwissMap<>(size); }

    @SuppressWarnings("unchecked")
    @Serial
    private void readObject(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        this.nullVal = NULL;

        {
            final var hasNullVal = ois.readBoolean();
            final var val = ois.readObject();

            if (hasNullVal) this.nullVal = val;
        }

        {
            final var m = (Map) ois.readObject();
            final var map = new SwissMap(m.size());
            map.putAll(m);
            this.map = map;
        }
    }

    @SuppressWarnings("unchecked")
    @Serial
    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeBoolean(this.nullVal != NULL);
        oos.writeObject(this.nullVal != NULL ? this.nullVal : null);
        oos.writeObject(new HashMap(this.map));
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(final Object key) {
        if (key != null) return this.map.get(key);

        return this.nullVal == NULL ? null : (V) this.nullVal;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(final K key, final V val) {
        if (key != null) return this.map.put(key, val);

        final V old = (this.nullVal == NULL) ? null : (V) this.nullVal;
        this.nullVal = val;
        return old;
    }

    @Override
    public boolean containsKey(final Object key) {
        if (key != null) return this.map.containsKey(key);

        return this.nullVal != NULL;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V remove(final Object key) {
        if (key != null) return this.map.remove(key);
        if (this.nullVal == NULL) return null;

        final V old = (V) this.nullVal;
        this.nullVal = NULL;
        return old;
    }

    @Override
    public int size() { return this.map.size() + (this.nullVal == NULL ? 0 : 1); }

    @Override
    public void clear() {
        this.nullVal = NULL;
        this.map.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Entry<K, V>> entrySet() {

        final Set<Entry<K, V>> entries = new LinkedHashSet<>();
        if (this.nullVal != NULL) //
            entries.add(new AbstractMap.SimpleImmutableEntry<>(null, (V) this.nullVal));

        entries.addAll(this.map.entrySet());

        return Collections.unmodifiableSet(entries);
    }

    @Override
    public int hashCode() { return this.map.hashCode() + (this.nullVal == NULL ? 0 : Objects.hashCode(this.nullVal)); }

    @Override
    public String toString() { return new HashMap<>(this).toString(); }

    @Override
    public boolean equals(final Object o) {
        return (o == this) || //
               o instanceof Map<?, ?> that && //
               this.size() == that.size() && //
               (this.nullVal != NULL || that.containsKey(null) ? new HashMap<>(this).equals(o) : this.map.equals(o));
    }
}

final class NullSupportedSwissSet<E>
    extends AbstractSet<E> {

    private static final Object PRESENT = new Object();

    private final SwissMap<E, Object> map;

    private boolean hasNull;

    public NullSupportedSwissSet(final int size) { this.map = new SwissMap<>(size); }

    @Override
    public boolean contains(final Object o) {
        if (o != null) return this.map.containsKey(o);

        return this.hasNull;
    }

    @Override
    public boolean add(final E e) {
        if (e != null) return this.map.put(e, PRESENT) == null;

        if (this.hasNull) return false;
        this.hasNull = true;
        return true;
    }

    @Override
    public boolean remove(final Object o) {
        if (o != null) return this.map.remove(o) != null;

        if (!this.hasNull) return false;
        this.hasNull = false;
        return true;
    }

    @Override
    public int size() { return this.map.size() + (this.hasNull ? 1 : 0); }

    @Override
    public void clear() {
        this.hasNull = false;
        this.map.clear();
    }

    @Override
    public Iterator<E> iterator() {

        final var mapIter = this.map.keySet().iterator();

        return new Iterator<>() {

            private boolean nullDelivered = !NullSupportedSwissSet.this.hasNull;

            private boolean lastWasNull;

            private boolean removable;

            @Override
            public boolean hasNext() { return !this.nullDelivered || mapIter.hasNext(); }

            @Override
            public E next() {
                if (!this.nullDelivered) {
                    this.nullDelivered = true;
                    this.lastWasNull = true;
                    this.removable = true;
                    return null;
                }

                if (!mapIter.hasNext()) throw new NoSuchElementException();

                this.lastWasNull = false;
                this.removable = true;
                return mapIter.next();
            }

            @Override
            public void remove() {
                if (!this.removable) throw new IllegalStateException();
                this.removable = false;

                if (this.lastWasNull) NullSupportedSwissSet.this.hasNull = false;
                else mapIter.remove();
            }
        };
    }
}
