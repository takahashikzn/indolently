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

import jp.root42.indolently.Expressive.Match.Case;
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
    public interface When {

        <T> Then<T> then(Supplier<? extends T> then);

        default <T> Then<T> then(final T then) {
            return this.then(() -> then);
        }

        interface Then<T> {

            T none(Supplier<? extends T> none);

            Case<T> when(BooleanSupplier when);

            default T none(final T none) {
                return this.none(() -> none);
            }

            default Case<T> when(final boolean when) {
                return this.when(() -> when);
            }

            default T raise(final Supplier<? extends RuntimeException> raise) {
                return this.none(() -> Expressive.raise(() -> raise.get()));
            }

            default T fatal() {
                return Indolently.fatal();
            }

            default T fatal(final Object msg) {
                return Indolently.fatal(msg);
            }
        }

        public interface Case<T> {

            Then<T> then(Supplier<? extends T> then);

            default Then<T> then(final T then) {
                return this.then(() -> then);
            }
        }
    }

    @SuppressWarnings("javadoc")
    public static When when(final boolean pred) {
        return when(() -> pred);
    }

    @SuppressWarnings("javadoc")
    public static When when(final BooleanSupplier pred) {

        final class CaseAdaptor<T>
            implements When.Case<T> {

            final Match.Case<?, T> theCase;

            CaseAdaptor(final Match.Case<?, T> theCase) {
                this.theCase = theCase;
            }

            @Override
            public When.Then<T> then(final Supplier<? extends T> then) {

                return new When.Then<T>() {

                    @Override
                    public T none(final Supplier<? extends T> none) {
                        return CaseAdaptor.this.theCase.then(then).none(none);
                    }

                    @Override
                    public When.Case<T> when(final BooleanSupplier when) {
                        return new CaseAdaptor<>(CaseAdaptor.this.theCase.then(then).when(when));
                    }
                };
            }
        }

        final Match.IntroCase<?> intro = match(null).when(x -> pred.getAsBoolean());

        return new When() {

            @Override
            public <T> Then<T> then(final Supplier<? extends T> then) {

                return new Then<T>() {

                    @Override
                    public T none(final Supplier<? extends T> none) {
                        return intro.then(x -> (T) then.get()).none(none);
                    }

                    @Override
                    public Case<T> when(final BooleanSupplier when) {
                        return new CaseAdaptor<>(intro.then(x -> (T) then.get()).when(x -> when.getAsBoolean()));
                    }
                };
            }
        };
    }

    @SuppressWarnings("javadoc")
    public interface Match<C> {

        IntroCase<C> when(Predicate<? super C> pred);

        default IntroCase<C> when(final boolean pred) {
            return this.when(x -> pred);
        }

        interface IntroCase<C> {

            <T> Then<C, T> then(Function<? super C, ? extends T> then);

            default <T> Then<C, T> then(final T then) {
                return this.then(() -> then);
            }

            default <T> Then<C, T> then(final Supplier<? extends T> then) {
                return this.then(x -> then.get());
            }
        }

        interface Then<C, T> {

            T none(Function<? super C, ? extends T> none);

            Case<C, T> when(Predicate<? super C> when);

            default Case<C, T> when(final boolean when) {
                return this.when(() -> when);
            }

            default Case<C, T> when(final BooleanSupplier when) {
                return this.when(x -> when.getAsBoolean());
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
                return Indolently.fatal();
            }

            default T fatal(final Object msg) {
                return Indolently.fatal(msg);
            }
        }

        interface Case<C, T> {

            Then<C, T> then(Function<? super C, ? extends T> then);

            default Then<C, T> then(final T then) {
                return this.then(() -> then);
            }

            default Then<C, T> then(final Supplier<? extends T> then) {
                return this.then(x -> then.get());
            }
        }
    }

    @SuppressWarnings("javadoc")
    public static <C> Match<C> match(final C ctx) {

        final class Resolved<T>
            implements Match.Case<C, T> {

            final T value;

            Resolved(final T value) {
                this.value = value;
            }

            @Override
            public Match.Then<C, T> then(final Function<? super C, ? extends T> then) {

                return new Match.Then<C, T>() {

                    @Override
                    public T none(final Function<? super C, ? extends T> none) {
                        return Resolved.this.value;
                    }

                    @Override
                    public Case<C, T> when(final Predicate<? super C> when) {
                        return Resolved.this;
                    }
                };
            }
        }

        final class Unresolved<T>
            implements Match.Case<C, T> {

            final Predicate<? super C> pred;

            Unresolved(final Predicate<? super C> pred) {
                this.pred = Functional.wrap(pred).memoize();
            }

            @Override
            public Match.Then<C, T> then(final Function<? super C, ? extends T> then) {

                return new Match.Then<C, T>() {

                    @Override
                    public T none(final Function<? super C, ? extends T> none) {

                        return Unresolved.this.pred.test(ctx) //
                            ? then.apply(ctx) //
                            : none.apply(ctx);
                    }

                    @Override
                    public Case<C, T> when(final Predicate<? super C> when) {

                        return Unresolved.this.pred.test(ctx) //
                            ? new Resolved<>(then.apply(ctx)) //
                            : new Unresolved<>(when);
                    }
                };
            }
        }

        return pred -> {

            return new Match.IntroCase<C>() {

                @Override
                public <T> Match.Then<C, T> then(final Function<? super C, ? extends T> then) {
                    return new Unresolved<T>(pred).then(then);
                }
            };
        };
    }
}
