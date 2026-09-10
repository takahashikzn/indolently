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

import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.regex.AutomatonTest;
import jp.root42.indolently.regex.ReTest;
import jp.root42.indolently.regex.Regex;
import jp.root42.indolently.regex.RegexJDK;
import jp.root42.indolently.regex.RegexRe2;


/**
 * @author takahashikzn
 */
public class Regexive {

    /** non private for subtyping. */
    protected Regexive() { }

    /**
     * create pattern instance using JDK regex library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static Regex regex(final String pattern) { return new Regex(regex1(pattern)); }

    /**
     * create pattern instance using JDK regex library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static RegexJDK regex1(final String pattern) { return regex1(java.util.regex.Pattern.compile(pattern)); }

    /**
     * create pattern instance using RE2J library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static RegexRe2 regex2(final String pattern) { return RegexRe2.compile(pattern); }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static RegexJDK regex1(final java.util.regex.Pattern pattern) { return new RegexJDK(pattern); }

    // private static final boolean RE2_AVAIL = ObjFactory.isPresent("com.google.re2j.Pattern");

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static RegexRe2 regex2(final com.google.re2j.Pattern pattern) { return new RegexRe2(pattern); }

    private static final boolean AUTOMATON_AVAIL = ObjFactory.isPresent("dk.brics.automaton.RegExp");

    /**
     * create tester instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static ReTest tester(final String pattern) {
        if (AUTOMATON_AVAIL) {
            final var pred = AutomatonTest.of(pattern);
            if (pred.present()) return pred.get();
        }

        return ReTest.of(regex(pattern));
    }
}
