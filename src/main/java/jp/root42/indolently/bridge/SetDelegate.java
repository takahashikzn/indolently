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
import java.util.Iterator;
import java.util.Set;


/**
 * @author takahashikzn
 */
public class SetDelegate<T>
    extends ObjDelegate<Set<T>>
    implements Set<T> {

    @Override
    protected Set<T> getDelegate() {
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
    public boolean contains(final Object o) {
        return this.getDelegate().contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.getDelegate().iterator();
    }

    @Override
    public Object[] toArray() {
        return this.getDelegate().toArray();
    }

    @Override
    public <S> S[] toArray(final S[] a) {
        //noinspection SuspiciousToArrayCall
        return this.getDelegate().toArray(a);
    }

    @Override
    public boolean add(final T e) {
        return this.getDelegate().add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return this.getDelegate().remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.getDelegate().containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return this.getDelegate().addAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.getDelegate().retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.getDelegate().removeAll(c);
    }

    @Override
    public void clear() {
        this.getDelegate().clear();
    }
}
