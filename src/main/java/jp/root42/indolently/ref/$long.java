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
public class $long
    extends AbstractRef<Long, $long>
    implements NumberRef<Long, $long>, Comparable<$long>, LongSupplier, LongConsumer {

    private static final long serialVersionUID = -5582550853171233363L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public long $; // NOPMD

    /**
     * constructor.
     */
    protected $long() { this(0); }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $long(final long $) { this.$ = $; }

    @Override
    public void accept(final Long $) { this.$ = $; }

    /**
     * set value then return this instance.
     *
     * @param $ value
     * @return {@code this}
     */
    public $long set(final long $) {
        this.$ = $;
        return this;
    }

    @Override
    public Long get() { return this.$; }

    @Override
    public void accept(final long $) { this.$ = $; }

    @Override
    public long getAsLong() { return this.$; }

    @Override
    public int compareTo(final $long that) { return this.get().compareTo(that.get()); }

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
