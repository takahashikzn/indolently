// Copyright 2022 takahashikzn
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
package jp.root42.indolently.regex;

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author takahashikzn.
 */
public class RegexTest {

    @Test
    public void test() {
        //assertThat(Pattern.compile("[\\d\\[\\]]+").matcher("[420]12345[92]14991234567812345671").matches()).isTrue();
        assertThat(retest("[\\d\\[\\]]+").test("[420]12345[92]14991234567812345671")).isTrue();
    }

    @Test
    public void equals() {
        assertThat(re("foo")).isNotEqualTo(re("bar"));
        assertThat(re("foo")).isEqualTo(re("foo"));
    }

    @Test
    public void group() {
        assertThat(re("(\\d+)([a-z]+)").group("_123foo-", 0)).isEqualTo(just("123foo"));
        assertThat(re("(\\d+)([a-z]+)").group("_123foo-", 1)).isEqualTo(just("123"));
        assertThat(re("(\\d+)([a-z]+)").group("_123foo-", 2)).isEqualTo(just("foo"));
        assertThat(re("(\\d+)([a-z]+)").group("_123foo-", 3)).isEqualTo(none());
    }
}
