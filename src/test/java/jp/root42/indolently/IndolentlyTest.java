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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jp.root42.indolently.Indolently.Slist;
import jp.root42.indolently.Indolently.Smap;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

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
@RunWith(JUnitParamsRunner.class)
public class IndolentlyTest {

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

    /**
     * {@link Indolently#array(Object...)}
     */
    @Test
    public void testObjectArray() {

        final Slist<Object[]> actual = list( //
            oarray("int list", list(1, 2, 3), oarray(1, 2, 3)) //
            , oarray("mixed list", list(1, "a"), oarray(1, "a")) //
            , oarray("null args", list(), null) //
            , oarray("empty list", list(), oarray()));

        final List<Object[]> expected = parametersForTestListVarArgs();

        assertThat(actual).hasSize(4);
        assertArrayEquals(actual.get(0), expected.get(0));
        assertArrayEquals(actual.get(1), expected.get(1));
        assertArrayEquals(actual.get(2), expected.get(2));
        assertArrayEquals(actual.get(3), expected.get(3));
    }

    static List<Object[]> parametersForTestListVarArgs() {

        return Arrays.asList( //
            new Object[] { "int list", Arrays.asList(1, 2, 3), new Object[] { 1, 2, 3 } } //
            , new Object[] { "mixed list", Arrays.asList(1, "a"), new Object[] { 1, "a" } } //
            , new Object[] { "null args", new ArrayList<>(), null } //
            , new Object[] { "empty list", new ArrayList<>(), new Object[] {} });
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
                private static final long serialVersionUID = 1L;
                {
                    this.put("key", "value");
                }
            });

        assertThat(map("int", 1, "string", "abc")).describedAs("mixed type values") //
            .isEqualTo(new HashMap<Object, Object>() {
                private static final long serialVersionUID = 1L;
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
                    "level3", list(map("level4", 42)))));

        final Map<String, Object> expectedNestedMap = new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;
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
            new Object[] { "int Set", new HashSet<>(Arrays.asList(1, 2, 3)), new Object[] { 1, 2, 3 } } //
            , new Object[] { "mixed Set", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a" } } //
            , new Object[] { "duplicated elemement", new HashSet<>(Arrays.asList(1, "a")), new Object[] { 1, "a", 1 } } //
            , new Object[] { "empty Set", new HashSet<>(), new Object[] {} });
    }
}
