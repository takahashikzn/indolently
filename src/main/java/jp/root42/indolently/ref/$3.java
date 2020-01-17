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
import java.util.function.Supplier;

import jp.root42.indolently.Destructive;
import jp.root42.indolently.function.Consumer3;

import static jp.root42.indolently.Indolently.*;


/**
 * Three element tuple.
 *
 * @param <T1> 1st element type
 * @param <T2> 2nd element type
 * @param <T3> 3rd element type
 * @author takahashikzn
 */
public class $3<T1, T2, T3>
    implements Serializable, Consumer3<T1, T2, T3>, Supplier<$3<T1, T2, T3>>, Consumer<$3<T1, T2, T3>> {

    private static final long serialVersionUID = 1387913510813532191L;

    /** first element */
    @SuppressWarnings("PublicField")
    public volatile T1 _1; // NOPMD

    /** second element */
    @SuppressWarnings("PublicField")
    public volatile T2 _2; // NOPMD

    /** third element */
    @SuppressWarnings("PublicField")
    public volatile T3 _3; // NOPMD

    @Destructive
    @Override
    public void accept(final $3<T1, T2, T3> that) {
        this._1 = that._1;
        this._2 = that._2;
        this._3 = that._3;
    }

    /**
     * set elements then return {@code this} instance.
     *
     * @param that the element supplier
     * @return {@code this} instance
     */
    @Destructive
    public $3<T1, T2, T3> set(final $3<? extends T1, ? extends T2, ? extends T3> that) {
        return (that == null) ? this.set(null, null, null) : this.set(that._1, that._2, that._3);
    }

    /**
     * set elements then return {@code this} instance.
     *
     * @param _1 first element
     * @param _2 second element
     * @param _3 third element
     * @return {@code this} instance
     */
    @Destructive
    public $3<T1, T2, T3> set(final T1 _1, final T2 _2, final T3 _3) {
        this.accept(_1, _2, _3);
        return this;
    }

    @Override
    public $3<T1, T2, T3> get() { return this;}

    @Destructive
    @Override
    public void accept(final T1 _1, final T2 _2, final T3 _3) {
        this._1 = _1;
        this._2 = _2;
        this._3 = _3;
    }

    /**
     * get 1st element
     *
     * @return 1st element
     */
    public T1 _1() { return this._1;}

    /**
     * set 1st element
     *
     * @param _1 1st element
     * @return {@code this}
     */
    @Destructive
    public $3<T1, T2, T3> _1(final T1 _1) {
        this._1 = _1;
        return this;
    }

    /**
     * get 2nd element
     *
     * @return 2nd element
     */
    public T2 _2() { return this._2; }

    /**
     * set 2nd element
     *
     * @param _2 2st element
     * @return {@code this}
     */
    @Destructive
    public $3<T1, T2, T3> _2(final T2 _2) {
        this._2 = _2;
        return this;
    }

    /**
     * get 3rt element
     *
     * @return 1st element
     */
    public T3 _3() { return this._3; }

    /**
     * set 3rd element
     *
     * @param _3 3rd element
     * @return {@code this}
     */
    @Destructive
    public $3<T1, T2, T3> _3(final T3 _3) {
        this._3 = _3;
        return this;
    }

    /**
     * expand to all combination of two element tuples.
     *
     * @return all combination of two element tuples
     */
    public $3<$2<T1, T2>, $2<T2, T3>, $2<T1, T3>> _12_23_31() {
        return tuple(this._12(), this._23(), this._13());
    }

    /**
     * get duo of first and second element.
     *
     * @return duo of first and second element
     */
    public $2<T1, T2> _12() { return tuple(this._1, this._2); }

    /**
     * get duo of second and third element.
     *
     * @return duo of second and third element
     */
    public $2<T2, T3> _23() { return tuple(this._2, this._3); }

    /**
     * get duo of first and third element.
     *
     * @return duo of first and third element
     */
    public $2<T1, T3> _13() { return tuple(this._1, this._3); }

    /**
     * set first and second element the return {@code this} instance.
     *
     * @param t first and second element
     * @return {@code this} instance
     */
    @Destructive
    public $3<T1, T2, T3> _12(final $2<T1, T2> t) {
        this._1 = t._1;
        this._2 = t._2;
        return this;
    }

    /**
     * set second and third element the return {@code this} instance.
     *
     * @param t second and third element
     * @return {@code this} instance
     */
    @Destructive
    public $3<T1, T2, T3> _23(final $2<T2, T3> t) {
        this._2 = t._1;
        this._3 = t._2;
        return this;
    }

    /**
     * set first and third element the return {@code this} instance.
     *
     * @param t first and third element
     * @return {@code this} instance
     */
    @Destructive
    public $3<T1, T2, T3> _13(final $2<T1, T3> t) {
        this._1 = t._1;
        this._3 = t._2;
        return this;
    }

    /**
     * create order reversed tuple.
     *
     * @return newly constructed reversed tuple
     */
    public $3<T3, T2, T1> _321() { return tuple(this._3, this._2, this._1); }

    /**
     * rotate this tuple.
     *
     * @return rotated tuple
     */
    public $3<T3, T1, T2> _312() { return tuple(this._3, this._1, this._2); }

    @Override
    public int hashCode() { return Objects.hash(this.getClass(), this._1, this._2, this._3); }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof $3)) {
            return false;
        }

        final $3<?, ?, ?> that = cast(o);

        return equiv(this._1, that._1) && equiv(this._2, that._2) && equiv(this._3, that._3);
    }

    @Override
    public String toString() { return String.format("(%s, %s, %s)", this._1, this._2, this._3); }
}
