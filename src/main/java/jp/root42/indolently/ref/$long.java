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

import java.util.function.LongConsumer;
import java.util.function.LongSupplier;


/**
 * @author takahashikzn
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public final class $long
    extends _ref_num<Long, $long>
    implements LongSupplier, LongConsumer {

    @SuppressWarnings("PublicField")
    public long $; // NOPMD

    $long(final long $) { this.$ = $; }

    @Override
    public void accept(final Long $) { this.$ = $; }

    public $long set(final long $) {
        this.$ = $;
        return this;
    }

    @Override
    public Long get() { return this.$; }

    @Override
    public long longValue() { return this.$; }

    @Override
    public void accept(final long $) { this.$ = $; }

    @Override
    public long getAsLong() { return this.$; }

    @Override
    public int compareTo(final $long that) { return this.compareTo(that.$); }

    public int compareTo(final long that) { return Long.compare(this.$, that); }

    @Override
    public $long add(final Long $) {
        this.$ += $;
        return this;
    }

    @Override
    public $long mul(final Long $) {
        this.$ *= $;
        return this;
    }

    @Override
    public $long div(final Long $) {
        this.$ /= $;
        return this;
    }

    @Override
    public $long negate() {
        this.$ = -this.$;
        return this;
    }
}
