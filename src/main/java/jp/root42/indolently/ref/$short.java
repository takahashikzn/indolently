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
public class $short
    extends AbstractRef<Short, $short>
    implements NumberRef<Short, $short>, Comparable<$short> {

    private static final long serialVersionUID = -6819998391641172785L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public short $; // NOPMD

    /**
     * constructor.
     */
    protected $short() { this((short) 0); /* NOPMD*/ }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $short(final short $) { /* NOPMD*/this.$ = $; }

    @Override
    public void accept(final Short $) { this.$ = $; }

    /**
     * set value then return this instance.
     *
     * @param $ value
     * @return {@code this}
     */
    public $short set(final short $) { // NOPMD
        this.$ = $;
        return this;
    }

    @Override
    public Short get() { return this.$; }

    @Override
    public int compareTo(final $short that) { return this.get().compareTo(that.get()); }

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
