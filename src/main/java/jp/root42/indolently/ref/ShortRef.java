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

import java.io.Serializable;


/**
 * @author takahashikzn
 * @version $Id$
 */
public class ShortRef
    extends AbstractRef<Short>
    implements Serializable {

    private static final long serialVersionUID = -6819998391641172785L;

    /** the value. */
    public short val;

    /**
     * constructor.
     */
    public ShortRef() {
        this((short) 0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    public ShortRef(final short val) {
        this.val = val;
    }

    @Override
    public void accept(final Short val) {
        this.val = val;
    }

    @Override
    public Short get() {
        return this.val;
    }
}
