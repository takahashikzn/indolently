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
package jp.root42.indolently.function;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

import jp.root42.indolently.Functional;


/**
 * @param <T> return value type
 * @author takahashikzn
 */
public class Sspplr<T>
    implements Serializable, Supplier<T>, Slambda<Sspplr<T>> {

    private static final long serialVersionUID = 6611508603865244405L;

    private final Function<? super Supplier<T>, ? extends T> body;

    /**
     * constructor
     *
     * @param body function body
     */
    public Sspplr(final Function<? super Supplier<T>, ? extends T> body) {
        this.body = Objects.requireNonNull(body);
    }

    @Override
    public T get() {
        return this.body.apply(this);
    }

    /**
     * return function body.
     *
     * @return function body
     */
    public Function<? super Supplier<T>, ? extends T> body() {
        return this.body;
    }

    @Override
    public Sspplr<T> memoize() {
        return new Sspplr<>(Functional.memoize(this.body));
    }

    @Override
    public String toString() {
        return this.body.toString();
    }

    @Override
    public int hashCode() {
        return this.body.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof Sspplr)) {
            return false;
        }

        final Sspplr<?> that = (Sspplr<?>) o;
        return this.body.equals(that.body);
    }
}
