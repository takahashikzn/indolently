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

import static jp.root42.indolently.Indolently.*;


/**
 * Two element tuple.
 *
 * @param <F> 1st element type
 * @param <S> 2nd element type
 * @author takahashikzn
 */
public class Tuple2<F, S>
    implements Serializable, BiConsumer<F, S> {

    private static final long serialVersionUID = 4058877644750960140L;

    /** first element */
    public F fst;

    /** second element */
    public S snd;

    @Override
    public void accept(final F fst, final S snd) {
        this.fst = fst;
        this.snd = snd;
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
    public Tuple2<F, S> fst(final F fst) {
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
    public Tuple2<F, S> snd(final S snd) {
        this.snd = snd;
        return this;
    }

    /**
     * create order reversed tuple.
     *
     * @return newly constructed reversed tuple
     */
    public Tuple2<S, F> reverse() {
        return tuple(this.snd, this.fst);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode() ^ Objects.hash(this.fst, this.snd) ^ 42;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof Tuple2)) {
            return false;
        }

        final Tuple2<?, ?> that = (Tuple2<?, ?>) o;

        return equiv(this.fst, that.fst) && equiv(this.snd, that.snd);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.fst, this.snd);
    }
}
