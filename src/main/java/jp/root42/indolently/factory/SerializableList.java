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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Serializable wrapper of List.
 *
 * @author takahashikzn
 */
abstract class SerializableList<T>
    implements List<T>, Serializable {

    private static final long serialVersionUID = -2690931773619464155L;

    private List<T> list;

    public SerializableList(final List<T> list) {
        this.list = this.newList();
    }

    protected abstract List<T> newList();

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return this.list.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.list.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public <S> S[] toArray(final S[] a) {
        return this.list.toArray(a);
    }

    @Override
    public boolean add(final T e) {
        return this.list.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return this.list.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.list.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return this.list.addAll(c);
    }

    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        return this.list.addAll(index, c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.list.removeAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.list.retainAll(c);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return this.list.equals(o);
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public T get(final int index) {
        return this.list.get(index);
    }

    @Override
    public T set(final int index, final T element) {
        return this.list.set(index, element);
    }

    @Override
    public void add(final int index, final T element) {
        this.list.add(index, element);
    }

    @Override
    public T remove(final int index) {
        return this.list.remove(index);
    }

    @Override
    public int indexOf(final Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(final Object o) {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.list.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(final int index) {
        return this.list.listIterator(index);
    }

    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    @Override
    public String toString() {
        return this.list.toString();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(new ArrayList<>(this));
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.list = this.newList();
        this.list.addAll((List<T>) in.readObject());
    }
}
