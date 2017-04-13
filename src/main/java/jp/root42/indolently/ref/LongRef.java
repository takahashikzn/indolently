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
 * @version $Id$
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public class LongRef
    extends AbstractRef<Long, LongRef>
    implements NumberRef<Long, LongRef>, Comparable<LongRef>, LongSupplier, LongConsumer {

    private static final long serialVersionUID = -5582550853171233363L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public volatile long val; // NOPMD

    /**
     * constructor.
     */
    protected LongRef() {
        this(0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected LongRef(final long val) {
        this.val = val;
    }

    @Override
    public void accept(final Long val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public LongRef set(final long val) {
        this.val = val;
        return this;
    }

    @Override
    public Long get() {
        return this.val;
    }

    @Override
    public void accept(final long val) {
        this.val = val;
    }

    @Override
    public long getAsLong() {
        return this.val;
    }

    @Override
    public int compareTo(final LongRef that) {
        return this.get().compareTo(that.get());
    }

    @Override
    public LongRef add(final Long val) {
        this.val += val;
        return this;
    }

    @Override
    public LongRef mul(final Long val) {
        this.val *= val;
        return this;
    }

    @Override
    public LongRef div(final Long val) {
        this.val /= val;
        return this;
    }

    @Override
    public LongRef negate() {
        this.val = -this.val;
        return this;
    }
}
