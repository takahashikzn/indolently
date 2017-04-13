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
package jp.root42.indolently.bridge;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * @author takahashikzn
 */
public class MapDelegate<K, V>
    extends ObjDelegate<Map<K, V>>
    implements Map<K, V> {

    @Override
    protected Map<K, V> getDelegate() {
        throw new UnsupportedOperationException("PLEASE OVERRIDE IT");
    }

    @Override
    public int size() {
        return this.getDelegate().size();
    }

    @Override
    public boolean isEmpty() {
        return this.getDelegate().isEmpty();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.getDelegate().containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.getDelegate().containsValue(value);
    }

    @Override
    public V get(final Object key) {
        return this.getDelegate().get(key);
    }

    @Override
    public V put(final K key, final V value) {
        return this.getDelegate().put(key, value);
    }

    @Override
    public V remove(final Object key) {
        return this.getDelegate().remove(key);
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        this.getDelegate().putAll(m);
    }

    @Override
    public void clear() {
        this.getDelegate().clear();
    }

    @Override
    public Set<K> keySet() {
        return this.getDelegate().keySet();
    }

    @Override
    public Collection<V> values() {
        return this.getDelegate().values();
    }

    @Override
    public Set<java.util.Map.Entry<K, V>> entrySet() {
        return this.getDelegate().entrySet();
    }
}
