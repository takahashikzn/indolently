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

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Functions.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterations.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;


/**
 * A test class for {@link Iterations}.
 *
 * @author takahashikzn
 * @version $Id$
 */
@RunWith(JUnitParamsRunner.class)
public class IterationsTest {

    /**
     * {@link Iterations#generator(Supplier)}
     */
    @Test
    public void testGenerator() {

        final Slist<Integer> ints = list();

        System.out.println(list(generator(ref(1), env -> (10 <= env.val) ? Generator.stop() : env.val++)));

        generator( //
            generator(ref(1), env -> (10 <= env.val) ? Generator.stop() : env.val++)) //
            .forEach(consumerOf((final Integer x) -> ints.add(x)) //
                .andThen(x -> {
                }));

        assertThat(ints.reduce((x, y) -> x + y).get()).isEqualTo(55);
    }

    /**
     * [@link {@link Iterations#iterator(Supplier...)}
     */
    @Test
    public void testIterator() {

        final int[] eval = { 0, 0, 0 };

        final Iterator<Integer> g = iterator( //
            () -> eval[0] = 1, //
            () -> eval[1] = 2, //
            () -> eval[2] = 3).iterator();

        assertThat(eval).isEqualTo(parray(0, 0, 0));

        assertThat(g.hasNext()).isEqualTo(true);
        assertThat(g.next()).isEqualTo(1);
        assertThat(eval).isEqualTo(parray(1, 0, 0));

        assertThat(g.hasNext()).isEqualTo(true);
        assertThat(g.next()).isEqualTo(2);
        assertThat(eval).isEqualTo(parray(1, 2, 0));

        assertThat(g.hasNext()).isEqualTo(true);
        assertThat(g.next()).isEqualTo(3);
        assertThat(eval).isEqualTo(parray(1, 2, 3));

        assertThat(g.hasNext()).isEqualTo(false);
    }

    /**
     * {@link Iterations#sequence(int)}
     */
    @Test
    public void testSequence() {

        final Iterator<Integer> i = sequence(Integer.MAX_VALUE - 1).iterator();

        assertTrue(i.hasNext());
        assertThat(i.next()).isEqualTo(Integer.MAX_VALUE - 1);
        assertTrue(i.hasNext());
        assertThat(i.next()).isEqualTo(Integer.MAX_VALUE);

        assertFalse(i.hasNext());

        try {
            i.next();
            fail();
        } catch (final NoSuchElementException e) {
            assert true;
        }
    }

    /**
     * internal iterators.
     */
    @Test
    public void testInternalIterators() {

        final Slist<Integer> ints = list();

        assertThat(range(1, 10).list() //
            .slice(-5, 0) //
            .map(i -> i * i) //
            .each(i -> ints.add(i)) //
            .reduce(0, (i, k) -> i + k).get()) //
            .isEqualTo(330);

        assertThat(ints) //
            .isEqualTo(list(36, 49, 64, 81, 100));
    }

    /**
     * {@link Iterations#range(int, int, int)}
     *
     * @param desc description
     * @param expected expected value
     * @param from from
     * @param to to
     * @param step step
     */
    @Parameters
    @Test
    public void testRange(final String desc, final List<Integer> expected, final int from, final int to, final int step) {

        assertThat(range(from, to, step).list()).as(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestRange() {

        return list( //
            oarray("1..4 (stepping 1)", list(1, 2, 3, 4), 1, 4, 1) //
            , oarray("4..1 (stepping 2)", list(4, 2), 4, 1, 2) //
            , oarray("-2..2 (stepping 1)", list(-2, -1, 0, 1, 2), -2, 2, 1) //
            , oarray("0..0 (stepping 1)", list(0), 0, 0, 1) //
        );
    }
}