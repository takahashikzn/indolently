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
import java.util.List;
import java.util.ListIterator;


/**
 * @author takahashikzn
 */
public class ListDelegate<T>
    extends ObjDelegate<List<T>>
    implements List<T> {

    @Override
    protected List<T> getDelegate() {
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
    public boolean addAll(final int index, final Collection<? extends T> c) {
        return this.getDelegate().addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.getDelegate().removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.getDelegate().retainAll(c);
    }

    @Override
    public void clear() {
        this.getDelegate().clear();
    }

    @Override
    public T get(final int index) {
        return this.getDelegate().get(index);
    }

    @Override
    public T set(final int index, final T element) {
        return this.getDelegate().set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        this.getDelegate().add(index, element);
    }

    @Override
    public T remove(final int index) {
        return this.getDelegate().remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return this.getDelegate().indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return this.getDelegate().lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.getDelegate().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return this.getDelegate().listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.getDelegate().subList(fromIndex, toIndex);
    }
}
