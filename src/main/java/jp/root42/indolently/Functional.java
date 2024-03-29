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

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.function.Consumer3;
import jp.root42.indolently.function.Expression;
import jp.root42.indolently.function.Function3;
import jp.root42.indolently.function.Function4;
import jp.root42.indolently.function.Predicate3;
import jp.root42.indolently.function.SBoolSuppl;
import jp.root42.indolently.function.SFunc;
import jp.root42.indolently.function.SFunc2;
import jp.root42.indolently.function.SFunc3;
import jp.root42.indolently.function.SPred;
import jp.root42.indolently.function.SPred2;
import jp.root42.indolently.function.SSuppl;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.ref.$;
import jp.root42.indolently.ref.$2;

import static java.util.Objects.*;
import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
@SuppressWarnings({ "overloads", "RedundantSuppression" })
public class Functional {

    protected Functional() { }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static <T> $<T> apply(final $<T> x, final $<T> y, final BiFunction<? super T, ? super T, T> f) {
        return x.map(x0 -> opt(y.map(y0 -> f.apply(x0, y0)).or(x0))).or(y);
    }

    public static <X, Y> Function<X, Y> unbound(final Supplier<? extends Y> f) {
        return x -> f.get();
    }

    public static <X0, X1, R> BiFunction<X0, X1, R> unbound(final Function<? super X1, ? extends R> f) {
        return (x0, x1) -> f.apply(x1);
    }

    public static <X0, X1, X2, R> Function3<X0, X1, X2, R> unbound(final BiFunction<? super X1, ? super X2, ? extends R> f) {
        return (x0, x1, x2) -> f.apply(x1, x2);
    }

    public static <X, Y> Supplier<Y> bind(final Function<? super X, ? extends Y> f, final X x) {
        return () -> f.apply(x);
    }

    public static <X0, X1, R> Function<X1, R> bind(final BiFunction<? super X0, ? super X1, ? extends R> f, final X0 x0) {
        return x1 -> f.apply(x0, x1);
    }

    public static <X0, X1, X2, Y> BiFunction<X1, X2, Y> bind(final Function3<? super X0, ? super X1, ? super X2, ? extends Y> f, final X0 x0) {
        return (x2, x3) -> f.apply(x0, x2, x3);
    }

    public static <X> BooleanSupplier bind(final Predicate<? super X> f, final X x) {
        return () -> f.test(x);
    }

    public static <X0, X1> Predicate<X1> bind(final BiPredicate<? super X0, ? super X1> f, final X0 x0) {
        return x1 -> f.test(x0, x1);
    }

    public static <X0, X1, X2> BiPredicate<X1, X2> bind(final Predicate3<? super X0, ? super X1, ? super X2> f, final X0 x0) {
        return (x1, x2) -> f.test(x0, x1, x2);
    }

    public static <T> Supplier<T> memoize(final Supplier<? extends T> f) {
        return bind(memoize((Function<Object, T>) x -> f.get()), null);
    }

    public static <X, Y> Function<X, Y> memoize(final Function<? super X, ? extends Y> f) {

        final BiFunction<X, String, Y> g = memoize((BiFunction<X, String, Y>) (x0, x1) -> f.apply(x0));

        return x -> g.apply(x, "");
    }

    public static <X0, X1, Y> BiFunction<X0, X1, Y> memoize(final BiFunction<? super X0, ? super X1, ? extends Y> f) {

        final Function3<X0, X1, String, Y> g = memoize((Function3<X0, X1, String, Y>) (x0, x1, x2) -> f.apply(x0, x1));

        return (x0, x1) -> g.apply(x0, x1, "");
    }

    public static <X0, X1, X2, Y> Function3<X0, X1, X2, Y> memoize(final Function3<? super X0, ? super X1, ? super X2, ? extends Y> f) {

        final Function4<X0, X1, X2, String, Y> g = memoize((x0, x1, x2, x3) -> f.apply(x0, x1, x2));

        return (x0, x1, x2) -> g.apply(x0, x1, x2, "");
    }

    public static <X0, X1, X2, X3, Y> Function4<X0, X1, X2, X3, Y> memoize(final Function4<? super X0, ? super X1, ? super X2, ? super X3, ? extends Y> f) {

        final Map<$2<$2<X0, X1>, $2<X2, X3>>, Y> memo = map();

        return (x0, x1, x2, x3) -> {
            synchronized (memo) {
                return memo.computeIfAbsent(tuple(tuple(x0, x1), tuple(x2, x3)), key -> f.apply(x0, x1, x2, x3));
            }
        };
    }

    public static <T> Predicate<T> memoize(final Predicate<? super T> f) {

        final Function<T, Boolean> memoized = memoize(functionOf((final T x) -> f.test(x)));

        return memoized::apply;
    }

    public static <X0, X1> BiPredicate<X0, X1> memoize(final BiPredicate<? super X0, ? super X1> f) {

        final BiFunction<X0, X1, Boolean> memoized = memoize(function2Of((final X0 x0, final X1 x1) -> f.test(x0, x1)));

        return memoized::apply;
    }

    public static <X0, X1, X2> Predicate3<X0, X1, X2> memoize(final Predicate3<? super X0, ? super X1, ? super X2> f) {

        final Function3<X0, X1, X2, Boolean> memoized = memoize(function4Of((final X0 x0, final X1 x1, final X2 x2) -> f.test(x0, x1, x2)));

        return memoized::apply;
    }

    public static SBoolSuppl boolsuppl(final Consumer<? super BooleanSupplier> init, final Predicate<? super BooleanSupplier> body) {
        return function(init, body);
    }

    public static SBoolSuppl function(final Consumer<? super BooleanSupplier> init, final Predicate<? super BooleanSupplier> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SBoolSuppl(self -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self);
        });
    }

    public static <T> SSuppl<T> $(final Supplier<? extends T> suppl) {
        return suppl(suppl);
    }

    public static <T> SSuppl<T> suppl(final Supplier<? extends T> suppl) {
        return suppl(self -> { }, self -> suppl.get());
    }

    public static <T> SSuppl<T> suppl(final Consumer<? super Supplier<T>> init, final Function<? super Supplier<? extends T>, ? extends T> body) {
        return function(init, body);
    }

    public static <T> SSuppl<T> function(final Consumer<? super Supplier<T>> init, final Function<? super Supplier<? extends T>, ? extends T> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SSuppl<>(self -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self);
        });
    }

    public static <X, Y> SFunc<X, Y> $(final Function<? super X, ? extends Y> func) {
        return func(func);
    }

    public static <X, Y> SFunc<X, Y> func(final Function<? super X, ? extends Y> func) {
        return func(self -> { }, (self, x) -> func.apply(x));
    }

    public static <X, Y> SFunc<X, Y> func(final Consumer<? super Function<X, Y>> init,
        final BiFunction<? super Function<? super X, ? extends Y>, ? super X, ? extends Y> body) {

        return function(init, body);
    }

    public static <X, Y> SFunc<X, Y> function(final Consumer<? super Function<X, Y>> init,
        final BiFunction<? super Function<? super X, ? extends Y>, ? super X, ? extends Y> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SFunc<>((self, x) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x);
        });
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> $(final BiFunction<? super X0, ? super X1, ? extends Y> func) {
        return func2(func);
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> func2(final BiFunction<? super X0, ? super X1, ? extends Y> func) {
        return func2(self -> { }, (self, x0, x1) -> func.apply(x0, x1));
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> func2(final Consumer<? super BiFunction<X0, X1, Y>> init,
        final Function3<? super BiFunction<? super X0, ? super X1, ? extends Y>, ? super X0, ? super X1, ? extends Y> body) {

        return function(init, body);
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> function(final Consumer<? super BiFunction<X0, X1, Y>> init,
        final Function3<? super BiFunction<? super X0, ? super X1, ? extends Y>, ? super X0, ? super X1, ? extends Y> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SFunc2<>((self, x0, x1) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x0, x1);
        });
    }

    public static <X0, X1, X2, Y> SFunc3<X0, X1, X2, Y> func3(final Consumer<? super Function3<X0, X1, X2, Y>> init,
        final Function4<? super Function3<? super X0, ? super X1, ? super X2, ? extends Y>, ? super X0, ? super X1, ? super X2, ? extends Y> body) {

        return function(init, body);
    }

    public static <X0, X1, X2, Y> SFunc3<X0, X1, X2, Y> function(final Consumer<? super Function3<X0, X1, X2, Y>> init,
        final Function4<? super Function3<? super X0, ? super X1, ? super X2, ? extends Y>, ? super X0, ? super X1, ? super X2, ? extends Y> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SFunc3<>((self, x0, x1, x2) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x0, x1, x2);
        });
    }

    public static <X> SPred<X> $(final Predicate<? super X> pred) {
        return pred(pred);
    }

    public static <X> SPred<X> pred(final Predicate<? super X> pred) {
        return pred(self -> { }, (self, x) -> pred.test(x));
    }

    public static <X> SPred<X> pred(final Consumer<? super Predicate<X>> init, final BiPredicate<? super Predicate<X>, ? super X> body) {

        return function(init, body);
    }

    public static <X> SPred<X> function(final Consumer<? super Predicate<X>> init, final BiPredicate<? super Predicate<X>, ? super X> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SPred<>((self, x) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self, x);
        });
    }

    public static <X0, X1> SPred2<X0, X1> $(final BiPredicate<X0, X1> pred) {
        return pred2(pred);
    }

    public static <X0, X1> SPred2<X0, X1> pred2(final BiPredicate<X0, X1> pred) {
        return pred2(self -> { }, (self, x0, x1) -> pred.test(x0, x1));
    }

    public static <X0, X1> SPred2<X0, X1> pred2(final Consumer<? super BiPredicate<X0, X1>> init,
        final Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body) {

        return function(init, body);
    }

    public static <X0, X1> SPred2<X0, X1> function(final Consumer<? super BiPredicate<X0, X1>> init,
        final Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final var initialized = ref(false);

        return new SPred2<>((self, x0, x1) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self, x0, x1);
        });
    }

    public static <T> Expression<T> expressionOf(final Expression<? extends T> f) { return cast(f); }

    public static Statement statementOf(final Statement f) { return f; }

    public static <T> Supplier<T> supplierOf(final Supplier<? extends T> f) { return cast(f); }

    public static <X> Consumer<X> consumerOf(final Consumer<? super X> f) { return cast(f); }

    public static <X0, X1> BiConsumer<X0, X1> consumer2Of(final BiConsumer<? super X0, ? super X1> f) {
        return cast(f);
    }

    public static <X0, X1, X2> Consumer3<X0, X1, X2> consumer3Of(final Consumer3<? super X0, ? super X1, ? super X2> f) { return cast(f); }

    public static <X0, X1> Function<X0, X1> functionOf(final Function<? super X0, ? extends X1> f) { return cast(f); }

    public static <X0, X1, Y> BiFunction<X0, X1, Y> function2Of(final BiFunction<? super X0, ? super X1, ? extends Y> f) { return cast(f); }

    public static <X0, X1, X2, Y> Function3<X0, X1, X2, Y> function4Of(final Function3<? super X0, ? super X1, ? super X2, ? extends Y> f) { return cast(f); }

    public static <X> Predicate<X> predicateOf(final Predicate<? super X> f) { return cast(f); }

    public static <X0, X1> BiPredicate<X0, X1> predicate2Of(final BiPredicate<? super X0, ? super X1> f) {
        return cast(f);
    }

    public static <X0, X1, X2> Predicate3<X0, X1, X2> predicate3Of(final Predicate3<? super X0, ? super X1, ? super X2> f) { return cast(f); }

    @FunctionalInterface
    public interface ThrowableFunction<X0, X1> {

        X1 apply(X0 x) throws Exception;
    }

    public static <X0, X1> Function<X0, X1> adapt(final ThrowableFunction<X0, X1> f) {
        return x -> {
            try {
                return f.apply(x);
            } catch (final Exception e) {
                return raise(e);
            }
        };
    }

    @FunctionalInterface
    public interface ThrowableFunction2<X0, X1, X2> {

        X2 apply(X0 x0, X1 x1) throws Exception;
    }

    public static <X0, X1, X2> BiFunction<X0, X1, X2> adapt(final ThrowableFunction2<X0, X1, X2> f) {
        return (x0, x1) -> {
            try {
                return f.apply(x0, x1);
            } catch (final Exception e) {
                return raise(e);
            }
        };
    }

    @FunctionalInterface
    public interface ThrowableFunction3<X0, X1, X2, X3> {

        X3 apply(X0 x0, X1 x1, X2 x2) throws Exception;
    }

    public static <X0, X1, X2, X3> Function3<X0, X1, X2, X3> adapt(final ThrowableFunction3<X0, X1, X2, X3> f) {
        return (x0, x1, x2) -> {
            try {
                return f.apply(x0, x1, x2);
            } catch (final Exception e) {
                return raise(e);
            }
        };
    }

    @FunctionalInterface
    public interface ThrowableFunction4<X0, X1, X2, X3, X4> {

        X4 apply(X0 x0, X1 x1, X2 x2, X3 x3) throws Exception;
    }

    public static <X0, X1, X2, X3, X4> Function4<X0, X1, X2, X3, X4> adapt(final ThrowableFunction4<X0, X1, X2, X3, X4> f) {
        return (x0, x1, x2, x3) -> {
            try {
                return f.apply(x0, x1, x2, x3);
            } catch (final Exception e) {
                return raise(e);
            }
        };
    }

    public static <X> Supplier<$<X>> soften(final Supplier<X> f) {
        return () -> {
            try { return opt(f.get()); } //
            catch (final RuntimeException e) { return none(); }
        };
    }

    public static <X0, X1> Function<X0, $<X1>> soften(final Function<X0, X1> f) {
        return x -> {
            try { return opt(f.apply(x)); } //
            catch (final RuntimeException e) { return none(); }
        };
    }

    public static <X0, X1, X2> BiFunction<X0, X1, $<X2>> soften(final BiFunction<X0, X1, X2> f) {
        return (x0, x1) -> {
            try { return opt(f.apply(x0, x1)); } //
            catch (final RuntimeException e) { return none(); }
        };
    }

    public static <X0, X1, X2, X3> Function3<X0, X1, X2, $<X3>> soften(final Function3<X0, X1, X2, X3> f) {
        return (x0, x1, x2) -> {
            try { return opt(f.apply(x0, x1, x2)); } //
            catch (final RuntimeException e) { return none(); }
        };
    }

    public static <X> Function<X, Boolean> asFunction(final Predicate<X> pred) {
        return x -> pred.test(x);
    }

    public static <X0, X1> BiFunction<X0, X1, Boolean> asFunction(final BiPredicate<X0, X1> pred) {
        return (x0, x1) -> pred.test(x0, x1);
    }

    public static <X> Predicate<X> asPredicate(final Function<X, Boolean> pred) {
        return x -> pred.apply(x);
    }

    public static <X0, X1> BiPredicate<X0, X1> asPredicate(final BiFunction<X0, X1, Boolean> pred) {
        return (x0, x1) -> pred.apply(x0, x1);
    }

    public static <T0, T1, T2> Function<T0, T2> compose(final Function<T0, T1> f1, final Function<T1, T2> f2) {
        return compose(f1, f2, it(), it());
    }

    public static <T0, T1, T2, T3> Function<T0, T3> compose(final Function<T0, T1> f1, final Function<T1, T2> f2, final Function<T2, T3> f3) {
        return compose(f1, f2, f3, it());
    }

    public static <T0, T1, T2, T3, T4> Function<T0, T4> compose(final Function<T0, T1> f1, final Function<T1, T2> f2, final Function<T2, T3> f3,
        final Function<T3, T4> f4) { return f1.andThen(f2).andThen(f3).andThen(f4); }
}
