/*
 * Copyright (C) 2014 root42 Inc. All rights reserved.
 */
package jp.root42.indolently;

import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;


/**
 * Extended {@link Set} class for indolent person.
 *
 * @param <T> value type
 * @author takahashikzn
 */
public interface Sset<T>
    extends Scol<T, Sset<T>>, Set<T> {

    @Override
    default Sset<T> freeze() {
        return Indolently.freeze(this);
    }

    @Override
    default Sset<T> tail() {
        return Indolently.set(this.list().tail());
    }

    /**
     * convert this set to {@link Slist}.
     *
     * @return a list newly constructed from this instance.
     */
    default Slist<T> list() {
        return Indolently.list(this);
    }

    @Override
    default Sset<T> map(final BiFunction<Integer, ? super T, ? extends T> f) {

        final Sset<T> rslt = Indolently.set();

        int i = 0;
        for (final T val : this) {
            rslt.add(f.apply(i++, val));
        }

        return rslt;
    }

    @Override
    default Sset<T> filter(final BiPredicate<Integer, ? super T> f) {

        final Sset<T> rslt = Indolently.set();

        int i = 0;
        for (final T val : this) {
            if (f.test(i++, val)) {
                rslt.add(val);
            }
        }

        return this;
    }

    /**
     * compute union of set.
     *
     * @param values
     * @return newly constructed set as a computed union
     */
    default Sset<T> union(final Iterable<? extends T> values) {
        return Indolently.set(this).pushAll(values);
    }

    /**
     * compute intersection of set.
     *
     * @param values
     * @return newly constructed set as a computed intersection.
     */
    default Sset<T> intersect(final Iterable<? extends T> values) {
        return this.union(values).delete(this.diff(values));
    }

    /**
     * compute difference of set.
     *
     * @param values
     * @return newly constructed set as a computed difference.
     */
    default Sset<T> diff(final Iterable<? extends T> values) {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final Sset<T> rslt = Indolently.set(this).delete(values).union(Indolently.set(values).delete((Set) this));
        return rslt;
    }
}
