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
package jp.root42.indolently.bridge;

/**
 * @param <T> delegation target type
 * @author takahashikzn
 */
abstract class ObjDelegate<T> {

    protected abstract T getDelegate();

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object o) {
        return (o == this) || this.getDelegate().equals(o);
    }

    @Override
    public int hashCode() {
        return this.getDelegate().hashCode();
    }

    @Override
    public String toString() {
        return this.getDelegate().toString();
    }
}
