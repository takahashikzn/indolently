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
public class $char
    extends AbstractRef<Character, $char>
    implements Comparable<$char> {

    private static final long serialVersionUID = 6739224434382670429L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public char $; // NOPMD

    /**
     * constructor.
     */
    protected $char() { this((char) 0); }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $char(final char $) { this.$ = $; }

    @Override
    public void accept(final Character $) { this.$ = $; }

    /**
     * set value then return this instance.
     *
     * @param $ value
     * @return {@code this}
     */
    public $char set(final char $) {
        this.$ = $;
        return this;
    }

    @Override
    public Character get() { return this.$; }

    @Override
    public int compareTo(final $char that) { return this.compareTo(that.$); }

    public int compareTo(final char that) { return Character.compare(this.$, that); }
}
