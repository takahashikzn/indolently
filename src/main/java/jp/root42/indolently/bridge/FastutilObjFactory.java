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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;


/**
 * Implementation of {@link ObjFactory} using <a href="http://fastutil.di.unimi.it/">fastutil</a>.
 *
 * @author takahashikzn
 */
final class FastutilObjFactory
    extends ObjFactory {

    /**
     * @throws UnsupportedOperationException if Koloboke isn't available.
     */
    public FastutilObjFactory() throws UnsupportedOperationException {
        if (!isPresent("it.unimi.dsi.fastutil.objects.ObjectOpenHashSet")) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <K, V> SortedMap<K, V> newSortedMap(final Comparator<? super K> comp) {
        return new Object2ObjectAVLTreeMap<>(comp);
    }

    @Override
    public <V> SortedSet<V> newSortedSet(final Comparator<? super V> comp) {
        return new ObjectAVLTreeSet<>(comp);
    }

    @Override
    public <K, V> Map<K, V> newFifoMap() {
        return new Object2ObjectLinkedOpenHashMap<>();
    }

    @Override
    public <V> Set<V> newFifoSet() {
        return new ObjectLinkedOpenHashSet<>();
    }

    @Override
    public <K, V> Map<K, V> newMap() {
        return new Object2ObjectOpenHashMap<>();
    }

    @Override
    public <V> Set<V> newSet() {
        return new ObjectOpenHashSet<>();
    }

    @Override
    public <V> List<V> newList() {
        return new ObjectArrayList<>();
    }

    @Override
    public <K, V> Map<K, V> optimize(final Map<K, V> map) { return new Object2ObjectOpenHashMap<>(map); }
}
