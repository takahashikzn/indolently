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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import jp.root42.indolently.bridge.BytesInputStream;
import jp.root42.indolently.bridge.BytesOutputStream;
import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.conc.Promise;
import jp.root42.indolently.conc.Promissory;
import jp.root42.indolently.function.RunnableE;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.ref.$;
import jp.root42.indolently.ref.$$;
import jp.root42.indolently.ref.$2;
import jp.root42.indolently.ref.$3;
import jp.root42.indolently.ref.$bool;
import jp.root42.indolently.ref.$byte;
import jp.root42.indolently.ref.$char;
import jp.root42.indolently.ref.$double;
import jp.root42.indolently.ref.$float;
import jp.root42.indolently.ref.$int;
import jp.root42.indolently.ref.$long;
import jp.root42.indolently.ref.$short;
import jp.root42.indolently.ref.$void;
import jp.root42.indolently.ref.$voidc;
import jp.root42.indolently.ref.Ref;
import jp.root42.indolently.regex.AutomatonTest;
import jp.root42.indolently.regex.ReTest;
import jp.root42.indolently.regex.Regex;
import jp.root42.indolently.regex.RegexJDK;
import jp.root42.indolently.regex.RegexRe2;

import static jp.root42.indolently.Expressive.*;


/**
 * The Java Syntactic sugar collection for indolent person (like you).
 *
 * @author takahashikzn
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Indolently {

    /** non private for subtyping. */
    protected Indolently() { }

    static int idx(final List<?> list, final int idx) {
        return 0 <= idx ? idx : list.size() + idx;
    }

    /**
     * A shortcut of {@code System.currentTimeMillis()}.
     *
     * @return current time in milliseconds.
     */
    public static long now() {
        return System.currentTimeMillis();
    }

    /**
     * Cast the value.
     *
     * @param o the value
     * @return casted value
     */
    @SuppressWarnings("unchecked")
    public static <T> T cast(final Object o) {
        return (T) o;
    }

    public static <T> Function<Object, $<T>> castTo(final Class<T> type) {
        return x -> opt(x).when(type::isInstance).map(type::cast);
    }

    public static <T> Function<Object, $<? extends T>> castAs(final Class<T> type) {
        return x -> opt(x).when(type::isInstance).map(type::cast);
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static int unbox(final Integer x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static long unbox(final Long x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static short unbox(final Short x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static byte unbox(final Byte x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static float unbox(final Float x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static double unbox(final Double x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static char unbox(final Character x) {
        return (x == null) ? 0 : x;
    }

    /**
     * null-safe wrapper to primitive conversion.
     *
     * @param x wrapper value
     * @return primitive value
     */
    public static boolean unbox(final Boolean x) {
        return (x != null) && x;
    }

    public static <T> T fatal() {
        throw new AssertionError();
    }

    public static <T> T fatal(final Object msg) {
        throw new AssertionError(msg);
    }

    public static <T> T TODO() {
        throw new UnsupportedOperationException();
    }

    public static <T> T TODO(final Object msg) {
        throw new UnsupportedOperationException();
    }

    public static <T> T FIXME() {
        throw new UnsupportedOperationException();
    }

    public static <T> T FIXME(final Object msg) {
        throw new UnsupportedOperationException();
    }

    public static <T> T UNSUPPORTED() {
        throw new UnsupportedOperationException();
    }

    public static <T> T UNSUPPORTED(final Object msg) {
        throw new UnsupportedOperationException();
    }

    public static void ASSERT(final boolean flag) { if (!flag) throw new AssertionError(); }

    public static void ASSERT(final boolean flag, final Object msg) {
        if (!flag) throw new AssertionError("" + msg);
    }

    public static void ASSERT(final boolean flag, final Supplier<?> msg) {
        if (!flag) throw new AssertionError("" + msg.get());
    }

    private static final boolean assertive;

    static {
        boolean b;
        try {
            assert false;
            b = false;
        } catch (final AssertionError ignored) {
            b = true;
        }
        assertive = b;
    }

    public static boolean ASSERTIVE() { return assertive; }

    /**
     * An alias of {@link #optionalEmpty(Map)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     */
    public static <T extends Map<?, ?>> $<T> nonEmpty(final T value) {
        return optionalEmpty(value);
    }

    /**
     * {@link Optional} representation of collection.
     * Equivalent to {@code empty(col) ? Optional.empty() : Optional.of(col)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     * @see #empty(Map)
     */
    public static <T extends Map<?, ?>> $<T> optionalEmpty(final T value) {
        return empty(value) ? $.none() : $.of(value);
    }

    /**
     * An alias of {@link #optionalEmpty(Iterable)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     */
    public static <T extends Iterable<?>> $<T> nonEmpty(final T value) {
        return optionalEmpty(value);
    }

    /**
     * {@link Optional} representation of collection.
     * Equivalent to {@code empty(col) ? Optional.empty() : Optional.of(col)}.
     *
     * @param <T> type of value
     * @param value collection value
     * @return Optional representation of collection
     * @see #empty(Iterable)
     */
    public static <T extends Iterable<?>> $<T> optionalEmpty(final T value) {
        return empty(value) ? $.none() : $.of(value);
    }

    /**
     * An alias of {@link #optionalEmpty(CharSequence)}.
     *
     * @param <T> type of value
     * @param value string value
     * @return Optional representation of string
     */
    public static <T extends CharSequence> $<T> nonEmpty(final T value) {
        return optionalEmpty(value);
    }

    /**
     * {@link Optional} representation of string.
     * Equivalent to {@code empty(str) ? Optional.empty() : Optional.of(str)}.
     *
     * @param <T> type of value
     * @param value string value
     * @return Optional representation of string
     * @see #empty(CharSequence)
     */
    public static <T extends CharSequence> $<T> optionalEmpty(final T value) {
        return empty(value) ? $.none() : $.of(value);
    }

    /**
     * An alias of {@link #optionalBlank(CharSequence)}.
     *
     * @param <T> type of value
     * @param value string value
     * @return Optional representation of string
     */
    public static <T extends CharSequence> $<T> nonBlank(final T value) {
        return optionalBlank(value);
    }

    /**
     * {@link Optional} representation of string.
     * Equivalent to {@code blank(str) ? Optional.empty() : Optional.of(str)}.
     *
     * @param <T> type of value
     * @param value string value
     * @return Optional representation of string
     * @see #blank(CharSequence)
     */
    public static <T extends CharSequence> $<T> optionalBlank(final T value) {
        return blank(value) ? $.none() : $.of(value);
    }

    /**
     * An alias of {@link #opt(Object)}.
     *
     * @param <T> type of value
     * @param value the value
     * @return Optional representation of value
     */
    public static <T> $<T> nonNull(final T value) { return opt(value); }

    /**
     * An alias of {@link $#none()}.
     *
     * @return Optional representation of nothing
     */
    public static <T> $<T> none() { return $.none(); }

    /**
     * An alias of {@link Optional#empty()}.
     *
     * @return Optional representation of nothing
     */
    public static <T> Optional<T> none$() { return Optional.empty(); }

    public static <T> T nullable(final $<T> value) { return value.orNull(); }

    public static <T> Function<T, $<T>> opt() { return x -> opt(x); }

    /**
     * An alias of {@link Optional#ofNullable(Object)}.
     *
     * @param <T> type of value
     * @param value the value
     * @return Optional representation of value
     */
    public static <T> $<T> opt(final T value) { return $.of(value); }

    /**
     * An alias of {@link Optional#ofNullable(Object)}.
     *
     * @param <T> type of value
     * @param value the value
     * @param consumers invoked consumers only if value is present
     * @return Optional representation of value
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $<T> opt(final T value, final Consumer<? super T>... consumers) {
        final $<T> opt = opt(value);
        list(consumers).each(f -> opt.tap(f));
        return opt;
    }

    public static <T1, T2, R> $<R> opt(final $<T1> root, final Function<T1, T2> c1, final Function<T2, R> last) {
        return root.map(c1).fmap(opt()).map(last).fmap(opt());
    }

    public static <T1, T2, T3, R> $<R> opt(final $<T1> root, final Function<T1, T2> c1, final Function<T2, T3> c2,
        final Function<T3, R> last) {
        return opt(root, c1, c2).map(last).fmap(opt());
    }

    public static <T1, T2, T3, T4, R> $<R> opt(final $<T1> root, final Function<T1, T2> c1, final Function<T2, T3> c2,
        final Function<T3, T4> c3, final Function<T4, R> last) {
        return opt(root, c1, c2, c3).map(last).fmap(opt());
    }

    public static <T1, T2, T3, T4, T5, R> $<R> opt(final $<T1> root, final Function<T1, T2> c1,
        final Function<T2, T3> c2, final Function<T3, T4> c3, final Function<T4, T5> c4, final Function<T5, R> last) {
        return opt(root, c1, c2, c3, c4).map(last).fmap(opt());
    }

    public static <T1, T2, T3, T4, T5, T6, R> $<R> opt(final $<T1> root, final Function<T1, T2> c1,
        final Function<T2, T3> c2, final Function<T3, T4> c3, final Function<T4, T5> c4, final Function<T5, T6> c5,
        final Function<T6, R> last) {
        return opt(root, c1, c2, c3, c4, c5).map(last).fmap(opt());
    }

    public static <T1, T2, R> $<R> opt(final T1 root, final Function<T1, T2> c1,
        final Function<T2, R> last) { return opt(opt(root), c1, last); }

    public static <T1, T2, T3, R> $<R> opt(final T1 root, final Function<T1, T2> c1, final Function<T2, T3> c2,
        final Function<T3, R> last) { return opt(opt(root), c1, c2, last); }

    public static <T1, T2, T3, T4, R> $<R> opt(final T1 root, final Function<T1, T2> c1, final Function<T2, T3> c2,
        final Function<T3, T4> c3, final Function<T4, R> last) {
        return opt(opt(root), c1, c2, c3, last);
    }

    public static <T1, T2, T3, T4, T5, R> $<R> opt(final T1 root, final Function<T1, T2> c1, final Function<T2, T3> c2,
        final Function<T3, T4> c3, final Function<T4, T5> c4, final Function<T5, R> last) {
        return opt(opt(root), c1, c2, c3, c4, last);
    }

    public static <T1, T2, T3, T4, T5, T6, R> $<R> opt(final T1 root, final Function<T1, T2> c1,
        final Function<T2, T3> c2, final Function<T3, T4> c3, final Function<T4, T5> c4, final Function<T5, T6> c5,
        final Function<T6, R> last) { return opt(opt(root), c1, c2, c3, c4, c5, last); }

    public static <L, R> $$<L, R> left(final L val) { return $$.left(val); }

    public static <L, R> $$<L, R> right(final R val) { return $$.right(val); }

    public static Predicate<$$<?, ?>> isLeft() { return x -> x.isL(); }

    public static Predicate<$$<?, ?>> isRight() { return x -> x.isR(); }

    public static <L, R> Function<L, $$<L, R>> left() { return Indolently::left; }

    public static <L, R> Function<R, $$<L, R>> right() { return Indolently::right; }

    public static final $$.None NONE = $$.None.NONE;

    /**
     * SQL's coalesce function. The name comes from ELVis operator.
     *
     * @param value value
     * @param other default value
     * @return value or default value
     */
    public static <T> T elv(final T value, final T other) { return (value == null) ? other : value; }

    /**
     * SQL's coalesce function. The name comes from ELVis operator.
     *
     * @param value value
     * @param other0 default value
     * @param other1 default value
     * @return value or default value
     */
    public static <T> T elv(final T value, final T other0, final T other1) { return elv(elv(value, other0), other1); }

    /**
     * SQL's coalesce function. The name comes from ELVis operator.
     *
     * @param value value
     * @param other0 default value
     * @param other1 default value
     * @param other2 default value
     * @return value or default value
     */
    public static <T> T elv(final T value, final T other0, final T other1, final T other2) {
        return elv(elv(value, other0, other1), other2);
    }

    /**
     * SQL's coalesce function. The name comes from ELVis operator.
     *
     * @param value value
     * @param other0 default value
     * @param other1 default value
     * @param other2 default value
     * @param other3 default value
     * @return value or default value
     */
    public static <T> T elv(final T value, final T other0, final T other1, final T other2, final T other3) {
        return elv(elv(value, other0, other1, other2), other3);
    }

    public static <T> T elv(final T value, final Supplier<T> other) { return (value == null) ? other.get() : value; }

    public static <T> T elv(final T value, final Supplier<T> other0, final Supplier<T> other1) {
        return elv(elv(value, other0), other1);
    }

    public static <T> T elv(final T value, final Supplier<T> other0, final Supplier<T> other1,
        final Supplier<T> other2) { return elv(elv(value, other0, other1), other2); }

    public static <T> T elv(final T value, final Supplier<T> other0, final Supplier<T> other1, final Supplier<T> other2,
        final Supplier<T> other3) { return elv(elv(value, other0, other1, other2), other3); }

    /**
     * A shortcut notation of {@code Optional.ofNullable(value).map(mapper).orElse(other)}.
     *
     * @param value value
     * @param mapper mapper function
     * @param other default value
     * @return mapped value or default value
     */
    public static <T, S> S optional(final T value, final Function<? super T, S> mapper, final S other) {
        return opt(value).map(mapper).or(other);
    }

    /**
     * A shortcut notation of {@code Optional.ofNullable(value).map(mapper).orElseGet(other)}.
     *
     * @param value value
     * @param mapper mapper function
     * @param other default value supplier
     * @return mapped value or default value
     */
    public static <T, S> S optional(final T value, final Function<? super T, S> mapper, final Supplier<S> other) {
        return opt(value).map(mapper).or(other);
    }

    /**
     * Just an alias of {@link #list(Object...)} but not overloaded one.
     * If compiler fail to do type inference on {@link #list(Object...)}, for example such a
     * {@code List<List<Integer>> nested = list(list(42))}, you can use this method instead of.
     *
     * @param <T> type of value
     * @param elems elements of list
     * @return new list
     * @see #list(Object...)
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $list<T> listOf(final T... elems) {
        return list(elems);
    }

    /**
     * Just an alias of {@link #list(Object...)} but not overloaded one.
     * If compiler fail to do type inference on {@link #list(Object...)} with no argument, for example such a
     * {@code let(list(), x -> x.add("foo"))}, you can use this method instead of.
     *
     * @param <T> type of value
     * @param type type
     * @return new list
     * @see #list(Object...)
     */
    public static <T> $list<T> newList(final Class<T> type) {
        return list();
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param <T> type of value
     * @param elem element of the list
     * @return new list
     */
    public static <T> $list<T> list(final $<? extends T> elem) {
        return new $list_impl<T>().push(elem);
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param <T> type of value
     * @param elems elements of the list
     * @return new list
     */
    public static <T> $list<T> list(final Iterable<? extends T> elems) {
        return new $list_impl<T>().pushAll(opt(elems));
    }

    /**
     * construct new list which contains specified elements.
     *
     * @param <T> type of value
     * @param elems elements of the list
     * @return new list
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $list<T> list(final T... elems) {

        final $list<T> list = new $list_impl<>();

        if (elems != null) {
            Collections.addAll(list, elems);
        }

        return list;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <V, T extends Predicate<V>> $<T> find(final V cond, final T... preds) {

        for (final T p: preds) {
            if (p.test(cond)) {
                return $.of(p);
            }
        }

        return $.none();
    }

    /**
     * Convert {@link Iterable} to typed array.
     * The array type is same as first element type.
     *
     * @param <T> type of value
     * @param elems elements
     * @return typed array
     * @throws IllegalArgumentException iterable don't contain any element.
     */
    public static <T> T[] array(final Iterable<? extends T> elems) {
        if (empty(elems)) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        final $list<? extends T> list = list(elems);

        // pseudo typing. actually this type wouldn't be T[].
        @SuppressWarnings({ "unchecked", "SuspiciousArrayCast" })
        final T[] pseudoTyped = (T[]) list.tail().toArray();

        return array(list.head(), pseudoTyped);
    }

    /**
     * Convert arguments to typed array.
     * The array type is same as first element type.
     *
     * @param <T> type of value
     * @param first first element
     * @param rest rest elements
     * @return typed array
     * @throws IllegalArgumentException first element is null..
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression", "unchecked" })
    public static <T> T[] array(final T first, final T... rest) {
        if (first == null) {
            throw new IllegalArgumentException("can't infer empty array type");
        }

        final int len = 1 + ((rest == null) ? 0 : rest.length);

        return list(first) //
            .pushAll(list(rest)) //
            .toArray((T[]) Array.newInstance(first.getClass(), len));
    }

    /**
     * Convert arguments to typed array.
     *
     * @param <T> type of array
     * @param <V> type of value
     * @param type array type
     * @param first first element
     * @param rest rest elements
     * @return typed array
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression", "unchecked" })
    public static <T, V extends T> T[] array(final Class<T> type, final V first, final V... rest) {

        if (first == null) {
            return cast(Array.newInstance(type, 0));
        }

        final int len = 1 + ((rest == null) ? 0 : rest.length);

        return list(first) //
            .pushAll(list(rest)) //
            .toArray((T[]) Array.newInstance(type, len));
    }

    /**
     * Convert arguments to typed array.
     *
     * @param elems elements of array
     * @return {@link Object} array
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> T[] arrayOf(final T... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new Object[] { ... }}.
     *
     * @param elems elements of array
     * @return {@link Object} array
     */
    public static Object[] oarray(final Object... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new char[] { ... }}.
     *
     * @param elems elements of array
     * @return char array
     */
    public static char[] parray(final char... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new int[] { ... }}.
     *
     * @param elems elements of array
     * @return int array
     */
    public static int[] parray(final int... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new long[] { ... }}.
     *
     * @param elems elements of array
     * @return long array
     */
    public static long[] parray(final long... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new float[] { ... }}.
     *
     * @param elems elements of array
     * @return float array
     */
    public static float[] parray(final float... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new byte[] { ... }}.
     *
     * @param elems elements of array
     * @return byte array
     */
    public static byte[] parray(final byte... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new double[] { ... }}.
     *
     * @param elems elements of array
     * @return double array
     */
    public static double[] parray(final double... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new boolean[] { ... }}.
     *
     * @param elems elements of array
     * @return boolean array
     */
    public static boolean[] parray(final boolean... elems) {
        return elems;
    }

    /**
     * The shortcut notation of {@code new byte[] { (byte) 0xFF, ... }}.
     *
     * @param elems elements of array
     * @return int array
     */
    public static byte[] bytes(final int... elems) {

        final byte[] ret = new byte[elems.length];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = (byte) elems[i];
        }

        return ret;
    }

    /**
     * Create an iterator of {@code char}.
     *
     * @param cs character sequence
     * @return char iterator
     */
    public static $iter<Character> chars(final CharSequence cs) {

        return $(new Iterator<Character>() {

            private final int len = cs.length();

            private int pos;

            @Override
            public boolean hasNext() {
                return this.pos < this.len;
            }

            @Override
            public Character next() {
                //noinspection ProhibitedExceptionCaught
                try {
                    return cs.charAt(this.pos++);
                } catch (final IndexOutOfBoundsException e) {
                    throw (NoSuchElementException) new NoSuchElementException().initCause(e);
                }
            }
        });
    }

    public static <T> $<T> head(final T[] ary) {
        return empty(ary) ? none() : opt(ary[0]);
    }

    public static <T> $<T> last(final T[] ary) {
        return empty(ary) ? none() : opt(ary[ary.length - 1]);
    }

    /**
     * Create a list of {@code char}.
     *
     * @param elems elements of array
     * @return char list
     */
    public static $list<Character> plist(final char... elems) {
        final $list<Character> list = list();
        for (final char e: elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of {@code int}.
     *
     * @param elems elements of array
     * @return int list
     */
    public static $list<Integer> plist(final int... elems) {
        final $list<Integer> list = list();
        for (final int e: elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of {@code long}.
     *
     * @param elems elements of array
     * @return long list
     */
    public static $list<Long> plist(final long... elems) {
        final $list<Long> list = list();
        for (final long e: elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of {@code float}.
     *
     * @param elems elements of array
     * @return float list
     */
    public static $list<Float> plist(final float... elems) {
        final $list<Float> list = list();
        for (final float e: elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of {@code short}.
     *
     * @param elems elements of array
     * @return short list
     */
    public static $list<Short> plist(final short... elems) {
        final $list<Short> list = list();
        for (final short e: elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of {@code double}.
     *
     * @param elems elements of array
     * @return double list
     */
    public static $list<Double> plist(final double... elems) {
        final $list<Double> list = list();
        for (final double e: elems) {
            list.add(e);
        }
        return list;
    }

    /**
     * Create a list of {@code boolean}.
     *
     * @param elems elements of array
     * @return boolean list
     */
    public static $list<Boolean> plist(final boolean... elems) {
        final $list<Boolean> list = list();
        for (final boolean e: elems) {
            list.add(e);
        }
        return list;
    }

    public static <T> $set<T> set(final $<? extends T> elem) {
        return new $set_impl<T>().push(elem);
    }

    public static <T> $set<T> set(final Iterable<? extends T> elems) {
        return new $set_impl<T>().pushAll(opt(elems));
    }

    /**
     * Just an alias of {@link #set(Object...)} but not overloaded one.
     * If compiler fail to do type inference on {@link #set(Object...)}, for example such a
     * {@code Set<List<Integer>> nested = set(list(42))}, you can use this method instead of.
     *
     * @param <T> type of value
     * @param elems elements of set
     * @return new set
     * @see #set(Object...)
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $set<T> setOf(final T... elems) {
        return set(elems);
    }

    /**
     * Just an alias of {@link #set(Object...)} but not overloaded one.
     * If compiler fail to do type inference on {@link #set(Object...)}, for example such a
     * {@code let(set(), x -> x.add("foo"))}, you can use this method instead of.
     *
     * @param <T> type of value
     * @param type type
     * @return new set
     * @see #set(Object...)
     */
    public static <T> $set<T> newSet(final Class<T> type) {
        return set();
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $set<T> set(final T... elems) {

        final $set<T> set = new $set_impl<>();

        if (elems != null) {
            set.addAll(list(elems));
        }

        return set;
    }

    public static <K extends Comparable<K>, V> $map<K, V> sort(final Map<? extends K, ? extends V> map) {
        return sort(map, Comparator.naturalOrder());
    }

    public static <K, V, S extends Comparable<S>> $map<K, V> sort(final Map<? extends K, ? extends V> map,
        final Function<? super K, ? extends S> f) {

        return sort(map, Comparator.comparing(f::apply));
    }

    public static <K, V> $map<K, V> sort(final Map<? extends K, ? extends V> map, final Comparator<? super K> comp) {
        return $(ObjFactory.getInstance().<K, V> newSortedMap(Objects.requireNonNull(comp, "comparator"))).pushAll(map);
    }

    public static <T extends Comparable<T>> $set<T> sort(final Set<? extends T> elems) {
        return sort(elems, Comparator.naturalOrder());
    }

    public static <T, S extends Comparable<S>> $set<T> sort(final Set<? extends T> elems,
        final Function<? super T, ? extends S> f) {

        return sort(elems, Comparator.comparing(f::apply));
    }

    public static <T> $set<T> sort(final Set<? extends T> elems, final Comparator<? super T> comp) {
        return $(ObjFactory.getInstance().<T> newSortedSet(Objects.requireNonNull(comp, "comparator"))).pushAll(elems);
    }

    public static <T extends Comparable<T>> $list<T> sort(final List<? extends T> elems) {
        return sort(elems, Comparator.naturalOrder());
    }

    public static <T, S extends Comparable<S>> $list<T> sort(final List<? extends T> elems,
        final Function<? super T, ? extends S> f) {

        return sort(elems, Comparator.comparing(f::apply));
    }

    public static <T> $list<T> sort(final List<? extends T> elems, final Comparator<? super T> comp) {
        final $list<T> rslt = list(elems);
        rslt.sort(Objects.requireNonNull(comp, "comparator"));
        return rslt;
    }

    public static <T> $list<T> uniq(final List<? extends T> elems) {
        return uniq(elems, (x, y) -> equal(x, y));
    }

    public static <T> $list<T> uniq(final List<? extends T> elems, final BiPredicate<? super T, ? super T> f) {

        return $(elems).reduce(list(), (ret, x) -> {
            if (ret.isEmpty() || !ret.some(y -> f.test(x, y))) {
                ret.add(x);
            }

            return ret;
        });
    }

    @SuppressWarnings("rawtypes")
    private static <T> T freeze0(final T x) {
        if (x instanceof Map) return cast(freeze((Map) x));
        if (x instanceof Set) return cast(freeze((Set) x));
        if (x instanceof List) return cast(freeze((List) x));
        return x;
    }

    public static <K, V> $map<K, V> freeze(final Map<? extends K, ? extends V> map) {
        return (map instanceof $map_impl) && (($map_impl<?, ?>) map).frozen()
            ? cast(map)
            : $(Collections.unmodifiableMap($(map).map(Indolently::freeze0)));
    }

    public static <T> $set<T> freeze(final Set<? extends T> set) {
        return (set instanceof $set_impl) && (($set_impl<?>) set).frozen()
            ? cast(set)
            : $(Collections.unmodifiableSet($(set).map(Indolently::freeze0)));
    }

    public static <T> $list<T> freeze(final List<? extends T> list) {
        return (list instanceof $list_impl) && (($list_impl<?>) list).frozen()
            ? cast(list)
            : $(Collections.unmodifiableList($(list).map(Indolently::freeze0)));
    }

    /**
     * test whether the argument is empty or not.
     *
     * @param i test target
     * @return test result
     */
    public static boolean empty(final Iterable<?> i) {
        if (i == null) return true;

        // for optimization
        if (i instanceof Collection) return ((Collection<?>) i).isEmpty();
        if (i instanceof Map) return ((Map<?, ?>) i).isEmpty();
        if (i instanceof Iterator<?>) return !((Iterator<?>) i).hasNext();
        if (i instanceof Enumeration<?>) return !((Enumeration<?>) i).hasMoreElements();

        return !i.iterator().hasNext();
    }

    /**
     * test whether the argument is empty or not.
     *
     * @param i test target
     * @return test result
     */
    public static boolean empty(final Iterable<?>... i) {
        return empty((Object[]) i) || list(i).every(Indolently::empty);
    }

    /**
     * test whether the argument is empty or not.
     *
     * @param map test target
     * @return test result
     */
    public static boolean empty(final Map<?, ?> map) {
        return (map == null) || map.isEmpty();
    }

    /**
     * test whether the argument is empty or not.
     *
     * @param map test target
     * @return test result
     */
    public static boolean empty(final Map<?, ?>... map) {
        return empty((Object[]) map) || list(map).every(Indolently::empty);
    }

    /**
     * test whether the argument is present or not.
     *
     * @param opt test target
     * @return test result
     */
    public static boolean empty(final $<?> opt) { return isNull(opt) || !opt.present(); }

    /**
     * test whether the argument is present or not.
     *
     * @param opt test target
     * @return test result
     */
    public static boolean empty(final $<?>... opt) { return empty((Object[]) opt) || list(opt).every(x -> empty(x)); }

    /**
     * test whether the argument is present or not.
     *
     * @param opt test target
     * @return test result
     */
    public static boolean empty(final Optional<?> opt) { return isNull(opt) || !opt.isPresent(); }

    /**
     * test whether the argument is present or not.
     *
     * @param opt test target
     * @return test result
     */
    public static boolean empty(final Optional<?>... opt) {
        return empty((Object[]) opt) || list(opt).every(x -> empty(x));
    }

    /**
     * test whether the argument is empty string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean empty(final CharSequence cs) {
        return (cs == null) || (cs == "") || (cs.length() == 0);
    }

    /**
     * test whether the argument is empty string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean empty(final CharSequence... cs) {
        return empty((Object[]) cs) || list(cs).every(x -> empty(x));
    }

    public static boolean present(final $<?> opt) {
        return !empty(opt);
    }

    public static boolean presentAny(final $<?> x, final $<?> y, final $<?>... rest) {
        return present(x) || present(y) || list(rest).some(o -> present(o));
    }

    public static boolean presentAll(final $<?> x, final $<?> y, final $<?>... rest) {
        return present(x) && present(y) && list(rest).every(o -> present(o));
    }

    public static boolean presentNone(final $<?> x, final $<?> y, final $<?>... rest) {
        return !presentAny(x, y, rest);
    }

    /**
     * test whether the argument is null or not.
     *
     * @param o test target
     * @return test result
     */
    public static boolean isNull(final Object o) {
        return o == null;
    }

    /**
     * test whether the argument is blank string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean blank(final CharSequence cs) {
        if (empty(cs)) return true;

        // don't use "cs.chars().allMatch(Character::isWhitespace);" for performance
        for (int i = 0, M = cs.length(); i < M; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) return false;
        }

        return true;
    }

    /**
     * test whether the argument is blank string or not.
     *
     * @param cs test target
     * @return test result
     */
    public static boolean blank(final CharSequence... cs) {
        return empty((Object[]) cs) || list(cs).every(Indolently::blank);
    }

    public static boolean empty(final Object[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final byte[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final int[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final long[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final short[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final float[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final double[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final boolean[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean empty(final char[] ary) { return (ary == null) || (ary.length == 0); }

    public static boolean contains(final Object[] ary, final Object val) {
        if (empty(ary)) return false;

        for (final Object x: ary) {
            if (Objects.equals(x, val)) return true;
        }

        return false;
    }

    public static boolean contains(final byte[] ary, final byte val) {
        if (empty(ary)) return false;

        for (final byte x: ary) {
            if (x == val) return true;
        }

        return false;
    }

    public static boolean contains(final int[] ary, final int val) {
        if (empty(ary)) return false;

        for (final int x: ary) {
            if (x == val) return true;
        }

        return false;
    }

    public static boolean contains(final long[] ary, final long val) {
        if (empty(ary)) return false;

        for (final long x: ary) {
            if (x == val) return true;
        }

        return false;
    }

    public static boolean contains(final short[] ary, final short val) {
        if (empty(ary)) return false;

        for (final short x: ary) {
            if (x == val) return true;
        }

        return false;
    }

    public static boolean contains(final float[] ary, final float val) {
        if (empty(ary)) return false;

        for (final float x: ary) {
            if (equal(x, val)) return true;
        }

        return false;
    }

    public static boolean contains(final double[] ary, final double val) {
        if (empty(ary)) return false;

        for (final double x: ary) {
            if (equal(x, val)) return true;
        }

        return false;
    }

    public static boolean contains(final boolean[] ary, final boolean val) {
        if (empty(ary)) return false;

        for (final boolean x: ary)
            if (x == val) return true;

        return false;
    }

    public static boolean contains(final char[] ary, final char val) {
        if (empty(ary)) return false;

        for (final char x: ary)
            if (x == val) return true;

        return false;
    }

    public static boolean contains(final CharSequence cs, final char c) {

        return (cs != null) && (0 < cs.length()) //
            && ((cs instanceof String) ? (0 <= ((String) cs).indexOf(c)) //
            : (cs instanceof StringBuilder) ? (0 <= ((StringBuilder) cs).indexOf(String.valueOf(c))) //
                : (cs instanceof StringBuffer) ? (0 <= ((StringBuffer) cs).indexOf(String.valueOf(c))) //
                    : (0 <= cs.toString().indexOf(c)));
    }

    public static String join(final CharSequence... ary) { return join(list(ary), null); }

    public static String join(final String[] ary, final String sep) { return join(list(ary), sep); }

    public static String join(final Iterable<? extends CharSequence> col) { return join(col, null); }

    public static String join(final Iterable<? extends CharSequence> col, final String sep) {

        return optional(col, x -> {
            final var sj = new StringJoiner(elv(sep, ""));
            col.forEach(sj::add);
            return sj.toString();
        }, (String) null);
    }

    public static byte[] bytes(final String s) { return bytes(s, StandardCharsets.UTF_8); }

    public static byte[] bytes(final String s, final Charset cs) { return s.getBytes(cs); }

    public static String string(final byte[] b) { return string(b, StandardCharsets.UTF_8); }

    public static String string(final byte[] b, final Charset cs) { return new String(b, cs); }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> T choose(final T... elems) {

        return list(elems) //
            .reduce((rem, val) -> rem != null ? rem : val) //
            .orFail(() -> new IllegalArgumentException("all elements are null"));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> T choose(final Supplier<? extends T>... suppliers) {
        //noinspection RedundantCast
        return choose((T) null, suppliers);
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> T choose(final T initial, final Supplier<? extends T>... suppliers) {

        if (initial != null) return initial;

        if (suppliers != null) {
            for (final var s: suppliers) {
                final var val = s.get();
                if (val != null) return val;
            }
        }

        throw new IllegalArgumentException("all elements are null");
    }

    /**
     * Test that the lower is less than upper.
     * i.e. this method tests {@code l < r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean lt(final double l, final double r) { return Double.compare(l, r) < 0; }

    /**
     * Test that the lower is less equal than upper.
     * i.e. this method tests {@code l <= r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean le(final double l, final double r) { return Double.compare(l, r) <= 0; }

    /**
     * Test that the lower is greater than upper.
     * i.e. this method tests {@code l > r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean gt(final double l, final double r) { return Double.compare(l, r) > 0; }

    /**
     * Test that the lower is greater equal than upper.
     * i.e. this method tests {@code l >= r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean ge(final double l, final double r) { return Double.compare(l, r) >= 0; }

    /**
     * Test that the lower is less than upper.
     * i.e. this method tests {@code l < r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean lt(final long l, final long r) { return l < r; }

    /**
     * Test that the lower is less equal than upper.
     * i.e. this method tests {@code l <= r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean le(final long l, final long r) { return l <= r; }

    /**
     * Test that the lower is greater than upper.
     * i.e. this method tests {@code l > r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean gt(final long l, final long r) { return l > r; }

    /**
     * Test that the lower is greater equal than upper.
     * i.e. this method tests {@code l >= r}.
     *
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static boolean ge(final long l, final long r) { return l >= r; }

    /**
     * Test that the lower is less than upper.
     * i.e. this method tests {@code l < r}.
     *
     * @param <T> value type
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean lt(final T l, final T r) { return l.compareTo(r) < 0; }

    /**
     * Test that the lower is less equal than upper.
     * i.e. this method tests {@code l <= r}.
     *
     * @param <T> value type
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean le(final T l, final T r) { return l.compareTo(r) <= 0; }

    /**
     * Test that the lower is greater than upper.
     * i.e. this method tests {@code l > r}.
     *
     * @param <T> value type
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gt(final T l, final T r) { return l.compareTo(r) > 0; }

    /**
     * Test that the lower is greater equal than upper.
     * i.e. this method tests {@code l >= r}.
     *
     * @param <T> value type
     * @param l left side value
     * @param r right side value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean ge(final T l, final T r) { return l.compareTo(r) >= 0; }

    /**
     * Test that the value is gerater than lower and less than upper.
     * i.e. this method tests {@code lower < val < upper}.
     *
     * @param <T> value type
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gtlt(final T l, final T m, final T u) {
        return lt(l, m) && lt(m, u);
    }

    /**
     * Test that the value is gerater equal lower and less than upper.
     * i.e. this method tests {@code lower <= val < upper}.
     *
     * @param <T> value type
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gelt(final T l, final T m, final T u) {
        return le(l, m) && lt(m, u);
    }

    /**
     * Test that the value is gerater than lower and less equal upper.
     * i.e. this method tests {@code lower < val <= upper}.
     *
     * @param <T> value type
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gtle(final T l, final T m, final T u) {
        return lt(l, m) && le(m, u);
    }

    /**
     * Test that the value is gerater equal lower and less equal upper.
     * i.e. this method tests {@code lower <= val <= upper}.
     *
     * @param <T> value type
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean gele(final T l, final T m, final T u) {
        return le(l, m) && le(m, u);
    }

    /**
     * Test that the value is gerater than lower and less than upper.
     * i.e. this method tests {@code lower < val < upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gtlt(final long l, final long m, final long u) {
        return lt(l, m) && lt(m, u);
    }

    /**
     * Test that the value is gerater equal lower and less than upper.
     * i.e. this method tests {@code lower <= val < upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gelt(final long l, final long m, final long u) {
        return le(l, m) && lt(m, u);
    }

    /**
     * Test that the value is gerater than lower and less equal upper.
     * i.e. this method tests {@code lower < val <= upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gtle(final long l, final long m, final long u) {
        return lt(l, m) && le(m, u);
    }

    /**
     * Test that the value is gerater equal lower and less equal upper.
     * i.e. this method tests {@code lower <= val <= upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gele(final long l, final long m, final long u) {
        return le(l, m) && le(m, u);
    }

    /**
     * Test that the value is gerater than lower and less than upper.
     * i.e. this method tests {@code lower < val < upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gtlt(final double l, final double m, final double u) {
        return lt(l, m) && lt(m, u);
    }

    /**
     * Test that the value is gerater equal lower and less than upper.
     * i.e. this method tests {@code lower <= val < upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gelt(final double l, final double m, final double u) {
        return le(l, m) && lt(m, u);
    }

    /**
     * Test that the value is gerater than lower and less equal upper.
     * i.e. this method tests {@code lower < val <= upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gtle(final double l, final double m, final double u) {
        return lt(l, m) && le(m, u);
    }

    /**
     * Test that the value is gerater equal lower and less equal upper.
     * i.e. this method tests {@code lower <= val <= upper}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean gele(final double l, final double m, final double u) {
        return le(l, m) && le(m, u);
    }

    /**
     * An alias of {@link #gele(Comparable, Comparable, Comparable)}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static <T extends Comparable<T>> boolean between(final T l, final T m, final T u) {
        return gele(l, m, u);
    }

    /**
     * An alias of {@link #gele(long, long, long)}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean between(final long l, final long m, final long u) {
        return gele(l, m, u);
    }

    /**
     * An alias of {@link #gele(double, double, double)}.
     *
     * @param l lower value
     * @param m middle value
     * @param u upper value
     * @return test result
     */
    public static boolean between(final double l, final double m, final double u) {
        return gele(l, m, u);
    }

    public static boolean equal(final long l, final long r) {
        return l == r;
    }

    public static boolean equal(final long l, final long r, final long... rest) {

        if (l != r) {
            return false;
        }

        for (final long x: rest) {
            if (l != x) {
                return false;
            }
        }

        return true;
    }

    public static boolean equal(final double l, final double r) {
        return Double.compare(l, r) == 0;
    }

    public static boolean equal(final double l, final double r, final double... rest) {

        if (Double.compare(l, r) != 0) {
            return false;
        }

        for (final double x: rest) {
            if (Double.compare(l, x) != 0) {
                return false;
            }
        }

        return true;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T extends Comparable<T>> boolean equal(final T l, final T r, final T... rest) {
        return list(r).pushAll(list(rest)).every(x -> equal(l, x));
    }

    public static boolean equal(final Object l, final Object r, final Object... rest) {
        return list(r).pushAll(list(rest)).every(x -> equal(l, x));
    }

    public static <T extends Comparable<T>> boolean equal(final T l, final T r) {
        return (l == null) ? (r == null) : (r != null) && ((r == l) || (l.compareTo(r) == 0));
    }

    public static boolean equal(final Object l, final Object r) {
        return (l == null) ? (r == null) : (r != null) && ((l == r) || l.equals(r));
    }

    public static boolean eql(final Object l, final Object r) { return equal(l, r); }

    public enum ComparisonResult {

        SMALL(-1), EQUAL(0), LARGE(1);

        public final int sign;

        ComparisonResult(final int sign) {
            this.sign = sign;
        }

        public int sign() {
            return this.sign;
        }

        public int sign(final boolean asc) {
            return asc ? this.sign : -this.sign;
        }

        public int reverse() {
            return -this.sign;
        }
    }

    public static <T extends Comparable<T>> ComparisonResult compare(final T l, final T r) {

        final int rslt = l.compareTo(r);

        if (rslt < 0) {
            return ComparisonResult.SMALL;
        } else if (rslt == 0) {
            return ComparisonResult.EQUAL;
        } else {
            return ComparisonResult.LARGE;
        }
    }

    public static <T extends Comparable<T>> ComparisonResult compare(final Iterable<T> l, final Iterable<T> r) {

        if ((l instanceof List) && (r instanceof List)) {
            return compare((List<T>) l, (List<T>) r);
        }

        for (final Iterator<T> li = l.iterator(), ri = r.iterator(); ; ) {

            final var moreL = li.hasNext();
            final var moreR = ri.hasNext();

            if (moreL && moreR) {
                final var rslt = compare(li.next(), ri.next());

                if (rslt != ComparisonResult.EQUAL) {
                    return rslt;
                }
            } else {
                return compare(moreL ? 1 : 0, moreR ? 1 : 0);
            }
        }
    }

    public static <T extends Comparable<T>> ComparisonResult compare(final List<T> l, final List<T> r) {

        final int lsize = l.size();
        final int rsize = r.size();
        final int len = Math.min(lsize, rsize);

        for (int i = 0; i < len; i++) {

            final var rslt = compare(l.get(i), r.get(i));

            if (rslt != ComparisonResult.EQUAL) {
                return rslt;
            }
        }

        return compare(lsize, rsize);
    }

    @TypeUnsafe
    public static <T> boolean equiv(final T l, final T r) {

        if ((l instanceof Comparable) && (r instanceof Comparable)) {
            return equal(cast(l), cast(r));
        } else {
            return equal(l, r);
        }
    }

    @TypeUnsafe
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> boolean equiv(final T l, final T r, final T... rest) {
        return list(r).pushAll(list(rest)).every(x -> equiv(l, x));
    }

    public static <T extends Comparable<T>> T max(final T l, final T r) {
        return ge(l, r) ? l : r;
    }

    public static <T extends Comparable<T>> T min(final T l, final T r) {
        return le(l, r) ? l : r;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T extends Comparable<T>> T max(final T first, final T second, final T... rest) {
        return max(list(rest)).map(x -> max(x, max(first, second))).or(() -> max(first, second));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T extends Comparable<T>> T min(final T first, final T second, final T... rest) {
        return min(list(rest)).map(x -> min(x, min(first, second))).or(() -> min(first, second));
    }

    public static <T extends Comparable<T>> $<T> max(final Iterable<T> values) {
        return list(values).reduce((l, r) -> max(l, r));
    }

    public static <T extends Comparable<T>> $<T> min(final Iterable<T> values) {
        return list(values).reduce((l, r) -> min(l, r));
    }

    public static Class<?> typed(final Class cls) { return cls; }

    public static Map<?, ?> typed(final Map raw) { return raw; }

    public static List<?> typed(final List raw) { return raw; }

    public static Set<?> typed(final Set raw) { return raw; }

    public static <K, V> $map<K, V> map(final Map<? extends K, ? extends V> map) {
        return new $map_impl<K, V>().pushAll(opt(map));
    }

    public static <K, V> $map<K, V> map() { return new $map_impl<>(); }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <T> $list<T> $(final $list<T> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <T> $set<T> $(final $set<T> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <K, V> $map<K, V> $(final $map<K, V> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return clone of the argument
     * @deprecated Use {@link $list#clone()} instead of to get more intentional.
     */
    @Deprecated
    public static <T> $list<T> list(final $list<T> x) {
        return x.clone();
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return clone of the argument
     * @deprecated Use {@link $set#clone()} instead of to get more intentional.
     */
    @Deprecated
    public static <T> $set<T> set(final $set<T> x) {
        return x.clone();
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return clone of the argument
     * @deprecated Use {@link $map#clone()} instead of to get more intentional.
     */
    @Deprecated
    public static <K, V> $map<K, V> map(final $map<K, V> x) {
        return x.clone();
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <T> $iter<T> $(final $iter<T> x) {
        return x;
    }

    /**
     * Just for producing compilation warning.
     *
     * @param x any wrapped one
     * @return argument itself
     * @deprecated this is meaningless method call.
     */
    @Deprecated
    public static <T> $stream<T> $(final $stream<T> x) {
        return x;
    }

    /**
     * Wrap a map.
     *
     * @param map map to wrap
     * @return wrapped map
     */
    public static <K, V> $map<K, V> $(final Map<K, V> map) {
        return (map == null) ? null //
            : (map instanceof $map) ? cast(map) //
                : new $map_impl<>(map);
    }

    /**
     * Wrap a list.
     *
     * @param list list to wrap
     * @return wrapped list
     */
    public static <T> $list<T> $(final List<T> list) {
        return (list == null) ? null //
            : (list instanceof $list) ? cast(list)//
                : new $list_impl<>(list);
    }

    /**
     * Wrap a set.
     *
     * @param set set to wrap
     * @return wrapped set
     */
    public static <T> $set<T> $(final Set<T> set) {
        return (set == null) ? null //
            : (set instanceof $set) ? cast(set) //
                : new $set_impl<>(set);
    }

    /**
     * Wrap a map.
     *
     * @param map map to wrap
     * @param key key to put
     * @param val value to put
     * @return wrapped map
     */
    public static <K, V> $map<K, V> $(final Map<K, V> map, final K key, final V val) {
        return $(Objects.requireNonNull(map, "map")).push(key, val);
    }

    /**
     * Wrap a list.
     *
     * @param list list to wrap
     * @param elems elements to add
     * @return wrapped list
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $list<T> $(final List<T> list, final T... elems) {
        return $(Objects.requireNonNull(list, "list")).pushAll(list(elems));
    }

    /**
     * Wrap a set.
     *
     * @param set set to wrap
     * @param elems elements to add
     * @return wrapped set
     */
    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> $set<T> $(final Set<T> set, final T... elems) {
        return $(Objects.requireNonNull(set, "set")).pushAll(list(elems));
    }

    /**
     * Wrap a iterator.
     *
     * @param iter iterator to wrap
     * @return wrapped iterator
     */
    public static <T> $iter<T> $(final Iterator<? extends T> iter) {
        if (iter == null) return null;

        if (iter instanceof $iter) return cast(iter);

        return new $iter<T>() {

            @Override
            public boolean hasNext() { return iter.hasNext(); }

            @Override
            public T next() { return iter.next(); }

            @Override
            public void remove() { iter.remove(); }
        };
    }

    /**
     * Wrap a stream.
     *
     * @param stream stream to wrap
     * @return wrapped stream
     */
    public static <T> $stream<T> $(final Stream<T> stream) {
        return (stream == null) ? null //
            : (stream instanceof $stream) ? cast(stream) //
                : new $stream_impl<>(stream);
    }

    public static <K, V> $map<K, V> newMap(final Class<K> keyType, final Class<V> valType) { return map(); }

    @SuppressWarnings("unchecked")
    public static <K, V> $map<K, V> map(final K key, final V val) { return ($map<K, V>) map().push(key, val); }

    @SuppressWarnings("unchecked")
    public static <K, V> $map<K, V> map(final K key, final $<? extends V> val) {
        return ($map<K, V>) map().push(key, val);
    }

    /**
     * create two element tuple.
     *
     * @param _1 1st element
     * @param _2 2nd element
     * @return tuple
     */
    public static <F, S> $2<F, S> tuple(final F _1, final S _2) { return new $2<F, S>()._1(_1)._2(_2); }

    /**
     * create three element tuple.
     *
     * @param _1 1st element
     * @param _2 2nd element
     * @param _3 3rd element
     * @return tuple
     */
    public static <F, S, T> $3<F, S, T> tuple(final F _1, final S _2, final T _3) {
        return new $3<F, S, T>()._1(_1)._2(_2)._3(_3);
    }

    public static <F, S> Function<$2<F, S>, F> _1() { return x -> x._1; }

    public static <F, S> Function<$2<F, S>, S> _2() { return x -> x._2; }

    public static <F, S, T> Function<$3<F, S, T>, F> _1of3() { return x -> x._1; }

    public static <F, S, T> Function<$3<F, S, T>, S> _2of3() { return x -> x._2; }

    public static <F, S, T> Function<$3<F, S, T>, T> _3of3() { return x -> x._3; }

    public static <T, R> Function<List<T>, List<R>> mapl(final Function<T, R> f) {
        return (List<T> list) -> $(list).map(f);
    }

    public static <T, R> Function<Set<T>, Set<R>> maps(final Function<T, R> f) {
        return (Set<T> set) -> $(set).map(f);
    }

    public static <K, T, R> Function<Map<K, T>, Map<K, R>> mapm(final Function<T, R> f) {
        return (Map<K, T> map) -> $(map).map(f);
    }

    /**
     * Convert tuple to list.
     *
     * @param tuple two element tuple
     * @return list of tuple elements
     */
    public static <T> $list<T> list(final $2<? extends T, ? extends T> tuple) { return list(tuple._1, tuple._2); }

    /**
     * Convert tuple to list.
     *
     * @param tuple three element tuple
     * @return list of tuple elements
     */
    public static <T> $list<T> list(final $3<? extends T, ? extends T, ? extends T> tuple) {
        return list(tuple._1, tuple._2, tuple._3);
    }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $bool ref(final boolean val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $int ref(final int val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $long ref(final long val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $double ref(final double val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $float ref(final float val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $short ref(final short val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $byte ref(final byte val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static $char ref(final char val) { return Ref.of(val); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static <T> $void<T> ref(final T val) { return Ref.of(val); }

    /**
     * create an empty reference.
     *
     * @return empty reference
     */
    public static <T> $void<T> ref() { return Ref.of((T) null); }

    /**
     * create a reference of value.
     *
     * @param val initial value
     * @return reference of value
     */
    public static <T extends Comparable<T>> $voidc<T> ref(final T val) { return Ref.of(val); }

    // CHECKSTYLE:OFF
    public static <K, V> $map<K, V> map(final K k0, final V v0, final K k1, final V v1) {
        return map(k0, v0).push(k1, v1);
    }

    public static <K, V> $map<K, V> map(final K k0, final V v0, final K k1, final V v1, final K k2, final V v2) {
        return map(k0, v0, k1, v1).push(k2, v2);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3) {
        return map(k0, v0, k1, v1, k2, v2).push(k3, v3);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4,
        final V v4) {
        return map(k0, v0, k1, v1, k2, v2, k3, v3).push(k4, v4);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4,
        final V v4, final K k5, final V v5) {
        return map(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4).push(k5, v5);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4,
        final V v4, final K k5, final V v5, final K k6, final V v6) {
        return map(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5).push(k6, v6);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4,
        final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7) {
        return map(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6).push(k7, v7);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4,
        final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8) {

        return map(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7).push(k8, v8);
    }

    public static <K, V> $map<K, V> map( //
        final K k0, final V v0, final K k1, final V v1, final K k2, final V v2, final K k3, final V v3, final K k4,
        final V v4, final K k5, final V v5, final K k6, final V v6, final K k7, final V v7, final K k8, final V v8,
        final K k9, final V v9) {
        return map(k0, v0, k1, v1, k2, v2, k3, v3, k4, v4, k5, v5, k6, v6, k7, v7, k8, v8).push(k9, v9);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10) {
        return map(k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09,
            v09).push(k10, v10);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10) //
            .push(k11, v11);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11) //
            .push(k12, v12);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12) //
            .push(k13, v13);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13) //
            .push(k14, v14);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14) //
            .push(k15, v15);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15) //
            .push(k16, v16);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16) //
            .push(k17, v17);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17) //
            .push(k18, v18);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18) //
            .push(k19, v19);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19) //
            .push(k20, v20);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20) //
            .push(k21, v21);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21) //
            .push(k22, v22);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22) //
            .push(k23, v23);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23) //
            .push(k24, v24);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24) //
            .push(k25, v25);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24, k25, v25) //
            .push(k26, v26);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24, k25, v25, k26, v26) //
            .push(k27, v27);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24, k25, v25, k26, v26, k27, v27) //
            .push(k28, v28);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24, k25, v25, k26, v26, k27, v27, k28, v28) //
            .push(k29, v29);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24, k25, v25, k26, v26, k27, v27, k28, v28, k29, v29) //
            .push(k30, v30);
    }

    public static <K, V> $map<K, V> map( //
        final K k00, final V v00, final K k01, final V v01, final K k02, final V v02, final K k03, final V v03,
        final K k04, final V v04, final K k05, final V v05, final K k06, final V v06, final K k07, final V v07,
        final K k08, final V v08, final K k09, final V v09, final K k10, final V v10, final K k11, final V v11,
        final K k12, final V v12, final K k13, final V v13, final K k14, final V v14, final K k15, final V v15,
        final K k16, final V v16, final K k17, final V v17, final K k18, final V v18, final K k19, final V v19,
        final K k20, final V v20, final K k21, final V v21, final K k22, final V v22, final K k23, final V v23,
        final K k24, final V v24, final K k25, final V v25, final K k26, final V v26, final K k27, final V v27,
        final K k28, final V v28, final K k29, final V v29, final K k30, final V v30, final K k31, final V v31) {
        return map( //
            k00, v00, k01, v01, k02, v02, k03, v03, k04, v04, k05, v05, k06, v06, k07, v07, k08, v08, k09, v09, //
            k10, v10, k11, v11, k12, v12, k13, v13, k14, v14, k15, v15, k16, v16, k17, v17, k18, v18, k19, v19, //
            k20, v20, k21, v21, k22, v22, k23, v23, k24, v24, k25, v25, k26, v26, k27, v27, k28, v28, k29, v29, //
            k30, v30) //
            .push(k31, v31);
    }

    // CHECKSTYLE:ON

    private static final Function<?, ?> itself = x -> x;

    public static <T> Function<T, T> it() { return itself(); }

    public static <T> Function<T, T> itself() { return cast(itself); }

    public static <T, S> Function<T, S> fixed(final S val) { return x -> val; }

    public static <T> Predicate<T> fixed(final boolean val) { return val ? vrai() : faux(); }

    private static final Predicate<Boolean> asis = x -> x;

    public static Predicate<Boolean> asis() { return asis; }

    private static final Predicate<?> vrai = x -> true;

    public static <T> Predicate<T> vrai() { return cast(vrai); }

    private static final Predicate<?> faux = x -> false;

    public static <T> Predicate<T> faux() { return cast(faux); }

    private static final Predicate<?> nil = x -> x == null;

    public static <T> Predicate<T> nil() { return cast(nil); }

    private static final Predicate<?> nonnil = x -> x != null;

    public static <T> Predicate<T> not(final Predicate<T> f) {
        return (f == nil) ? cast(nonnil) //
            : (f == nonnil) ? cast(nil) //
                : f.negate();
    }

    public static BooleanSupplier and(final BooleanSupplier x0, final BooleanSupplier x1, final BooleanSupplier... x2) {

        return () -> {
            final $list<BooleanSupplier> preds =
                cast(list(Objects.requireNonNull(x0), Objects.requireNonNull(x1)).pushAll(list(x2)));

            return preds.every(BooleanSupplier::getAsBoolean);
        };
    }

    public static boolean and(final boolean x0, final boolean x1, final boolean... x2) {

        if (!x0 || !x1) return false;

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < x2.length; i++) {
            if (!x2[i]) return false;
        }

        return true;
    }

    public static BooleanSupplier or(final BooleanSupplier x0, final BooleanSupplier x1, final BooleanSupplier... x2) {

        return () -> {
            final $list<BooleanSupplier> preds =
                cast(list(Objects.requireNonNull(x0), Objects.requireNonNull(x1)).pushAll(list(x2)));

            return preds.some(BooleanSupplier::getAsBoolean);
        };
    }

    public static boolean or(final boolean x0, final boolean x1, final boolean... x2) {

        if (x0 || x1) return true;

        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < x2.length; i++) {
            if (x2[i]) return true;
        }

        return false;
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> Predicate<T> and(final Predicate<? super T> x0, final Predicate<? super T> x1,
        final Predicate<? super T>... x2) {

        return y -> {
            final $list<Predicate<? super T>> preds =
                cast(list(Objects.requireNonNull(x0), Objects.requireNonNull(x1)).pushAll(list(x2)));

            return preds.every(z -> z.test(y));
        };
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> Predicate<T> or(final Predicate<? super T> x0, final Predicate<? super T> x1,
        final Predicate<? super T>... x2) {

        return y -> {
            final $list<Predicate<? super T>> preds =
                cast(list(Objects.requireNonNull(x0), Objects.requireNonNull(x1)).pushAll(list(x2)));

            return preds.some(z -> z.test(y));
        };
    }

    public static <T extends Comparable<T>> Predicate<T> lt(final T r) { return lt(it(), r); }

    public static <T extends Comparable<T>> Predicate<T> le(final T r) { return le(it(), r); }

    public static <T extends Comparable<T>> Predicate<T> gt(final T r) { return gt(it(), r); }

    public static <T extends Comparable<T>> Predicate<T> ge(final T r) { return ge(it(), r); }

    public static <T extends Comparable<T>> Predicate<T> gtlt(final T l, final T u) { return gtlt(it(), l, u); }

    public static <T extends Comparable<T>> Predicate<T> gelt(final T l, final T u) { return gelt(it(), l, u); }

    public static <T extends Comparable<T>> Predicate<T> gtle(final T l, final T u) { return gtle(it(), l, u); }

    public static <T extends Comparable<T>> Predicate<T> gele(final T l, final T u) { return gele(it(), l, u); }

    private static final Predicate<? extends Iterable<?>> hollow = x -> empty(x);

    public static <X extends Iterable<T>, T> Predicate<X> hollow() { return cast(hollow); }

    private static final Predicate<? extends CharSequence> empty = empty(it());

    public static <T extends CharSequence> Predicate<T> empty() { return cast(empty); }

    private static final Predicate<$<?>> present = $::present;

    public static Predicate<$<?>> present() { return present; }

    private static final Predicate<? extends CharSequence> blank = blank(it());

    public static <T extends CharSequence> Predicate<T> blank() { return cast(blank); }

    public static <T> Predicate<T> isa(final Class<?> cls) { return isa(it(), cls); }

    public static Predicate<Class<?>> assignable(final Class<?> cls) { return assignable(it(), cls); }

    public static <T> Predicate<T> same(final T val) { return x -> (x == val); }

    public static <T> Predicate<T> eq(final T val) { return eq(it(), val); }

    public static <T extends Comparable<T>> Predicate<T> eq(final T val) { return eq(it(), val); }

    public static <T> Predicate<T> in(final Collection<? extends T> val) { return in(it(), val); }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <T> Predicate<T> in(final T... val) { return in(it(), val); }

    public static <X, T extends Comparable<T>> Predicate<X> lt(final Function<X, ? extends T> f, final T r) {
        return l -> lt(f.apply(l), r);
    }

    public static <X, T extends Comparable<T>> Predicate<X> le(final Function<X, ? extends T> f, final T r) {
        return l -> le(f.apply(l), r);
    }

    public static <X, T extends Comparable<T>> Predicate<X> gt(final Function<X, ? extends T> f, final T r) {
        return l -> gt(f.apply(l), r);
    }

    public static <X, T extends Comparable<T>> Predicate<X> ge(final Function<X, ? extends T> f, final T r) {
        return l -> ge(f.apply(l), r);
    }

    public static <X, T extends Comparable<T>> Predicate<X> gtlt(final Function<X, ? extends T> f, final T l,
        final T u) {
        return m -> gtlt(l, f.apply(m), u);
    }

    public static <X, T extends Comparable<T>> Predicate<X> gelt(final Function<X, ? extends T> f, final T l,
        final T u) {
        return m -> gelt(l, f.apply(m), u);
    }

    public static <X, T extends Comparable<T>> Predicate<X> gtle(final Function<X, ? extends T> f, final T l,
        final T u) {
        return m -> gtle(l, f.apply(m), u);
    }

    public static <X, T extends Comparable<T>> Predicate<X> gele(final Function<X, ? extends T> f, final T l,
        final T u) {
        return m -> gele(l, f.apply(m), u);
    }

    public static <X, T extends CharSequence> Predicate<X> empty(final Function<X, ? extends T> f) {
        return x -> empty(f.apply(x));
    }

    public static <X, T extends CharSequence> Predicate<X> blank(final Function<X, ? extends T> f) {
        return x -> blank(f.apply(x));
    }

    public static Function<String, Regex> re() { return regex -> re(regex); }

    public static Regex re(final String regex) { return re(regex, "`"); }

    public static Regex re(final String regex, final String escape) {
        return new Regex(Regexive.regex(empty(escape) ? regex : regex.replaceAll(escape, "\\\\")));
    }

    public static Function<String, RegexJDK> re1() { return regex -> re1(regex); }

    public static RegexJDK re1(final String regex) { return re1(regex, "`"); }

    public static RegexJDK re1(final String regex, final String escape) {
        return Regexive.regex1(empty(escape) ? regex : regex.replaceAll(escape, "\\\\"));
    }

    public static Function<String, RegexRe2> re2() { return regex -> re2(regex); }

    public static RegexRe2 re2(final String regex) { return re2(regex, "`"); }

    public static RegexRe2 re2(final String regex, final String escape) {
        return Regexive.regex2(empty(escape) ? regex : regex.replaceAll(escape, "\\\\"));
    }

    public static RegexJDK re(final java.util.regex.Pattern regex) { return Regexive.regex1(regex); }

    public static Function<String, ReTest> retest() { return Indolently::retest; }

    public static ReTest retest(final String regex) { return Regexive.tester(regex); }

    public static Function<String, ReTest> refind() { return Indolently::refind; }

    public static ReTest refind(final String regex) {
        final var ptest = retest(regex);

        if (ptest instanceof AutomatonTest) {
            if (ASSERTIVE()) {
                final var pregex = re(regex);
                return ReTest.of(x -> {
                    final var actual = ((AutomatonTest) ptest).find(x);
                    final var expected = pregex.matcher(x).find();
                    assert actual == expected : String.format(
                        "original: %s, automaton: %s, expected: %s, actual: %s, input: %s", regex,
                        ((AutomatonTest) ptest).regex(), expected, actual, x);
                    return actual;
                }, regex);
            }

            return ReTest.of(x -> ((AutomatonTest) ptest).find(x), regex);
        }

        final var pregex = re(regex);
        return ReTest.of(x -> pregex.matcher(x).find(), regex);
    }

    public static <X, T> Predicate<X> nil(final Function<X, ? extends T> f) { return x -> f.apply(x) == null; }

    public static <X, T> Predicate<X> isa(final Function<X, ? extends T> f, final Class<?> cls) {
        return x -> cls.isInstance(f.apply(x));
    }

    public static <X> Predicate<X> assignable(final Function<X, Class<?>> f, final Class<?> cls) {
        return x -> f.apply(x).isAssignableFrom(cls);
    }

    @SuppressWarnings("overloads")
    public static <X, T> Predicate<X> eq(final Function<X, ? extends T> f, final T val) {
        return x -> equal(val, f.apply(x));
    }

    @SuppressWarnings("overloads")
    public static <X, T extends Comparable<T>> Predicate<X> eq(final Function<X, ? extends T> f, final T val) {
        return x -> equal(val, f.apply(x));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs", "RedundantSuppression" })
    public static <X, T> Predicate<X> in(final Function<X, ? extends T> f, final T... val) { return in(f, list(val)); }

    public static <X, T> Predicate<X> in(final Function<X, ? extends T> f, final Collection<? extends T> val) {
        return x -> val.contains(f.apply(x));
    }

    public static DoublePredicate between(final double l, final double u) { return m -> between(l, m, u); }

    public static LongPredicate between(final long l, final long u) { return m -> between(l, m, u); }

    public static IntPredicate between(final int l, final int u) { return m -> between(l, m, u); }

    /**
     * Do nothing Statement.
     *
     * @return do nothing Statement
     */
    public static Statement np() { return Statement.NOP; }

    /**
     * Do nothing Consumer.
     *
     * @return do nothing Consumer
     */
    public static <T> Consumer<T> nop() { return x -> { }; }

    /**
     * Do nothing Consumer.
     *
     * @return do nothing Consumer
     */
    public static <X, Y> BiConsumer<X, Y> noop() { return (x, y) -> { }; }

    public static BiFunction<Number, Number, Integer> sum() { return sum(0); }

    public static BiFunction<Number, Number, Integer> sum(final int z) {
        return (x, y) -> x.intValue() + y.intValue() + z;
    }

    public static BiFunction<Number, Number, Long> sum(final long z) {
        return (x, y) -> x.longValue() + y.longValue() + z;
    }

    public static BiFunction<Number, Number, Float> sum(final float z) {
        return (x, y) -> x.floatValue() + y.floatValue() + z;
    }

    public static BiFunction<Number, Number, Double> sum(final double z) {
        return (x, y) -> x.doubleValue() + y.doubleValue() + z;
    }

    public static int narrow(final int lower, final int x, final int upper) {
        return (x < lower) ? lower : (upper < x) ? upper : x;
    }

    public static int narrow(final int lower, final long x, final int upper) {
        return (int) ((x < lower) ? lower : (upper < x) ? upper : x);
    }

    public static long narrow(final long lower, final long x, final long upper) {
        return (x < lower) ? lower : (upper < x) ? upper : x;
    }

    public static float narrow(final float lower, final float x, final float upper) {
        return (x < lower) ? lower : (upper < x) ? upper : x;
    }

    public static double narrow(final double lower, final double x, final double upper) {
        return (x < lower) ? lower : (upper < x) ? upper : x;
    }

    public static List<String> split(final String s, final char sep) { return split(s, sep, 0, s.length()); }

    public static List<String> split(final String s, final char sep, int from, int to) {
        if (from < 0) { from = s.length() + from; }
        if (to < 0) { to = s.length() + to; }
        checkSplitRange(s, from, to);

        final var chars = s.toCharArray();
        final int begin = from;
        final int end = to;

        final $list<String> ret = list();
        int cur = begin;
        for (int i = cur; i < end; i++) {
            final var c = chars[i];

            if (c == sep) {
                ret.add(s.substring(cur, i));
                cur = i + 1;
            }
        }

        ret.add(s.substring(cur, end));

        return ret;
    }

    public static List<String> split(final String s, final String sep) { return split(s, sep, 0, s.length()); }

    public static List<String> split(final String s, final String sep, int from, int to) {
        final int sl = sep.length();
        if (sl == 0) return list(s);
        if (sl == 1) return split(s, sep.charAt(0), from, to);

        if (from < 0) { from = s.length() + from; }
        if (to < 0) { to = s.length() + to; }
        checkSplitRange(s, from, to);

        final int begin = from;
        final int end = to;

        final $list<String> ret = list();
        int cur = begin;
        for (int i = cur; i < end; i++) {
            if (s.indexOf(sep, cur) == i) {
                ret.add(s.substring(cur, i));
                cur = i + sl;
            }
        }

        ret.add(s.substring(cur, end));

        return ret;
    }

    private static void checkSplitRange(final String s, final int from, final int to) {
        if (to < from) throw new IllegalArgumentException(String.format("to=%d < from=%d", to, from));
        if (s.length() < from || s.length() < to)
            throw new StringIndexOutOfBoundsException(String.format("from=%d, to=%d, len=%d", from, to, s.length()));
    }

    public static <E extends Enum<E>> $<E> enumOf(final Class<E> type, final String name) {
        try { return opt(Enum.valueOf(type, name)); } //
        catch (final IllegalArgumentException e) { return none(); }
    }

    public static <E extends Enum<E>> Function<String, $<E>> enumOf(final Class<E> type) {
        return x -> enumOf(type, x);
    }

    public static <E extends Enum<E>> E[] enumValues(final Class<E> type) {
        //noinspection unchecked
        return (E[]) eval(() -> type.getDeclaredMethod("values").invoke(null));
    }

    public static <E extends Enum<E>> $<E> ienumOf(final Class<E> type, final String name) {
        for (final var e: enumValues(type)) {
            if (e.name().equalsIgnoreCase(name)) return opt(e);
        }
        return none();
    }

    public static <E extends Enum<E>> Function<String, $<E>> ienumOf(final Class<E> type) {
        return x -> ienumOf(type, x);
    }

    public static InputStream bytesIn(final byte[] bin) { return BytesInputStream.create(bin); }

    public static InputStream bytesIn(final ByteArrayOutputStream baos) { return BytesInputStream.create(baos); }

    public static BytesOutputStream bytesOut() { return BytesOutputStream.create(); }

    public static BytesOutputStream bytesOut(final int len) { return BytesOutputStream.create(len); }

    public static byte[] bytes(final InputStream in) throws IOException {
        if (in instanceof ByteArrayInputStream) return in.readAllBytes();

        final var out = bytesOut();
        out.write(in);
        return out.toByteArray();
    }

    public static void shut(final AutoCloseable c) {
        if (c != null) //
            try { c.close(); } //
            catch (final Exception e) { raise(e); }
    }

    public static Consumer<AutoCloseable> shut() { return Indolently::shut; }

    public static void qshut(final AutoCloseable c) {
        if (c != null) //
            try { c.close(); } //
            catch (final Exception e) {
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
    }

    public static Consumer<AutoCloseable> qshut() { return Indolently::qshut; }

    public static String FQCN(final Class<?> cls) { return cls.getName(); }

    public static Promise<Void> async(final RunnableE<? super Exception> run) { return Promissory.async(run); }

    public static Promise<Void> async(final RunnableE<? super Exception> run, final Executor exec) {
        return Promissory.async(run, exec);
    }

    public static <T> Promise<T> async(final Callable<? extends T> run) { return Promissory.async(run); }

    public static <T> Promise<T> async(final Callable<? extends T> run, final Executor exec) {
        return Promissory.async(run, exec);
    }

    public static <T> T await(final Future<? extends T> promise) { return Promissory.await(promise); }

    public static <T> $$<T, $$.None> await(final Future<? extends T> promise, final long timeout) {
        return Promissory.await(promise, timeout);
    }

    public static <T> T await(final Promise<? extends T> promise) { return Promissory.await(promise); }

    public static <T> $$<T, $$.None> await(final Promise<? extends T> promise, final long timeout) {
        return Promissory.await(promise, timeout);
    }

    @SafeVarargs
    public static <T> List<T> await(final Promise<? extends T>... promise) { return await(list(promise)); }

    @SafeVarargs
    public static <T> $$<List<T>, $$.None> await(final long timeout, final Promise<? extends T>... promise) {
        return await(list(promise), timeout);
    }

    public static <T> List<T> await(final Iterable<? extends Promise<? extends T>> promise) {
        return Promissory.await(Promise.all(promise));
    }

    public static <T> $$<List<T>, $$.None> await(final Iterable<? extends Promise<? extends T>> promise,
        final long timeout) { return Promissory.await(Promise.all(promise), timeout); }

    public static <T> Function<Callable<? extends T>, Promise<T>> async() { return Indolently::async; }

    public static Function<Runnable, Promise<Void>> rasync() {
        return x -> async(() -> {
            x.run();
            return null;
        });
    }

    public static <T> Function<Promise<? extends T>, T> await() { return Indolently::await; }

    public static <T> Function<Promise<? extends T>, $$<T, $$.None>> await(final long timeout) {
        return x -> await(x, timeout);
    }

    public static double i2d(final int x) { return x; }

    public static float i2f(final int x) { return x; }

    public static double l2d(final long x) { return x; }

    public static float l2f(final long x) { return x; }

    public static int d2i(final double x) { return (int) x; }

    public static int f2i(final float x) { return (int) x; }

    public static long d2l(final double x) { return (long) x; }

    public static long f2l(final float x) { return (long) x; }

    public static float d2f(final double x) { return (float) x; }

    public static long l2i(final long x) { return (int) x; }

    public static byte i2b(final int x) { return (byte) x; }

    public static byte l2b(final long x) { return (byte) x; }

    public static InputStream openRead(final Path file) throws IOException {
        return new BufferedInputStream(Files.newInputStream(file), 1024 * 1024);
    }

    public static OutputStream openWrite(final Path file) throws IOException {
        return new BufferedOutputStream(Files.newOutputStream(file), 1024 * 1024);
    }

    public static String lcase(final String s) { return s == null ? null : s.toLowerCase(); }

    public static String ucase(final String s) { return s == null ? null : s.toUpperCase(); }
}
