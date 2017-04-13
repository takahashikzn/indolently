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
package jp.root42.indolently.bridge;

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
 * @param <T> type of value
 * @author takahashikzn
 */
public class StreamDelegate<T>
    extends ObjDelegate<Stream<T>>
    implements Stream<T> {

    private final Stream<T> store;

    public StreamDelegate(final Stream<T> store) {
        this.store = store;
    }

    @Override
    protected Stream<T> getDelegate() {
        return this.store;
    }

    @Override
    public Iterator<T> iterator() {
        return this.getDelegate().iterator();
    }

    @Override
    public Spliterator<T> spliterator() {
        return this.getDelegate().spliterator();
    }

    @Override
    public boolean isParallel() {
        return this.getDelegate().isParallel();
    }

    @Override
    public Stream<T> sequential() {
        return this.getDelegate().sequential();
    }

    @Override
    public Stream<T> parallel() {
        return this.getDelegate().parallel();
    }

    @Override
    public Stream<T> unordered() {
        return this.getDelegate().unordered();
    }

    @Override
    public Stream<T> onClose(final Runnable closeHandler) {
        return this.getDelegate().onClose(closeHandler);
    }

    @Override
    public void close() {
        this.getDelegate().close();
    }

    @Override
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return this.getDelegate().filter(predicate);
    }

    @Override
    public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
        return this.getDelegate().map(mapper);
    }

    @Override
    public IntStream mapToInt(final ToIntFunction<? super T> mapper) {
        return this.getDelegate().mapToInt(mapper);
    }

    @Override
    public LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        return this.getDelegate().mapToLong(mapper);
    }

    @Override
    public DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return this.getDelegate().mapToDouble(mapper);
    }

    @Override
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return this.getDelegate().flatMap(mapper);
    }

    @Override
    public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return this.getDelegate().flatMapToInt(mapper);
    }

    @Override
    public LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        return this.getDelegate().flatMapToLong(mapper);
    }

    @Override
    public DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        return this.getDelegate().flatMapToDouble(mapper);
    }

    @Override
    public Stream<T> distinct() {
        return this.getDelegate().distinct();
    }

    @Override
    public Stream<T> sorted() {
        return this.getDelegate().sorted();
    }

    @Override
    public Stream<T> sorted(final Comparator<? super T> comparator) {
        return this.getDelegate().sorted(comparator);
    }

    @Override
    public Stream<T> peek(final Consumer<? super T> action) {
        return this.getDelegate().peek(action);
    }

    @Override
    public Stream<T> limit(final long maxSize) {
        return this.getDelegate().limit(maxSize);
    }

    @Override
    public Stream<T> skip(final long n) {
        return this.getDelegate().skip(n);
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        this.getDelegate().forEach(action);
    }

    @Override
    public void forEachOrdered(final Consumer<? super T> action) {
        this.getDelegate().forEachOrdered(action);
    }

    @Override
    public Object[] toArray() {
        return this.getDelegate().toArray();
    }

    @Override
    public <A> A[] toArray(final IntFunction<A[]> generator) {
        return this.getDelegate().toArray(generator);
    }

    @Override
    public T reduce(final T identity, final BinaryOperator<T> accumulator) {
        return this.getDelegate().reduce(identity, accumulator);
    }

    @Override
    public Optional<T> reduce(final BinaryOperator<T> accumulator) {
        return this.getDelegate().reduce(accumulator);
    }

    @Override
    public <U> U reduce(final U identity, final BiFunction<U, ? super T, U> accumulator,
        final BinaryOperator<U> combiner) {
        return this.getDelegate().reduce(identity, accumulator, combiner);
    }

    @Override
    public <R> R collect(final Supplier<R> supplier, final BiConsumer<R, ? super T> accumulator,
        final BiConsumer<R, R> combiner) {
        return this.getDelegate().collect(supplier, accumulator, combiner);
    }

    @Override
    public <R, A> R collect(final Collector<? super T, A, R> collector) {
        return this.getDelegate().collect(collector);
    }

    @Override
    public Optional<T> min(final Comparator<? super T> comparator) {
        return this.getDelegate().min(comparator);
    }

    @Override
    public Optional<T> max(final Comparator<? super T> comparator) {
        return this.getDelegate().max(comparator);
    }

    @Override
    public long count() {
        return this.getDelegate().count();
    }

    @Override
    public boolean anyMatch(final Predicate<? super T> predicate) {
        return this.getDelegate().anyMatch(predicate);
    }

    @Override
    public boolean allMatch(final Predicate<? super T> predicate) {
        return this.getDelegate().allMatch(predicate);
    }

    @Override
    public boolean noneMatch(final Predicate<? super T> predicate) {
        return this.getDelegate().noneMatch(predicate);
    }

    @Override
    public Optional<T> findFirst() {
        return this.getDelegate().findFirst();
    }

    @Override
    public Optional<T> findAny() {
        return this.getDelegate().findAny();
    }
}
