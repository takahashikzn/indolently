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
public class ByteRef
    extends AbstractRef<Byte>
    implements Comparable<ByteRef> {

    private static final long serialVersionUID = -6479571510369831870L;

    /** the value. */
    public byte val;

    /**
     * constructor.
     */
    protected ByteRef() {
        this((byte) 0);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected ByteRef(final byte val) {
        this.val = val;
    }

    @Override
    public void accept(final Byte val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public ByteRef set(final byte val) {
        this.val = val;
        return this;
    }

    @Override
    public Byte get() {
        return this.val;
    }

    @Override
    public int compareTo(final ByteRef that) {
        return this.get().compareTo(that.get());
    }
}
