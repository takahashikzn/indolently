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
 * @version $Id$
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public class FloatRef
    extends AbstractRef<Float, FloatRef>
    implements NumberRef<Float, FloatRef>, Comparable<FloatRef> {

    private static final long serialVersionUID = -6479571510369831870L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public volatile float val; // NOPMD

    /**
     * constructor.
     */
    protected FloatRef() {
        this(0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected FloatRef(final float val) {
        this.val = val;
    }

    @Override
    public void accept(final Float val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public FloatRef set(final float val) {
        this.val = val;
        return this;
    }

    @Override
    public Float get() {
        return this.val;
    }

    @Override
    public int compareTo(final FloatRef that) {
        return this.get().compareTo(that.get());
    }

    @Override
    public FloatRef add(final Float val) {
        this.val += val;
        return this;
    }

    @Override
    public FloatRef mul(final Float val) {
        this.val *= val;
        return this;
    }

    @Override
    public FloatRef div(final Float val) {
        this.val /= val;
        return this;
    }

    @Override
    public FloatRef negate() {
        this.val = -this.val;
        return this;
    }
}
