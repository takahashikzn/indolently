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
 * Two element tuple.
 *
 * @param <F> 1st element type
 * @param <S> 2nd element type
 * @author takahashikzn
 */
public class Duo<F, S>
    implements Serializable, BiConsumer<F, S>, Supplier<Duo<F, S>>, Consumer<Duo<F, S>> {

    private static final long serialVersionUID = 4058877644750960140L;

    /** first element */
    @SuppressWarnings("PublicField")
    public volatile F fst; // NOPMD

    /** second element */
    @SuppressWarnings("PublicField")
    public volatile S snd; // NOPMD

    @Destructive
    @Override
    public void accept(final Duo<F, S> that) {
        this.fst = that.fst;
        this.snd = that.snd;
    }

    /**
     * set elements then return {@code this} instance.
     *
     * @param that the element supplier
     * @return {@code this} instance
     */
    @Destructive
    public Duo<F, S> set(final Duo<? extends F, ? extends S> that) {
        return (that == null) ? this.set(null, null) : this.set(that.fst, that.snd);
    }

    /**
     * set elements then return {@code this} instance.
     *
     * @param fst first element
     * @param snd second element
     * @return {@code this} instance
     */
    @Destructive
    public Duo<F, S> set(final F fst, final S snd) {
        this.accept(fst, snd);
        return this;
    }

    @Override
    public Duo<F, S> get() {
        return this;
    }

    @Destructive
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
    @Destructive
    public Duo<F, S> fst(final F fst) {
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
    @Destructive
    public Duo<F, S> snd(final S snd) {
        this.snd = snd;
        return this;
    }

    /**
     * create order reversed tuple.
     *
     * @return newly constructed reversed tuple
     */
    public Duo<S, F> reverse() {
        return tuple(this.snd, this.fst);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.fst, this.snd);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof Duo)) {
            return false;
        }

        final Duo<?, ?> that = cast(o);

        return equiv(this.fst, that.fst) && equiv(this.snd, that.snd);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", this.fst, this.snd);
    }
}
