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

import jp.root42.indolently.Indolently;


/**
 * @param <T> value type
 * @author takahashikzn
 */
public class CmpRef<T extends Comparable<T>>
    extends Ref<T>
    implements Comparable<ValueReference<? extends T, ? extends ValueReference<? extends T, ?>>> {

    private static final long serialVersionUID = 8031677453769730996L;

    /**
     * constructor.
     */
    protected CmpRef() {}

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected CmpRef(final T val) {
        super(val);
    }

    @Override
    public int compareTo(final ValueReference<? extends T, ? extends ValueReference<? extends T, ?>> that) {
        return this.get().compareTo(that.get());
    }

    /**
     * test if value is equivalent or not.
     *
     * @param val value to compare.
     * @return test result
     */
    public boolean equiv(final T val) {
        return Indolently.equiv(this.get(), val);
    }

    /**
     * compute largest value.
     *
     * @param that comparison target
     * @return largest value.
     */
    public T max(final ValueReference<? extends T, ?> that) {
        return Indolently.max(this.get(), that.get());
    }

    /**
     * compute largest value.
     *
     * @param val comparison target
     * @return largest value.
     */
    public T max(final T val) {
        return Indolently.max(this.get(), val);
    }

    /**
     * compute smallest value.
     *
     * @param that comparison target
     * @return smallest value.
     */
    public T min(final ValueReference<? extends T, ?> that) {
        return Indolently.min(this.get(), that.get());
    }

    /**
     * compute smallest value.
     *
     * @param val comparison target
     * @return smallest value.
     */
    public T min(final T val) {
        return Indolently.min(this.get(), val);
    }
}
