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

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;
import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.regex.AdaptiveSPtrn;
import jp.root42.indolently.regex.RETest;
import jp.root42.indolently.regex.SPtrn;
import jp.root42.indolently.regex.SPtrnBase;
import jp.root42.indolently.regex.SPtrnJDK;
import jp.root42.indolently.regex.SPtrnRE2;


/**
 * @author takahashikzn
 */
public class Regexive {

    private static final boolean RE2_AVAIL = ObjFactory.isPresent("com.google.re2j.Pattern");

    private static final boolean AUTOMATON_AVAIL = ObjFactory.isPresent("dk.brics.automaton.RegExp");

    /** non private for subtyping. */
    protected Regexive() {}

    /**
     * create pattern instance using JDK regex library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static SPtrn regex(final String pattern) {
        if (!RE2_AVAIL) {
            return new SPtrn(regex1(pattern));
        }

        final List<SPtrnBase<?, ?>> patterns = Indolently.list();

        try {
            patterns.add(regex2(pattern));
        } catch (final RuntimeException e) {
            if (!e.getClass().getName().equals("com.google.re2j.PatternSyntaxException")) {
                throw e;
            }
        }

        patterns.add(regex1(pattern));

        if (patterns.size() == 1) {
            return new SPtrn(patterns.get(0));
        } else {
            return new SPtrn(new AdaptiveSPtrn(patterns));
        }
    }

    /**
     * create pattern instance using JDK regex library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static SPtrnJDK regex1(final String pattern) {
        return regex1(java.util.regex.Pattern.compile(pattern));
    }

    /**
     * create pattern instance using RE2J library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static SPtrnRE2 regex2(final String pattern) {
        return regex2(com.google.re2j.Pattern.compile(pattern));
    }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static SPtrnJDK regex1(final java.util.regex.Pattern pattern) {
        return new SPtrnJDK(pattern);
    }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static SPtrnRE2 regex2(final com.google.re2j.Pattern pattern) {
        return new SPtrnRE2(pattern);
    }

    /**
     * create tester instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static RETest tester(final String pattern) {
        if (AUTOMATON_AVAIL) {
            try {
                final var pred = automatonTester(pattern);

                if (pred != null) {
                    return pred;
                }
            } catch (final IllegalArgumentException ignored) {}
        }

        return RETest.of(regex(pattern));
    }

    private static final SPtrnJDK JDK_REGEX = regex1(".*(?:" //
        + "[^\\\\][$^]" //
        + "|\\(\\?" //
        + "|\\\\Q" //
        + "|\\\\E" //
        + "|\\\\b" //
        + "|\\\\B" //
        + "|\\\\G" //
        + "|\\\\z" //
        + "|\\\\Z" //
        + "|\\\\p" //
        + "|\\\\n" //
        + "|\\\\k" //
        + "|&&" //
        + "|\\?\\?" //
        + "|\\*\\?" //
        + "|\\+\\?" //
        + "|\\{\\d+(?:,(?:\\d+)?)?}\\?" //
        + ").*");

    private static final String HORIZONTAL_SPACE =
        regex1("[ \t\u00a0\u1680\u180e\u2000-\u200a\u202f\u205f\u3000]").pattern();

    private static final String SPACE = regex1("[ \t\n\u000b\f\r]").pattern();

    private static final String VERTICAL_SPACE = regex1("[\n\u000b\f\r\u0085\u2028\u2029]").pattern();

    private static final String WORD = regex1("[A-Za-z0-9_]").pattern();

    private static final String DIGIT = regex1("[0-9]").pattern();

    private static RETest automatonTester(final String pattern) {

        if (JDK_REGEX.test(pattern)) { return null; }

        final var ra = new RunAutomaton(new RegExp(pattern //
            .replaceAll("(?<!\\\\)\\(\\?:", "(") //
            .replaceAll("\\\\w", WORD) //
            .replaceAll("\\\\W", not(WORD)) //
            .replaceAll("\\\\d", DIGIT) //
            .replaceAll("\\\\D", not(DIGIT)) //
            .replaceAll("\\\\h", HORIZONTAL_SPACE) //
            .replaceAll("\\\\H", not(HORIZONTAL_SPACE)) //
            .replaceAll("\\\\s", SPACE) //
            .replaceAll("\\\\S", not(SPACE)) //
            .replaceAll("\\\\v", VERTICAL_SPACE) //
            .replaceAll("\\\\V", not(VERTICAL_SPACE))) //
            .toAutomaton());

        return RETest.of(x -> ra.run(x.toString()), pattern);
    }

    private static String not(final String word) { return "[^" + word + "]"; }
}
