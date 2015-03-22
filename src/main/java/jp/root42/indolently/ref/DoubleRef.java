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
public class DoubleRef
    extends AbstractRef<Double, DoubleRef>
    implements NumberRef<Double, DoubleRef>, Comparable<DoubleRef>, DoubleSupplier, DoubleConsumer {

    private static final long serialVersionUID = -3198217652103277386L;

    /** the value. */
    public volatile double val; // NOPMD

    /**
     * constructor.
     */
    protected DoubleRef() {
        this(0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected DoubleRef(final double val) {
        this.val = val;
    }

    @Override
    public void accept(final Double val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public DoubleRef set(final double val) {
        this.val = val;
        return this;
    }

    @Override
    public Double get() {
        return this.val;
    }

    @Override
    public void accept(final double val) {
        this.val = val;
    }

    @Override
    public double getAsDouble() {
        return this.val;
    }

    @Override
    public int compareTo(final DoubleRef that) {
        return this.get().compareTo(that.get());
    }

    @Override
    public DoubleRef add(final Double val) {
        this.val += val;
        return this;
    }

    @Override
    public DoubleRef mul(final Double val) {
        this.val *= val;
        return this;
    }

    @Override
    public DoubleRef div(final Double val) {
        this.val /= val;
        return this;
    }

    @Override
    public DoubleRef negate() {
        this.val = -this.val;
        return this;
    }
}
