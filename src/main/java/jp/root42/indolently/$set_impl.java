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
import java.util.Set;

import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.bridge.SetDelegate;

import static jp.root42.indolently.Expressive.*;


/**
 * Simple implementation of {@link $set}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
final class $set_impl<T>
    extends SetDelegate<T>
    implements $set<T>, Serializable {

    private static final long serialVersionUID = 8705188807596442213L;

    private final Set<T> store;

    public $set_impl() { this(ObjFactory.getInstance().newSet()); }

    public $set_impl(final Set<T> store) { this.store = store; }

    @Override
    protected Set<T> getDelegate() { return this.store; }

    @Override
    public $set<T> clone() { return $set.super.clone(); }

    @Override
    public $iter<T> iterator() { return Indolently.$(this.store.iterator()); }

    private static final Class<?> FROZEN = eval(() -> Class.forName("java.util.Collections$UnmodifiableSet"));

    boolean frozen() {
        return (this.store instanceof $set_impl) && (($set_impl<?>) this.store).frozen()
            || this.store.getClass() == FROZEN;
    }
}
