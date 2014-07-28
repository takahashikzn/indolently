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
package jp.root42.indolently;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.function.TriConsumer;
import jp.root42.indolently.function.TriFunction;
import jp.root42.indolently.function.TriPredicate;
import jp.root42.indolently.ref.Ref;
import jp.root42.indolently.ref.Tuple2;
import jp.root42.indolently.ref.Tuple3;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
@SuppressWarnings("javadoc")
public class Functional {

    protected Functional() {
    }

    public static <T, R> Supplier<R> curry(final Function<? super T, ? extends R> f, final T t) {
        return () -> f.apply(t);
    }

    public static <T, U, R> Function<U, R> curry(final BiFunction<? super T, ? super U, ? extends R> f, final T t) {
        return x -> f.apply(t, x);
    }

    public static <T, U, V, R> BiFunction<U, V, R> curry(
        final TriFunction<? super T, ? super U, ? super V, ? extends R> f, final T t) {
        return (u, v) -> f.apply(t, u, v);
    }

    public static <T> Supplier<T> memoize(final Supplier<? extends T> f) {

        // DON'T INLINE THIS
        // to avoid compilation error
        final Function<Object, T> g = x -> f.get();

        return curry(memoize(g), null);
    }

    public static <T, R> Function<T, R> memoize(final Function<? super T, ? extends R> f) {

        final Smap<T, R> memo = map();

        return x -> (memo.containsKey(x) ? memo : memo.push(x, f.apply(x))).get(x);
    }

    public static <T, U, R> BiFunction<T, U, R> memoize(final BiFunction<? super T, ? super U, ? extends R> f) {

        final Smap<Tuple2<T, U>, R> memo = map();

        return (x, y) -> {

            final Tuple2<T, U> key = tuple(x, y);

            return (memo.containsKey(key) ? memo : memo.push(key, f.apply(x, y))).get(key);
        };
    }

    public static <T, U, V, R> TriFunction<T, U, V, R> memoize(
        final TriFunction<? super T, ? super U, ? super V, ? extends R> f) {

        final Smap<Tuple3<T, U, V>, R> memo = map();

        return (x, y, z) -> {

            final Tuple3<T, U, V> key = tuple(x, y, z);

            return (memo.containsKey(key) ? memo : memo.push(key, f.apply(x, y, z))).get(key);
        };
    }

    public static <T> Predicate<T> memoize(final Predicate<? super T> f) {

        final Function<T, Boolean> memoized = memoize(functionOf((final T x) -> f.test(x)));

        return x -> memoized.apply(x);
    }

    public static <T, U> BiPredicate<T, U> memoize(final BiPredicate<? super T, ? super U> f) {

        final BiFunction<T, U, Boolean> memoized = memoize(biFunctionOf((final T x, final U y) -> f.test(x, y)));

        return (x, y) -> memoized.apply(x, y);
    }

    public static <T, U, V> TriPredicate<T, U, V> memoize(final TriPredicate<? super T, ? super U, ? super V> f) {

        final TriFunction<T, U, V, Boolean> memoized =
            memoize(triFunctionOf((final T x, final U y, final V z) -> f.test(x, y, z)));

        return (x, y, z) -> memoized.apply(x, y, z);
    }

    public static class FuncSpec
        implements Serializable {

        private static final long serialVersionUID = 8640673561464941017L;

        public boolean memoize;

        public FuncSpec memoize(final boolean memoize) {
            this.memoize = memoize;
            return this;
        }
    }

    public static <T> Supplier<T> function(final Function<? super Supplier<T>, ? extends T> f) {
        return function(f, new FuncSpec());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> Supplier<T> function(final Function<? super Supplier<T>, ? extends T> f, final FuncSpec spec) {

        final Function func = f;
        final Ref<Supplier> self = ref((Supplier) null);

        final Supplier g = () -> func.apply(self.val);

        return self.val = (spec.memoize ? memoize(g) : g);
    }

    public static <T, R> Function<T, R> function(final BiFunction<? super Function<T, R>, ? super T, ? extends R> f) {
        return function(f, new FuncSpec());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T, R> Function<T, R> function(final BiFunction<? super Function<T, R>, ? super T, ? extends R> f,
        final FuncSpec spec) {

        final BiFunction func = f;
        final Ref<Function> self = ref((Function) null);

        final Function g = x -> func.apply(self.val, x);

        return self.val = (spec.memoize ? memoize(g) : g);
    }

    public static <T, U, R> BiFunction<T, U, R> function(
        final TriFunction<? super BiFunction<T, U, R>, ? super T, ? super U, ? extends R> f) {
        return function(f, new FuncSpec());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T, U, R> BiFunction<T, U, R> function(
        final TriFunction<? super BiFunction<T, U, R>, ? super T, ? super U, ? extends R> f, final FuncSpec spec) {

        final TriFunction func = f;
        final Ref<BiFunction> self = ref((BiFunction) null);

        final BiFunction g = (x, y) -> func.apply(self.val, x, y);

        return self.val = spec.memoize ? memoize(g) : g;
    }

    public static <T> Predicate<T> function(final BiPredicate<? super Predicate<T>, ? super T> f) {
        return function(f, new FuncSpec());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> Predicate<T> function(final BiPredicate<? super Predicate<T>, ? super T> f, final FuncSpec spec) {

        final BiPredicate func = f;
        final Ref<Predicate> self = ref((Predicate) null);

        final Predicate g = x -> func.test(self.val, x);

        return self.val = spec.memoize ? memoize(g) : g;
    }

    public static <T, U> BiPredicate<T, U> function(
        final TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> f) {
        return function(f, new FuncSpec());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T, U> BiPredicate<T, U> function(
        final TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> f, final FuncSpec spec) {

        final TriPredicate func = f;
        final Ref<BiPredicate> self = ref((BiPredicate) null);

        final BiPredicate g = (x, y) -> func.test(self.val, x, y);

        return self.val = spec.memoize ? memoize(g) : g;
    }

    public static <T> Supplier<? extends T> supplierOf(final Supplier<? extends T> f) {
        return f;
    }

    public static <T> Consumer<? super T> consumerOf(final Consumer<? super T> f) {
        return f;
    }

    public static <T, U> BiConsumer<? super T, ? super U> biConsumerOf(final BiConsumer<? super T, ? super U> f) {
        return f;
    }

    public static <T, U, V> TriConsumer<? super T, ? super U, ? super V> triConsumerOf(
        final TriConsumer<? super T, ? super U, ? super V> f) {
        return f;
    }

    public static <T, R> Function<? super T, ? extends R> functionOf(final Function<? super T, ? extends R> f) {
        return f;
    }

    public static <T, U, R> BiFunction<? super T, ? super U, ? extends R> biFunctionOf(
        final BiFunction<? super T, ? super U, ? extends R> f) {
        return f;
    }

    public static <T, U, V, R> TriFunction<? super T, ? super U, ? super V, ? extends R> triFunctionOf(
        final TriFunction<? super T, ? super U, ? super V, ? extends R> f) {
        return f;
    }

    public static <T> Predicate<? super T> predicateOf(final Predicate<? super T> f) {
        return f;
    }

    public static <T, U> BiPredicate<? super T, ? super U> biPredicateOf(final BiPredicate<? super T, ? super U> f) {
        return f;
    }

    public static <T, U, V> TriPredicate<? super T, ? super U, ? super V> triPredicateOf(
        final TriPredicate<? super T, ? super U, ? super V> f) {
        return f;
    }
}
