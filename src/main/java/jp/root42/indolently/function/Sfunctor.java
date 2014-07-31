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
package jp.root42.indolently.function;

/**
 * @param <SELF> type of this function
 * @author takahashikzn
 */
public interface Sfunctor<SELF extends Sfunctor<SELF>> {

    /**
     * create memoized version of this function.
     *
     * @return memoized version of this function
     */
    SELF memoize();

    /**
     * create synchronized version of this function.
     *
     * @return synchronized version of this function
     */
    default SELF synchronize() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
