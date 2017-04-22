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

import jp.root42.indolently.ref.IntRef;

import static jp.root42.indolently.Functional.*;
import static jp.root42.indolently.Generator.*;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Indolently.tuple;
import static jp.root42.indolently.Iterative.*;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;


/**
 * A test class for {@link Iterative}.
 *
 * @author takahashikzn
 */
@RunWith(JUnitParamsRunner.class)
public class IterativeTest {

    /**
     * {@link Iterative#generator(Supplier)}
     */
    @Test
    public void testGenerator() {

        final Generator<Integer> g = generator(ref(0), env -> (2 < env.val) ? breaks() : env.val++);

        assertThat(g.hasNext()).isTrue();
        assertThat(g.next()).isEqualTo(0);
        assertThat(g.hasNext()).isTrue();
        assertThat(g.next()).isEqualTo(1);
        assertThat(g.hasNext()).isTrue();
        assertThat(g.next()).isEqualTo(2);
        assertThat(g.hasNext()).isFalse();

        try {
            g.next();
            fail();
        } catch (final NoSuchElementException ignored) {
            assert true;
        }
    }

    /**
     * {@link Iterative#generator(Supplier)}
     */
    @Test
    public void testGeneratorHandleBreak() {

        final SList<Integer> ints = list();

        generator(//
            ref(1), //
            env -> (10 < env.val) ? breaks() : env.val++) //
            .forEach(consumerOf((final Integer x) -> ints.add(x)) //
                .andThen(x -> {}));

        assertThat(ints.reduce((x, y) -> x + y).get()).isEqualTo(55);
    }

    /**
     * {@link Iterative#generator(Supplier)}
     */
    @Test
    public void testGeneratorHandleBreak2() {

        final SList<Integer> ints = list(1, 2, 3, 4, 5);

        assertThat(generator(ref(0), //
            (final IntRef pos) -> tuple( //
                ints.get(pos.val), //
                ints.opt(++pos.val) //
                    .orElseGet(() -> breaks()))).list()) //
            .isEqualTo(list( //
                tuple(1, 2) //
                , tuple(2, 3) //
                , tuple(3, 4) //
                , tuple(4, 5)));
    }

    /**
     * {@link Iterative#generator(Supplier)}
     */
    @Test(expected = NoSuchElementException.class)
    public void testGeneratorHandleNoSuchException() {

        generator( //
            generator( //
                ref(1), //
                env -> (10 < env.val) ? breaks() : env.val++)) //
            .forEach(consumerOf((final Integer x) -> {}));
    }

    /**
     * [@link {@link Iterative#iterator(Supplier...)}
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

        try {
            g.next();
            fail();
        } catch (final NoSuchElementException ignored) {
            assert true;
        }
    }

    /**
     * {@link Iterative#sequence(int)}
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
        } catch (final NoSuchElementException ignored) {
            assert true;
        }
    }

    /**
     * internal iterators.
     */
    @Test
    public void testInternalIterators() {

        final SList<Integer> ints = list();

        final Integer actual = range(1, 10).list() //
            .slice(-5, 0) //
            .map(i -> i * i) //
            .each(i -> ints.add(i)) //
            .reduce(0, (i, k) -> i + k);

        assertThat(actual) //
            .isEqualTo(330);

        assertThat(ints) //
            .isEqualTo(list(36, 49, 64, 81, 100));
    }

    /**
     * {@link Iterative#range(int, int, int)}
     *
     * @param desc description
     * @param expected expected value
     * @param from from
     * @param to to
     * @param step step
     */
    @Parameters
    @Test
    public void testRange(final String desc, final List<Integer> expected, final int from, final int to,
        final int step) {

        assertThat(range(from, to, step).list()).as(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestRange() {

        return list( //
            oarray("1..4 (stepping 1)", list(1, 2, 3, 4), 1, 4, 1), //
            oarray("4..1 (stepping 2)", list(4, 2), 4, 1, 2), //
            oarray("-2..2 (stepping 1)", list(-2, -1, 0, 1, 2), -2, 2, 1), //
            oarray("0..0 (stepping 1)", list(0), 0, 0, 1) //
        );
    }
}
