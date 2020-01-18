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
public class $byte
    extends AbstractRef<Byte, $byte>
    implements NumberRef<Byte, $byte>, Comparable<$byte> {

    private static final long serialVersionUID = -6479571510369831870L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public byte $; // NOPMD

    /**
     * constructor.
     */
    protected $byte() { this((byte) 0); }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $byte(final byte $) { this.$ = $; }

    @Override
    public void accept(final Byte ＄) { this.$ = ＄; }

    /**
     * set value then return this instance.
     *
     * @param ＄ value
     * @return {@code this}
     */
    public $byte set(final byte ＄) {
        this.$ = ＄;
        return this;
    }

    @Override
    public Byte get() { return this.$; }

    @Override
    public int compareTo(final $byte that) { return this.compareTo(that.$); }

    public int compareTo(final byte that) { return Byte.compare(this.$, that); }

    @Override
    public $byte add(final Byte ＄) {
        this.$ += ＄;
        return this;
    }

    @Override
    public $byte mul(final Byte ＄) {
        this.$ *= ＄;
        return this;
    }

    @Override
    public $byte div(final Byte ＄) {
        this.$ /= ＄;
        return this;
    }

    @Override
    public $byte negate() {
        this.$ = (byte) -this.$;
        return this;
    }
}
