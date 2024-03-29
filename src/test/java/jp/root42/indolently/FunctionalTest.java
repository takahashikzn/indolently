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

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import jp.root42.indolently.function.Function3;
import jp.root42.indolently.function.SFunc;
import jp.root42.indolently.function.SFunc3;
import jp.root42.indolently.ref.$3;
import jp.root42.indolently.ref.$int;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Functional.*;
import static jp.root42.indolently.Indolently.list;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;

import junitparams.JUnitParamsRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;


/**
 * A test class for {@link Functional}.
 *
 * @author takahashikzn
 */
@RunWith(JUnitParamsRunner.class)
public class FunctionalTest {

    /**
     * {@link Functional#function(Consumer, Function3)}
     */
    @Test
    public void testListComprehension() {

        assertThat(range(2, 10).take(z -> func2((final BiFunction<Integer, Integer, Boolean> self) -> { }, (self, x, y) -> {
                if (y <= 1) {
                    return true;
                } else if ((x % y) == 0) {
                    return false;
                } else {
                    return self.apply(x, y - 1);
                }
            }).apply(z, z - 1)) //
            .map(x -> "" + x) //
            .list()) //
            .isEqualTo(list(2, 3, 5, 7).map(x -> "" + x));
    }

    /**
     * fibonacci function.
     */
    @Test
    public void testFunction() {

        final $int initCount = ref(0);

        final Function<Integer, Integer> fib = func( //
            (final Function<Integer, Integer> self) -> {
                assertThat(self.apply(1)).isEqualTo(1); // check no stackoverflow
                initCount.$++;
            }, (self, x) -> //
                (x <= 1) ? x : self.apply(x - 1) + self.apply(x - 2)).memoize();

        final $list<Integer> fibonacciNums =
            list(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025, 121393, 196418,
                317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887, 9227465, 14930352, 24157817, 39088169, 63245986, 102334155, 165580141,
                267914296).freeze();

        assertThat(initCount.$).isEqualTo(0);

        assertThat(range(1, 10).each(x -> {
            assertThat(range(0, 42).reduce(list(), (rem, val) -> rem.push(fib.apply(val)))).isEqualTo(fibonacciNums);
            assertThat(initCount.$).isEqualTo(1);
        }).last()).isEqualTo(10);
    }

    /**
     * Tarai function.
     *
     * <pre>
     * def tarai( x, y, z)
     *   if y < x
     *     tarai(
     *          tarai(x-1, y, z),
     *          tarai(y-1, z, x),
     *          tarai(z-1, x, y)
     *        )
     *   else
     *     y          # not z!
     *   end
     * end
     * </pre>
     *
     * @see <a href="http://en.wikipedia.org/wiki/Tak_(function)">tarai function</a>
     */
    @Test
    public void testFunction2() {

        assertThat(func( //
            (final Function<$3<Integer, Integer, Integer>, Integer> self) -> { }, // function decl
            (self, v) -> { // function body

                final int x = v._1();
                final int y = v._2();
                final int z = v._3();

                return when(() -> (y < x)) //
                    .then(() -> (int) self.apply( //
                        Indolently.tuple( //
                            self.apply(Indolently.tuple(x - 1, y, z)), //
                            self.apply(Indolently.tuple(y - 1, z, x)), //
                            self.apply(Indolently.tuple(z - 1, x, y))))) //
                    .none(y);
            }).memoize().apply(Indolently.tuple(20, 6, 0))).isEqualTo(20);
    }

    /**
     * Tarai function.
     */
    @Test
    public void testFunction3() {

        final SFunc<$3<Integer, Integer, Integer>, Integer> tarai = func(self -> { }, (self, v) -> {

            final int x = v._1();
            final int y = v._2();
            final int z = v._3();

            return when(() -> (y < x)) //
                .then(() -> (int) self.apply( //
                    Indolently.tuple( //
                        self.apply(Indolently.tuple(x - 1, y, z)), //
                        self.apply(Indolently.tuple(y - 1, z, x)), //
                        self.apply(Indolently.tuple(z - 1, x, y))))) //
                .none(y);
        });

        assertThat(tarai.memoize().apply(Indolently.tuple(20, 6, 0))).isEqualTo(20);
    }

    /**
     * Tarai function.
     */
    @Test
    public void testFunction4() {

        assertThat(function( //
            // function decl
            (final Function3<Integer, Integer, Integer, Integer> self) -> { },

            // function body
            (self, x, y, z) -> when(() -> (y < x)) //
                .then(() -> (int) self.apply( //
                    self.apply(x - 1, y, z), //
                    self.apply(y - 1, z, x), //
                    self.apply(z - 1, x, y))).none(y)).memoize().apply(20, 6, 0)).isEqualTo(20);
    }

    /**
     * Tarai function.
     */
    @Test
    public void testFunction5() {

        final SFunc3<Integer, Integer, Integer, Integer> tarai = func3(
            // function initializer
            self -> { },

            // function body
            (self, x, y, z) -> when(() -> (y < x)) //
                .then(() -> (int) self.apply( //
                    self.apply(x - 1, y, z), //
                    self.apply(y - 1, z, x), //
                    self.apply(z - 1, x, y))).none(y));

        assertThat(tarai.memoize().apply(20, 6, 0)).isEqualTo(20);
    }
}
