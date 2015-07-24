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

import java.util.function.Consumer;

import jp.root42.indolently.Indolently;


/**
 * Express this instance can be identical.
 *
 * @param <SELF> self type
 * @author takahashikzn
 */
public interface Identical<SELF extends Identical<SELF>> {

    /**
     * return this instance.
     *
     * @return {@code this} instance.
     */
    default SELF identity() {
        return Indolently.cast(this);
    }

    /**
     * return this instance.
     *
     * @param f 'tap' operator
     * @return {@code this} instance.
     */
    default SELF identity(final Consumer<? super SELF> f) {
        final SELF self = Indolently.cast(this);
        f.accept(self);
        return self;
    }
}
