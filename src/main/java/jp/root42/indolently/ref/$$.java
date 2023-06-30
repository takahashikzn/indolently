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

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public record $$<L, R>($<L> l, $<R> r) {

    public enum None { NONE }

    public static <L, R> $$<L, R> left(final L l) { return new $$<>($.of(l), none()); }

    public static <L, R> $$<L, R> right(final R r) { return new $$<>(none(), $.of(r)); }

    public boolean isL() { return this.l.present(); }

    public boolean isR() { return this.r.present(); }

    public <T> T map(final Function<L, ? extends T> lfn, final Function<R, ? extends T> rfn) {
        return this.l.map(lfn).or(() -> cast(this.r.map(rfn).get()));
    }

    public $$<L, R> do_(final Consumer<L> lfn, final Consumer<R> rfn) {
        this.l.tap(lfn);
        this.r.tap(rfn);
        return this;
    }

    // alias
    public $$<L, R> tap(final Consumer<L> lfn, final Consumer<R> rfn) { return this.do_(lfn, rfn); }

    public <T> $<T> lmap(final Function<L, T> fn) { return this.l.map(fn); }

    public <T> $<T> rmap(final Function<R, T> fn) { return this.r.map(fn); }

    public boolean ltest(final Predicate<L> fn) { return this.l.test(fn); }

    public boolean rtest(final Predicate<R> fn) { return this.r.test(fn); }

    @Override
    public String toString() { return String.format("Either[%s,%s]", tostr(this.l), tostr(this.r)); }

    private static Object tostr(final $<?> x) { return x.empty() ? "_" : x.get(); }
}
