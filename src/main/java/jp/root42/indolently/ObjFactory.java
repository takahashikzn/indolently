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
package jp.root42.indolently;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

    static volatile ObjFactory instance = new JdkObjFactory();

    // static {
    // if (isPresent("com.gs.collections.impl.factory.Lists")) {
    // instance = new GscObjFactory();
    // } else if (isPresent("net.openhft.koloboke.collect.map.hash.HashObjObjMaps")) {
    // instance = new KolobokeObjFactory();
    // } else {
    // instance = new JdkObjFactory();
    // }
    // }
    // private static boolean isPresent(final String fqcn) {
    // try {
    // return Class.forName(fqcn) != null;
    // } catch (final ClassNotFoundException e) {
    // return false;
    // }
    // }

    /**
     * Set default instance.
     *
     * @param instance default instance
     */
    public static void setInstance(final ObjFactory instance) {
        ObjFactory.instance = Objects.requireNonNull(instance);
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
     * Create new listinstance.
     *
     * @return new list instance
     */
    public abstract <V> List<V> newList();
}


/**
 * Implementation of {@link ObjFactory} using JDK collection framework.
 *
 * @author takahashikzn
 */
final class JdkObjFactory
    extends ObjFactory {

    @Override
    public <K, V> Map<K, V> newMap() {
        return new HashMap<>();
    }

    @Override
    public <V> Set<V> newSet() {
        return new HashSet<>();
    }

    @Override
    public <V> List<V> newList() {
        return new ArrayList<>();
    }
}

// /**
// * Implementation of {@link ObjFactory} using <a href="http://openhft.net/products/koloboke-collections">Koloboke
// * collection framework</a>.
// *
// * @author takahashikzn
// */
// final class KolobokeObjFactory
// extends ObjFactory {
//
// @Override
// public <K, V> Map<K, V> newMap() {
// return HashObjObjMaps.newMutableMap();
// }
//
// @Override
// public <V> Set<V> newSet() {
// return HashObjSets.newMutableSet();
// }
//
// @Override
// public <V> List<V> newList() {
// return new ArrayList<>();
// }
// }
//
//
// /**
// * Implementation of {@link ObjFactory} using <a href="https://github.com/goldmansachs/gs-collections">Goldman sachs
// * collection framework</a>.
// *
// * @author takahashikzn
// */
// final class GscObjFactory
// extends ObjFactory {
//
// @Override
// public <K, V> Map<K, V> newMap() {
// return Maps.mutable.of();
// }
//
// @Override
// public <V> Set<V> newSet() {
// return Sets.mutable.of();
// }
//
// @Override
// public <V> List<V> newList() {
// return Lists.mutable.of();
// }
// }
