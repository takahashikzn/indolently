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

import java.util.function.Function;

import jp.root42.indolently.function.Function3;
import jp.root42.indolently.ref.$;


/**
 * @param <T> -
 * @author takahashikzn
 */
public interface ReducibleIterable<T>
    extends Iterable<T>, Reducible<T> {

    @Override
    default <R> $<R> mapred(final Function<? super T, ? extends R> fm, final Function3<Integer, ? super R, ? super R, ? extends R> fr) {

        final var i = this.iterator();

        if (!i.hasNext()) return $.none();

        R rem = fm.apply(i.next());

        int idx = 0;
        while (i.hasNext()) {
            rem = fr.apply(idx++, rem, fm.apply(i.next()));
        }

        return $.of(rem);
    }

    @Override
    default <R> $<R> reduce(final $<? extends R> initial, final Function3<Integer, ? super R, ? super T, ? extends R> f) {

        var rem = initial.orNull();

        int idx = 0;
        for (final T val: this) {
            rem = f.apply(idx++, rem, val);
        }

        return $.of(rem);
    }
}
