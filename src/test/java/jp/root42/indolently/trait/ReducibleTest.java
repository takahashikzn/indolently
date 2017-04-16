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
package jp.root42.indolently.trait;

import java.util.function.BiFunction;

import jp.root42.indolently.Indolently;

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


/**
 * @author takahashikzn
 */
public class ReducibleTest {

    /**
     * Test of {@link Reducible#reduce(BiFunction)}.
     */
    @Test
    public void reduce() {

        assertThat(Indolently.<String> list().reduce((x, y) -> x + y).isPresent()).isFalse();
        assertThat(Indolently.<String> list().reduce("0", (x, y) -> x + y)).isEqualTo("0");

        assertThat(list("1").reduce((x, y) -> x + y).get()).isEqualTo("1");
        assertThat(list("1", "2").reduce((x, y) -> x + y).get()).isEqualTo("12");
        assertThat(list("1", "2").reduce("0", (x, y) -> x + y)).isEqualTo("012");
    }
}
