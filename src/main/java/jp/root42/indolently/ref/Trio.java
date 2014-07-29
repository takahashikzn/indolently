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

import jp.root42.indolently.function.TriConsumer;

import static jp.root42.indolently.Indolently.*;


/**
 * Three element tuple.
 *
 * @param <F> 1st element type
 * @param <S> 2nd element type
 * @param <T> 3rd element type
 * @author takahashikzn
 */
public class Trio<F, S, T>
    implements Serializable, TriConsumer<F, S, T>, Supplier<Trio<F, S, T>>, Consumer<Trio<F, S, T>> {

    private static final long serialVersionUID = 1387913510813532191L;

    /** first element */
    public F fst;

    /** second element */
    public S snd;

    /** third element */
    public T trd;

    @Override
    public void accept(final Trio<F, S, T> that) {
        this.fst = that.fst;
        this.snd = that.snd;
        this.trd = that.trd;
    }

    @Override
    public Trio<F, S, T> get() {
        return this;
    }

    @Override
    public void accept(final F fst, final S snd, final T trd) {
        this.fst = fst;
        this.snd = snd;
        this.trd = trd;
    }

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
    public Trio<F, S, T> fst(final F fst) {
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
    public Trio<F, S, T> snd(final S snd) {
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
    public Trio<F, S, T> trd(final T trd) {
        this.trd = trd;
        return this;
    }

    /**
     * expand to all combination of two element tuples.
     *
     * @return all combination of two element tuples
     */
    public Trio<Duo<F, S>, Duo<S, T>, Duo<F, T>> expand() {

        return tuple(this.fstsnd(), this.sndtrd(), this.fsttrd());
    }

    /**
     * get duo of first and second element.
     *
     * @return duo of first and second element
     */
    public Duo<F, S> fstsnd() {
        return tuple(this.fst, this.snd);
    }

    /**
     * get duo of second and third element.
     *
     * @return duo of second and third element
     */
    public Duo<S, T> sndtrd() {
        return tuple(this.snd, this.trd);
    }

    /**
     * get duo of first and third element.
     *
     * @return duo of first and third element
     */
    public Duo<F, T> fsttrd() {
        return tuple(this.fst, this.trd);
    }

    /**
     * create order reversed tuple.
     *
     * @return newly constructed reversed tuple
     */
    public Trio<T, S, F> reverse() {
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
        } else if (!(o instanceof Trio)) {
            return false;
        }

        final Trio<?, ?, ?> that = (Trio<?, ?, ?>) o;

        return equiv(this.fst, that.fst) && equiv(this.snd, that.snd) && equiv(this.trd, that.trd);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", this.fst, this.snd, this.trd);
    }
}
