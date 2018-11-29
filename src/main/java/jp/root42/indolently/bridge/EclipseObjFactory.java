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

import org.eclipse.collections.impl.list.mutable.FastList;
import org.eclipse.collections.impl.map.mutable.UnifiedMap;
import org.eclipse.collections.impl.map.sorted.mutable.TreeSortedMap;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;
import org.eclipse.collections.impl.set.sorted.mutable.TreeSortedSet;


/**
 * Implementation of {@link ObjFactory} using <a href="https://www.eclipse.org/collections/">Eclipse Collections framework</a>.
 *
 * @author takahashikzn
 */
final class EclipseObjFactory
    extends JdkObjFactory {

    /**
     * @throws UnsupportedOperationException if Eclipse Collections isn't available.
     */
    public EclipseObjFactory() throws UnsupportedOperationException {
        if (!isPresent("org.eclipse.collections.api.map.MutableMap")) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <K, V> SortedMap<K, V> newSortedMap(final Comparator<? super K> comp) {
        return new TreeSortedMap<>(comp);
    }

    @Override
    public <V> SortedSet<V> newSortedSet(final Comparator<? super V> comp) {
        return new TreeSortedSet<>(comp);
    }

    @Override
    public <K, V> Map<K, V> newMap() {
        return UnifiedMap.newMap(INITIAL_CAPACITY);
    }

    @Override
    public <V> Set<V> newSet() {
        return UnifiedSet.newSet(INITIAL_CAPACITY);
    }

    @Override
    public <V> List<V> newList() {
        return new FastList<>(INITIAL_CAPACITY);
    }
}
