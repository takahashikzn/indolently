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
 * @param <T> value type
 * @author takahashikzn
 */
public class Ref<T>
    extends AbstractRef<T, Ref<T>> {

    private static final long serialVersionUID = 2548417883489580934L;

    /** the value. */
    @SuppressWarnings("PublicField")
    public volatile T val; // NOPMD

    /**
     * constructor.
     */
    protected Ref() {
        this(null);
    }

    /**
     * constructor.
     *
     * @param val the value.
     */
    protected Ref(final T val) {
        this.val = val;
    }

    @Override
    public void accept(final T val) {
        this.val = val;
    }

    /**
     * set value then return this instance.
     *
     * @param val value
     * @return {@code this}
     */
    public Ref<T> set(final T val) {
        this.val = val;
        return this;
    }

    @Override
    public T get() {
        return this.val;
    }
}
