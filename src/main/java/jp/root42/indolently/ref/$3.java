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

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import jp.root42.indolently.Destructive;
import jp.root42.indolently.function.Consumer3;
import jp.root42.indolently.function.Function3;

import static jp.root42.indolently.Indolently.*;


/**
 * Three element record.
 *
 * @param <T1> 1st element type
 * @param <T2> 2nd element type
 * @param <T3> 3rd element type
 * @author takahashikzn
 */
public record $3<T1, T2, T3>(T1 _1, T2 _2, T3 _3)
    implements tpl3<$3<T1, T2, T3>, T1, T2, T3>, Serializable {

    public $2<T1, T2> _12() { return tuple(this._1, this._2); }

    public $2<T2, T3> _23() { return tuple(this._2, this._3); }

    public $2<T1, T3> _13() { return tuple(this._1, this._3); }

    public $3<T3, T2, T1> _321() { return tuple(this._3, this._2, this._1); }

    public $3<T3, T1, T2> _312() { return tuple(this._3, this._1, this._2); }

    public mut<T1, T2, T3> mut() { return new mut<T1, T2, T3>().set(this._1, this._2, this._3); }

    @Override
    public <S1, S2, S3> $3<S1, S2, S3> map(
        final Function3<T1, T2, T3, ? extends tpl3<?, ? extends S1, ? extends S2, ? extends S3>> f) {

        final var x = f.apply(this._1, this._2, this._3);
        return x instanceof $3<?, ?, ?> ? cast(x) : tuple(x._1(), x._2(), x._3());
    }

    @Override
    public <S1, S2, S3> $3<S1, S2, S3> map(final Function<T1, ? extends S1> f1, final Function<T2, ? extends S2> f2,
        final Function<T3, ? extends S3> f3) {

        return this.map((__1, __2, __3) -> tuple(f1.apply(__1), f2.apply(__2), f3.apply(__3)));
    }

    public static final class mut<T1, T2, T3>
        implements tpl3<mut<T1, T2, T3>, T1, T2, T3>, Serializable, Consumer3<T1, T2, T3>,
        Consumer<tpl3<?, T1, T2, T3>> {

        private mut() { }

        @SuppressWarnings("PublicField")
        public T1 _1; // NOPMD

        @SuppressWarnings("PublicField")
        public T2 _2; // NOPMD

        @SuppressWarnings("PublicField")
        public T3 _3; // NOPMD

        @Override
        public <S1, S2, S3> mut<S1, S2, S3> map(
            final Function3<T1, T2, T3, ? extends tpl3<?, ? extends S1, ? extends S2, ? extends S3>> f) {

            final var x = f.apply(this._1, this._2, this._3);
            return x instanceof mut<?, ?, ?> ? cast(x) : new mut<S1, S2, S3>().set(x._1(), x._2(), x._3());
        }

        @Override
        public <S1, S2, S3> mut<S1, S2, S3> map(final Function<T1, ? extends S1> f1,
            final Function<T2, ? extends S2> f2, final Function<T3, ? extends S3> f3) {

            return this.map((__1, __2, __3) -> tuple(f1.apply(__1), f2.apply(__2), f3.apply(__3)));
        }

        @Destructive
        @Override
        public void accept(final tpl3<?, T1, T2, T3> that) {
            this._1 = that._1();
            this._2 = that._2();
            this._3 = that._3();
        }

        @Destructive
        public mut<T1, T2, T3> set(final mut<? extends T1, ? extends T2, ? extends T3> that) {
            return (that == null) ? this.set(null, null, null) : this.set(that._1, that._2, that._3);
        }

        @Destructive
        public mut<T1, T2, T3> set(final T1 _1, final T2 _2, final T3 _3) {
            this.accept(_1, _2, _3);
            return this;
        }

        @Destructive
        @Override
        public void accept(final T1 _1, final T2 _2, final T3 _3) {
            this._1 = _1;
            this._2 = _2;
            this._3 = _3;
        }

        @Override
        public T1 _1() { return this._1; }

        @Destructive
        public mut<T1, T2, T3> _1(final T1 _1) {
            this._1 = _1;
            return this;
        }

        @Override
        public T2 _2() { return this._2; }

        @Destructive
        public mut<T1, T2, T3> _2(final T2 _2) {
            this._2 = _2;
            return this;
        }

        @Override
        public T3 _3() { return this._3; }

        @Destructive
        public mut<T1, T2, T3> _3(final T3 _3) {
            this._3 = _3;
            return this;
        }

        public $2.mut<T1, T2> _12() { return tuple(this._1, this._2).mut(); }

        public $2.mut<T2, T3> _23() { return tuple(this._2, this._3).mut(); }

        public $2.mut<T1, T3> _13() { return tuple(this._1, this._3).mut(); }

        @Destructive
        public mut<T1, T2, T3> _12(final $2.mut<T1, T2> t) {
            this._1 = t._1;
            this._2 = t._2;
            return this;
        }

        @Destructive
        public mut<T1, T2, T3> _23(final $2.mut<T2, T3> t) {
            this._2 = t._1;
            this._3 = t._2;
            return this;
        }

        @Destructive
        public mut<T1, T2, T3> _13(final $2.mut<T1, T3> t) {
            this._1 = t._1;
            this._3 = t._2;
            return this;
        }

        public mut<T3, T2, T1> _321() { return tuple(this._3, this._2, this._1).mut(); }

        public mut<T3, T1, T2> _312() { return tuple(this._3, this._1, this._2).mut(); }

        public $3<T1, T2, T3> immut() { return tuple(this._1, this._2, this._3); }

        @Override
        public int hashCode() { return Objects.hash(this.getClass(), this._1, this._2, this._3); }

        @Override
        public boolean equals(final Object o) {
            return this == o || (o instanceof mut<?, ?, ?> that && this.equiv(that));
        }

        @Override
        public String toString() { return "(%s, %s, %s)".formatted(this._1, this._2, this._3); }
    }
}
