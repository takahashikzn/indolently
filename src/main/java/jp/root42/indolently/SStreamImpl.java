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

import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;


/**
 * Extended {@link Stream} class for indolent person.
 * It's name comes from "Sugared iterator".
 *
 * @param <T> value type
 * @author takahashikzn
 */
class SStreamImpl<T>
    extends DelegatingStream<T>
    implements SStream<T> {

    public SStreamImpl(final Stream<T> store) {
        super(store);
    }

    @Override
    public SStream<T> filter(final Predicate<? super T> f) {
        return new SStreamImpl<>(super.filter(f));
    }
}


class DelegatingStream<T>
    implements Stream<T> {

    private final Stream<T> store;

    public DelegatingStream(final Stream<T> store) {
        this.store = store;
    }

    @Override
    public Iterator<T> iterator() {
        return this.store.iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return this.store.spliterator();
    }

    @Override
    public boolean isParallel() {
        return this.store.isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return this.store.sequential();
    }

    @Override
    public Stream<T> parallel() {
        return this.store.parallel();
    }

    @Override
    public Stream<T> unordered() {
        return this.store.unordered();
    }

    @Override
    public Stream<T> onClose(final Runnable closeHandler) {
        return this.store.onClose(closeHandler);
    }

    @Override
    public void close() {
        this.store.close();
    }

    @Override
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return this.store.filter(predicate);
    }

    @Override
    public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
        return this.store.map(mapper);
    }

    @Override
    public IntStream mapToInt(final ToIntFunction<? super T> mapper) {
        return this.store.mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        return this.store.mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return this.store.mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return this.store.flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return this.store.flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        return this.store.flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        return this.store.flatMapToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return this.store.distinct();
    }

    @Override
    public Stream<T> sorted() {
        return this.store.sorted();
    }

    @Override
    public Stream<T> sorted(final Comparator<? super T> comparator) {
        return this.store.sorted(comparator);
    }

    @Override
    public Stream<T> peek(final Consumer<? super T> action) {
        return this.store.peek(action);
    }

    @Override
    public Stream<T> limit(final long maxSize) {
        return this.store.limit(maxSize);
    }

    @Override
    public Stream<T> skip(final long n) {
        return this.store.skip(n);
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        this.store.forEach(action);
    }

    @Override
    public void forEachOrdered(final Consumer<? super T> action) {
        this.store.forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return this.store.toArray();
    }

    @Override
    public <A> A[] toArray(final IntFunction<A[]> generator) {
        return this.store.toArray(generator);
    }

    @Override
    public T reduce(final T identity, final BinaryOperator<T> accumulator) {
        return this.store.reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(final BinaryOperator<T> accumulator) {
        return this.store.reduce(accumulator);
    }

    @Override
    public <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator,
        final BinaryOperator<U> combiner) {
        return this.store.reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator,
        final BiConsumer<R, R> combiner) {
        return this.store.collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(final Collector<? super T, A, R> collector) {
        return this.store.collect(collector);
    }

    @Override
    public Optional<T> min(final Comparator<? super T> comparator) {
        return this.store.min(comparator);
    }

    @Override
    public Optional<T> max(final Comparator<? super T> comparator) {
        return this.store.max(comparator);
    }

    @Override
    public long count() {
        return this.store.count();
    }

    @Override
    public boolean anyMatch(final Predicate<? super T> predicate) {
        return this.store.anyMatch(predicate);
    }

    @Override
    public boolean allMatch(final Predicate<? super T> predicate) {
        return this.store.allMatch(predicate);
    }

    @Override
    public boolean noneMatch(final Predicate<? super T> predicate) {
        return this.store.noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return this.store.findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return this.store.findAny();
    }
}
