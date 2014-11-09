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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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
