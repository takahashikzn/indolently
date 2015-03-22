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
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import jp.root42.indolently.function.Expression;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.function.TriFunction;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class Expressive {

    /** non private for subtyping. */
    protected Expressive() {}

    @SuppressWarnings("javadoc")
    public static class RaisedException
        extends RuntimeException {

        private static final long serialVersionUID = 1037309592665638717L;

        public RaisedException(final Throwable e) {
            super(e);
        }
    }

    /**
     * Throw exception in expression manner.
     *
     * @param <T> pseudo expression type
     * @param e exception
     * @return actually return nothing
     */
    public static <T> T raise(final Throwable e) {
        return raise(() -> e);
    }

    /**
     * Throw exception in expression manner.
     *
     * @param <T> pseudo expression type
     * @param f supplies exception to throw
     * @return actually return nothing
     */
    public static <T> T raise(final Supplier<? extends Throwable> f) {

        throw optional(f.get()).map(e -> {
            if (e instanceof Error) {
                throw (Error) e;
            }

            if (e instanceof RuntimeException) {
                return (RuntimeException) e;
            } else {
                return new RaisedException(e);
            }
        } ).orElseGet(() -> new NullPointerException("supplier returns null: " + f));
    }

    /**
     * Block statement.
     *
     * @param stmt expression body
     */
    public static void let(final Statement stmt) {
        stmt.execute();
    }

    /**
     * In-line evaluation expression.
     *
     * @param <R> expression type
     * @param expr expression body
     * @return the value of body expression
     */
    public static <R> R eval(final Expression<? extends R> expr) {
        return expr.get();
    }

    /**
     * Block statement.
     *
     * @param value the value
     * @param stmt expression body
     */
    public static <T> void let(final T value, final Consumer<? super T> stmt) {
        stmt.accept(value);
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
    public static <T> void let(final T context, final Predicate<? super T> cond, final Consumer<? super T> then,
        final Consumer<? super T> other) {

        if (cond.test(context)) {
            then.accept(context);
        } else {
            other.accept(context);
        }
    }

    /**
     * In-line evaluation expression.
     *
     * @param <T> value type
     * @param <R> expression type
     * @param val the value
     * @param expr expression body
     * @return the value function returned
     */
    public static <T, R> R eval(final T val, final Function<? super T, ? extends R> expr) {
        return expr.apply(val);
    }

    /**
     * In-line evaluation expression.
     *
     * @param <T1> first value type
     * @param <T2> second value type
     * @param <R> expression type
     * @param val1 first value
     * @param val2 second value
     * @param expr expression body
     * @return the value function returned
     */
    public static <T1, T2, R> R eval(final T1 val1, final T2 val2,
        final BiFunction<? super T1, ? super T2, ? extends R> expr) {

        return expr.apply(val1, val2);
    }

    /**
     * In-line evaluation expression.
     *
     * @param <T1> first value type
     * @param <T2> second value type
     * @param <T3> third value type
     * @param val1 first value
     * @param val2 second value
     * @param val3 third value
     * @param expr expression body
     * @return the value function returned
     */
    public static <T1, T2, T3, R> R eval(final T1 val1, final T2 val2, final T3 val3,
        final TriFunction<? super T1, ? super T2, ? super T3, ? extends R> expr) {

        return expr.apply(val1, val2, val3);
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
    public static <T> T ifelse(final boolean cond, final Supplier<? extends T> then,
        final Supplier<? extends T> other) {
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

        list(forms).each(f -> f.execute());

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

    @SuppressWarnings("javadoc")
    @SafeVarargs
    public static <C> BoolMatch<C> test(final When<C, Boolean>... cases) {
        return BoolMatch.of(cases);
    }

    /**
     * A 'case' clause of 'match' expression.
     *
     * @param <C> testing value type
     * @param <V> expression body value type
     * @author takahashikzn
     */
    @SuppressWarnings("javadoc")
    public interface When<C, V>
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
            } );
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

            return x -> optional(eval(() -> {
                for (final When<C, V> c : cases) {
                    if (c.test(x)) {
                        return c.apply(x);
                    }
                }

                return null;
            } ));
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
            } );
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
    }

    /**
     * The 'match' expression.
     *
     * @param <C> testing value type
     * @author takahashikzn
     */
    @FunctionalInterface
    public interface BoolMatch<C>
        extends Predicate<C> {

        /**
         * Create match expression functor.
         *
         * @param cases case clauses
         * @return match expression functor
         */
        @SafeVarargs
        static <C> BoolMatch<C> of(final When<C, Boolean>... cases) {

            return x -> {
                for (final When<C, Boolean> c : cases) {
                    if (c.test(x)) {
                        return c.apply(x);
                    }
                }

                return false;
            };
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param e exception to throw
         * @return 'default' attached match expression
         */
        default Predicate<C> raise(final RuntimeException e) {
            return this.raise(() -> e);
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param f exception supplier to throw
         * @return 'default' attached match expression
         */
        default Predicate<C> raise(final Supplier<? extends RuntimeException> f) {
            Objects.requireNonNull(f);

            return this.raise(x -> f.get());
        }

        /**
         * Append 'default throw' clause to the end of this expression.
         *
         * @param f exception supplier to throw
         * @return 'default' attached match expression
         */
        default Predicate<C> raise(final Function<? super C, ? extends RuntimeException> f) {
            Objects.requireNonNull(f);

            return x -> {
                if (this.test(x)) {
                    return true;
                } else {
                    throw f.apply(x);
                }
            };
        }

        /**
         * Append 'default' clause to the end of this match expression.
         *
         * @param f default value supplier
         * @return 'default' attached match expression
         */
        default Predicate<C> defaults(final BooleanSupplier f) {
            Objects.requireNonNull(f);

            return this.defaults(x -> f.getAsBoolean());
        }

        /**
         * Append 'default' clause to the end of this match expression.
         *
         * @param f default value supplier
         * @return 'default' attached match expression
         */
        default Predicate<C> defaults(final Predicate<? super C> f) {
            Objects.requireNonNull(f);

            return x -> {
                if (this.test(x)) {
                    return true;
                } else {
                    return f.test(x);
                }
            };
        }
    }
}
