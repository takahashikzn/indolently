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

import jp.root42.indolently.Expressive.RaisedException;
import jp.root42.indolently.function.Expression;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.ref.BoolRef;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;


/**
 * A test class for {@link Expressive}.
 *
 * @author takahashikzn
 */
@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class ExpressiveTest {

    /**
     * {@link Expressive#raise(Throwable)}
     */
    @Test
    public void testRaiseException() {

        final RaiseTestException e = new RaiseTestException();

        try {
            Assert.fail(eval(() -> raise(e)));
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

        Assert.fail(eval(() -> raise(new RaiseTestRuntimeException())));
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

        Assert.fail(eval(() -> raise(new RaiseTestError())));
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

        final BoolRef called = ref(false);

        let(() -> called.val = true);

        assertTrue(called.val);
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

        final BoolRef called = ref(false);

        let(called, x -> x.val = true);

        assertTrue(called.val);
    }

    /**
     * {@link Expressive#match(Object)}
     */
    @Test
    public void testMatch() {

        final Function<Integer, String> f1 = ctx -> match(ctx) //
            .when(x -> x == 1).then(x -> "one") //
            .when(x -> x == 2).then(x -> "two") //
            .none(x -> "" + x);

        assertThat(f1.apply(1)).isEqualTo("one");
        assertThat(f1.apply(2)).isEqualTo("two");
        assertThat(f1.apply(3)).isEqualTo("3");
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
    public void testComplicatedTypeInference(final List<Integer> expected, final int from, final int to,
        final int step) {

        assertThat(list( //
            iterator( //
                ref(from), //
                ref -> when(from < to).then(() -> ref.val <= to) //
                    .when(to < from).then(() -> to <= ref.val) //
                    .none(() -> ref.val == from),
                ref -> when(from < to) //
                    .then(() -> ref.getThen(self -> self.val += step)) //
                    .none(() -> prog1(ref::get, () -> ref.val -= step)) //
        ) //
        )) //
            .isEqualTo(expected);

        assertThat(list( //
            iterator( //
                ref(from), //
                ref -> when(from < to).then(() -> ref.val <= to) //
                    .when(to < from).then(() -> to <= ref.val) //
                    .none(() -> ref.val == from),
                ref -> when(from < to) //
                    .then(() -> ref.getThen(self -> self.val += step)) //
                    .none(() -> prog1(ref::get, () -> ref.val -= step)) //
        ) //
        ).reduce((l, r) -> l + r)) //
            .isEqualTo(list(expected).reduce((l, r) -> l + r));
    }

    static List<Object[]> parametersForTestComplicatedTypeInference() {

        return list( //
            oarray(list(1, 3, 5), 1, 6, 2) //
            , oarray(list(3, 1, -1), 3, -1, 2) //
            , oarray(list(1), 1, 1, 1) //
        );
    }

    /**
     * {@link Expressive.ContextualWhen.Then#raise(Supplier)}
     */
    @Test
    public void testSwitchOfFailure() {

        final Function<Integer, String> f1 = ctx -> match(ctx) //
            .when(x -> x == 1).then(x -> "one") //
            .when(x -> x == 2).then(x -> "two") //
            .raise(x -> new RuntimeException("THE TEST OF " + x));

        try {
            f1.apply(42);
            fail();
        } catch (final RuntimeException e) {
            assertThat(e.getMessage()).contains("THE TEST OF 42");
        }
    }
}
