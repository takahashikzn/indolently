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

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;


/**
 * @author takahashikzn
 * @version $Id$
 */
public class IntRef
    extends AbstractRef<Integer, IntRef>
    implements NumberRef<Integer, IntRef>, Comparable<IntRef>, IntSupplier, IntConsumer {

    private static final long serialVersionUID = -5827553121494604722L;

    /** the value. */
    public volatile int val; // NOPMD

    /**
     * constructor.
     */
    protected IntRef() {
        this(0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected IntRef(final int val) {
        this.val = val;
    }

    @Override
    public void accept(final Integer val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public IntRef set(final int val) {
        this.val = val;
        return this;
    }

    @Override
    public Integer get() {
        return this.val;
    }

    @Override
    public int getAsInt() {
        return this.val;
    }

    @Override
    public void accept(final int val) {
        this.val = val;
    }

    @Override
    public int compareTo(final IntRef that) {
        return this.get().compareTo(that.get());
    }

    @Override
    public IntRef add(final Integer val) {
        this.val += val;
        return this;
    }

    @Override
    public IntRef mul(final Integer val) {
        this.val *= val;
        return this;
    }

    @Override
    public IntRef div(final Integer val) {
        this.val /= val;
        return this;
    }

    @Override
    public IntRef negate() {
        this.val = -this.val;
        return this;
    }
}
