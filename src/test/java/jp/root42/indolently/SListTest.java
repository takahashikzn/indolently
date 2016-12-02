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

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Assert;
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
     * Test of {@link SList#opt(int)}.
     */
    @Test
    public void opt() {

        assertThat(list().opt(0)).isEmpty();
        assertThat(list('a').opt(0)).contains('a');
        assertThat(list('a').opt(1)).isEmpty();
        assertThat(list('a').opt(-1)).contains('a');
        assertThat(list('a').opt(-2)).isEmpty();
    }

    /**
     * Test of {@link SList#group(Function)}.
     */
    @Test
    public void group() {

        assertThat(prog1(() -> list( //
            prog1(Bean1::new, x -> {
                x.key = "key1";
                x.bean2 = prog1(Bean2::new, y -> y.val = 1);
            }), //
            prog1(Bean1::new, x -> {
                x.key = "key2";
                x.bean2 = prog1(Bean2::new, y -> y.val = 2);
            }), //
            prog1(Bean1::new, x -> {
                x.key = "key3";
                x.bean2 = prog1(Bean2::new, y -> y.val = 4);
            }), //
            prog1(Bean1::new, x -> {
                x.key = "key1";
                x.bean2 = prog1(Bean2::new, y -> y.val = 8);
            }), //
            prog1(Bean1::new, x -> {
                x.key = "key2";
                x.bean2 = prog1(Bean2::new, y -> y.val = 16);
            })) //
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

    /**
     * Test of {@link SList#subList(int, int)}
     */
    @Test
    public void subList() {

        assertThat(list(1, 2, 3).subList(1)).isEqualTo(list(2, 3));
        assertThat(list(1, 2, 3).subList(0, -1)).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3).subList(0, -2)).isEqualTo(list(1));
        assertThat(list(1, 2, 3).subList(-3)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).subList(-3, -1)).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3).subList(-3, 0)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).subList(3)).isEqualTo(list());

        try {
            list(1, 2, 3).subList(4);
            Assert.fail();
        } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
            assert true;
        }

        try {
            list(1, 2, 3).subList(0, 4);
            Assert.fail();
        } catch (@SuppressWarnings("unused") final IndexOutOfBoundsException e) {
            assert true;
        }

        try {
            list(1, 2, 3).subList(2, 1);
            Assert.fail();
        } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
            assert true;
        }
    }

    /**
     * Test of {@link SList#slice(int, int)}
     */
    @Test
    public void slice() {

        assertThat(list(1, 2, 3).slice(1)).isEqualTo(list(2, 3));
        assertThat(list(1, 2, 3).slice(0, -1)).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3).slice(0, -2)).isEqualTo(list(1));
        assertThat(list(1, 2, 3).slice(-3)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).slice(-3, -1)).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3).slice(-3, 0)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).slice(3)).isEqualTo(list());
        assertThat(list(1, 2, 3).slice(4)).isEqualTo(list());
        assertThat(list(1, 2, 3).slice(0, 4)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).slice(2, 1)).isEqualTo(list());
        assertThat(list().slice(0, 0)).isEqualTo(list());
        assertThat(list().slice(0, 1)).isEqualTo(list());
        assertThat(list().slice(1, 1)).isEqualTo(list());
        assertThat(list(1).slice(1, 1)).isEqualTo(list());
        assertThat(list().slice(0, -1)).isEqualTo(list());
        assertThat(list(1).slice(0, -1)).isEqualTo(list());
        assertThat(Iterative.range(1, 10).list().slice(-5, 0)).isEqualTo(list(6, 7, 8, 9, 10));

        final SList<Integer> original = list(1, 2, 3);
        final SList<Integer> sliced = original.slice(0, 2);
        sliced.add(4);
        assertThat(original).isEqualTo(list(1, 2, 3));
        assertThat(sliced).isEqualTo(list(1, 2, 4));
    }

    /**
     * Test of {@link SList#narrow(int, int)}
     */
    @Test
    public void narrow() {

        assertThat(list(1, 2, 3).narrow(1)).isEqualTo(list(2, 3));
        assertThat(list(1, 2, 3).narrow(0, -1)).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3).narrow(0, -2)).isEqualTo(list(1));
        assertThat(list(1, 2, 3).narrow(-3)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).narrow(-3, -1)).isEqualTo(list(1, 2));
        assertThat(list(1, 2, 3).narrow(-3, 0)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).narrow(-4, 0)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).narrow(3)).isEqualTo(list());
        assertThat(list(1, 2, 3).narrow(4)).isEqualTo(list());
        assertThat(list(1, 2, 3).narrow(0, 4)).isEqualTo(list(1, 2, 3));
        assertThat(list(1, 2, 3).narrow(2, 1)).isEqualTo(list());
        assertThat(Iterative.range(1, 10).list().narrow(-5, 0)).isEqualTo(list(6, 7, 8, 9, 10));
        assertThat(list().narrow(-1, 0)).isEqualTo(list());

        final SList<Integer> original = list(1, 2, 3);
        final SList<Integer> sliced = original.narrow(0, 2);
        sliced.add(4);
        assertThat(original).isEqualTo(list(1, 2, 4, 3));
        assertThat(sliced).isEqualTo(list(1, 2, 4));
    }

    /**
     * Test of {@link SList#startsWith(Collection)}
     */
    @Test
    public void startsWith() {
        assertThat(list(1, 2, 3).startsWith(null)).isFalse();
        assertThat(list(1, 2, 3).startsWith(list())).isTrue();
        assertThat(list().startsWith(list())).isTrue();
        assertThat(list().startsWith(list(1))).isFalse();

        assertThat(list(1, 2, 3).startsWith(list(1))).isTrue();
        assertThat(list(1, 2, 3).startsWith(list(1, 2))).isTrue();
        assertThat(list(1, 2, 3).startsWith(list(1, 2, 3))).isTrue();
        assertThat(list(1, 2, 3).startsWith(list(1, 2, 3, 4))).isFalse();

        assertThat(list(1, 2, 3).startsWith(list(2))).isFalse();
        assertThat(list(1, 2, 3).startsWith(list(2, 3, 4))).isFalse();
    }

    /**
     * Test of {@link SList#endsWith(Collection)}
     */
    @Test
    public void endsWith() {
        assertThat(list(1, 2, 3).endsWith(null)).isFalse();
        assertThat(list(1, 2, 3).endsWith(list())).isTrue();
        assertThat(list().endsWith(list())).isTrue();
        assertThat(list().endsWith(list(1))).isFalse();

        assertThat(list(1, 2, 3).endsWith(list(3))).isTrue();
        assertThat(list(1, 2, 3).endsWith(list(2, 3))).isTrue();
        assertThat(list(1, 2, 3).endsWith(list(1, 2, 3))).isTrue();
        assertThat(list(1, 2, 3).endsWith(list(1, 2, 3, 4))).isFalse();

        assertThat(list(1, 2, 3).endsWith(list(2))).isFalse();
        assertThat(list(1, 2, 3).endsWith(list(2, 3, 4))).isFalse();
    }
}
