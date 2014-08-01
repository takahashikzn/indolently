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

import java.util.Iterator;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;


/**
 * @param <T> -
 * @author takahashikzn
 */
public interface ReducibleIterable<T>
    extends Iterable<T>, Reducible<T> {

    @Override
    default <R> Optional<R> mapred(final Function<? super T, ? extends R> fm,
        final BiFunction<? super R, ? super R, ? extends R> fr) {

        final Iterator<T> i = iterator();

        if (!i.hasNext()) {
            return Optional.empty();
        }

        return this.mapred( //
            Optional.ofNullable(fm.apply(i.next())), //
            (final R rem, final T val) -> fr.apply(rem, fm.apply(val)));
    }

    @Override
    default <R> Optional<R> mapred(final Optional<? extends R> initial,
        final BiFunction<? super R, ? super T, ? extends R> f) {

        R rem = initial.orElse(null);

        for (final T val : this) {
            rem = f.apply(rem, val);
        }

        return Optional.ofNullable(rem);
    }
}
