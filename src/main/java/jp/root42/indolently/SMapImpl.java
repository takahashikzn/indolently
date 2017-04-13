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

import java.io.Serializable;
import java.util.Map;

import jp.root42.indolently.bridge.MapDelegate;
import jp.root42.indolently.bridge.ObjFactory;


/**
 * Simple implementation of {@link SMap}.
 *
 * @param <K> key type
 * @param <V> value type
 * @author takahashikzn
 */
final class SMapImpl<K, V>
    extends MapDelegate<K, V>
    implements SMap<K, V>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private final Map<K, V> store;

    public SMapImpl() {
        this(ObjFactory.getInstance().newMap());
    }

    public SMapImpl(final Map<K, V> store) {
        this.store = store;
    }

    @Override
    protected Map<K, V> getDelegate() {
        return this.store;
    }

    @Override
    public SMap<K, V> clone() {
        return SMap.super.clone();
    }
}
