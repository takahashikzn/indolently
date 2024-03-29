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

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import jp.root42.indolently.function.Expression;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.ref.$bool;
import jp.root42.indolently.ref.$int;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.in;
import static jp.root42.indolently.Indolently.list;
import static jp.root42.indolently.Indolently.not;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.iterator;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assert.*;


/**
 * A test class for {@link Expressive}.
 *
 * @author takahashikzn
 */
@RunWith(JUnitParamsRunner.class)
public class ExpressiveTest {

    /**
     * {@link Expressive#raise(Throwable)}
     */
    @Test
    public void testRaiseException() {

        final RaiseTestException e = new RaiseTestException();

        try {
            fail(eval(() -> raise(e)));
        } catch (final RaisedException err) {
            assertThat(err.getCause()).isSameAs(e);
        }
    }

    private static final class RaiseTestException
        extends Exception {

        private static final long serialVersionUID = 1L;
    }

    /**
     * {@link Expressive#raise(Throwable)}
     */
    @Test(expected = RaiseTestRuntimeException.class)
    public void testRaiseRuntimeException() {

        fail(eval(() -> raise(new RaiseTestRuntimeException())));
    }

    private static final class RaiseTestRuntimeException
        extends RuntimeException {

        private static final long serialVersionUID = 1L;
    }

    /**
     * {@link Expressive#raise(Throwable)}
     */
    @Test(expected = RaiseTestError.class)
    public void testRaiseError() {

        fail(eval(() -> raise(new RaiseTestError())));
    }

    private static final class RaiseTestError
        extends Error {

        private static final long serialVersionUID = 1L;
    }

    /**
     * {@link Expressive#eval(Expression)}
     */
    @Test
    public void testEval() {

        assertEquals( //
            list(1, 2, 3).map(x -> x * x), //
            eval( //
                list(1, 2, 3), //
                l -> l.map(x -> x * x)) //
        );
    }

    /**
     * {@link Expressive#eval(Expression)}
     */
    @Test
    public void testEval2() {

        final Exception e = new Exception();

        try {
            eval(() -> {
                throw e;
            });

            fail();
        } catch (final RaisedException err) {
            assertThat(err.getCause()).isSameAs(e);
        }
    }

    /**
     * {@link Expressive#let(Statement)}
     */
    @Test
    public void testLet() {

        final $bool called = ref(false);

        let(() -> called.$ = true);

        assertTrue(called.$);
    }

    /**
     * {@link Expressive#let(Statement)}
     */
    @Test
    public void testLet2() {

        final Exception e = new Exception();

        try {
            let(() -> {
                throw e;
            });

            fail();
        } catch (final RaisedException err) {
            assertThat(err.getCause()).isSameAs(e);
        }
    }

    /**
     * {@link Expressive#let(Object, Consumer)}
     */
    @Test
    public void testLet3() {

        final $bool called = ref(false);

        let(called, x -> x.$ = true);

        assertTrue(called.$);
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatch() {

        final Function<Integer, String> f = //
            ctx -> match(ctx) //
                .when(x -> x == 1).then(x -> "one") //
                .when(x -> x == 2).then(x -> "two") //
                .when(x -> x == 3).then("three") //
                .none(x -> "" + x);

        assertThat(f.apply(1)).isEqualTo("one");
        assertThat(f.apply(2)).isEqualTo("two");
        assertThat(f.apply(3)).isEqualTo("three");
        assertThat(f.apply(4)).isEqualTo("4");
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatchType() {

        final Function<Number, String> f = //
            ctx -> match(ctx) //
                .type((final Long x) -> "long: " + x) //
                .when(x -> x.intValue() < 0).then(x -> "negative: " + x) //
                .when(Double.class).then(x -> "double: " + x.doubleValue()) //
                .none(x -> "num: " + x.doubleValue());

        assertThat(f.apply(1)).isEqualTo("num: 1.0");
        assertThat(f.apply(2L)).isEqualTo("long: 2");
        assertThat(f.apply(3.1)).isEqualTo("double: 3.1");
        assertThat(f.apply(-1)).isEqualTo("negative: -1");
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatchType2() {

        final Function<Foo, String> f = //
            ctx -> match(ctx) //
                .type((final Bar x) -> "BAR!!") //
                .type((final Baz x) -> "BAZ!!") //
                .none("FOO!!");

        assertThat(f.apply(new Foo())).isEqualTo("FOO!!");
        assertThat(f.apply(new Bar())).isEqualTo("BAR!!");
        assertThat(f.apply(new Baz())).isEqualTo("BAZ!!");
    }

    public static class Foo { }

    public static class Bar
        extends Foo { }

    public static class Baz
        extends Foo { }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatchConst() {

        final Function<Integer, String> f = //
            ctx -> match(ctx) //
                .when(() -> 1).then(x -> "one") //
                .when(() -> 2).then(x -> "two") //
                .when(() -> 3).then("three") //
                .none(x -> "" + x);

        assertThat(f.apply(1)).isEqualTo("one");
        assertThat(f.apply(2)).isEqualTo("two");
        assertThat(f.apply(3)).isEqualTo("three");
        assertThat(f.apply(4)).isEqualTo("4");
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatchFatal() {

        //noinspection ErrorNotRethrown
        try {
            match(1) //
                .when(eq(1)) //
                .then(() -> "NG").fatal("OK");
        } catch (final AssertionError e) {
            assertThat(e.getMessage()).isEqualTo("OK");
        }
    }

    /**
     * {@link Expressive#when(boolean)}
     */
    @Test
    public void testWhenFatal() {

        //noinspection ErrorNotRethrown
        try {
            when(false).then("NG").fatal("OK");
        } catch (final AssertionError e) {
            assertThat(e.getMessage()).isEqualTo("OK");
        }
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatchWithOps() {

        assertThat(match(7) //
            .when(eq(1)).then(() -> "NG") //
            .when(not(not(eq(2)))).then(() -> "NG") //
            .when(in(3, 4)).then(() -> "NG") //
            .when(or(eq(5), eq(6))).then(() -> "NG") //
            .when(and(ge(7), lt(8))).then(() -> "OK") //
            .when(gele(7, 8)).then("NG") //
            .fatal()).isEqualTo("OK");
    }

    public enum EnumOfTestMatch {
        FOO, BAR, BAZ
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatchEnum() {

        final Function<EnumOfTestMatch, String> f = //
            ctx -> match(ctx) //
                .when(() -> EnumOfTestMatch.FOO).then(x -> "foo") //
                .when(() -> EnumOfTestMatch.BAR).then(x -> "bar") //
                .none(x -> "none");

        assertThat(f.apply(EnumOfTestMatch.FOO)).isEqualTo("foo");
        assertThat(f.apply(EnumOfTestMatch.BAR)).isEqualTo("bar");
        assertThat(f.apply(EnumOfTestMatch.BAZ)).isEqualTo("none");
    }

    /**
     * {@link Expressive#when(boolean)}
     */
    @Test
    public void testWhen() {

        final $int i = ref(0);

        final Supplier<String> f = //
            () -> when(() -> i.$ == 1).then(() -> "one") //
                .when(() -> i.$ == 2).then(() -> "two") //
                .when(() -> i.$ == 3).then("three") //
                .none("" + i.$);

        i.$ = 1;
        assertThat(f.get()).isEqualTo("one");

        i.$ = 2;
        assertThat(f.get()).isEqualTo("two");

        i.$ = 3;
        assertThat(f.get()).isEqualTo("three");

        i.$ = 4;
        assertThat(f.get()).isEqualTo("4");
    }

    /**
     * complicated type inference test of {@link Expressive#when(boolean)}.
     *
     * @param expected expected value
     * @param from range from
     * @param to range to
     * @param step range step
     */
    @Parameters
    @Test
    public void testComplicatedTypeInference(final List<Integer> expected, final int from, final int to, final int step) {

        assertThat(list( //
            iterator( //
                ref(from), //
                ref -> when(from < to).then(() -> ref.$ <= to) //
                    .when(to < from).then(() -> to <= ref.$) //
                    .none(() -> ref.$ == from), //
                ref -> when(from < to) //
                    .then(() -> ref.getThen(self -> self.$ += step)) //
                    .none(() -> prog1(ref, () -> ref.$ -= step)) //
            ) //
        )) //
            .isEqualTo(expected);

        assertThat(list( //
            iterator( //
                ref(from), //
                ref -> when(from < to).then(() -> ref.$ <= to) //
                    .when(to < from).then(() -> to <= ref.$) //
                    .none(() -> ref.$ == from), //
                ref -> when(from < to) //
                    .then(() -> ref.getThen(self -> self.$ += step)) //
                    .none(() -> prog1(ref, () -> ref.$ -= step)) //
            ) //
        ).reduce((l, r) -> l + r)) //
            .isEqualTo(list(expected).reduce((l, r) -> l + r));
    }

    static List<Object[]> parametersForTestComplicatedTypeInference() {

        return list( //
            oarray(list(1, 3, 5), 1, 6, 2), //
            oarray(list(3, 1, -1), 3, -1, 2), //
            oarray(list(1), 1, 1, 1) //
        );
    }

    /**
     * {@link Match.IntroCase#raise(Supplier)}
     */
    @Test
    public void testSwitchOfFailure() {

        final Function<Integer, String> f = //
            ctx -> match(ctx) //
                .when(x -> x == 1).then(x -> "one") //
                .when(x -> x == 2).then(x -> "two") //
                .raise(x -> new RuntimeException("THE TEST OF " + x));

        try {
            f.apply(42);
            fail();
        } catch (final RuntimeException e) {
            assertThat(e.getMessage()).contains("THE TEST OF 42");
        }
    }
}
