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

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.function.Expression;
import jp.root42.indolently.function.SBiFunc;
import jp.root42.indolently.function.SBiPred;
import jp.root42.indolently.function.SBoolSuppl;
import jp.root42.indolently.function.SFunc;
import jp.root42.indolently.function.SPred;
import jp.root42.indolently.function.SSuppl;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.function.TriConsumer;
import jp.root42.indolently.function.TriFunction;
import jp.root42.indolently.function.TriPredicate;
import jp.root42.indolently.ref.BoolRef;
import jp.root42.indolently.ref.Duo;
import jp.root42.indolently.ref.Trio;

import static java.util.Objects.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
@SuppressWarnings("javadoc")
public class Functional {

    protected Functional() {}

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

    public static <T> BooleanSupplier curry(final Predicate<? super T> f, final T t) {
        return () -> f.test(t);
    }

    public static <T, U> Predicate<U> curry(final BiPredicate<? super T, ? super U> f, final T t) {
        return x -> f.test(t, x);
    }

    public static <T, U, V> BiPredicate<U, V> curry(final TriPredicate<? super T, ? super U, ? super V> f, final T t) {
        return (u, v) -> f.test(t, u, v);
    }

    public static <T> Supplier<T> memoize(final Supplier<? extends T> f) {

        // DON'T INLINE THIS
        // to avoid compilation error
        final Function<Object, T> g = x -> f.get();

        return curry(memoize(g), null);
    }

    public static <T, R> Function<T, R> memoize(final Function<? super T, ? extends R> f) {

        final SMap<T, R> memo = map();

        synchronized (memo) {
            return x -> (memo.containsKey(x) ? memo : memo.push(x, f.apply(x))).get(x);
        }
    }

    public static <T, U, R> BiFunction<T, U, R> memoize(final BiFunction<? super T, ? super U, ? extends R> f) {

        final SMap<Duo<T, U>, R> memo = map();

        return (x, y) -> {

            final Duo<T, U> key = tuple(x, y);

            synchronized (memo) {
                return (memo.containsKey(key) ? memo : memo.push(key, f.apply(x, y))).get(key);
            }
        };
    }

    public static <T, U, V, R> TriFunction<T, U, V, R> memoize(
        final TriFunction<? super T, ? super U, ? super V, ? extends R> f) {

        final SMap<Trio<T, U, V>, R> memo = map();

        return (x, y, z) -> {

            final Trio<T, U, V> key = tuple(x, y, z);

            synchronized (memo) {
                return (memo.containsKey(key) ? memo : memo.push(key, f.apply(x, y, z))).get(key);
            }
        };
    }

    public static <T> Predicate<T> memoize(final Predicate<? super T> f) {

        final Function<T, Boolean> memoized = memoize(functionOf((final T x) -> f.test(x)));

        return memoized::apply;
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

    public static SBoolSuppl boolsuppl(final Consumer<? super BooleanSupplier> init,
        final Predicate<? super BooleanSupplier> body) {
        return function(init, body);
    }

    public static SBoolSuppl function(final Consumer<? super BooleanSupplier> init,
        final Predicate<? super BooleanSupplier> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SBoolSuppl(self -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self);
        } );
    }

    public static <T> SSuppl<T> suppl(final Consumer<? super Supplier<T>> init,
        final Function<? super Supplier<? extends T>, ? extends T> body) {
        return function(init, body);
    }

    public static <T> SSuppl<T> function(final Consumer<? super Supplier<T>> init,
        final Function<? super Supplier<? extends T>, ? extends T> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SSuppl<>(self -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self);
        } );
    }

    public static <T, R> SFunc<T, R> func(final Consumer<? super Function<T, R>> init,
        final BiFunction<? super Function<? super T, ? extends R>, ? super T, ? extends R> body) {

        return function(init, body);
    }

    public static <T, R> SFunc<T, R> function(final Consumer<? super Function<T, R>> init,
        final BiFunction<? super Function<? super T, ? extends R>, ? super T, ? extends R> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SFunc<>((self, x) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x);
        } );
    }

    public static <T, U, R> SBiFunc<T, U, R> bifunc(final Consumer<? super BiFunction<T, U, R>> init,
        final TriFunction<? super BiFunction<? super T, ? super U, ? extends R>, ? super T, ? super U, ? extends R> body) {

        return function(init, body);
    }

    public static <T, U, R> SBiFunc<T, U, R> function(final Consumer<? super BiFunction<T, U, R>> init,
        final TriFunction<? super BiFunction<? super T, ? super U, ? extends R>, ? super T, ? super U, ? extends R> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SBiFunc<>((self, x, y) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x, y);
        } );
    }

    public static <T> SPred<T> pred(final Consumer<? super Predicate<T>> init,
        final BiPredicate<? super Predicate<T>, ? super T> body) {

        return function(init, body);
    }

    public static <T> SPred<T> function(final Consumer<? super Predicate<T>> init,
        final BiPredicate<? super Predicate<T>, ? super T> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SPred<>((self, x) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self, x);
        } );
    }

    public static <T, U> SBiPred<T, U> bipred(final Consumer<? super BiPredicate<T, U>> init,
        final TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> body) {

        return function(init, body);
    }

    public static <T, U> SBiPred<T, U> function(final Consumer<? super BiPredicate<T, U>> init,
        final TriPredicate<? super BiPredicate<T, U>, ? super T, ? super U> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SBiPred<>((self, x, y) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self, x, y);
        } );
    }

    public static <T> Expression<T> expressionOf(final Expression<T> f) {
        return f;
    }

    public static Statement statementOf(final Statement f) {
        return f;
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
