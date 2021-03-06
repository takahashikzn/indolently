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
import java.util.function.Predicate;

import jp.root42.indolently.bridge.ListDelegate;
import jp.root42.indolently.bridge.ObjFactory;

import static jp.root42.indolently.Expressive.*;


/**
 * Simple implementation of {@link $list}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
class $list_impl<T>
    extends ListDelegate<T>
    implements $list<T>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private final List<T> store;

    public $list_impl() { this(newList()); }

    public $list_impl(final List<T> store) { this.store = store; }

    private static <T> List<T> newList() { return ObjFactory.getInstance().newList(); }

    @Override
    protected List<T> getDelegate() { return this.store; }

    @Override
    public $list<T> clone() {
        final List<T> newStore = newList();
        newStore.addAll(this.store);
        return new $list_impl<>(newStore);
    }

    @Override
    public $iter<T> iterator() { return Indolently.$(this.store.iterator()); }

    // keep original order
    @Override
    public $set<T> set() { return new $set_impl<>(ObjFactory.getInstance().<T> newFifoSet()).pushAll(this); }

    @Override
    public T set(final int i, final T val) { return this.store.set(Indolently.idx(this, i), val); }

    @Override
    public void add(final int i, final T val) { this.store.add(Indolently.idx(this, i), val); }

    @Override
    public T remove(final int i) { return this.store.remove(Indolently.idx(this, i)); }

    @Override
    public T get(final int i) { return this.store.get(Indolently.idx(this, i)); }

    @Override
    public $list<T> subList(final int from, final int to) {

        int actFrom = Indolently.idx(this, from);

        if (actFrom < 0) {
            actFrom = 0;
        }

        final int actTo;
        if ((from < 0) && (to == 0)) {
            actTo = this.size();
        } else {
            actTo = Indolently.idx(this, to);
        }

        return Indolently.$(this.store.subList(actFrom, actTo));
    }

    @Override
    public ListIterator<T> listIterator(final int i) { return this.store.listIterator(Indolently.idx(this, i)); }

    // for optimization
    @Override
    public boolean some(final Predicate<? super T> f) {

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0, Z = this.size(); i < Z; i++) {
            if (f.test(this.get(i))) return true;
        }

        return false;
    }

    @Override
    public int hashCode() { return this.store.hashCode(); }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object o) { return (this == o) || this.store.equals(o); }

    private static final Class<?> FROZEN = eval(() -> Class.forName("java.util.Collections$UnmodifiableList"));

    boolean frozen() {
        return (this.store instanceof $list_impl) && (($list_impl<?>) this.store).frozen()
            || this.store.getClass() == FROZEN;
    }
}
