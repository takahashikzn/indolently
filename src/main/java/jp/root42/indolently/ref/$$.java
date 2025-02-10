// Copyright 2021 takahashikzn
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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;


/**
 * @author takahashikzn
 */
@SuppressWarnings("InstanceofThis")
public sealed interface $$<L, R>
    permits $$.Left, $$.Right {

    final class Left<L, R>
        implements $$<L, R> {

        private final L l;

        private Left(final L l) { this.l = requireNonNull(l); }

        @Override
        public L value() { return this.l(); }

        @Override
        public L l() { return this.l; }

        @Deprecated
        @Override
        public R r() { throw new UnsupportedOperationException(); }

        @Override
        public String toString() { return String.format("Left[%s]", this.l); }
    }

    final class Right<L, R>
        implements $$<L, R> {

        private final R r;

        private Right() { this.r = null; }

        private Right(final R r) { this.r = requireNonNull(r); }

        @Override
        public R value() { return this.r(); }

        @Deprecated
        @Override
        public L l() { throw new UnsupportedOperationException(); }

        @Override
        public R r() {
            if (this.r == null) throw new UnsupportedOperationException();
            return this.r;
        }

        @Override
        public String toString() { return String.format("Right[%s]", this.r); }
    }

    static <L, R> Left<L, R> left(final L l) { return new Left<>(l); }

    static <L, R> Right<L, R> right(final R r) { return new Right<>(r); }

    static <L, R> Right<L, R> rightNone() { return new Right<>(); }

    Object value();

    L l();

    R r();

    default boolean isL() { return this instanceof Left; }

    default boolean isR() { return this instanceof Right; }

    default <T> T flat(final Function<L, ? extends T> lfn, final Function<R, ? extends T> rfn) {
        return this instanceof Left<?, ?> ? lfn.apply(this.l()) : rfn.apply(this.r());
    }

    default <L2, R2> $$<L2, R2> map(final Function<L, ? extends L2> lfn, final Function<R, ? extends R2> rfn) {
        return this instanceof Left<?, ?> ? left(lfn.apply(this.l())) : right(rfn.apply(this.r()));
    }

    default $$<L, R> do_(final Consumer<L> lfn, final Consumer<R> rfn) {
        if (this instanceof Left<?, ?>) lfn.accept(this.l());
        else rfn.accept(this.r());

        return this;
    }

    // alias
    default $$<L, R> tap(final Consumer<L> lfn, final Consumer<R> rfn) { return this.do_(lfn, rfn); }

    default <T> $<T> lmap(final Function<L, T> fn) { return this instanceof Left ? $.of(this.l()).map(fn) : $.none(); }

    default <T> $<T> rmap(final Function<R, T> fn) { return this instanceof Right ? $.of(this.r()).map(fn) : $.none(); }

    default boolean ltest(final Predicate<L> fn) { return this instanceof Left && fn.test(this.l()); }

    default boolean rtest(final Predicate<R> fn) { return this instanceof Right && fn.test(this.r()); }

    default boolean test(final Predicate<L> lfn, final Predicate<R> rfn) { return this instanceof Left ? this.ltest(lfn) : this.rtest(rfn); }
}
