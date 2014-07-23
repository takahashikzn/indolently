/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Simple implementation of {@link Sset}.
 *
 * @param <T> value type
 * @author root42 Inc.
 */
class SsetImpl<T>
    extends AbstractSet<T>
    implements Sset<T>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private final Set<T> store;

    public SsetImpl() {
        this(new HashSet<>());
    }

    public SsetImpl(final Set<T> store) {
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
