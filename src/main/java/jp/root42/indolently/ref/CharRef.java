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
public class CharRef
    extends AbstractRef<Character, CharRef>
    implements Comparable<CharRef> {

    private static final long serialVersionUID = 6739224434382670429L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public volatile char val; // NOPMD

    /**
     * constructor.
     */
    protected CharRef() {
        this((char) 0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected CharRef(final char val) {
        this.val = val;
    }

    @Override
    public void accept(final Character val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public CharRef set(final char val) {
        this.val = val;
        return this;
    }

    @Override
    public Character get() {
        return this.val;
    }

    @Override
    public int compareTo(final CharRef that) {
        return this.get().compareTo(that.get());
    }
}
