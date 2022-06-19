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

import java.util.function.Predicate;

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Test class for {@link Matchable}.
 *
 * @author takahashikzn
 */
public class MatchableTest {

    /**
     * Test of {@link Matchable#any(Predicate)}
     */
    @Test
    public void some() {

        assertTrue(map("123", 1, "456", 2).keys().any(x -> x.matches("\\d+")));
        assertFalse(map("123", 1, "456", 2).keys().any(x -> x.matches("[A-Za-z]+")));

        assertFalse(map().keys().any(vrai()));
        assertFalse(map().keys().any(faux()));
    }

    /**
     * Test of {@link Matchable#all(Predicate)}
     */
    @Test
    public void every() {

        assertTrue(map("123", 1, "456", 2).keys().all(x -> x.matches("\\d+")));
        assertFalse(map("123", 1, "456", 2).keys().all(x -> x.matches("[A-Za-z]+")));
        assertFalse(map("123", 1, "abc", 2).keys().all(x -> x.matches("[A-Za-z]+")));

        assertTrue(map().keys().all(vrai()));
        assertTrue(map().keys().all(faux()));
    }

    /**
     * Test of {@link Matchable#none(Predicate)}
     */
    @Test
    public void none() {

        assertFalse(map("123", 1, "456", 2).keys().none(x -> x.matches("\\d+")));
        assertTrue(map("123", 1, "456", 2).keys().none(x -> x.matches("[A-Za-z]+")));
        assertFalse(map("123", 1, "abc", 2).keys().none(x -> x.matches("[A-Za-z]+")));

        assertTrue(map().keys().none(vrai()));
        assertTrue(map().keys().none(faux()));
    }
}
