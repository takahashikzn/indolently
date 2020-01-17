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

import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;


/**
 * @author takahashikzn
 * @version $Id$
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public class $double
    extends AbstractRef<Double, $double>
    implements NumberRef<Double, $double>, Comparable<$double>, DoubleSupplier, DoubleConsumer {

    private static final long serialVersionUID = -3198217652103277386L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public double $; // NOPMD

    /**
     * constructor.
     */
    protected $double() { this(0); }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $double(final double $) { this.$ = $; }

    @Override
    public void accept(final Double $) { this.$ = $; }

    /**
     * set value then return this instance.
     *
     * @param $ value
     * @return {@code this}
     */
    public $double set(final double $) {
        this.$ = $;
        return this;
    }

    @Override
    public Double get() { return this.$; }

    @Override
    public void accept(final double $) { this.$ = $; }

    @Override
    public double getAsDouble() { return this.$; }

    @Override
    public int compareTo(final $double that) { return this.get().compareTo(that.get()); }

    @Override
    public $double add(final Double $) {
        this.$ += $;
        return this;
    }

    @Override
    public $double mul(final Double $) {
        this.$ *= $;
        return this;
    }

    @Override
    public $double div(final Double $) {
        this.$ /= $;
        return this;
    }

    @Override
    public $double negate() {
        this.$ = -this.$;
        return this;
    }
}
