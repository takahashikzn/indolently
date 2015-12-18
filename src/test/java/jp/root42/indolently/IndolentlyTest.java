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

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;


/**
 * A test class for {@link Indolently}.
 *
 * @author takahashikzn
 */
@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class IndolentlyTest {

    /**
     * {@link Indolently#gtlt(long, long, long)}/{@link Indolently#gtlt(double, double, double)}/
     * {@link Indolently#gtlt(Comparable, Comparable, Comparable)}
     */
    @Test
    public void testGtlt() {

        assertTrue(gtlt(0, 1, 2));
        assertFalse(gtlt(1, 1, 2));
        assertFalse(gtlt(1, 2, 2));
        assertFalse(gtlt(0, 0, 0));
        assertFalse(gtlt(1, 0, 2));
        assertFalse(gtlt(0, 2, 1));

        assertTrue(gtlt(0.0, 1.0, 2.0));
        assertFalse(gtlt(1.0, 1.0, 2.0));
        assertFalse(gtlt(1.0, 2.0, 2.0));
        assertFalse(gtlt(0.0, 0.0, 0.0));
        assertFalse(gtlt(1.0, 0.0, 2.0));
        assertFalse(gtlt(0.0, 2.0, 1.0));

        assertTrue(gtlt("0", "1", "2"));
        assertFalse(gtlt("1", "1", "2"));
        assertFalse(gtlt("1", "2", "2"));
        assertFalse(gtlt("0", "0", "0"));
        assertFalse(gtlt("1", "0", "2"));
        assertFalse(gtlt("0", "2", "1"));
    }

    /**
     * {@link Indolently#gelt(long, long, long)}/{@link Indolently#gelt(double, double, double)}/
     * {@link Indolently#gelt(Comparable, Comparable, Comparable)}
     */
    @Test
    public void testGelt() {

        assertTrue(gelt(0, 1, 2));
        assertTrue(gelt(1, 1, 2));
        assertFalse(gelt(1, 2, 2));
        assertFalse(gelt(0, 0, 0));
        assertFalse(gelt(1, 0, 2));
        assertFalse(gelt(0, 2, 1));

        assertTrue(gelt(0.0, 1.0, 2.0));
        assertTrue(gelt(1.0, 1.0, 2.0));
        assertFalse(gelt(1.0, 2.0, 2.0));
        assertFalse(gelt(0.0, 0.0, 0.0));
        assertFalse(gelt(1.0, 0.0, 2.0));
        assertFalse(gelt(0.0, 2.0, 1.0));

        assertTrue(gelt("0", "1", "2"));
        assertTrue(gelt("1", "1", "2"));
        assertFalse(gelt("1", "2", "2"));
        assertFalse(gelt("0", "0", "0"));
        assertFalse(gelt("1", "0", "2"));
        assertFalse(gelt("0", "2", "1"));
    }

    /**
     * {@link Indolently#gtle(long, long, long)}/{@link Indolently#gtle(double, double, double)}/
     * {@link Indolently#gtle(Comparable, Comparable, Comparable)}
     */
    @Test
    public void testGtle() {

        assertTrue(gtle(0, 1, 2));
        assertFalse(gtle(1, 1, 2));
        assertTrue(gtle(1, 2, 2));
        assertFalse(gtle(0, 0, 0));
        assertFalse(gtle(1, 0, 2));
        assertFalse(gtle(0, 2, 1));

        assertTrue(gtle(0.0, 1.0, 2.0));
        assertFalse(gtle(1.0, 1.0, 2.0));
        assertTrue(gtle(1.0, 2.0, 2.0));
        assertFalse(gtle(0.0, 0.0, 0.0));
        assertFalse(gtle(1.0, 0.0, 2.0));
        assertFalse(gtle(0.0, 2.0, 1.0));

        assertTrue(gtle("0", "1", "2"));
        assertFalse(gtle("1", "1", "2"));
        assertTrue(gtle("1", "2", "2"));
        assertFalse(gtle("0", "0", "0"));
        assertFalse(gtle("1", "0", "2"));
        assertFalse(gtle("0", "2", "1"));
    }

    /**
     * {@link Indolently#gele(long, long, long)}/{@link Indolently#gele(double, double, double)}/
     * {@link Indolently#gele(Comparable, Comparable, Comparable)}
     */
    @Test
    public void testGele() {

        assertTrue(gele(0, 1, 2));
        assertTrue(gele(1, 1, 2));
        assertTrue(gele(1, 2, 2));
        assertTrue(gele(0, 0, 0));
        assertFalse(gele(1, 0, 2));
        assertFalse(gele(0, 2, 1));

        assertTrue(gele(0.0, 1.0, 2.0));
        assertTrue(gele(1.0, 1.0, 2.0));
        assertTrue(gele(1.0, 2.0, 2.0));
        assertTrue(gele(0.0, 0.0, 0.0));
        assertFalse(gele(1.0, 0.0, 2.0));
        assertFalse(gele(0.0, 2.0, 1.0));

        assertTrue(gele("0", "1", "2"));
        assertTrue(gele("1", "1", "2"));
        assertTrue(gele("1", "2", "2"));
        assertTrue(gele("0", "0", "0"));
        assertFalse(gele("1", "0", "2"));
        assertFalse(gele("0", "2", "1"));
    }

    /**
     * {@link Indolently#ge(long, long)}/{@link Indolently#ge(double, double)}/
     * {@link Indolently#ge(Comparable, Comparable)}
     */
    @Test
    public void testGreaterEqual() {

        assertTrue(ge(2, 1));
        assertTrue(ge(1, 1));
        assertFalse(ge(0, 1));

        assertTrue(ge(2.0, 1.0));
        assertTrue(ge(1.0, 1.0));
        assertFalse(ge(0.0, 1.0));

        assertTrue(ge("2", "1"));
        assertTrue(ge("1", "1"));
        assertFalse(ge("0", "1"));
    }

    /**
     * {@link Indolently#gt(long, long)}/{@link Indolently#gt(double, double)}/
     * {@link Indolently#gt(Comparable, Comparable)}
     */
    @Test
    public void testGreaterThan() {

        assertTrue(gt(2, 1));
        assertFalse(gt(1, 1));
        assertFalse(gt(0, 1));

        assertTrue(gt(2.0, 1.0));
        assertFalse(gt(1.0, 1.0));
        assertFalse(gt(0.0, 1.0));

        assertTrue(gt("2", "1"));
        assertFalse(gt("1", "1"));
        assertFalse(gt("0", "1"));
    }

    /**
     * {@link Indolently#le(long, long)}/{@link Indolently#le(double, double)}/
     * {@link Indolently#le(Comparable, Comparable)}
     */
    @Test
    public void testLessEqual() {

        assertTrue(le(1, 2));
        assertTrue(le(1, 1));
        assertFalse(le(1, 0));

        assertTrue(le(1.0, 2.0));
        assertTrue(le(1.0, 1.0));
        assertFalse(le(1.0, 0.0));

        assertTrue(le("1", "2"));
        assertTrue(le("1", "1"));
        assertFalse(le("1", "0"));
    }

    /**
     * {@link Indolently#lt(long, long)}/{@link Indolently#lt(double, double)}/
     * {@link Indolently#lt(Comparable, Comparable)}
     */
    @Test
    public void testLessThan() {

        assertTrue(lt(1, 2));
        assertFalse(lt(1, 1));
        assertFalse(lt(1, 0));

        assertTrue(lt(1.0, 2.0));
        assertFalse(lt(1.0, 1.0));
        assertFalse(lt(1.0, 0.0));

        assertTrue(lt("1", "2"));
        assertFalse(lt("1", "1"));
        assertFalse(lt("1", "0"));
    }

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
            oarray("null", true, null), //
            oarray("empty collection", true, list()), //
            oarray("non empty collection", false, list(0)), //
            oarray("empty iterable", true, iterator(() -> false, () -> 0)), //
            oarray("non empty iterable", false, iterator(() -> true, () -> 1)) //
        );
    }

    /**
     * {@link Indolently#empty(CharSequence...)}, {@link Indolently#empty(Optional...)},
     * {@link Indolently#empty(Iterable...)}, {@link Indolently#empty(Map...)}
     *
     * @param desc description
     * @param expected expected value
     * @param args args
     * @param type array type
     */
    @Parameters
    @Test
    public void testEmptyVarargs(final String desc, final boolean expected, final Object[] args, final Class<?> type) {

        if (type == String.class) {

            assertThat(empty((CharSequence[]) args)).isEqualTo(expected);
        } else if (type == Optional.class) {

            assertThat(empty((Optional[]) args)).isEqualTo(expected);
        } else if (type == Iterable.class) {

            assertThat(empty((Iterable<?>[]) args)).isEqualTo(expected);
        } else if (type == Map.class) {

            assertThat(empty((Map<?, ?>[]) args)).isEqualTo(expected);
        } else {

            fail();
        }
    }

    static List<?> parametersForTestEmptyVarargs() {

        return list( //
            oarray("null", true, null, Iterable.class), //
            oarray("empty arguments", true, new List<?>[0], Iterable.class), //
            oarray("null argument", true, new List<?>[1], Iterable.class), //
            oarray("empty list", true, arrayOf(list()), Iterable.class), //
            oarray("empty lists", true, arrayOf(list(), list()), Iterable.class), //
            oarray("non empty list only", false, arrayOf(list(0)), Iterable.class), //
            oarray("empty and non empty lists", false, arrayOf(list(0), list()), Iterable.class), //
            oarray("non empty lists only", false, arrayOf(list(0), list(0)), Iterable.class), //
            oarray("null", true, null, Map.class), //
            oarray("empty arguments", true, new Map<?, ?>[0], Map.class), //
            oarray("null argument", true, new Map<?, ?>[1], Map.class), //
            oarray("empty map", true, arrayOf(map()), Map.class), //
            oarray("empty maps", true, arrayOf(map(), map()), Map.class), //
            oarray("non empty map only", false, arrayOf(map("a", 1)), Map.class), //
            oarray("empty and non empty maps", false, arrayOf(map("a", 1), map()), Map.class), //
            oarray("non empty maps only", false, arrayOf(map("a", 1), map("a", 1)), Map.class), //
            oarray("null", true, null, String.class), //
            oarray("empty arguments", true, new String[0], String.class), //
            oarray("null argument", true, new String[1], String.class), //
            oarray("empty string", true, arrayOf(""), String.class), //
            oarray("blank string", false, arrayOf(" "), String.class), //
            oarray("empty strings", true, arrayOf("", ""), String.class), //
            oarray("non empty string only", false, arrayOf("a"), String.class), //
            oarray("empty and non empty strings", false, arrayOf("a", ""), String.class), //
            oarray("non empty strings only", false, arrayOf("a", "b"), String.class), //
            oarray("null", true, null, Optional.class), //
            oarray("empty arguments", true, new Optional[0], Optional.class), //
            oarray("null argument", true, new Optional[1], Optional.class), //
            oarray("empty optional", true, arrayOf(Optional.empty()), Optional.class), //
            oarray("empty optionals", true, arrayOf(Optional.empty(), Optional.empty()), Optional.class), //
            oarray("non empty optional only", false, arrayOf(Optional.of("a")), Optional.class), //
            oarray("empty and non empty optionals", false, arrayOf(Optional.of("a"), Optional.empty()), Optional.class), //
            oarray("non empty optionals only", false, arrayOf(Optional.of("a"), Optional.of("b")), Optional.class));
    }

    /**
     * {@link Indolently#blank(CharSequence...)}
     */
    @Test
    public void testBlankNullPassed() {

        this.testBlank("null", true, null);
    }

    /**
     * {@link Indolently#blank(CharSequence...)}
     *
     * @param desc description
     * @param expected expected value
     * @param val test value
     */
    @Parameters
    @Test
    public void testBlank(final String desc, final boolean expected, final CharSequence[] val) {

        assertThat(blank(val)).as(desc).isEqualTo(expected);
    }

    static List<Object[]> parametersForTestBlank() {

        return list( //
            oarray("empty array", true, new String[0]), //
            oarray("empty", true, array("")), //
            oarray("blank", true, array("  ")), //
            oarray("empty and blank", true, array("", "  ")), //
            oarray("blank strings", true, array("  ", "  ")), //
            oarray("non blank and empty", false, array("a", "")), //
            oarray("non blank and blank", false, array("a", "")), //
            oarray("non blnak strings", false, array("a", "b")) //
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

    private static class SortKey {

        public final int val;

        public SortKey(final int val) {
            this.val = val;
        }
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

        assertThat(sort(set(new SortKey(3), new SortKey(1), new SortKey(2)), x -> x.val).list().map(x -> x.val)) //
            .isEqualTo(list(1, 2, 3));

        assertThat(sort(list(new SortKey(3), new SortKey(1), new SortKey(2)), x -> x.val).map(x -> x.val)) //
            .isEqualTo(list(1, 2, 3));
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

        assertThat(list(
            sort(wrap(new LinkedHashMap<>(), new SortKey(3), "a").push(new SortKey(1), "b").push(new SortKey(2), "c"),
                x -> x.val).keySet()).map(x -> x.val)) //
                    .isEqualTo(list(1, 2, 3));
    }

    /**
     * {@link Indolently#max(Iterable)} / {@link Indolently#min(Iterable)}
     */
    @Test
    public void testMinMax() {

        final List<Integer> ints = range(1, 100).list();

        Collections.shuffle(ints);

        assertThat(min(ints)).isEqualTo(Optional.of(1));
        assertThat(max(ints)).isEqualTo(Optional.of(100));

        // explicit type annotation is required for OrackeJDK' compiler
        assertThat(min(Indolently.<String> list()).isPresent()).isFalse();
        assertThat(max(Indolently.<String> list()).isPresent()).isFalse();
    }

    /**
     * {@link Indolently#min(Comparable, Comparable, Comparable...)} /
     * {@link Indolently#max(Comparable, Comparable, Comparable...)}
     */
    @Test
    public void testMinMaxVarargs() {

        assertThat(min(1, 3, 2, 4)).isEqualTo(1);
        assertThat(max(1, 3, 2, 4)).isEqualTo(4);

        assertThat(min(1, 3, new Integer[0])).isEqualTo(1);
        assertThat(max(1, 3, new Integer[0])).isEqualTo(3);
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
            oarray("standard", set(1, 2, 3, 4), set(1, 2, 3), set(2, 3, 4)), //
            oarray("completely different", set(1, 2, 3, 4, 5, 6), set(1, 2, 3), set(4, 5, 6)), //
            oarray("completely equivalent", set(1, 2, 3), set(1, 2, 3), set(1, 2, 3)), //
            oarray("empty", set(), set(), set()) //
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
            oarray("standard", set(2, 3), set(1, 2, 3), set(2, 3, 4)), //
            oarray("completely different", set(), set(1, 2, 3), set(4, 5, 6)), //
            oarray("completely equivalent", set(1, 2, 3), set(1, 2, 3), set(1, 2, 3)), //
            oarray("empty", set(), set(), set()) //
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
            oarray("standard", set(1, 4), set(1, 2, 3), set(2, 3, 4)), //
            oarray("completely different", set(1, 2, 3, 4, 5, 6), set(1, 2, 3), set(4, 5, 6)), //
            oarray("completely equivalent", set(), set(1, 2, 3), set(1, 2, 3)), //
            oarray("empty", set(), set(), set()) //
        );
    }

    /**
     * {@link Indolently#list(Object...)}
     */
    @Test
    public void testListVarArgsNullPassed() {

        this.testListVarArgs("null args", new ArrayList<>(), null);
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
            new Object[] { "int list", Arrays.asList(1, 2, 3), new Object[] { 1, 2, 3 } }, //
            new Object[] { "compound typed list", Arrays.asList(1, "a"), new Object[] { 1, "a" } }, //
            new Object[] { "empty list", new ArrayList<>(), new Object[] {} });
    }

    /**
     * {@link Indolently#oarray(Object...)}
     */
    @Test
    public void testObjectArray() {

        final SList<Object[]> actual = list( //
            oarray("int list", list(1, 2, 3), oarray(1, 2, 3)), //
            oarray("compound typed list", list(1, "a"), oarray(1, "a")), //
            oarray("empty list", list(), oarray()));

        final List<Object[]> expected = parametersForTestListVarArgs();

        assertThat(actual).hasSize(3);
        assertArrayEquals(expected.get(0), actual.get(0));
        assertArrayEquals(expected.get(1), actual.get(1));
        assertArrayEquals(expected.get(2), actual.get(2));
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
            , "level1",
            map( //
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

        try {
            list0.add(new Object());
            fail();
        } catch (final UnsupportedOperationException e) {
            assert true;
        }

        try {
            list1.add(new Object());
            fail();
        } catch (final UnsupportedOperationException e) {
            assert true;
        }
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
            new Object[] { "int set", new HashSet<>(Arrays.asList(1, 2, 3)), new Object[] { 1, 2, 3 } }, //
            new Object[] { "compound typed set", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a" } }, //
            new Object[] { "duplicated elemement", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a", 1 } }, //
            new Object[] { "empty set", new HashSet<>(), new Object[] {} });
    }

    /**
     * {@link Indolently#map(Object, Object)}
     */
    @Test
    public void testMap() {

        assertThat((Map<String, Integer>) map("0", 0, "1", 1, "2", 2, "3", 3, "4", 4, "5", 5, "6", 6, "7", 7, "8", 8,
            "9", 9, "10", 10, "11", 11, "12", 12, "13", 13, "14", 14, "15", 15, "16", 16, "17", 17, "18", 18, "19", 19,
            "20", 20, "21", 21, "22", 22, "23", 23, "24", 24, "25", 25, "26", 26, "27", 27, "28", 28, "29", 29, "30",
            30, "31", 31)) //
                .hasSize(32) //
                .containsKey("0") //
                .containsValue(0) //
                .containsKey("31") //
                .containsValue(31);
    }

    /**
     * {@link Indolently#assignable(Class)}
     */
    @Test
    public void testAssignable() {

        assertThat(assignable(Integer.class).test(Number.class)).isTrue();
        assertThat(assignable(Number.class).test(Integer.class)).isFalse();
        assertThat(assignable(x -> Number.class, Integer.class).test(0)).isTrue();
        assertThat(assignable(x -> x.getClass(), Number.class).test(0)).isFalse();
    }
}
