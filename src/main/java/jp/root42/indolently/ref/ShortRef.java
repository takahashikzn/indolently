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
public class ShortRef
    extends AbstractRef<Short, ShortRef>
    implements NumberRef<Short, ShortRef>, Comparable<ShortRef> {

    private static final long serialVersionUID = -6819998391641172785L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public volatile short val; // NOPMD

    /**
     * constructor.
     */
    protected ShortRef() {
        this((short) 0); // NOPMD
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected ShortRef(final short val) { // NOPMD
        this.val = val;
    }

    @Override
    public void accept(final Short val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public ShortRef set(final short val) { // NOPMD
        this.val = val;
        return this;
    }

    @Override
    public Short get() {
        return this.val;
    }

    @Override
    public int compareTo(final ShortRef that) {
        return this.get().compareTo(that.get());
    }

    @Override
    public ShortRef add(final Short val) {
        this.val += val;
        return this;
    }

    @Override
    public ShortRef mul(final Short val) {
        this.val *= val;
        return this;
    }

    @Override
    public ShortRef div(final Short val) {
        this.val /= val;
        return this;
    }

    @Override
    public ShortRef negate() {
        this.val = (short) -this.val; // NOPMD
        return this;
    }
}
