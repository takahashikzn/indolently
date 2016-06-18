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

import org.junit.Test;

import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Regexive.*;
import static org.assertj.core.api.Assertions.*;


/**
 * @author takahashikzn
 */
public class SMatcherTest {

    /**
     * Test of {@link SMatcher#iterator()}.
     */
    @Test
    public void testRegex() {

        assertThat(list(regex("\\w+").matcher("foo.bar.baz"))) //
            .isEqualTo(list("foo", "bar", "baz"));

        assertThat(list(regex("\\d+").matcher("abc"))) //
            .isEqualTo(list());
    }
}
