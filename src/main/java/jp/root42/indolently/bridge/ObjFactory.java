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
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;


/**
 * The object factory.
 *
 * @author takahashikzn
 */
public abstract class ObjFactory {

    private static volatile ObjFactory instance;

    static {
        try {
            instance = new KolobokeObjFactory();
        } catch (@SuppressWarnings("unused") final UnsupportedOperationException e) {
            instance = new JdkObjFactory();
        }
    }

    /**
     * Get default instance.
     *
     * @return default instance
     */
    public static ObjFactory getInstance() {
        return instance;
    }

    /**
     * Set default instance.
     *
     * @param ofactory default instance
     */
    public static void setInstance(final ObjFactory ofactory) {
        instance = Objects.requireNonNull(ofactory);
    }

    /**
     * Create new map instance.
     *
     * @return new map instance
     */
    public <K extends Comparable<K>, V> SortedMap<K, V> newSortedMap() {
        return this.newSortedMap(Comparator.naturalOrder());
    }

    /**
     * Create new map instance.
     *
     * @param comp comparator
     * @return new map instance
     */
    public abstract <K, V> SortedMap<K, V> newSortedMap(Comparator<? super K> comp);

    /**
     * Create new set instance.
     *
     * @return new set instance
     */
    public <V extends Comparable<V>> SortedSet<V> newSortedSet() {
        return this.newSortedSet(Comparator.naturalOrder());
    }

    /**
     * Create new set instance.
     *
     * @param comp comparator
     * @return new set instance
     */
    public abstract <V> SortedSet<V> newSortedSet(Comparator<? super V> comp);

    /**
     * Create new map instance.
     *
     * @return new map instance
     */
    public abstract <K, V> Map<K, V> newFifoMap();

    /**
     * Create new set instance.
     *
     * @return new set instance
     */
    public abstract <V> Set<V> newFifoSet();

    /**
     * Create new map instance.
     *
     * @return new map instance
     */
    public abstract <K, V> Map<K, V> newMap();

    /**
     * Create new set instance.
     *
     * @return new set instance
     */
    public abstract <V> Set<V> newSet();

    /**
     * Create new list instance.
     *
     * @return new list instance
     */
    public abstract <V> List<V> newList();
}
