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

import java.util.function.Function;

import jp.root42.indolently.function.Consumer3;
import jp.root42.indolently.function.Function3;
import jp.root42.indolently.function.Predicate3;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public interface tpl3<SELF extends tpl3<SELF, T1, T2, T3>, T1, T2, T3> {

    T1 _1();

    T2 _2();

    T3 _3();

    default $<T1> _1$() { return opt(this._1()); }

    default $<T2> _2$() { return opt(this._2()); }

    default $<T3> _3$() { return opt(this._3()); }

    <S1, S2, S3> tpl3<?, S1, S2, S3> map(
        Function3<T1, T2, T3, ? extends tpl3<?, ? extends S1, ? extends S2, ? extends S3>> f);

    <S1, S2, S3> tpl3<?, S1, S2, S3> map(Function<T1, ? extends S1> f1, Function<T2, ? extends S2> f2,
        Function<T3, ? extends S3> f3);

    default boolean test(final Predicate3<T1, T2, T3> t) { return t.test(this._1(), this._2(), this._3()); }

    default SELF tap(final Consumer3<T1, T2, T3> f) {
        f.accept(this._1(), this._2(), this._3());
        return cast(this);
    }

    default boolean equiv(final tpl3<?, ?, ?, ?> that) {
        return this == that || (equal(this._1(), that._1()) && equal(this._2(), that._2()) && equal(this._3(),
            that._3()));
    }
}
