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

import jp.root42.indolently.function.TriFunction;

import junitparams.JUnitParamsRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Functional.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;
import static org.assertj.core.api.Assertions.*;


/**
 * A test class for {@link Functional}.
 *
 * @author takahashikzn
 * @version $Id$
 */
@RunWith(JUnitParamsRunner.class)
public class FunctionalTest {

    /**
     * {@link Functional#function(Consumer, TriFunction)}
     */
    @Test
    public void testListComprehension() {

        assertThat(
            range(2, 10)
                .filter(z -> function((final BiFunction<Integer, Integer, Boolean> self) -> {}, (self, x, y) -> {
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
     * {@link Functional#function(Consumer, TriFunction)}
     */
    @Test
    public void testFunction() {

        final Function<Integer, Integer> fib = function( //
            (final Function<Integer, Integer> self) -> {}, (self, x) -> //
            (x <= 1) ? x : self.apply(x - 1) + self.apply(x - 2)).memoize();

        final Slist<Integer> fibonacciNums =
            list(0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946,
                17711, 28657, 46368, 75025, 121393, 196418, 317811, 514229, 832040, 1346269, 2178309, 3524578, 5702887,
                9227465, 14930352, 24157817, 39088169, 63245986, 102334155, 165580141, 267914296).freeze();

        assertThat(range(0, 42).mapred(list(), (rem, val) -> rem.push(fib.apply(val))).get()).isEqualTo(fibonacciNums);
    }
}
