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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jp.root42.indolently.trait.Freezable;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;


/**
 * A test class for {@link Indolently}.
 *
 * @author takahashikzn
 * @version $Id$
 */
@RunWith(JUnitParamsRunner.class)
public class IndolentlyTest {

    /**
     * {@link Indolently#empty(Iterable)}
     *
     * @param desc description
     * @param expected expected value
     * @param val test value
     */
    @Parameters
    @Test
    public void testEmpty(final String desc, final boolean expected, final Iterable<?> val) {

        assertThat(empty(val)).as(desc).isEqualTo(expected);
    }

    static List<Object[]> parametersForTestEmpty() {

        return list( //
            oarray("null", true, null) //
            , oarray("empty collection", true, list()) //
            , oarray("non empty collection", false, list(0)) //
            , oarray("empty iterable", true, iterator(() -> false, () -> 0)) //
            , oarray("non empty iterable", false, iterator(() -> true, () -> 1)) //
        );
    }

    /**
     * {@link Indolently#array(Object, Object...)} / {@link Indolently#array(Iterable)} /
     * {@link Indolently#array(Class, Object, Object...)}
     */
    @Test
    public void testArray() {

        assertThat(array(1, 2, 3)).as("int array") //
            .isEqualTo(new Integer[] { 1, 2, 3 }) //
            .isExactlyInstanceOf(Integer[].class);

        assertThat(array(list(1, 2, 3))).as("int list -> array") //
            .isEqualTo(new Integer[] { 1, 2, 3 }) //
            .isExactlyInstanceOf(Integer[].class);

        assertThat(array("a")).as("single element") //
            .isEqualTo(new String[] { "a" }) //
            .isExactlyInstanceOf(String[].class);

        assertThat(array(Number.class, 1, 2, 3)).as("int array as Number[]") //
            .isEqualTo(new Number[] { 1, 2, 3 }) //
            .isExactlyInstanceOf(Number[].class);

        assertThat(array(Number.class, (Integer) null)).as("number empty array") //
            .isEqualTo(new Number[0]) //
            .isExactlyInstanceOf(Number[].class);
    }

    /**
     * {@link Indolently#sort(List)} / {@link Indolently#sort(Set)}
     */
    @Test
    public void testSortListSet() {

        final List<Integer> ints = range(1, 5).list();
        Collections.shuffle(ints);

        assertThat(sort(ints)) //
            .isEqualTo(list(1, 2, 3, 4, 5));
        assertThat(sort(set(ints)).list()) //
            .isEqualTo(list(1, 2, 3, 4, 5));
    }

    /**
     * {@link Indolently#sort(Map)}
     */
    @Test
    public void testSortMap() {

        final SMap<Integer, Integer> map = wrap(new LinkedHashMap<>(), 1, 1).push(3, 3).push(2, 2);

        assertThat(list(map.keySet())) //
            .isEqualTo(list(1, 3, 2));

        assertThat(list(sort(map).keySet())) //
            .isEqualTo(list(1, 2, 3));
    }

    /**
     * {@link Indolently#min(Comparable, Comparable, Comparable...)} /
     * {@link Indolently#min(Comparable, Comparable, Comparable...)}
     */
    @Test
    public void testMinMax() {

        final List<Integer> ints = range(1, 100).list();

        Collections.shuffle(ints);

        assertThat(min(ints)).isEqualTo(1);
        assertThat(max(ints)).isEqualTo(100);
    }

    /**
     * {@link SSet#union(Iterable)}
     *
     * @param desc description
     * @param expected expected value
     * @param lhs left hand side
     * @param rhs right hand side
     */
    @Parameters
    @Test
    public void testUnion(final String desc, final Set<Object> expected, final Set<Object> lhs, final Set<Object> rhs) {

        assertThat(set(lhs).union(rhs)).as(desc) //
            .isEqualTo(expected);

        assertThat(set(rhs).union(lhs)).as(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestUnion() {

        return list( //
            oarray("standard", set(1, 2, 3, 4), set(1, 2, 3), set(2, 3, 4)) //
            , oarray("completely different", set(1, 2, 3, 4, 5, 6), set(1, 2, 3), set(4, 5, 6)) //
            , oarray("completely equivalent", set(1, 2, 3), set(1, 2, 3), set(1, 2, 3)) //
            , oarray("empty", set(), set(), set()) //
        );
    }

    /**
     * {@link SSet#intersect(Iterable)}
     *
     * @param desc description
     * @param expected expected value
     * @param lhs left hand side
     * @param rhs right hand side
     */
    @Parameters
    @Test
    public void testIntersect(final String desc, final Set<Object> expected, final Set<Object> lhs,
        final Set<Object> rhs) {

        assertThat(set(lhs).intersect(rhs)).as(desc) //
            .isEqualTo(expected);

        assertThat(set(rhs).intersect(lhs)).as(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestIntersect() {

        return list( //
            oarray("standard", set(2, 3), set(1, 2, 3), set(2, 3, 4)) //
            , oarray("completely different", set(), set(1, 2, 3), set(4, 5, 6)) //
            , oarray("completely equivalent", set(1, 2, 3), set(1, 2, 3), set(1, 2, 3)) //
            , oarray("empty", set(), set(), set()) //
        );
    }

    /**
     * {@link SSet#diff(Iterable)}
     *
     * @param desc description
     * @param expected expected value
     * @param lhs left hand side
     * @param rhs right hand side
     */
    @Parameters
    @Test
    public void testDiff(final String desc, final Set<Object> expected, final Set<Object> lhs, final Set<Object> rhs) {

        assertThat(set(lhs).diff(rhs)).as(desc) //
            .isEqualTo(expected);

        assertThat(set(rhs).diff(lhs)).as(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestDiff() {

        return list( //
            oarray("standard", set(1, 4), set(1, 2, 3), set(2, 3, 4)) //
            , oarray("completely different", set(1, 2, 3, 4, 5, 6), set(1, 2, 3), set(4, 5, 6)) //
            , oarray("completely equivalent", set(), set(1, 2, 3), set(1, 2, 3)) //
            , oarray("empty", set(), set(), set()) //
        );
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

        assertThat(list(args)).as(desc) //
            .isEqualTo(expected);

        final List<Object> listOfArgs = Arrays.asList(Optional.ofNullable(args).orElse(new Object[0]));

        assertThat(list().pushAll(listOfArgs)).as(desc) //
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

        final SList<Object[]> actual = list( //
            oarray("int list", list(1, 2, 3), oarray(1, 2, 3)) //
            , oarray("compound typed list", list(1, "a"), oarray(1, "a")) //
            , oarray("null args", list(), null) //
            , oarray("empty list", list(), oarray()));

        final List<Object[]> expected = parametersForTestListVarArgs();

        assertThat(actual).hasSize(4);
        assertArrayEquals(expected.get(0), actual.get(0));
        assertArrayEquals(expected.get(1), actual.get(1));
        assertArrayEquals(expected.get(2), actual.get(2));
        assertArrayEquals(expected.get(3), actual.get(3));
    }

    /**
     * {@link Indolently#map()}
     */
    @Test
    public void testWrapVarArgs() {

        assertThat((Map<?, ?>) map()) //
            .isEqualTo(new HashMap<>());

        assertThat((Map<String, String>) map("key", "value")).as("single key/value pair") //
            .isEqualTo(new HashMap<Object, Object>() {
                private static final long serialVersionUID = 2171800138194558313L;
                {
                    this.put("key", "value");
                }
            });

        assertThat((Map<String, ?>) map("int", 1, "string", "abc")).as("compound typed map") //
            .isEqualTo(new HashMap<Object, Object>() {
                private static final long serialVersionUID = 6192281449667726402L;
                {
                    this.put("int", 1);
                    this.put("string", "abc");
                }
            });

        final SMap<String, Object> actualNestedMap = map( //
            "int", 1 //
            , "string", "abc" //
            , "level1", map( //
                "level2", map( //
                    "level3", listOf(map("level4", 42))))).freeze();

        final Map<String, Object> expectedNestedMap = new HashMap<String, Object>() {
            private static final long serialVersionUID = 7482761660213570745L;
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

        assertThat((Map<?, ?>) actualNestedMap).as("nested structure") //
            .isEqualTo(expectedNestedMap);
    }

    /**
     * {@link Freezable#freeze()}
     */
    @Test
    public void testFreeze() {

        final SMap<String, SMap<String, SList<SSet<Integer>>>> frozen = map( //
            "level1", map( //
                "level2", listOf(set(42)))).freeze();

        try {
            frozen.put("level1.1", map("level2.1", listOf(set(43))));
            fail();
        } catch (final UnsupportedOperationException e) {
            assert true;
        }

        try {
            frozen.get("level1").put("level2.1", listOf(set(43)));
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

        final SList<Object> list0 = list(0);
        final SList<Object> list1 = list(1);

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

        assertThat(set(args)).as(desc) //
            .isEqualTo(expected);

        final List<Object> listOfArgs = Arrays.asList(Optional.ofNullable(args).orElse(new Object[0]));

        assertThat(set().pushAll(listOfArgs)).as(desc) //
            .isEqualTo(expected);
    }

    static List<Object[]> parametersForTestSetVarArgs() {

        return Arrays.asList( //
            new Object[] { "int set", new HashSet<>(Arrays.asList(1, 2, 3)), new Object[] { 1, 2, 3 } } //
            , new Object[] { "compound typed set", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a" } } //
            , new Object[] { "duplicated elemement", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a", 1 } } //
            , new Object[] { "empty set", new HashSet<>(), new Object[] {} });
    }
}
