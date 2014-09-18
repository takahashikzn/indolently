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

import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.Expressive.Match.When;
import jp.root42.indolently.function.Statement;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class Expressive {

    /** non private for subtyping. */
    protected Expressive() {}

    /**
     * Block expression.
     *
     * @param <T> value type
     * @param f expression body
     * @return the value of body expression
     */
    public static <T> T block(final Supplier<? extends T> f) {
        return f.get();
    }

    /**
     * Block statement.
     *
     * @param f expression body
     */
    public static void block(final Statement f) {
        f.perform();
    }

    /**
     * if-else statement alternative.
     *
     * @param <T> context type
     * @param context context value
     * @param cond context test function
     * @param then context conversion function used if the condition is {@code true}
     * @param other context conversion function used if the condition is {@code false}
     */
    public static <T> void block(final T context, final Predicate<? super T> cond, final Consumer<? super T> then,
        final Consumer<? super T> other) {

        if (cond.test(context)) {
            then.accept(context);
        } else {
            other.accept(context);
        }
    }

    /**
     * Lazy evaluated inline if-else operator.
     *
     * @param <T> value type
     * @param cond condition
     * @param then result value if condition is {@code true}
     * @param other result value if condition is {@code false}
     * @return evaluation result
     */
    public static <T> T ifelse(final boolean cond, final Supplier<? extends T> then, final Supplier<? extends T> other) {
        return cond ? then.get() : other.get();
    }

    /**
     * Lazy evaluated inline if-else operator.
     *
     * @param <T> value type
     * @param cond condition
     * @param then result value if condition is {@code true}
     * @param other result value if condition is {@code false}
     * @return evaluation result
     */
    public static <T> T ifelse(final BooleanSupplier cond, final Supplier<? extends T> then,
        final Supplier<? extends T> other) {

        return cond.getAsBoolean() ? then.get() : other.get();
    }

    /**
     * Lazy evaluated inline if-else operator.
     *
     * @param <C> context type
     * @param <V> return value type
     * @param context context value
     * @param cond context test function
     * @param then context conversion function used if the condition is {@code true}
     * @param other context conversion function used if the condition is {@code false}
     * @return evaluation result
     */
    public static <C, V> V ifelse(final C context, final Predicate<? super C> cond,
        final Function<? super C, ? extends V> then, final Function<? super C, ? extends V> other) {

        return cond.test(context) ? then.apply(context) : other.apply(context);
    }

    /**
     * evaluate following forms then return evaluation result of first expressions.
     *
     * @param first evaluation result of this expression
     * @param form evaluation target form. argument is evaluation result of {@code first}.
     * @return first expression evaluation result
     */
    public static <T> T prog1(final Supplier<? extends T> first, final Consumer<? super T> form) {
        return prog1Internal(first, form);
    }

    /**
     * evaluate following forms then return evaluation result of first expressions.
     *
     * @param first evaluation result of this expression
     * @param forms evaluation target forms. argument is evaluation result of {@code first}.
     * @return first expression evaluation result
     */
    @SafeVarargs
    public static <T> T prog1(final Supplier<? extends T> first, final Consumer<? super T>... forms) {
        return prog1Internal(first, forms);
    }

    @SafeVarargs
    private static <T> T prog1Internal(final Supplier<? extends T> first, final Consumer<? super T>... forms) {

        final T val = first.get();

        list(forms).each(f -> f.accept(val));

        return val;
    }

    /**
     * evaluate following forms then return first expression evaluation result.
     *
     * @param first evaluation result of this expression
     * @param forms evaluation target forms
     * @return first expression evaluation result
     */
    public static <T> T prog1(final Supplier<? extends T> first, final Statement... forms) {

        final T val = first.get();

        list(forms).each(f -> f.perform());

        return val;
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> whenNull(final V val) {
        return when(x -> x == null, val);
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> whenNonNull(final Function<? super C, ? extends V> expr) {
        return when(x -> x != null, expr);
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> whenEq(final C pred, final V val) {
        return when(x -> equiv(x, pred), () -> val);
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> whenEq(final C pred, final Supplier<? extends V> expr) {
        return when(x -> equiv(x, pred), expr);
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> whenEq(final C pred, final Function<? super C, ? extends V> expr) {
        return when(x -> equiv(x, pred), expr);
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final V val) {
        return when(pred, () -> val);
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final Supplier<? extends V> expr) {
        return When.of(pred, x -> expr.get());
    }

    @SuppressWarnings("javadoc")
    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final Function<? super C, ? extends V> expr) {
        return When.of(pred, expr);
    }

    @SuppressWarnings("javadoc")
    @SafeVarargs
    public static <C, V> Match<C, V> match(final When<C, V>... cases) {
        return Match.of(cases);
    }

    /**
     * The 'match' expression.
     *
     * @param <C> testing value type
     * @param <V> expression body value type
     * @author takahashikzn
     */
    @FunctionalInterface
    public interface Match<C, V>
        extends Function<C, Optional<V>> {

        /**
         * Create match expression functor.
         *
         * @param cases case clauses
         * @return match expression functor
         */
        @SafeVarargs
        static <C, V> Match<C, V> of(final When<C, V>... cases) {

            return x -> Optional.ofNullable( //
                Indolently.find(x, cases) //
                    .orElse(When.defaults(() -> null)) //
                    .apply(x));
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param e exception to throw
         * @return 'default' attached match expression
         */
        default Function<C, V> raise(final RuntimeException e) {
            return this.raise(() -> e);
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param f exception supplier to throw
         * @return 'default' attached match expression
         */
        default Function<C, V> raise(final Supplier<? extends RuntimeException> f) {
            Objects.requireNonNull(f);

            return this.raise(x -> f.get());
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param f exception supplier to throw
         * @return 'default' attached match expression
         */
        default Function<C, V> raise(final Function<? super C, ? extends RuntimeException> f) {
            Objects.requireNonNull(f);

            return this.defaults(x -> {
                throw f.apply(x);
            });
        }

        /**
         * Append 'default' clause to the end of this match expression.
         *
         * @param f default value supplier
         * @return 'default' attached match expression
         */
        default Function<C, V> defaults(final Supplier<? extends V> f) {
            Objects.requireNonNull(f);

            return this.defaults(x -> f.get());
        }

        /**
         * Append 'default' clause to the end of this match expression.
         *
         * @param f default value supplier
         * @return 'default' attached match expression
         */
        default Function<C, V> defaults(final Function<? super C, ? extends V> f) {
            Objects.requireNonNull(f);

            return x -> this.apply(x).orElseGet(() -> f.apply(x));
        }

        /**
         * A 'case' clause of 'match' expression.
         *
         * @param <C> testing value type
         * @param <V> expression body value type
         * @author takahashikzn
         */
        interface When<C, V>
            extends Predicate<C>, Function<C, V> {

            static <C, V> When<C, V> defaults(final Supplier<? extends V> expr) {
                return defaults(x -> expr.get());
            }

            static <C, V> When<C, V> defaults(final Function<? super C, ? extends V> expr) {
                return of(x -> true, x -> expr.apply(x));
            }

            static <C, V> When<C, V> raise(final Supplier<? extends RuntimeException> expr) {
                return raise(x -> expr.get());
            }

            static <C, V> When<C, V> raise(final Function<? super C, ? extends RuntimeException> expr) {
                return of(x -> true, (final C x) -> {
                    throw expr.apply(x);
                });
            }

            static <C, V> When<C, V> of(final Predicate<? super C> pred, final Supplier<? extends V> expr) {
                return of(pred, x -> expr.get());
            }

            static <C, V> When<C, V> of(final Predicate<? super C> pred, final Function<? super C, ? extends V> expr) {
                Objects.requireNonNull(pred, "pred");
                Objects.requireNonNull(expr, "expr");

                return new When<C, V>() {
                    @Override
                    public boolean test(final C cond) {
                        return pred.test(cond);
                    }

                    @Override
                    public V apply(final C cond) {
                        return expr.apply(cond);
                    }
                };
            }

            default Function<C, V> other(final V value) {
                return this.other(x -> value);
            }

            default Function<C, V> other(final Supplier<? extends V> f) {
                return this.other(x -> f.get());
            }

            default Function<C, V> other(final Function<? super C, ? extends V> f) {
                return x -> this.test(x) ? this.apply(x) : f.apply(x);
            }
        }
    }
}
