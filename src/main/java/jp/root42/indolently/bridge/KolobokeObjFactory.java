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

import java.util.Map;
import java.util.Set;

import net.openhft.koloboke.collect.map.hash.HashObjObjMaps;
import net.openhft.koloboke.collect.set.hash.HashObjSets;


/**
 * Implementation of {@link ObjFactory} using <a href="http://openhft.net/products/koloboke-collections">Koloboke
 * collection framework</a>.
 *
 * @author takahashikzn
 */
final class KolobokeObjFactory
    extends JdkObjFactory {

    /**
     * @throws UnsupportedOperationException if Koloboke isn't available.
     */
    public KolobokeObjFactory() throws UnsupportedOperationException {
        if (!isPresent("net.openhft.koloboke.collect.impl.hash.ObjHash")) {
            throw new UnsupportedOperationException();
        }
    }

    private static boolean isPresent(final String fqcn) {
        try {
            return Class.forName(fqcn) != null;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    private static final class KolobokeHashSet<V>
        extends SerializableSet<V> {

        private static final long serialVersionUID = -4881881384304670668L;

        static {
            // HashObjSets isn't thread-safe, so synchronized initialization is required.
            HashObjSets.newMutableSet();
        }

        @Override
        protected Set<V> newSet() {
            return HashObjSets.newMutableSet(INITIAL_CAPACITY);
        }
    }

    private static final class KolobokeHashMap<K, V>
        extends SerializableMap<K, V> {

        private static final long serialVersionUID = 3578828373651399016L;

        static {
            // HashObjObjMaps isn't thread-safe, so synchronized initialization is required.
            HashObjObjMaps.newMutableMap();
        }

        @Override
        protected Map<K, V> newMap() {
            return HashObjObjMaps.newMutableMap(INITIAL_CAPACITY);
        }
    }

    @Override
    public <K, V> Map<K, V> newMap() {
        return new KolobokeHashMap<>();
    }

    @Override
    public <V> Set<V> newSet() {
        return new KolobokeHashSet<>();
    }
}
