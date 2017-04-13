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
import jp.root42.indolently.ref.BoolRef;
import jp.root42.indolently.ref.Duo;

import static java.util.Objects.*;
import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class Functional {

    protected Functional() {}

    public static <X, Y> Supplier<Y> bind(final Function<? super X, ? extends Y> f, final X x) {
        return () -> f.apply(x);
    }

    public static <X0, X1, R> Function<X1, R> bind(final BiFunction<? super X0, ? super X1, ? extends R> f,
        final X0 x0) {
        return x1 -> f.apply(x0, x1);
    }

    public static <X0, X1, X2, Y> BiFunction<X1, X2, Y> bind(
        final Function3<? super X0, ? super X1, ? super X2, ? extends Y> f, final X0 x0) {
        return (x2, x3) -> f.apply(x0, x2, x3);
    }

    public static <X> BooleanSupplier bind(final Predicate<? super X> f, final X x) {
        return () -> f.test(x);
    }

    public static <X0, X1> Predicate<X1> bind(final BiPredicate<? super X0, ? super X1> f, final X0 x0) {
        return x1 -> f.test(x0, x1);
    }

    public static <X0, X1, X2> BiPredicate<X1, X2> bind(final Predicate3<? super X0, ? super X1, ? super X2> f,
        final X0 x0) {
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

    public static <X0, X1, X2, Y> Function3<X0, X1, X2, Y> memoize(
        final Function3<? super X0, ? super X1, ? super X2, ? extends Y> f) {

        final Function4<X0, X1, X2, String, Y> g = memoize((x0, x1, x2, x3) -> f.apply(x0, x1, x2));

        return (x0, x1, x2) -> g.apply(x0, x1, x2, "");
    }

    public static <X0, X1, X2, X3, Y> Function4<X0, X1, X2, X3, Y> memoize(
        final Function4<? super X0, ? super X1, ? super X2, ? super X3, ? extends Y> f) {

        final Map<Duo<Duo<X0, X1>, Duo<X2, X3>>, Y> memo = map();

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

        final Function3<X0, X1, X2, Boolean> memoized =
            memoize(function4Of((final X0 x0, final X1 x1, final X2 x2) -> f.test(x0, x1, x2)));

        return memoized::apply;
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
        });
    }

    public static <T> SSuppl<T> $(final Supplier<? extends T> suppl) {
        return suppl(self -> {}, self -> suppl.get());
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
        });
    }

    public static <X, Y> SFunc<X, Y> $(final Function<? super X, ? extends Y> func) {
        return func(self -> {}, (self, x) -> func.apply(x));
    }

    public static <X, Y> SFunc<X, Y> func(final Consumer<? super Function<X, Y>> init,
        final BiFunction<? super Function<? super X, ? extends Y>, ? super X, ? extends Y> body) {

        return function(init, body);
    }

    public static <X, Y> SFunc<X, Y> function(final Consumer<? super Function<X, Y>> init,
        final BiFunction<? super Function<? super X, ? extends Y>, ? super X, ? extends Y> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SFunc<>((self, x) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x);
        });
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> $(final BiFunction<? super X0, ? super X1, ? extends Y> func) {
        return func2(self -> {}, (self, x0, x1) -> func.apply(x0, x1));
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> func2(final Consumer<? super BiFunction<X0, X1, Y>> init,
        final Function3<? super BiFunction<? super X0, ? super X1, ? extends Y>, ? super X0, ? super X1, ? extends Y> body) {

        return function(init, body);
    }

    public static <X0, X1, Y> SFunc2<X0, X1, Y> function(final Consumer<? super BiFunction<X0, X1, Y>> init,
        final Function3<? super BiFunction<? super X0, ? super X1, ? extends Y>, ? super X0, ? super X1, ? extends Y> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

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

        final BoolRef initialized = ref(false);

        return new SFunc3<>((self, x0, x1, x2) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.apply(self, x0, x1, x2);
        });
    }

    public static <X> SPred<X> $(final Predicate<? super X> pred) {
        return pred(self -> {}, (self, x) -> pred.test(x));
    }

    public static <X> SPred<X> pred(final Consumer<? super Predicate<X>> init,
        final BiPredicate<? super Predicate<X>, ? super X> body) {

        return function(init, body);
    }

    public static <X> SPred<X> function(final Consumer<? super Predicate<X>> init,
        final BiPredicate<? super Predicate<X>, ? super X> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SPred<>((self, x) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self, x);
        });
    }

    public static <X0, X1> SPred2<X0, X1> $(final BiPredicate<X0, X1> pred) {
        return pred2(self -> {}, (self, x0, x1) -> pred.test(x0, x1));
    }

    public static <X0, X1> SPred2<X0, X1> pred2(final Consumer<? super BiPredicate<X0, X1>> init,
        final Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body) {

        return function(init, body);
    }

    public static <X0, X1> SPred2<X0, X1> function(final Consumer<? super BiPredicate<X0, X1>> init,
        final Predicate3<? super BiPredicate<X0, X1>, ? super X0, ? super X1> body) {

        requireNonNull(init, "init");
        requireNonNull(body, "body");

        final BoolRef initialized = ref(false);

        return new SPred2<>((self, x0, x1) -> {
            synchronized (initialized) {
                initialized.negateIf(false, () -> init.accept(self));
            }

            return body.test(self, x0, x1);
        });
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

    public static <X> Consumer<? super X> consumerOf(final Consumer<? super X> f) {
        return f;
    }

    public static <X0, X1> BiConsumer<? super X0, ? super X1> consumer2Of(final BiConsumer<? super X0, ? super X1> f) {
        return f;
    }

    public static <X0, X1, X2> Consumer3<? super X0, ? super X1, ? super X2> consumer3Of(
        final Consumer3<? super X0, ? super X1, ? super X2> f) {
        return f;
    }

    public static <X0, X1> Function<? super X0, ? extends X1> functionOf(final Function<? super X0, ? extends X1> f) {
        return f;
    }

    public static <X0, X1, Y> BiFunction<? super X0, ? super X1, ? extends Y> function2Of(
        final BiFunction<? super X0, ? super X1, ? extends Y> f) {
        return f;
    }

    public static <X0, X1, X2, Y> Function3<? super X0, ? super X1, ? super X2, ? extends Y> function4Of(
        final Function3<? super X0, ? super X1, ? super X2, ? extends Y> f) {
        return f;
    }

    public static <X> Predicate<? super X> predicateOf(final Predicate<? super X> f) {
        return f;
    }

    public static <X0, X1> BiPredicate<? super X0, ? super X1> predicate2Of(
        final BiPredicate<? super X0, ? super X1> f) {
        return f;
    }

    public static <X0, X1, X2> Predicate3<? super X0, ? super X1, ? super X2> predicate3Of(
        final Predicate3<? super X0, ? super X1, ? super X2> f) {
        return f;
    }
}
