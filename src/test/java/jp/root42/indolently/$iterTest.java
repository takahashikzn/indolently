// Copyright 2016 takahashikzn
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
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static jp.root42.indolently.Indolently.list;
import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.*;


/**
 * @author root42 Inc.
 * @version $Id$
 */
public class $iterTest {

    /**
     * test of {@link $iter#take(java.util.function.Predicate)}
     */
    @Test
    public void filter() {

        assertThat(range(1, 1).take(x -> x < 0).hasNext()).isFalse();
        assertThat(range(1, 2).take(x -> x > 0).hasNext()).isTrue();
        assertThat(range(1, 100000).take(x -> x < 0).hasNext()).isFalse();

        for (final Iterator<Integer> i = range(1, 100000).take(x -> x > 0); i.hasNext(); ) {
            assertThat(i.next()).isGreaterThan(0);
        }
    }

    /**
     * test of {@link $iter#flat(Function)}
     */
    @Test
    public void flatten() {

        assertThat(list(list(1), list(2, 3), list(4)).flat(it())).isEqualTo(list(1, 2, 3, 4));

        final $list<$list<Integer>> list = list(list(1), list(), list(2, 3), list(), list(), list(4));
        assertThat(list.flat(it())).isEqualTo(list(1, 2, 3, 4));

        assertThat($(new ArrayList<List<Integer>>()).flat(it())).isEqualTo(list());
    }
}
