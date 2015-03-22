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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Implementation of {@link ObjFactory} using JDK collection framework.
 *
 * @author takahashikzn
 */
class JdkObjFactory
    extends ObjFactory {

    static final int INITIAL_CAPACITY = 32;

    @Override
    public <K, V> SortedMap<K, V> newSortedMap(final Comparator<? super K> comp) {
        return new TreeMap<>(comp);
    }

    @Override
    public <V> SortedSet<V> newSortedSet(final Comparator<? super V> comp) {
        return new TreeSet<>(comp);
    }

    @Override
    public <K, V> Map<K, V> newFifoMap() {
        return new LinkedHashMap<>();
    }

    @Override
    public <V> Set<V> newFifoSet() {
        return new LinkedHashSet<>();
    }

    @Override
    public <K, V> Map<K, V> newMap() {
        return new HashMap<>(INITIAL_CAPACITY);
    }

    @Override
    public <V> Set<V> newSet() {
        return new HashSet<>(INITIAL_CAPACITY);
    }

    @Override
    public <V> List<V> newList() {
        return new ArrayList<>(INITIAL_CAPACITY);
    }
}
