// Copyright 2022 takahashikzn
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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface tpl2<SELF extends tpl2<SELF, T1, T2>, T1, T2> {

    T1 _1();

    T2 _2();

    default $<T1> _1$() { return opt(this._1()); }

    default $<T2> _2$() { return opt(this._2()); }

    <S1, S2> tpl2<?, S1, S2> map(BiFunction<T1, T2, ? extends tpl2<?, ? extends S1, ? extends S2>> f);

    <S1, S2> tpl2<?, S1, S2> map(Function<T1, ? extends S1> f1, Function<T2, ? extends S2> f2);

    default boolean test(final BiPredicate<T1, T2> t) { return t.test(this._1(), this._2()); }

    default SELF tap(final BiConsumer<T1, T2> f) {
        f.accept(this._1(), this._2());
        return cast(this);
    }

    default boolean equiv(final tpl2<?, ?, ?> that) {
        return this == that || (equal(this._1(), that._1()) && equal(this._2(), that._2()));
    }
}
