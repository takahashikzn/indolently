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
import java.util.List;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

import static jp.root42.indolently.Indolently.*;
import static org.assertj.core.api.Assertions.*;


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
    }

    static List<Object[]> parametersForTestListVarArgs() {

        return Arrays.asList( //
            new Object[] { "int list", Arrays.asList(1, 2, 3), new Object[] { 1, 2, 3 } } //
            , new Object[] { "mixed list", Arrays.asList(1, "a"), new Object[] { 1, "a" } } //
            , new Object[] { "empty list", new ArrayList<>(), new Object[] {} });
    }
}
