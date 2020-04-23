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

import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

import jp.root42.indolently.function.Statement;


/**
 * @author takahashikzn
 * @version $Id$
 */
@SuppressWarnings("ComparableImplementedButEqualsNotOverridden")
public class $bool
    extends AbstractRef<Boolean, $bool>
    implements Comparable<$bool>, BooleanSupplier {

    private static final long serialVersionUID = 8087914133902951131L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public boolean $; // NOPMD

    /**
     * constructor.
     */
    protected $bool() { this(false); }

    /**
     * constructor.
     *
     * @param $ the value.
     */
    protected $bool(final boolean $) { this.$ = $; }

    @Override
    public void accept(final Boolean $) { this.$ = $; }

    /**
     * set value then return this instance.
     *
     * @param $ value
     * @return {@code this}
     */
    public $bool set(final boolean $) {
        this.$ = $;
        return this;
    }

    @Override
    public Boolean get() { return this.$; }

    @Override
    public boolean getAsBoolean() { return this.$; }

    @Override
    public int compareTo(final $bool that) { return this.compareTo(that.$); }

    public int compareTo(final boolean that) { return Boolean.compare(this.$, that); }

    /**
     * execute the procedure then negate the value if and only if the condition satisfied.
     *
     * @param cond condition
     * @param f a procedure
     */
    public void negateIf(final boolean cond, final Statement f) { this.negateIf(x -> x == cond, f); }

    /**
     * execute the procedure then negate the value if and only if the condition satisfied.
     *
     * @param cond condition
     * @param f a procedure
     */
    public void negateIf(final Predicate<Boolean> cond, final Statement f) {
        this.ifThen(cond, () -> {
            this.$ = !this.$;
            f.run();
        });
    }

    /**
     * execute the procedure if and only if the condition satisfied.
     *
     * @param cond condition
     * @param f a procedure
     */
    public void ifThen(final boolean cond, final Statement f) { this.ifThen(x -> x == cond, f); }

    /**
     * execute the procedure if and only if the condition satisfied.
     *
     * @param cond condition
     * @param f a procedure
     */
    public void ifThen(final Predicate<Boolean> cond, final Statement f) {
        synchronized (this) {
            if (cond.test(this.$)) {
                f.run();
            }
        }
    }
}
