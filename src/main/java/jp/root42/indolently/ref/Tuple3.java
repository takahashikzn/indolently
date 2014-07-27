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

import static jp.root42.indolently.Indolently.*;


/**
 * Three element tuple.
 *
 * @param <F> 1st element type
 * @param <S> 2nd element type
 * @param <T> 3rd element type
 * @author takahashikzn
 */
public class Tuple3<F, S, T>
    implements Serializable {

    private static final long serialVersionUID = 1387913510813532191L;

    /** first element */
    public F fst;

    /** second element */
    public S snd;

    /** third element */
    public T trd;

    /**
     * get 1st element
     *
     * @return 1st element
     */
    public F fst() {
        return this.fst;
    }

    /**
     * set 1st element
     *
     * @param fst 1st element
     * @return {@code this}
     */
    public Tuple3<F, S, T> fst(final F fst) {
        this.fst = fst;
        return this;
    }

    /**
     * get 2nd element
     *
     * @return 2nd element
     */
    public S snd() {
        return this.snd;
    }

    /**
     * set 2nd element
     *
     * @param snd 2st element
     * @return {@code this}
     */
    public Tuple3<F, S, T> snd(final S snd) {
        this.snd = snd;
        return this;
    }

    /**
     * get 3rt element
     *
     * @return 1st element
     */
    public T trd() {
        return this.trd;
    }

    /**
     * set 3rd element
     *
     * @param trd 3rd element
     * @return {@code this}
     */
    public Tuple3<F, S, T> trd(final T trd) {
        this.trd = trd;
        return this;
    }

    /**
     * expand to all combination of two element tuples.
     *
     * @return all combination of two element tuples
     */
    public Tuple3<Tuple2<F, S>, Tuple2<S, T>, Tuple2<F, T>> expand() {

        return tuple( //
            tuple(this.fst, this.snd) //
            , tuple(this.snd, this.trd) //
            , tuple(this.fst, this.trd));
    }

    /**
     * create order reversed tuple.
     *
     * @return newly constructed reversed tuple
     */
    public Tuple3<T, S, F> reverse() {
        return tuple(this.trd, this.snd, this.fst);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ Objects.hash(this.fst, this.snd, this.trd) ^ 42;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof Tuple3)) {
            return false;
        }

        final Tuple3<?, ?, ?> that = (Tuple3<?, ?, ?>) o;

        return equiv(this.fst, that.fst) && equiv(this.snd, that.snd) && equiv(this.trd, that.trd);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", this.fst, this.snd, this.trd);
    }
}
