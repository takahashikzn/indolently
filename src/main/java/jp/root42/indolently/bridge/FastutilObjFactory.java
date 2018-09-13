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

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;


/**
 * Implementation of {@link ObjFactory} using <a href="http://fastutil.di.unimi.it/">fastutil</a>.
 *
 * @author takahashikzn
 */
final class FastutilObjFactory
    extends JdkObjFactory {

    /**
     * @throws UnsupportedOperationException if Koloboke isn't available.
     */
    public FastutilObjFactory() throws UnsupportedOperationException {
        if (!isPresent("it.unimi.dsi.fastutil.objects.ObjectOpenHashSet")) {
            throw new UnsupportedOperationException();
        }
    }

    private static boolean isPresent(final String fqcn) {
        //noinspection UnusedCatchParameter
        try {
            return Class.forName(fqcn) != null;
        } catch (final ClassNotFoundException e) {
            return false;
        }
    }

    private static final class FastutilHashSet<V>
        extends SerializableSet<V> {

        private static final long serialVersionUID = -4881881384304670668L;

        @Override
        protected Set<V> newSet() {
            return new ObjectOpenHashSet<>(INITIAL_CAPACITY);
        }
    }

    private static final class FastutilHashMap<K, V>
        extends SerializableMap<K, V> {

        private static final long serialVersionUID = 3578828373651399016L;

        @Override
        protected Map<K, V> newMap() {
            return new Object2ObjectOpenHashMap<>(INITIAL_CAPACITY);
        }
    }

    @Override
    public <K, V> Map<K, V> newMap() {
        return new FastutilHashMap<>();
    }

    @Override
    public <V> Set<V> newSet() {
        return new FastutilHashSet<>();
    }
}
