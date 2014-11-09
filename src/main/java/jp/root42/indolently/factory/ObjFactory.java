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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * The object factory.
 *
 * @author takahashikzn
 */
public abstract class ObjFactory {

    private static volatile ObjFactory instance = new JdkObjFactory();

    static {
        if (isPresent("net.openhft.koloboke.collect.impl.hash.ObjHash")) {
            instance = new KolobokeObjFactory();
        } else {
            instance = new JdkObjFactory();
        }
    }

    private static boolean isPresent(final String fqcn) {
        try {
            return Class.forName(fqcn) != null;
        } catch (final ClassNotFoundException e) {
            return false;
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
