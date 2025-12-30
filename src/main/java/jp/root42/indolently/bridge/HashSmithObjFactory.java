// Copyright 2026 takahashikzn
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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.github.bluuewhale.hashsmith.SwissMap;
import io.github.bluuewhale.hashsmith.SwissSet;


/**
 * Implementation of {@link ObjFactory} using <a href="https://github.com/bluuewhale/hash-smith">HashSmith</a>.
 *
 * @author takahashikzn
 */
final class HashSmithObjFactory
    extends JdkObjFactory {

    static final boolean swissset_available;

    static {
        boolean b;
        try {
            new SwissSet<>();
            b = true;
        } catch (NoClassDefFoundError e) {
            if (!e.getMessage().contains("jdk/incubator/vector/ByteVector")) throw e;
            else b = false;
        }

        swissset_available = b;
    }

    /**
     * @throws UnsupportedOperationException if Hash Smith isn't available.
     */
    public HashSmithObjFactory() throws UnsupportedOperationException {
        if (!isPresent("io.github.bluuewhale.hashsmith.SwissMap")) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public <K, V> Map<K, V> newMap() { return new SwissMap<>(INITIAL_CAPACITY); }

    @Override
    public <V> Set<V> newSet() { return swissset_available ? new SwissSet<>(INITIAL_CAPACITY) : new HashSet<>(INITIAL_CAPACITY); }

    @Override
    public <K, V> Map<K, V> optimize(final Map<K, V> map) {
        final var ret = new SwissMap<K, V>();
        ret.putAll(map);
        return ret;
    }
}
