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

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;


/**
 * @author takahashikzn
 */
@SuppressWarnings("InstanceofThis")
public sealed interface $$<L, R>
    extends Serializable
    permits $$.Left, $$.Right {

    record Left<L, R>(L val)
        implements $$<L, R>, Supplier<L> {

        @Deprecated
        public Left(final L val) { this.val = requireNonNull(val); }

        @Override
        public L get() { return this.val(); }

        @Override
        public L val() { return this.l(); }

        @Override
        public L l() { return this.val; }

        @Deprecated
        @Override
        public R r() { throw new NoSuchElementException(); }

        @Override
        public String toString() { return "Left[%s]".formatted(this.val); }
    }

    record Right<L, R>(R val)
        implements $$<L, R>, Supplier<R> {

        @Deprecated
        public Right(final R val) { this.val = requireNonNull(val); }

        @Override
        public R get() { return this.val(); }

        @Override
        public R val() { return this.r(); }

        @Deprecated
        @Override
        public L l() { throw new NoSuchElementException(); }

        @Override
        public R r() { return this.val; }

        @Override
        public String toString() { return "Right[%s]".formatted(this.val); }
    }

    static <L, R> Left<L, R> left(final L l) { return new Left<>(l); }

    static <L, R> Right<L, R> right(final R r) { return new Right<>(r); }

    Object val();

    L l();

    R r();

    default boolean isL() { return this instanceof Left; }

    default boolean isR() { return this instanceof Right; }

    default <T> T flat(final Function<L, ? extends T> lfn, final Function<R, ? extends T> rfn) {
        return switch (this) {
            case Left<L, ?>(var l) -> lfn.apply(l);
            case Right<?, R>(var r) -> rfn.apply(r);
        };
    }

    default $$<R, L> swap() {
        return switch (this) {
            case Left<L, ?>(var l) -> right(l);
            case Right<?, R>(var r) -> left(r);
        };
    }

    default <L2, R2> $$<L2, R2> map(final Function<L, ? extends L2> lfn, final Function<R, ? extends R2> rfn) {
        return switch (this) {
            case Left<L, ?>(var l) -> left(lfn.apply(l));
            case Right<?, R>(var r) -> right(rfn.apply(r));
        };
    }

    default $$<L, R> do_(final Consumer<L> lfn, final Consumer<R> rfn) {
        switch (this) {
            case Left<L, ?>(var l) -> lfn.accept(l);
            case Right<?, R>(var r) -> rfn.accept(r);
        }

        return this;
    }

    // alias
    default $$<L, R> tap(final Consumer<L> lfn, final Consumer<R> rfn) { return this.do_(lfn, rfn); }

    default <T> $<T> lmap(final Function<L, T> fn) {
        return switch (this) {
            case Left<L, ?>(var l) -> $.of(l).map(fn);
            case Right<?, R>(var __) -> $.none();
        };
    }

    default <T> $<T> rmap(final Function<R, T> fn) {
        return switch (this) {
            case Left<L, ?>(var __) -> $.none();
            case Right<?, R>(var r) -> $.of(r).map(fn);
        };
    }

    default boolean ltest(final Predicate<L> fn) {
        return switch (this) {
            case Left<L, ?>(var l) -> fn.test(l);
            case Right<?, R>(var __) -> false;
        };
    }

    default boolean rtest(final Predicate<R> fn) {
        return switch (this) {
            case Left<L, ?>(var __) -> false;
            case Right<?, R>(var r) -> fn.test(r);
        };
    }

    default boolean test(final Predicate<L> lfn, final Predicate<R> rfn) {
        return switch (this) {
            case Left<L, ?>(var l) -> lfn.test(l);
            case Right<?, R>(var r) -> rfn.test(r);
        };
    }
}
