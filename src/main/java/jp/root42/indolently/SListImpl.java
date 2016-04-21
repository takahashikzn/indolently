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
import java.util.List;
import java.util.ListIterator;

import jp.root42.indolently.bridge.ListDelegate;
import jp.root42.indolently.bridge.ObjFactory;


/**
 * Simple implementation of {@link SList}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
class SListImpl<T>
    extends ListDelegate<T>
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
    protected List<T> getDelegate() {
        return this.store;
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
        return new SSetImpl<>(ObjFactory.getInstance().<T> newFifoSet()).pushAll(this);
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

        final int actTo;
        if ((from < 0) && (to == 0)) {
            actTo = this.size();
        } else {
            actTo = Indolently.idx(this, to);
        }

        return this.store.subList(actFrom, actTo);
    }

    @Override
    public ListIterator<T> listIterator(final int i) {
        return this.store.listIterator(Indolently.idx(this, i));
    }

    @Override
    public boolean equals(final Object o) {
        return (this == o) || this.store.equals(o);
    }
}
