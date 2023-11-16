// Copyright 2023 takahashikzn
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

import static jp.root42.indolently.Numeric.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


/**
 * @author takahashikzn
 */
public class NumericTest {

    @Test
    public void compare() {
        assertThat(compareNumber(decimal(95.5), 95)).isEqualTo(1);
        assertThat(compareNumber(95, decimal(30.5))).isEqualTo(1);
        assertThat(compareNumber(95L, decimal(95.5))).isEqualTo(-1);
        assertThat(compareNumber(95L, 95.5)).isEqualTo(-1);
        assertThat(compareNumber(95.5, 95L)).isEqualTo(1);
    }
}
