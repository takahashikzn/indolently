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
    static <C, V> Match<C, V> of(final When<C, V>... cases) {

        return x -> {

            final Optional<When<C, V>> matched = Indolently.find(x, cases);

            if (matched.isPresent()) {
                return Optional.ofNullable(matched.get().apply(x));
            } else {
                return Optional.empty();
            }
        };

        // return x -> Optional.ofNullable( //
        // Indolently.find(x, cases) //
        // .orElse(When.defaults(() -> null)) //
        // .apply(x));
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
