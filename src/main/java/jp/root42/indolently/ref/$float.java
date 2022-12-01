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
public final class $float
    extends _ref_num<Float, $float> {

    @SuppressWarnings("PublicField")
    public float $; // NOPMD

    $float(final float $) { this.$ = $; }

    @Override
    public void accept(final Float $) { this.$ = $; }

    public $float set(final float $) {
        this.$ = $;
        return this;
    }

    @Override
    public Float get() { return this.$; }

    @Override
    public float floatValue() { return this.$; }

    @Override
    public int compareTo(final $float that) { return this.compareTo(that.$); }

    public int compareTo(final float that) { return Float.compare(this.$, that); }

    @Override
    public $float add(final Float $) {
        this.$ += $;
        return this;
    }

    @Override
    public $float mul(final Float $) {
        this.$ *= $;
        return this;
    }

    @Override
    public $float div(final Float $) {
        this.$ /= $;
        return this;
    }

    @Override
    public $float negate() {
        this.$ = -this.$;
        return this;
    }
}
