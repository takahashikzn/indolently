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
package jp.root42.indolently.ref;

import java.io.Serializable;
import java.util.Objects;


/**
 * @param <T> value type
 * @author takahashikzn
 */
abstract class AbstractRef<T, S extends AbstractRef<T, S>>
    implements Serializable, ValueReference<T, S> {

    private static final long serialVersionUID = -1617670706001823922L;

    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ Objects.hashCode(this.get()) ^ 13;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!o.getClass().isInstance(o)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        final AbstractRef<T, ?> that = (AbstractRef<T, ?>) o;

        return Objects.equals(this.get(), that.get());
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", this.getClass().getSimpleName(), this.get());
    }
}
