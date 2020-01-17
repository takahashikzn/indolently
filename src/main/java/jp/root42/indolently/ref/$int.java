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
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public class $int
    extends AbstractRef<Integer, $int>
    implements NumberRef<Integer, $int>, Comparable<$int>, IntSupplier, IntConsumer {

    private static final long serialVersionUID = -5827553121494604722L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public int $; // NOPMD

    /**
     * constructor.
     */
    protected $int() { this(0); }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $int(final int $) { this.$ = $; }

    @Override
    public void accept(final Integer $) { this.$ = $; }

    /**
     * set value then return this instance.
     *
     * @param $ value
     * @return {@code this}
     */
    public $int set(final int $) {
        this.$ = $;
        return this;
    }

    @Override
    public Integer get() { return this.$; }

    @Override
    public int getAsInt() { return this.$; }

    @Override
    public void accept(final int $) { this.$ = $; }

    @Override
    public int compareTo(final $int that) { return this.get().compareTo(that.get()); }

    @Override
    public $int add(final Integer $) {
        this.$ += $;
        return this;
    }

    @Override
    public $int mul(final Integer $) {
        this.$ *= $;
        return this;
    }

    @Override
    public $int div(final Integer $) {
        this.$ /= $;
        return this;
    }

    @Override
    public $int negate() {
        this.$ = -this.$;
        return this;
    }
}
