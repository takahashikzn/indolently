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
import java.util.AbstractSet;
import java.util.Set;

import jp.root42.indolently.factory.ObjFactory;


/**
 * Simple implementation of {@link SSet}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
class SSetImpl<T>
    extends AbstractSet<T>
    implements SSet<T>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private final Set<T> store;

    public SSetImpl() {
        this(ObjFactory.getInstance().newSet());
    }

    public SSetImpl(final Set<T> store) {
        this.store = store;
    }

    @Override
    public SSet<T> clone() {
        return SSet.super.clone();
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
    public SIter<T> iterator() {
        return Indolently.wrap(this.store.iterator());
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
