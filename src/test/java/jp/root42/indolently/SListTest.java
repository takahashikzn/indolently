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

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Test;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


/**
 * @author takahashikzn
 */
public class SListTest {

    /**
     * Test of {@link SList#group(Function)}.
     */
    @Test
    public void group() {

        assertThat(prog1(() -> list(prog1(() -> new Bean1(), x -> {
            x.key = "key1";
            x.bean2 = prog1(() -> new Bean2(), y -> y.val = 1);
        } ), prog1(() -> new Bean1(), x -> {
            x.key = "key2";
            x.bean2 = prog1(() -> new Bean2(), y -> y.val = 2);
        } ), prog1(() -> new Bean1(), x -> {
            x.key = "key3";
            x.bean2 = prog1(() -> new Bean2(), y -> y.val = 4);
        } ), prog1(() -> new Bean1(), x -> {
            x.key = "key1";
            x.bean2 = prog1(() -> new Bean2(), y -> y.val = 8);
        } ), prog1(() -> new Bean1(), x -> {
            x.key = "key2";
            x.bean2 = prog1(() -> new Bean2(), y -> y.val = 16);
        } )) //
            .group(x -> x.key),
            x -> assertEquals(x.map(y -> y.map(z -> z.bean2.val)),
                map("key1", list(1, 8), "key2", list(2, 16), "key3", list(4)))) //
                    .vals() //
                    .map(x -> x.map(y -> y.bean2.val) //
                        .reduce(0, (y, z) -> y + z)) //
        ) //
            .isEqualTo(list(9, 18, 4));
    }

    static class Bean1 {

        String key;

        Bean2 bean2;
    }

    static class Bean2 {

        int val;
    }

    /**
     * Test of {@link SList#flatten(Function)}
     */
    @Test
    public void flatten() {

        assertThat(list("123", "abc").flatten(x -> plist(x.toCharArray())))
            .isEqualTo(list('1', '2', '3', 'a', 'b', 'c'));
    }

    /**
     * Test of {@link SList#orElse(List)}
     */
    @Test
    public void orElse() {

        assertThat(list().orElse(list(42))).isEqualTo(list(42));
    }

    /**
     * Test of {@link SList#orElseGet(Supplier)}
     */
    @Test
    public void orElseGet() {

        assertThat(list().orElseGet(() -> list(42))).isEqualTo(list(42));
    }
}
