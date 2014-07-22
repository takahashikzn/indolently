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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import jp.root42.indolently.Indolently.Freezable;
import jp.root42.indolently.Indolently.Slist;
import jp.root42.indolently.Indolently.Smap;
import jp.root42.indolently.Indolently.Sset;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Indolently.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;


/**
 * A test class for {@link Indolently}.
 *
 * @author takahashikzn
 * @version $Id$
 */
@SuppressWarnings("serial")
@RunWith(JUnitParamsRunner.class)
public class IndolentlyTest {

    /**
     * {@link Indolently#sequence(int)}
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

        assertThat(range(1, 10) //
            .slice(-5, 0) //
            .map((i) -> i * i) //
            .each((i) -> ints.add(i)) //
            .reduce(0, (i, k) -> i + k).get()) //
            .isEqualTo(330);

        assertThat(ints) //
            .isEqualTo(list(36, 49, 64, 81, 100));
    }

    /**
     * {@link Indolently#list(Object...)}
     *
     * @param desc description
     * @param expected expected value
     * @param args args
     */
    @Parameters
    @Test
    public void testListVarArgs(final String desc, final List<?> expected, final Object[] args) {

        assertThat(list(args)).describedAs(desc) //
            .isEqualTo(expected);

        final List<Object> listOfArgs = Arrays.asList(Optional.ofNullable(args).orElse(new Object[0]));

        assertThat(list().pushAll(listOfArgs)).describedAs(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestListVarArgs() {

        return Arrays.asList( //
            new Object[] { "int list", Arrays.asList(1, 2, 3), new Object[] { 1, 2, 3 } } //
            , new Object[] { "compound typed list", Arrays.asList(1, "a"), new Object[] { 1, "a" } } //
            , new Object[] { "null args", new ArrayList<>(), null } //
            , new Object[] { "empty list", new ArrayList<>(), new Object[] {} });
    }

    /**
     * {@link Indolently#oarray(Object...)}
     */
    @Test
    public void testObjectArray() {

        final Slist<Object[]> actual = list( //
            oarray("int list", list(1, 2, 3), oarray(1, 2, 3)) //
            , oarray("compound typed list", list(1, "a"), oarray(1, "a")) //
            , oarray("null args", list(), null) //
            , oarray("empty list", list(), oarray()));

        final List<Object[]> expected = parametersForTestListVarArgs();

        assertThat(actual).hasSize(4);
        assertArrayEquals(actual.get(0), expected.get(0));
        assertArrayEquals(actual.get(1), expected.get(1));
        assertArrayEquals(actual.get(2), expected.get(2));
        assertArrayEquals(actual.get(3), expected.get(3));
    }

    /**
     * {@link Indolently#map()}
     */
    @Test
    public void testMapVarArgs() {

        assertThat(map()) //
            .isEqualTo(new HashMap<>());

        assertThat(map("key", "value")).describedAs("single key/value pair") //
            .isEqualTo(new HashMap<Object, Object>() {
                {
                    this.put("key", "value");
                }
            });

        assertThat(map("int", 1, "string", "abc")).describedAs("compound typed map") //
            .isEqualTo(new HashMap<Object, Object>() {
                {
                    this.put("int", 1);
                    this.put("string", "abc");
                }
            });

        final Smap<String, Object> actualNestedMap = map( //
            "int", 1 //
            , "string", "abc" //
            , "level1", map( //
                "level2", map( //
                    "level3", list(map("level4", 42))))).freeze();

        final Map<String, Object> expectedNestedMap = new HashMap<String, Object>() {
            {
                final Map<String, Object> level1 = new HashMap<>();
                final Map<String, Object> level2 = new HashMap<>();
                final List<Map<String, Object>> level3 = new ArrayList<>();
                final Map<String, Object> level4 = new HashMap<>();

                this.put("int", 1);
                this.put("string", "abc");
                this.put("level1", level1);
                level1.put("level2", level2);
                level2.put("level3", level3);
                level3.add(level4);
                level4.put("level4", 42);
            }
        };

        assertThat( //
            actualNestedMap).describedAs("nested structure") //
            .isEqualTo(expectedNestedMap);
    }

    /**
     * {@link Freezable#freeze()}
     */
    @Test
    public void testFreeze() {

        final Smap<String, Smap<String, Slist<Sset<Integer>>>> frozen = map( //
            "level1", map( //
                "level2", list(optional(set(42))))).freeze();

        try {
            frozen.put("level1.1", map("level2.1", list(optional(set(43)))));
            fail();
        } catch (final UnsupportedOperationException e) {
            assert true;
        }

        try {
            frozen.get("level1").put("level2.1", list(optional(set(43))));
            fail();
        } catch (final UnsupportedOperationException e) {
            assert true;
        }

        try {
            frozen.get("level1").get("level2").get(0).add(43);
            fail();
        } catch (final UnsupportedOperationException e) {
            assert true;
        }
    }

    /**
     * {@link Freezable#freeze()} (not implemented yet)
     */
    @Ignore
    @Test
    public void testCircularFreeze() {

        final Slist<Object> list0 = list(0);
        final Slist<Object> list1 = list(1);

        list0.add(list1);
        list1.add(list0);

        list0.freeze();
        list1.freeze();
    }

    /**
     * {@link Indolently#set(Object...)}
     *
     * @param desc description
     * @param expected expected value
     * @param args args
     */
    @Parameters
    @Test
    public void testSetVarArgs(final String desc, final Set<?> expected, final Object[] args) {

        assertThat(set(args)).describedAs(desc) //
            .isEqualTo(expected);

        final List<Object> listOfArgs = Arrays.asList(Optional.ofNullable(args).orElse(new Object[0]));

        assertThat(set().pushAll(listOfArgs)).describedAs(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestSetVarArgs() {

        return Arrays.asList( //
            new Object[] { "int set", new HashSet<>(Arrays.asList(1, 2, 3)), new Object[] { 1, 2, 3 } } //
            , new Object[] { "compound typed set", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a" } } //
            , new Object[] { "duplicated elemement", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a", 1 } } //
            , new Object[] { "empty set", new HashSet<>(), new Object[] {} });
    }

    /**
     * {@link Indolently#range(int, int, int)}
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

        assertThat(range(from, to, step)).describedAs(desc) //
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
