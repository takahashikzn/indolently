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
import java.util.ArrayList;
import java.util.List;


/**
 * Serializable wrapper of List.
 *
 * @author takahashikzn
 */
abstract class SerializableList<T>
    extends ListDelegate<T>
    implements Serializable {

    private static final long serialVersionUID = -2690931773619464155L;

    private List<T> list;

    public SerializableList(final List<T> list) {
        this.list = this.newList();
    }

    protected abstract List<T> newList();

    @Override
    protected List<T> getDelegate() {
        return this.list;
    }

    private void writeObject(final ObjectOutputStream out) throws IOException {
        out.writeObject(new ArrayList<>(this));
    }

    @SuppressWarnings("unchecked")
    private void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.list = this.newList();
        this.list.addAll((List<T>) in.readObject());
    }
}
