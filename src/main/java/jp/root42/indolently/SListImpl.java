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
import java.util.AbstractList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;

import jp.root42.indolently.factory.ObjFactory;


/**
 * Simple implementation of {@link SList}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
class SListImpl<T>
    extends AbstractList<T>
    implements SList<T>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private final List<T> store;

    public SListImpl() {
        this(ObjFactory.getInstance().newList());
    }

    public SListImpl(final List<T> store) {
        this.store = store;
    }

    @Override
    public SList<T> clone() {
        return SList.super.clone();
    }

    @Override
    public SIter<T> iterator() {
        return Indolently.wrap(this.store.iterator());
    }

    // keep original order
    @Override
    public SSet<T> set() {
        return new SSetImpl<>(new LinkedHashSet<>(this));
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
