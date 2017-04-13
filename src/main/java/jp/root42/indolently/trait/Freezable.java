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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Express this instance knows freeze operation.
 *
 * @param <SELF> self type
 * @author takahashikzn
 */
public interface Freezable<SELF extends Freezable<SELF>> {

    /**
     * construct freezed new
     * {@link Collections#unmodifiableList(List) List} /
     * {@link Collections#unmodifiableMap(Map) Map} /
     * {@link Collections#unmodifiableSet(Set) Set}
     * instance.
     * Circular structure is not supported yet.
     *
     * @return freezed new instance
     * @see Collections#unmodifiableList(List)
     * @see Collections#unmodifiableMap(Map)
     * @see Collections#unmodifiableSet(Set)
     */
    SELF freeze();
}
