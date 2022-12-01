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

/**
 * @author takahashikzn
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public final class $short
    extends _ref_num<Short, $short> {

    @SuppressWarnings("PublicField")
    public short $; // NOPMD

    $short(final short $) { /* NOPMD*/this.$ = $; }

    @Override
    public void accept(final Short $) { this.$ = $; }

    public $short set(final short $) { // NOPMD
        this.$ = $;
        return this;
    }

    @Override
    public Short get() { return this.$; }

    @Override
    public short shortValue() { return this.$; }

    @Override
    public int compareTo(final $short that) { return this.compareTo(that.$); }

    public int compareTo(final short that) { return Short.compare(this.$, that); }

    @Override
    public $short add(final Short $) {
        this.$ += $;
        return this;
    }

    @Override
    public $short mul(final Short $) {
        this.$ *= $;
        return this;
    }

    @Override
    public $short div(final Short $) {
        this.$ /= $;
        return this;
    }

    @Override
    public $short negate() {
        this.$ = (short) -this.$; // NOPMD
        return this;
    }
}
