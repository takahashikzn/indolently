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
public class DoubleRef
    extends AbstractRef<Double>
    implements Comparable<DoubleRef> {

    private static final long serialVersionUID = -3198217652103277386L;

    /** the value. */
    public double val;

    /**
     * constructor.
     */
    public DoubleRef() {
        this(0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    public DoubleRef(final double val) {
        this.val = val;
    }

    @Override
    public void accept(final Double val) {
        this.val = val;
    }

    @Override
    public Double get() {
        return this.val;
    }

    @Override
    public int compareTo(final DoubleRef that) {
        return this.get().compareTo(that.get());
    }
}
