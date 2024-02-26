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

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import jp.root42.indolently.bridge.ListDelegate;
import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.function.ConsumerE;
import jp.root42.indolently.function.Function3;
import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * Simple implementation of {@link $list}.
 *
 * @param <T> value type
 * @author takahashikzn
 */
class $list_impl<T>
    extends ListDelegate<T>
    implements $list_optimized<T>, Serializable {

    @Serial
    private static final long serialVersionUID = 8705188807596442213L;

    private final List<T> store;

    private final boolean randomAccessible;

    public $list_impl() { this(newList()); }

    public $list_impl(final List<T> store) {
        this.store = store;
        this.randomAccessible = store instanceof $list<?> x ? x.randomAccessible() : !(store instanceof LinkedList);
    }

    @Override
    public boolean randomAccessible() { return this.randomAccessible; }

    private static <T> List<T> newList() { return ObjFactory.getInstance().newList(); }

    @Override
    protected List<T> getDelegate() { return this.store; }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public $list<T> clone() {
        final List<T> newStore = newList();
        newStore.addAll(this.store);
        return new $list_impl<>(newStore);
    }

    @Override
    public $iter<T> iterator() { return Indolently.$(this.store.iterator()); }

    // keep original order
    @Override
    public $set<T> set() { return new $set_impl<>(ObjFactory.getInstance().<T> newFifoSet()).pushAll(this); }

    @Override
    public T set(final int i, final T val) { return this.store.set(Indolently.idx(this, i), val); }

    @Override
    public void add(final int i, final T val) { this.store.add(Indolently.idx(this, i), val); }

    @Override
    public T remove(final int i) { return this.store.remove(Indolently.idx(this, i)); }

    @Override
    public T get(final int i) { return this.store.get(Indolently.idx(this, i)); }

    @Override
    public $list<T> subList(final int from, final int to) {

        int actFrom = Indolently.idx(this, from);

        if (actFrom < 0) actFrom = 0;

        final int actTo;
        if ((from < 0) && (to == 0)) //
            actTo = this.size();
        else //
            actTo = Indolently.idx(this, to);

        return Indolently.$(this.store.subList(actFrom, actTo));
    }

    @Override
    public ListIterator<T> listIterator(final int i) { return this.store.listIterator(Indolently.idx(this, i)); }

    @Override
    public int hashCode() { return this.store.hashCode(); }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(final Object o) { return (this == o) || this.store.equals(o); }

    private static final Class<?> FROZEN = eval(() -> Class.forName("java.util.Collections$UnmodifiableList"));

    boolean frozen() { return (this.store instanceof $list_impl) && (($list_impl<?>) this.store).frozen() || this.store.getClass() == FROZEN; }
}

interface $list_optimized<T>
    extends $list<T> {

    @Override
    default $<T> head(final Predicate<? super T> f) {
        if (!this.randomAccessible()) return $list.super.head(f);

        final var size = this.size();
        if (size != 0) //
            for (int i = 0; i < size; i++) {
                final var val = this.get(i);
                if (f.test(val)) return $.of(val);
            }

        return $.none();
    }

    @Override
    default $<T> last(final Predicate<? super T> f) {
        if (!this.randomAccessible()) return $list.super.last(f);

        final int size = this.size();
        if (size != 0) //
            for (int i = size - 1; 0 <= i; i--) {
                final var val = this.get(i);
                if (f.test(val)) return $.of(val);
            }

        return $.none();
    }

    @Override
    default <R> $<R> fhead(final Function<T, $<R>> f) {
        if (!this.randomAccessible()) return $list.super.fhead(f);

        final int size = this.size();
        if (size != 0) //
            for (int i = 0; i < size; i++) {
                final var x = f.apply(this.get(i));
                if (x.present()) return x;
            }

        return $.none();
    }

    @Override
    default <R> $<R> flast(final Function<T, $<R>> f) {
        if (!this.randomAccessible()) return $list.super.flast(f);

        final int size = this.size();
        if (size != 0) //
            for (int i = size - 1; 0 <= i; i--) {
                final var x = f.apply(this.get(i));
                if (x.present()) return x;
            }

        return $.none();
    }

    @Override
    default void forEach(final Consumer<? super T> f) { this.eachTry(f::accept); }

    @Override
    default <E extends Exception> $list<T> eachTry(final ConsumerE<? super T, E> f) throws E {
        if (!this.randomAccessible()) return $list.super.eachTry(f);

        final int size = this.size();
        switch (size) {
            case 0 -> { }
            case 1 -> f.accept(this.get(0));
            default -> {
                for (int i = 0; i < size; i++)
                    f.accept(this.get(i));
            }
        }

        return this.identity();
    }

    @Override
    default boolean any(final Predicate<? super T> f) {
        if (!this.randomAccessible()) return $list.super.any(f);

        final int size = this.size();
        if (size != 0) //
            for (int i = 0; i < size; i++)
                if (f.test(this.get(i))) return true;

        return false;
    }

    @Override
    default <R> $<R> reduce(final $<? extends R> initial, final Function3<Integer, ? super R, ? super T, ? extends R> f) {
        if (!this.randomAccessible()) return $list.super.reduce(initial, f);

        final int size = this.size();
        return switch (size) {
            case 0 -> cast(initial);
            case 1 -> $.of(f.apply(0, initial.orNull(), this.get(0)));
            default -> {
                var rem = initial.orNull();
                for (int i = 0; i < size; i++)
                    rem = f.apply(i, rem, this.get(i));

                yield $.of(rem);
            }
        };
    }

    @Override
    default <R> $list<R> map(final Function<? super T, ? extends R> f) {
        if (!this.randomAccessible()) return $list.super.map(f);

        final $list<R> ret = Indolently.list();

        final int size = this.size();
        if (size != 0) //
            for (int i = 0; i < size; i++)
                ret.add(f.apply(this.get(i)));

        return ret;
    }
}
