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

import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.jodah.typetools.TypeResolver;

import jp.root42.indolently.function.Expression;
import jp.root42.indolently.function.Function3;
import jp.root42.indolently.function.Statement;

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
     * @param <T> the type of expression
     * @param e exception
     * @return actually return nothing
     */
    public static <T> T raise(final Throwable e) {
        return raise(() -> e);
    }

    /**
     * Throw exception in expression manner.
     *
     * @param <T> the type of expression
     * @param f supplies exception to throw
     * @return actually return nothing
     */
    public static <T> T raise(final Supplier<? extends Throwable> f) {

        throw opt(f.get()).map(e -> {
            if (e instanceof Error) {
                throw (Error) e;
            } else if (e instanceof RuntimeException) {
                return (RuntimeException) e;
            } else {
                return new RaisedException(e);
            }
        }).orElseGet(() -> new NullPointerException("supplier returns null: " + f));
    }

    /**
     * Block statement.
     *
     * @param stmt statement body
     */
    public static void let(final Statement stmt) {
        stmt.execute();
    }

    /**
     * In-line evaluation expression.
     *
     * @param <R> the type of expression
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
     * Block statement which applied only if the value is an instance of the statement's argument type.
     *
     * @param value the value
     * @param stmt statement body
     */
    public static <T> void ifInstance(final Object value, final Consumer<T> stmt) {

        if (argTypeOf(stmt).isInstance(value)) {
            stmt.accept(cast(value));
        }
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
     * @param <R> the type of expression
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
     * @param <R> the type of expression
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
     * @param <R> the type of expression
     * @param val1 first value
     * @param val2 second value
     * @param val3 third value
     * @param expr expression body
     * @return the value function returned
     */
    public static <T1, T2, T3, R> R eval(final T1 val1, final T2 val2, final T3 val3,
        final Function3<? super T1, ? super T2, ? super T3, ? extends R> expr) {

        return expr.apply(val1, val2, val3);
    }

    /**
     * evaluate following forms then return evaluation result of first expressions.
     *
     * @param <T> the type of expression
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
     * @param <T> the type of expression
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
     * @param <T> the type of entire expression
     * @param first evaluation result of this expression
     * @param forms evaluation target forms
     * @return first expression evaluation result
     */
    public static <T> T prog1(final Supplier<? extends T> first, final Statement... forms) {

        final T val = first.get();

        list(forms).each(f -> f.execute());

        return val;
    }

    /**
     * try-with-resource statement.
     *
     * @param res the resource
     * @param stmt expression body
     */
    public static <T extends AutoCloseable> void letWith(final T res, final Consumer<? super T> stmt) {
        // cast is required to avoid compilation failure on javac
        letWith((Supplier<T>) () -> res, stmt);
    }

    /**
     * try-with-resource statement.
     *
     * @param res the resource
     * @param stmt expression body
     */
    public static <T extends AutoCloseable> void letWith(final Supplier<? extends T> res,
        final Consumer<? super T> stmt) {

        evalWith(res, x -> {
            stmt.accept(x);
            return null;
        });
    }

    /**
     * try-with-resource expression.
     *
     * @param res the resource
     * @param expr expression body
     * @return the value function returned
     */
    public static <T extends AutoCloseable, R> R evalWith(final T res, final Function<? super T, ? extends R> expr) {
        // cast is required to avoid compilation failure on javac
        return evalWith((Supplier<T>) () -> res, expr);
    }

    /**
     * try-with-resource expression.
     *
     * @param res the resource
     * @param expr expression body
     * @return the value function returned
     */
    public static <T extends AutoCloseable, R> R evalWith(final Supplier<? extends T> res,
        final Function<? super T, ? extends R> expr) {

        return eval(() -> {
            try (T x = res.get()) {
                return expr.apply(x);
            }
        });
    }

    /**
     * if-then-else expression.
     *
     * @see Expressive#when(BooleanSupplier)
     * @author takahashikzn
     */
    public interface When {

        /**
         * The value expression treated as the return value of the entire expression if the just before conditional
         * expression evaluated as <code>true</code>.
         *
         * @param <T> the type of entire expression
         * @param then value expression
         * @return 'then' object
         */
        <T> Then<T> then(Supplier<? extends T> then);

        /**
         * The value treated as the return value of the entire expression if the just before conditional expression
         * evaluated as <code>true</code>.
         *
         * @param <T> the type of entire expression
         * @param then a value
         * @return 'then' object
         */
        default <T> Then<T> then(final T then) {
            return this.then(() -> then);
        }

        /**
         * The body expression of 'if' expression.
         *
         * @param <T> the type of entire expression
         * @author takahashikzn
         */
        interface Then<T> {

            /**
             * Returns the value of entire expression. This is terminal operation.
             *
             * @param none value expression evaluated if and only if all conditional expression evaluated as
             * <code>false</code>.
             * @return the return value of the entire expression
             */
            T none(Supplier<? extends T> none);

            /**
             * 'else-if' expression.
             *
             * @param when a condition
             * @return 'case' object as an 'else-if' expression.
             */
            When.Case<T> when(BooleanSupplier when);

            default T none(final T none) {
                return this.none(() -> none);
            }

            default When.Case<T> when(final boolean when) {
                return this.when(() -> when);
            }

            default T raise(final Supplier<? extends RuntimeException> raise) {
                return this.none(() -> Expressive.raise(() -> raise.get()));
            }

            default T fatal() {
                return this.none(() -> Indolently.fatal());
            }

            default T fatal(final Object msg) {
                return this.none(() -> Indolently.fatal(msg));
            }
        }

        /**
         * 'if' expression.
         *
         * @param <T> the type of entire expression
         * @author takahashikzn
         */
        interface Case<T> {

            When.Then<T> then(Supplier<? extends T> then);

            default When.Then<T> then(final T then) {
                return this.then(() -> then);
            }
        }
    }

    /**
     * A shortcut of <code>when(() -> pred)</code>.
     *
     * @param pred constant conditional value
     * @return 'when' object
     */
    public static When when(final boolean pred) {
        return when(() -> pred);
    }

    /**
     * if-then-else expression.
     * <p>
     * <h3>Example</h3>
     *
     * <pre>
     * <code>
     * int x = ...;
     *
     * String evenOrOdd = Expressive
     *     .when(() -> x % 2 == 0).then(() -> "even")
     *     .when(() -> x % 2 != 0).then(() -> "odd")
     *     .none(() -> "This is terminal operation but never called in this example.");
     *
     * // constants are acceptable
     * String simplerEvenOrOdd = Expressive
     *     .when(x % 2 == 0).then("even")
     *     .when(x % 2 != 0).then("odd")
     *     .none("This is terminal operation but never called in this example.");
     * </code>
     * </pre>
     * </p>
     *
     * @param pred first condition
     * @return 'when' object
     */
    public static When when(final BooleanSupplier pred) {

        final Match.IntroCase<?> intro = match(null).when(x -> pred.getAsBoolean());

        return new When() {

            @Override
            public <T> When.Then<T> then(final Supplier<? extends T> then) {

                return new When.Then<T>() {

                    @Override
                    public T none(final Supplier<? extends T> none) {
                        return intro //
                            .then(x -> (T) then.get()) //
                            .none(none);
                    }

                    @Override
                    public When.Case<T> when(final BooleanSupplier when) {
                        return toWhenCase(intro //
                            .then(x -> (T) then.get()) //
                            .when(x -> when.getAsBoolean()));
                    }
                };
            }
        };
    }

    private static <T> When.Case<T> toWhenCase(final Match.Case<?, T> theCase) {

        return then -> new When.Then<T>() {

            @Override
            public T none(final Supplier<? extends T> none) {
                return theCase.then(then).none(none);
            }

            @Override
            public When.Case<T> when(final BooleanSupplier when) {
                return toWhenCase(theCase.then(then).when(when));
            }
        };
    }

    private static <T> Class<T> argTypeOf(final Function<T, ?> f) {
        return cast(TypeResolver.resolveRawArguments(Function.class, f.getClass())[0]);
    }

    private static <T> Class<T> argTypeOf(final Consumer<T> f) {
        return cast(TypeResolver.resolveRawArguments(Consumer.class, f.getClass())[0]);
    }

    @SuppressWarnings("javadoc")
    public interface Match<C> {

        Match.IntroCase<C> when(Predicate<? super C> pred);

        default Match.IntroCase<C> when(final boolean pred) {
            return this.when(x -> pred);
        }

        default Match.IntroCase<C> when(final Supplier<? extends C> pred) {
            return this.when(x -> Indolently.equal(x, pred.get()));
        }

        default Match.IntroCase<C> when(final Class<?> type) {
            return this.when(x -> type.isInstance(x));
        }

        default <SC extends C, T> Match.Then<C, T> type(final Function<SC, ? extends T> then) {

            return eval(argTypeOf(then),
                argType -> this.when(argType::isInstance).then(ctx -> then.apply(argType.cast(ctx))));
        }

        interface IntroCase<C> {

            <T> Match.Then<C, T> then(Function<? super C, ? extends T> then);

            default <T> Match.Then<C, T> then(final T then) {
                return this.then(() -> then);
            }

            default <T> Match.Then<C, T> then(final Supplier<? extends T> then) {
                return this.then(x -> then.get());
            }
        }

        interface Then<C, T> {

            T none(Function<? super C, ? extends T> none);

            Match.Case<C, T> when(Predicate<? super C> when);

            default Match.Case<C, T> when(final Class<?> type) {
                return this.when(x -> type.isInstance(x));
            }

            default <SC extends C> Match.Then<C, T> type(final Function<SC, ? extends T> then) {

                return eval(argTypeOf(then),
                    argType -> this.when(argType::isInstance).then(ctx -> then.apply(argType.cast(ctx))));
            }

            default Match.Case<C, T> when(final boolean when) {
                return this.when(x -> when);
            }

            default Match.Case<C, T> when(final BooleanSupplier when) {
                return this.when(x -> when.getAsBoolean());
            }

            default Match.Case<C, T> when(final Supplier<? extends C> pred) {
                return this.when(x -> Indolently.equal(x, pred.get()));
            }

            default T none(final T none) {
                return this.none(() -> none);
            }

            default T none(final Supplier<? extends T> none) {
                return this.none(x -> none.get());
            }

            default T raise(final Function<? super C, ? extends RuntimeException> raise) {
                return this.none(x -> Expressive.raise(() -> raise.apply(x)));
            }

            default T fatal() {
                return this.none(() -> Indolently.fatal());
            }

            default T fatal(final Object msg) {
                return this.none(() -> Indolently.fatal(msg));
            }
        }

        interface Case<C, T> {

            Match.Then<C, T> then(Function<? super C, ? extends T> then);

            default Match.Then<C, T> then(final T then) {
                return this.then(() -> then);
            }

            default Match.Then<C, T> then(final Supplier<? extends T> then) {
                return this.then(x -> then.get());
            }
        }
    }

    @SuppressWarnings("javadoc")
    public static <C> Match<C> match(final C ctx) {

        return pred -> new Match.IntroCase<C>() {

            @Override
            public <T> Match.Then<C, T> then(final Function<? super C, ? extends T> then) {
                return Expressive.<C, T> toUnresolvedCase(ctx, pred).then(then);
            }
        };
    }

    private static <C, T> Match.Case<C, T> toResolvedCase(final T value) {

        return new Match.Case<C, T>() {

            @Override
            public Match.Then<C, T> then(final Function<? super C, ? extends T> then) {

                final Match.Case<C, T> self = this;

                return new Match.Then<C, T>() {

                    @Override
                    public T none(final Function<? super C, ? extends T> none) {
                        return value;
                    }

                    @Override
                    public Match.Case<C, T> when(final Predicate<? super C> when) {
                        return self;
                    }
                };
            }
        };
    }

    private static <C, T> Match.Case<C, T> toUnresolvedCase(final C ctx, final Predicate<? super C> condition) {

        final Predicate<? super C> pred = Functional.wrap(condition).memoize();

        return then -> new Match.Then<C, T>() {

            @Override
            public T none(final Function<? super C, ? extends T> none) {
                return pred.test(ctx) //
                    ? then.apply(ctx) //
                    : none.apply(ctx);
            }

            @Override
            public Match.Case<C, T> when(final Predicate<? super C> when) {
                return pred.test(ctx) //
                    ? toResolvedCase(then.apply(ctx)) //
                    : toUnresolvedCase(ctx, when);
            }
        };
    }
}
