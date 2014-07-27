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
public class LongRef
    extends AbstractRef<Long>
    implements Comparable<LongRef> {

    private static final long serialVersionUID = -5582550853171233363L;

    /** the value. */
    public long val;

    /**
     * constructor.
     */
    public LongRef() {
        this(0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    public LongRef(final long val) {
        this.val = val;
    }

    @Override
    public void accept(final Long val) {
        this.val = val;
    }

    @Override
    public Long get() {
        return this.val;
    }

    @Override
    public int compareTo(final LongRef that) {
        return this.get().compareTo(that.get());
    }
}
