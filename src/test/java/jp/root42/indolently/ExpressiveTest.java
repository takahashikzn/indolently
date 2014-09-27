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

import jp.root42.indolently.Expressive.Match;
import jp.root42.indolently.Expressive.When;
import jp.root42.indolently.function.Statement;
import jp.root42.indolently.ref.BoolRef;
import jp.root42.indolently.ref.IntRef;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;


/**
 * A test class for {@link Expressive}.
 *
 * @author takahashikzn
 * @version $Id$
 */
@RunWith(JUnitParamsRunner.class)
public class ExpressiveTest {

    /**
     * {@link Expressive#raise(Throwable)}
     */
    @Test(expected = RaiseTestException.class)
    public void testRaise() {

        Assert.fail(eval(() -> raise(new RaiseTestException())));
    }

    private static final class RaiseTestException
        extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    /**
     * {@link Expressive#eval(Supplier)}
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
     * {@link Expressive#let(Statement)}
     */
    @Test
    public void testLet() {

        final BoolRef called = ref(false);

        let(() -> called.val = true);

        assertTrue(called.val);
    }

    /**
     * {@link Expressive#let(Object, Consumer)}
     */
    @Test
    public void testLet2() {

        final BoolRef called = ref(false);

        let(called, x -> x.val = true);

        assertTrue(called.val);
    }

    /**
     * {@link Expressive#match(When...)}
     */
    @Test
    public void testMatch() {

        final Function<Integer, String> f1 = match( //
            when((final Integer x) -> x == 1, () -> "one") //
            , whenEq(2, "two") //
            ).defaults(x -> "" + x);

        assertThat(f1.apply(1)).isEqualTo("one");
        assertThat(f1.apply(2)).isEqualTo("two");
        assertThat(f1.apply(3)).isEqualTo("3");
    }

    /**
     * very complicated type inference test of {@link Expressive#match(When...)}.
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
                env -> match( //
                    when((final IntRef x) -> from < to, x -> x.val <= to), //
                    when(x -> to < from, x -> to <= x.val) //
                ).defaults(x -> x.val == from).apply(env), // Expressive#test raises compilation error with OracleJDK
                match( //
                    when((final IntRef x) -> from < to, x -> prog1( //
                        x::get, //
                        () -> x.val += step)) //
                ).defaults(x -> prog1(x::get, () -> x.val -= step)) //
            ) //
            )).isEqualTo(expected);

        assertThat(list( //
            iterator( //
                ref(from), //
                env -> match( //
                    when( //
                        (final IntRef x) -> from < to, //
                        x -> x.val <= to), //
                    when( //
                        x -> to < from, //
                        x -> to <= x.val) //
                ).defaults(x -> x.val == from).apply(env), //
                env -> when( //
                    (final IntRef x) -> from < to, //
                    x -> prog1( //
                        x::get, //
                        () -> x.val += step)) //
                    .other( //
                        x -> prog1( //
                            x::get, //
                            () -> x.val -= step)).apply(env) //
            )).reduce((l, r) -> l + r)).isEqualTo(list(expected).reduce((l, r) -> l + r));
    }

    static List<Object[]> parametersForTestComplicatedTypeInference() {

        return list( //
            oarray(list(1, 3, 5), 1, 6, 2) //
            , oarray(list(3, 1, -1), 3, -1, 2) //
            , oarray(list(1), 1, 1, 1) //
        );
    }

    /**
     * {@link Match#raise(Supplier)}
     */
    @Test
    public void testMatchFailure() {

        final Function<Integer, String> f1 = match( //
            when((final Integer x) -> x == 1, () -> "one")//
            , when(x -> x == 2, () -> "two") //
            ).raise(x -> new RuntimeException("THE TEST OF " + x));

        try {
            f1.apply(42);
            fail();
        } catch (final RuntimeException e) {
            assertThat(e.getMessage()).contains("THE TEST OF 42");
        }
    }
}
