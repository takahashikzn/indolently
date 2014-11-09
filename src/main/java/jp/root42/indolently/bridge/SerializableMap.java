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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Serializable wrapper of Map.
 *
 * @author takahashikzn
 */
abstract class SerializableMap<K, V>
    extends MapDelegate<K, V>
    implements Serializable {

    private static final long serialVersionUID = -624797042693721720L;

    private Map<K, V> map;

    public SerializableMap() {
        this.map = this.newMap();
    }

    protected abstract Map<K, V> newMap();

    @Override
    protected Map<K, V> getDelegate() {
        return this.map;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(new LinkedHashMap<>(this));
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.map = this.newMap();
        this.map.putAll((Map<K, V>) in.readObject());
    }
}
