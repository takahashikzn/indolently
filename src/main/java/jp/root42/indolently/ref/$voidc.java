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

import jp.root42.indolently.Indolently;


/**
 * @param <T> value type
 * @author takahashikzn
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public final class $voidc<T extends Comparable<T>>
    extends $void<T>
    implements Comparable<ref<? extends T, ? extends ref<? extends T, ?>>> {

    $voidc(final T $) { super($); }

    @Override
    public int compareTo(final ref<? extends T, ? extends ref<? extends T, ?>> that) {
        return this.get().compareTo(that.get());
    }

    public boolean equiv(final T $) { return Indolently.equiv(this.get(), $); }

    public T max(final ref<? extends T, ?> that) { return Indolently.max(this.get(), that.get()); }

    public T max(final T $) { return Indolently.max(this.get(), $); }

    public T min(final ref<? extends T, ?> that) { return Indolently.min(this.get(), that.get()); }

    public T min(final T $) { return Indolently.min(this.get(), $); }
}
