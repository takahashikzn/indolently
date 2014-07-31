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


/**
 * @author takahashikzn
 * @version $Id$
 */
public class BoolRef
    extends AbstractRef<Boolean>
    implements Comparable<BoolRef>, BooleanSupplier {

    private static final long serialVersionUID = 8087914133902951131L;

    /** the value. */
    public volatile boolean val;

    /**
     * constructor.
     */
    protected BoolRef() {
        this(false);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected BoolRef(final boolean val) {
        this.val = val;
    }

    @Override
    public void accept(final Boolean val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public BoolRef set(final boolean val) {
        this.val = val;
        return this;
    }

    @Override
    public Boolean get() {
        return this.val;
    }

    @Override
    public boolean getAsBoolean() {
        return this.val;
    }

    @Override
    public int compareTo(final BoolRef that) {
        return this.get().compareTo(that.get());
    }
}
