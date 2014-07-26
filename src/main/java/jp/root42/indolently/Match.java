/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


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
    static <C, V> Match<C, V> of(final When<C, Optional<V>>... cases) {
        return (cond) -> Indolently.optional(Indolently.find(cond, cases).flatMap((x) -> x.apply(cond)).orElse(null));
    }

    /**
     * Append 'default throw' clause to the end of this expression.
     *
     * @param f exception supplier to throw
     * @return 'default' attached match expression
     */
    default Function<C, V> failure(final Supplier<? extends RuntimeException> f) {
        return this.failure((x) -> f.get());
    }

    /**
     * Append 'default throw' clause to the end of this expression.
     *
     * @param f exception supplier to throw
     * @return 'default' attached match expression
     */
    default Function<C, V> failure(final Function<? super C, ? extends RuntimeException> f) {
        return this.defaults((x) -> {
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
        return this.defaults((x) -> f.get());
    }

    /**
     * Append 'default' clause to the end of this match expression.
     *
     * @param f default value supplier
     * @return 'default' attached match expression
     */
    default Function<C, V> defaults(final Function<? super C, ? extends V> f) {
        return (c) -> this.apply(c).orElseGet(() -> f.apply(c));
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

        static <C, V> When<C, Optional<V>> of(final Predicate<? super C> pred, final Supplier<? extends V> expr) {
            return of(pred, x -> expr.get());
        }

        static <C, V> When<C, Optional<V>> of(final Predicate<? super C> pred,
            final Function<? super C, ? extends V> expr) {

            return new When<C, Optional<V>>() {
                @Override
                public boolean test(final C cond) {
                    return pred.test(cond);
                }

                @Override
                public Optional<V> apply(final C cond) {
                    return Indolently.optional(expr.apply(cond));
                }
            };
        }
    }
}
