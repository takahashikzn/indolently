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
package jp.root42.indolently.factory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Serializable wrapper of Map.
 *
 * @author takahashikzn
 */
abstract class SerializableMap<K, V>
    implements Map<K, V>, Serializable {

    private static final long serialVersionUID = -624797042693721720L;

    private Map<K, V> map;

    public SerializableMap() {
        this.map = this.newMap();
    }

    protected abstract Map<K, V> newMap();

    @Override
    public int size() {
        return this.map.size();
    }

    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.map.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.map.containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return this.map.get(key);
    }

    @Override
    public V put(final K key, final V value) {
        return this.map.put(key, value);
    }

    @Override
    public V remove(final Object key) {
        return this.map.remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.map.putAll(m);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.map.keySet();
    }

    @Override
    public Collection<V> values() {
        return this.map.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(final Object o) {
        return this.map.equals(o);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(new LinkedHashMap<>(this));
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.map = this.newMap();
        this.map.putAll((Map<K, V>) in.readObject());
    }
}
