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
import java.util.Set;


/**
 * Serializable wrapper of Set.
 *
 * @author takahashikzn
 */
abstract class SerializableSet<T>
    implements Set<T>, Serializable {

    private static final long serialVersionUID = -2690931773619464155L;

    private Set<T> set;

    public SerializableSet() {
        this.set = this.newSet();
    }

    protected abstract Set<T> newSet();

    @Override
    public int size() {
        return this.set.size();
    }

    @Override
    public boolean isEmpty() {
        return this.set.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        return this.set.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.set.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.set.toArray();
    }

    @Override
    public <S> S[] toArray(final S[] a) {
        return this.set.toArray(a);
    }

    @Override
    public boolean add(final T e) {
        return this.set.add(e);
    }

    @Override
    public boolean remove(final Object o) {
        return this.set.remove(o);
    }

    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.set.containsAll(c);
    }

    @Override
    public boolean addAll(final Collection<? extends T> c) {
        return this.set.addAll(c);
    }

    @Override
    public boolean retainAll(final Collection<?> c) {
        return this.set.retainAll(c);
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        return this.set.removeAll(c);
    }

    @Override
    public void clear() {
        this.set.clear();
    }

    @Override
    public boolean equals(final Object o) {
        return this.set.equals(o);
    }

    @Override
    public int hashCode() {
        return this.set.hashCode();
    }

    @Override
    public String toString() {
        return this.set.toString();
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(new ArrayList<>(this));
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.set = this.newSet();
        this.set.addAll((List<T>) in.readObject());
    }
}
