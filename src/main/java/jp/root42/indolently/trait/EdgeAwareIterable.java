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
package jp.root42.indolently.trait;

import java.util.NoSuchElementException;


/**
 * @param <T> -
 * @author takahashikzn
 */
public interface EdgeAwareIterable<T>
    extends Iterable<T> {

    /**
     * get first element.
     *
     * @return first element
     * @throws NoSuchElementException if empty
     */
    default T first() {
        return this.iterator().next();
    }

    /**
     * get last element of this iterator.
     *
     * @return last element
     * @throws NoSuchElementException if this iterator is empty
     */
    default T last() {

        T rslt = null;

        for (final T val : this) {
            rslt = val;
        }

        return rslt;
    }
}
