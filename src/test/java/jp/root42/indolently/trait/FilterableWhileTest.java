// Copyright 2024 takahashikzn
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

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author takahashikzn
 */
public class FilterableWhileTest {

    @Test
    public void takeWhile() {

        assertThat(list(1, 2, 3, 4).takeWhile(lt(3))).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3, 4).dropWhile(lt(3))).isEqualTo(list(3, 4));
        assertThat(list(1, 2, 3, 4).takeWhile(lt(999))).isEqualTo(list(1, 2, 3, 4));
        assertThat(list(1, 2, 3, 4).dropWhile(lt(0))).isEqualTo(list(1, 2, 3, 4));
        assertThat(list(1, 2, 3, 4).takeWhile(lt(0))).isEmpty();
        assertThat(list(1, 2, 3, 4).dropWhile(lt(999))).isEmpty();
    }
}
