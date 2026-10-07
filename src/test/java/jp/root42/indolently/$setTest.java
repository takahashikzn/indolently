// Copyright 2026 takahashikzn
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

import java.util.Comparator;
import java.util.function.Function;

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit test of {@link $set}.
 *
 * @author takahashikzn
 */
public class $setTest {

    /**
     * {@link $set#join(Function, String)}
     */
    @Test
    public void join() {

        assertThat(set(1, 2, 3).order(Comparator.reverseOrder()).join(",")).isEqualTo("3,2,1");
        assertThat(set(1, 2, 3).order(Comparator.reverseOrder()).join(x -> "x" + x, ",")).isEqualTo("x3,x2,x1");
        assertThat(set(1, 2).join(x -> "x", ",")).isEqualTo("x,x");
    }
}
