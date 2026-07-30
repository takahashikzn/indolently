// Copyright 2026 takahashikzn
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

import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import static jp.root42.indolently.Indolently.*;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author takahashikzn.
 */
public class AutomatonTestTest {

    /** patterns this engine has to take over. */
    private static final String[] TRANSLATABLE = { //
        "-?\\d+", //
        "\\d{6}", //
        "\\d{20}(?:-(?:\\d{5}|\\d{9}|\\d{11})?)?", //
        "\\d{4}-\\d{1,2}-\\d{1,2}(?:T|\\s+)\\d{1,2}:\\d{1,2}Z?", //
        "\\d{7}(?:(?:\\p{Alnum}+-)*\\p{Alnum}+-?)?", //
        "\\d{4}\\D\\d.+", //
        "[A-Za-z0-9]+([-_][A-Za-z0-9]+)*", //
        "(?:https?://|application|vnd\\.|xmlns[=:]).+", //
        "(java|javax|jdk)\\..+", //
        "com[./]github[./]hervian[./]lambdas[./]Lambda", //
        ".*#\\w+\\s*\\([^)]*", //
        "\\p{ASCII}+", //
        "\\p{Print}*", //
        "[-. $/+%0-9A-Z]+", //
        "[_$\\d]\\w+", //
        "[\\d\\[\\]]+", //
        "[\\w-]+", //
        "[a][b]", // adjacent character classes
        "[a-]", "[-a]", // literal hyphen
        "[a\\]b]", // escaped bracket
        "a\\tb", "a\\nb", // control escapes
        "\\W", "a{2,}", "a*b+c?", //
        "(?s)a.b", "(?ms).*%\\d?\\{.+", "(?ms)[^%$]*[%$].*", "(?m)abc", // leading inline flags
    };

    /** patterns this engine has to decline. */
    private static final String[] DECLINED = { //
        "(a)\\1", // back reference
        "\\Aabc", "x\\bz", "x\\Bz", "a\\Rb", "\\p{L}", "\\P{Alpha}", // unsupported escapes
        "a\\x41b", "a\\u0041b", "a\\0101b", "a\\cAb", // numeric escapes
        "a?+", "(ab){2}+", "a{1,2}+", // possessive
        "a+?", "a??", "a*?", "a{1,2}?", // reluctant
        "\\Q.\\E", "(?=a)b", "(?<x>a)", "(?>a)", "(?s:a)", // unsupported groups
        "[a-z&&[^bc]]", "[a-z[0-9]]", "[\\W]", "[]", // unsupported classes
        "|a", "a|", "(a|)", "()", "{2}", "*a", "a{", "(a", "a)", // degenerate
        "\\D\\S", "..", ".{2}", "\\D\\s?\\D", "[^a][^b]", ".{2,3}", "(?s).{2}", // surrogate pair splitting
        "(?m)^abc$", "(?i)a", "(?ims)a", "(?x)a b", "(?u)a", // unusable inline flags
        "^abc$", "^abc", "abc$", "a|^b", // anchors: no-op under matches() but not under find()
        "\ud83d\ude00+", "[\ud83d\ude00]", // supplementary character in the pattern
        "[a-z]{1,20000}", // repetition budget
    };

    /** {@code { pattern, input... }}: the tester has to agree with the JDK engine. */
    private static final String[][] EQUIVALENT = { //
        { "a.b", "a\nb", "a\rb", "axb", "a b", "ab" }, //
        { ".", "\ud83d\ude00", "x", "\n", "", "ab" }, //
        { ".+", "\ud83d\ude00", "a\ud83d\ude00b", "", "a\nb" }, //
        { "[^x]", "\ud83d\ude00", "y", "x", "\n" }, //
        { "\\W", "!", "a", "\ud83d\ude00", "\u00e9" }, //
        { "\\D+", "ab", "12", "a1" }, //
        { "\\S", "a", " ", "\t" }, //
        { "\\h+", "  \t", "x", "\u3000" }, //
        { "\\v+", "\n\r", "x", "\u2028" }, //
        { "\\p{ASCII}+", "abc", "\u00e9", "" }, //
        { "\\p{Print}+", "a b", "a\tb" }, //
        { "^abc$", "abc", "abc\n", "xabc" }, //
        { "\\$[0-9]+", "$42", "42", "$" }, //
        { "[_$\\d]\\w+", "$ab", "%ab", "1_x" }, //
        { "[-. $/+%0-9A-Z]+", "A-1 $", "a", "" }, //
        { "[\\w-]+", "a-b", "a!b" }, //
        { "[\\d\\[\\]]+", "[420]12345[92]1499", "1a" }, //
        { "a\\tb", "a\tb", "atb" }, //
        { "a\\nb", "a\nb", "anb" }, //
        { "[a][b]", "ab", "a", "" }, //
        { "[a-]", "a", "-", "b" }, //
        { "[-a]", "a", "-", "b" }, //
        { "[a\\]b]", "]", "a", "b", "c" }, //
        { "\\d{4}\\D\\d.+", "2024/1/23", "2024123", "2024/1\n3" }, //
        { "(?:https?://|application|vnd\\.|xmlns[=:]).+", "https://x.y", "vnd.a", "xmlns=a\nb", "ftp://x" }, //
        { "[A-Za-z0-9]+([-_][A-Za-z0-9]+)*", "snake_case-1", "-x", "x-" }, //
        // the ones below have to fall back, but the answer still has to be right
        { "(a)\\1", "aa", "a1" }, //
        { "\\Aabc", "abc", "Aabc" }, //
        { "a\\x41b", "aAb", "ax41b" }, //
        { "a?+", "aa", "a", "" }, //
        { "a{1,2}+", "aaa", "aa" }, //
        { "a+?", "", "a", "aa" }, //
        { "\\\\w", "\\w", "[A-Za-z0-9_]" }, //
        { "\\\\(?:x)", "\\x", "\\?:x" }, //
        { "[a-z&&[^bc]]", "a", "b" }, //
        { "[a-z[0-9]]", "a", "5", "!" }, //
        { "\ud83d\ude00+", "\ud83d\ude00", "\ud83d\ude00\ud83d\ude00", "\ude00" }, //
        { "\\D\\s?\\D", "\ud83d\ude00", "ab", "a b" }, //
        { ".{2}", "\ud83d\ude00", "ab", "a" }, //
        // leading inline flags
        { "(?s)a.b", "a\nb", "axb", "ab" }, //
        { "(?s).", "\n", "\ud83d\ude00", "x", "ab" }, //
        { "(?ms).*%\\d?\\{.+", "x%{a}y", "a\n<% #foreach($l in %{lines}) #end %>\nb", "a\nb" }, //
        { "(?ms)[^%$]*[%$].*", "a\n%{x}", "a\nb", "%" }, //
        { "(?m)^abc$", "abc", "x\nabc", "abc\nx" }, //
        { "(?i)abc", "ABC", "abc", "xbc" }, //
    };

    @Test
    public void translatable() {
        for (final var p : TRANSLATABLE)
            assertThat(AutomatonTest.of(p).present()).as("translatable: %s", p).isTrue();
    }

    @Test
    public void declined() {
        for (final var p : DECLINED)
            assertThat(AutomatonTest.of(p).present()).as("declined: %s", p).isFalse();
    }

    @Test
    public void equivalent() {
        for (final var row : EQUIVALENT) {
            final var ptrn = Pattern.compile(row[0]);
            final var tester = retest(row[0]);

            for (var i = 1; i < row.length; i++)
                assertThat(tester.test(row[i])).as("%s vs %s", row[0], dump(row[i])) //
                    .isEqualTo(ptrn.matcher(row[i]).matches());
        }
    }

    /** an element of a randomly assembled pattern. */
    private static final String[] TOKENS = { //
        "a", "b", "1", "-", "_", ".", "*", "+", "?", "|", "(", ")", "[", "]", "{", "}", "^", "$", "&&", //
        "[a-z]", "[^a-z]", "[-a]", "[a-]", "[\\w]", "[\\d-]", "[a\\]b]", "[^\\s]", "[\\p{Alpha}x]", "[]", //
        "[.^$]", "[a-z0-9_]", "[^]", "[\\t\\n]", "[\\\\]", "[&&]", "[a&b]", "[\\p{Punct}]", "[\\x41]", //
        "\\\\", "\\d", "\\w", "\\s", "\\W", "\\D", "\\S", "\\h", "\\H", "\\v", "\\V", "\\p{Alnum}", //
        "\\p{ASCII}", "\\p{Cntrl}", "\\p{XDigit}", "\\p{Blank}", "\\p{Graph}", "\\p{L}", "\\P{Alpha}", //
        "(?:", "(?=", "(?!", "(?i)", "{0}", "{2}", "{1,3}", "{2,}", "{0,1}", "\\t", "\\n", "\\r", "\\f", //
        "\\a", "\\e", "\\Q", "\\E", "\\b", "\\A", "\\z", "\\Z", "\\1", "\\x41", "\\u0041", "\\cA", "\\0101", //
        "\\.", "\\-", "\\]", "\\[", "\\$", "\\^", "\\+", "\\*", "\\?", "\\{", "\\}", "\\|", "\\/", "\\&", //
        "a-z", "0-9", "\u00e9", "\u3042", "\ud83d\ude00", "\ud800", "\r\n", "", " ", //
    };

    /** a leading inline flag group of a randomly assembled pattern. */
    private static final String[] PREFIX = { "", "", "", "(?s)", "(?m)", "(?ms)", "(?sm)", "(?i)", "(?ims)" };

    /** a character of a randomly assembled input. */
    private static final char[] ALPHA = { //
        'a', 'b', '1', '-', '_', '.', ' ', '\n', '\r', '\t', 0x0b, '\f', 0x07, '\u00a0', '\u3000', //
        'A', 'Z', '[', ']', '$', '\\', '^', '&', '|', '{', '}', '(', ')', '\u00e9', '\u3042', '\u2028', '\u0085', //
    };

    /**
     * assemble random patterns, and make sure the translated ones answer exactly as the JDK engine does.
     */
    @Test
    public void differential() {
        for (final var seed : new long[] { 1, 7, 42 }) {
            final var rnd = new Random(seed);

            final var in = new String[32];
            for (var i = 0; i < in.length; i++) {
                final var sb = new StringBuilder();
                for (var j = rnd.nextInt(6); 0 < j; j--) sb.append(ALPHA[rnd.nextInt(ALPHA.length)]);
                in[i] = sb.toString();
            }
            in[0] = "";
            in[1] = "\ud83d\ude00";
            in[2] = "a\ud83d\ude00b";
            in[3] = "\ud800"; // lone surrogate
            in[4] = "\r\n";

            var translated = 0;

            for (var n = 0; n < 30000; n++) {
                final var sb = new StringBuilder();
                sb.append(PREFIX[rnd.nextInt(PREFIX.length)]);
                for (var j = 1 + rnd.nextInt(7); 0 < j; j--) sb.append(TOKENS[rnd.nextInt(TOKENS.length)]);
                final var p = sb.toString();

                final Pattern ptrn;
                try { ptrn = Pattern.compile(p); } catch (final PatternSyntaxException e) { continue; }

                final var tester = AutomatonTest.of(p);
                if (tester.empty()) continue;
                translated++;

                for (final var s : in) {
                    assertThat(tester.get().test(s)).as("matches: %s vs %s", dump(p), dump(s)) //
                        .isEqualTo(ptrn.matcher(s).matches());

                    // Indolently#refind drives the very same instance with find() semantics
                    assertThat(tester.get().find(s)).as("find: %s vs %s", dump(p), dump(s)) //
                        .isEqualTo(ptrn.matcher(s).find());
                }
            }

            assertThat(translated).as("seed %d", seed).isGreaterThan(1000);
        }
    }

    private static String dump(final String s) {
        final var sb = new StringBuilder();
        for (final var c : s.toCharArray())
            sb.append(((0x20 <= c) && (c < 0x7f)) ? String.valueOf(c) : String.format("\\u%04x", (int) c));
        return sb.toString();
    }
}
