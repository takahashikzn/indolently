/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Simple implementation of {@link Smap}.
 *
 * @param <K> key type
 * @param <V> value type
 * @author root42 Inc.
 */
class SmapImpl<K, V>
    extends AbstractMap<K, V>
    implements Smap<K, V>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private Map<K, V> store;

    public SmapImpl() {
        this(new HashMap<>());
    }

    public SmapImpl(final Map<K, V> store) {
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
