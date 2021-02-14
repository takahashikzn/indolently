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

import dk.brics.automaton.RegExp;
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

    // private static final boolean RE2_AVAIL = ObjFactory.isPresent("com.google.re2j.Pattern");

    private static final boolean AUTOMATON_AVAIL = ObjFactory.isPresent("dk.brics.automaton.RegExp");

    /** non private for subtyping. */
    protected Regexive() {}

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
    public static RegexRe2 regex2(final String pattern) {
        try {
            return regex2(com.google.re2j.Pattern.compile(pattern));
        } catch (final com.google.re2j.PatternSyntaxException e) {
            throw new java.util.regex.PatternSyntaxException(e.getDescription(), e.getPattern(), e.getIndex());
        }
    }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static RegexJDK regex1(final java.util.regex.Pattern pattern) { return new RegexJDK(pattern); }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static RegexRe2 regex2(final com.google.re2j.Pattern pattern) { return new RegexRe2(pattern); }

    /**
     * create tester instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static ReTest tester(final String pattern) {
        if (AUTOMATON_AVAIL) {
            try {
                final var pred = automatonTester(pattern);

                if (pred != null) return pred;
            } catch (final IllegalArgumentException ignored) {}
        }

        return ReTest.of(regex(pattern));
    }

    private static final RegexJDK JDK_REGEX = regex1("(?ms).*(?:" //
        + "[^\\\\]\\$" // unescaped '$'

        + "|[^\\\\]\\[\\[" // unescaped '[['
        + "|[^\\\\\\[]\\^" // unescaped '^' or negated character class '^'
        + "|[^\\\\]][\\[\\]]" // unescaped ']]' or ']['

        + "|\\(\\?[^:]" //
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
        + "|\\?\\?" //
        + "|\\*\\?" //
        + "|\\+\\?" //
        + "|\\{\\d+(?:,(?:\\d+)?)?}\\?" //

        + "|[~&<\"]" // Automaton meta-characters

        + ").*");

    private static boolean isJDKRegex(final String p) { return JDK_REGEX.test(p); }

    private static final String HORIZONTAL_SPACE =
        regex1("[ \t\u00a0\u1680\u180e\u2000-\u200a\u202f\u205f\u3000]").pattern();

    private static final String SPACE = regex1("[ \t\n\u000b\f\r]").pattern();

    private static final String VERTICAL_SPACE = regex1("[\n\u000b\f\r\u0085\u2028\u2029]").pattern();

    private static final String WORD = regex1("[A-Za-z0-9_]").pattern();

    private static final String DIGIT = regex1("[0-9]").pattern();

    private static ReTest automatonTester(final String pattern) {

        final var pt = pattern //
            .replaceAll("(?<!\\\\)\\(\\?:", "(") //
            .replace("\\w", WORD) //
            .replace("\\W", not(WORD)) //
            .replace("\\d", DIGIT) //
            .replace("\\D", not(DIGIT)) //
            .replace("\\h", HORIZONTAL_SPACE) //
            .replace("\\H", not(HORIZONTAL_SPACE)) //
            .replace("\\s", SPACE) //
            .replace("\\S", not(SPACE)) //
            .replace("\\v", VERTICAL_SPACE) //
            .replace("\\V", not(VERTICAL_SPACE)) //
            .replace("\\p{Digit}", "[0-9]") //
            .replace("\\p{Alpha}", "[A-Za-z]") //
            .replace("\\p{Alnum}", "[A-Za-z0-9]") //
            .replace("#", "\\#") //
            .replace("@", "\\@") //
            ;

        if (isJDKRegex(pt)) return null;

        return new AutomatonTest(new RegExp(pt), pattern);
    }

    private static String not(final String word) { return "[^" + word + "]"; }
}
