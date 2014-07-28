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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.Expressions.Match.When;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
@SuppressWarnings("javadoc")
public class Expressions {

    /** non private for subtyping. */
    protected Expressions() {
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
    public static <T> T prog1(final Supplier<? extends T> first, final Closure... forms) {

        final T val = first.get();

        list(forms).each(f -> f.perform());

        return val;
    }

    public static <C, V> When<C, V> whenNull(final V val) {
        return when(x -> x == null, val);
    }

    public static <C, V> When<C, V> whenEq(final C pred, final V val) {
        return when(x -> equiv(x, pred), () -> val);
    }

    public static <C, V> When<C, V> whenEq(final C pred, final Supplier<? extends V> expr) {
        return when(x -> equiv(x, pred), expr);
    }

    public static <C, V> When<C, V> whenEq(final C pred, final Function<? super C, ? extends V> expr) {
        return when(x -> equiv(x, pred), expr);
    }

    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final V val) {
        return when(pred, () -> val);
    }

    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final Supplier<? extends V> expr) {
        return When.of(pred, x -> expr.get());
    }

    public static <C, V> When<C, V> when(final Predicate<? super C> pred, final Function<? super C, ? extends V> expr) {
        return When.of(pred, expr);
    }

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
        default Function<C, V> failure(final RuntimeException e) {
            return this.failure(() -> e);
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param f exception supplier to throw
         * @return 'default' attached match expression
         */
        default Function<C, V> failure(final Supplier<? extends RuntimeException> f) {
            Objects.requireNonNull(f);

            return this.failure(x -> f.get());
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param f exception supplier to throw
         * @return 'default' attached match expression
         */
        default Function<C, V> failure(final Function<? super C, ? extends RuntimeException> f) {
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

            static <C, V> When<C, V> failure(final Supplier<? extends RuntimeException> expr) {
                return failure(x -> expr.get());
            }

            static <C, V> When<C, V> failure(final Function<? super C, ? extends RuntimeException> expr) {
                return of(x -> true, x -> {
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

            default Function<C, V> other(final Supplier<? extends V> f) {
                return this.other(x -> f.get());
            }

            default Function<C, V> other(final Function<? super C, ? extends V> f) {
                return x -> this.test(x) ? this.apply(x) : f.apply(x);
            }
        }
    }
}
