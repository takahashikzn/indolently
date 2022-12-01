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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import jp.root42.indolently.Destructive;

import static jp.root42.indolently.Indolently.*;


/**
 * Two element record.
 *
 * @param <T1> 1st element type
 * @param <T2> 2nd element type
 * @author takahashikzn
 */
public record $2<T1, T2>(T1 _1, T2 _2)
    implements Elem2<T1, T2>, Serializable {

    public $2<T1, T2> _1(final T1 _1) { return tuple(_1, this._2); }

    public $2<T1, T2> _2(final T2 _2) { return tuple(this._1, _2); }

    public $2<T2, T1> _21() { return tuple(this._2, this._1); }

    public mutable<T1, T2> mutable() { return new mutable<T1, T2>()._1(this._1)._2(this._2); }

    public static final class mutable<T1, T2>
        implements Elem2<T1, T2>, Serializable, BiConsumer<T1, T2>, Supplier<mutable<T1, T2>>,
        Consumer<mutable<T1, T2>> {

        private mutable() { }

        @SuppressWarnings("PublicField")
        public T1 _1; // NOPMD

        @SuppressWarnings("PublicField")
        public T2 _2; // NOPMD

        @Destructive
        @Override
        public void accept(final mutable<T1, T2> that) {
            this._1 = that._1;
            this._2 = that._2;
        }

        @Destructive
        public mutable<T1, T2> set(final mutable<? extends T1, ? extends T2> that) {
            return (that == null) ? this.set(null, null) : this.set(that._1, that._2);
        }

        @Destructive
        public mutable<T1, T2> set(final T1 _1, final T2 _2) {
            this.accept(_1, _2);
            return this;
        }

        @Override
        public mutable<T1, T2> get() { return this; }

        @Destructive
        @Override
        public void accept(final T1 _1, final T2 _2) {
            this._1 = _1;
            this._2 = _2;
        }

        @Override
        public T1 _1() { return this._1; }

        @Destructive
        public mutable<T1, T2> _1(final T1 _1) {
            this._1 = _1;
            return this;
        }

        @Override
        public T2 _2() { return this._2; }

        @Destructive
        public mutable<T1, T2> _2(final T2 _2) {
            this._2 = _2;
            return this;
        }

        public mutable<T2, T1> _21() { return tuple(this._2, this._1).mutable(); }

        public $2<T1, T2> immutable() { return tuple(this._1, this._2); }

        @Override
        public int hashCode() { return Objects.hash(this.getClass(), this._1, this._2); }

        @Override
        public boolean equals(final Object o) {
            return this == o || (o instanceof $2.mutable that && equiv(this._1, that._1) && equiv(this._2, that._2));
        }

        @Override
        public String toString() { return String.format("(%s, %s)", this._1, this._2); }
    }
}
