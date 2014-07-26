/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * A 'match' expression.
 *
 * @param <C> testing value type
 * @param <V> expression body value type
 * @author takahashikzn
 */
@FunctionalInterface
public interface Match<C, V>
    extends Function<C, Optional<V>> {

    /**
     * Append 'default throw' clause to this match expression.
     *
     * @param f exception supplier to throw
     * @return 'default' attached match expression
     */
    default Function<C, V> failure(final Supplier<? extends RuntimeException> f) {
        return this.failure((x) -> f.get());
    }

    /**
     * Append 'default throw' clause to this match expression.
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
     * Append 'default' clause to this match expression.
     *
     * @param f default value supplier
     * @return 'default' attached match expression
     */
    default Function<C, V> defaults(final Supplier<? extends V> f) {
        return this.defaults((x) -> f.get());
    }

    /**
     * Append 'default' clause to this match expression.
     *
     * @param f default value supplier
     * @return 'default' attached match expression
     */
    default Function<C, V> defaults(final Function<? super C, ? extends V> f) {
        return (c) -> this.apply(c).orElseGet(() -> f.apply(c));
    }

    /**
     * A 'when' expression in pattern matching expression.
     *
     * @param <C> testing value type
     * @param <V> expression body value type
     * @author takahashikzn
     */
    @FunctionalInterface
    interface When<C, V>
        extends Predicate<C> {

        // default ThenApply<C, V> then(final Function<? super C, ? extends V> f) {
        //
        // return new ThenApply<C, V>() {
        //
        // @Override
        // public boolean test(final C cond) {
        // return When.this.test(cond);
        // }
        //
        // @Override
        // public V apply(final C cond) {
        // return f.apply(cond);
        // }
        // };
        // }

        // default ThenGet<C, V> then(final Supplier<? extends V> f) {
        //
        // return new ThenGet<C, V>() {
        //
        // @Override
        // public boolean test(final C cond) {
        // return When.this.test(cond);
        // }
        //
        // @Override
        // public V get() {
        // return f.get();
        // }
        // };
        // }

        /**
         * A 'if-then' expression.
         *
         * @param <C> testing value type
         * @param <V> expression body value type
         * @author takahashikzn
         */
        interface ThenApply<C, V>
            extends Predicate<C>, Function<C, V> {
        }

        /**
         * A 'if-then' expression.
         *
         * @param <C> testing value type
         * @param <V> expression body value type
         * @author takahashikzn
         */
        interface ThenGet<C, V>
            extends Predicate<C>, Supplier<V> {
        }
    }
}
