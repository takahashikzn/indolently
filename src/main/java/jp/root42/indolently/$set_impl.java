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

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.bridge.SetDelegate;
import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Expressive.*;


/**
 * Simple implementation of {@link $set}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
final class $set_impl<T>
    extends SetDelegate<T>
    implements $set_optimized<T>, Serializable {

    @Serial
    private static final long serialVersionUID = 8705188807596442213L;

    private final Set<T> store;

    public $set_impl() { this(ObjFactory.getInstance().newSet()); }

    public $set_impl(final Set<T> store) { this.store = store; }

    @Override
    protected Set<T> getDelegate() { return this.store; }

    @Override
    public $set<T> clone() { return $set_optimized.super.clone(); }

    @Override
    public $iter<T> iterator() { return Indolently.$(this.store.iterator()); }

    private static final Class<?> FROZEN = eval(() -> Class.forName("java.util.Collections$UnmodifiableSet"));

    boolean frozen() { return (this.store instanceof $set_impl) && (($set_impl<?>) this.store).frozen() || this.store.getClass() == FROZEN; }
}

interface $set_optimized<T>
    extends $set<T> {

    @Override
    default $<T> head(final Predicate<? super T> f) { return this.isEmpty() ? $.none() : $set.super.head(f); }

    @Override
    default $<T> last(final Predicate<? super T> f) { return this.isEmpty() ? $.none() : $set.super.last(f); }

    @Override
    default <R> $<R> fhead(final Function<T, $<R>> f) { return this.isEmpty() ? $.none() : $set.super.fhead(f); }

    @Override
    default <R> $<R> flast(final Function<T, $<R>> f) { return this.isEmpty() ? $.none() : $set.super.flast(f); }

    @Override
    default void forEach(final Consumer<? super T> f) { this.eachTry(f::accept); }

    @Override
    default $set<T> clone() { return $set.super.clone(); }
}
