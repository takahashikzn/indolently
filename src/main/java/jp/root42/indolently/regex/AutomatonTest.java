// Copyright 2020 takahashikzn
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

import java.util.function.Function;
import java.util.regex.MatchResult;

import dk.brics.automaton.AutomatonMatcher;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;

import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public class AutomatonTest
    implements ReTest, ReFindable {

    private final RegExp re;

    final RunAutomaton automaton;

    private final String pattern;

    public AutomatonTest(final RegExp re, final String pattern) { this(re, new RunAutomaton(re.toAutomaton()), pattern); }

    public AutomatonTest(final RegExp re, final RunAutomaton automaton, final String pattern) {
        this.re = re;
        this.automaton = automaton;
        this.pattern = pattern;
    }

    @Override
    public String pattern() { return this.pattern; }

    public RegExp regex() { return this.re; }

    @Override
    public boolean test(final CharSequence cs) { return this.automaton.run(cs.toString()); }

    @Override
    public boolean find(final CharSequence cs) { return this.matcher(cs).find(); }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public ReMatcherAutomaton matcher(final CharSequence cs) { return new ReMatcherAutomaton(this, cs); }

    @Override
    public String toString() { return this.pattern(); }

    /**
     * create tester instance for the JDK regex pattern, if this engine can express it.
     * <p>
     * the pattern is translated by a whitelist: everything which is not known to denote the very same language
     * on both engines makes it bail out, hence the caller has to fall back to the JDK engine. note that the
     * translation assumes {@link java.util.regex.Matcher#matches()} semantics.
     *
     * @param pattern JDK pattern string
     * @return created instance, or none if the pattern is not translatable
     */
    public static $<AutomatonTest> of(final String pattern) {
        try {
            return nonEmpty(translate(pattern)).map(x -> new AutomatonTest(new RegExp(x, RegExp.NONE), pattern));
        } catch (@SuppressWarnings("ErrorNotRethrown") final RuntimeException | StackOverflowError __) {
            return none();
        }
    }

    /** character class body of {@code \w}. */
    private static final String WORD = "A-Za-z0-9_";

    /** character class body of {@code \d}. */
    private static final String DIGIT = "0-9";

    /** character class body of {@code \s}. */
    private static final String SPACE = " \t\n\u000b\f\r";

    /** character class body of {@code \h}. */
    private static final String H_SPACE = " \t\u00a0\u1680\u180e\u2000-\u200a\u202f\u205f\u3000";

    /** character class body of {@code \v}. */
    private static final String V_SPACE = "\n\u000b\f\r\u0085\u2028\u2029";

    /** the negated shorthands, which expand to an element splittable into UTF-16 units. */
    private static final String NEGATED = "WDSHV";

    /** this engine counts UTF-16 units where JDK counts code points, so a pair has to be spelled out. */
    private static final String PAIR = "[" + range(Character.MIN_HIGH_SURROGATE, Character.MAX_HIGH_SURROGATE) + "]" //
                                       + "[" + range(Character.MIN_LOW_SURROGATE, Character.MAX_LOW_SURROGATE) + "]";

    /** JDK {@code .}: any single code point but line terminators. */
    private static final String ANY = "([^" //
                                      + esc('\n') + esc('\r') + esc('\u0085') + esc('\u2028') + esc('\u2029') //
                                      + "]|" + PAIR + ")";

    /** JDK {@code .} under {@link java.util.regex.Pattern#DOTALL}: any single code point. */
    private static final String ANY_ALL = "(.|" + PAIR + ")";

    /** the leading inline flags this translator understands. */
    private static final String FLAGS = "ms";

    /** upper bound of the total repetition count, to keep DFA construction sane. */
    private static final int REPEAT_BUDGET = 1024;

    private static final int MAX_DEPTH = 32;

    private static final int UNBOUND = -1;

    /**
     * translate JDK regex syntax into this engine's syntax.
     *
     * @param p JDK pattern string
     * @return translated pattern string, or null if not translatable
     */
    private static String translate(final String p) {

        final var len = p.length();
        if (len == 0) return null;

        var head = 0; // index the pattern body starts at
        var dotAll = false;

        if (p.startsWith("(?")) { // leading inline flags, which apply to the whole pattern
            var k = 2;
            while ((k < len) && (FLAGS.indexOf(p.charAt(k)) != -1)) k++;

            if ((2 < k) && (k < len) && (p.charAt(k) == ')')) { // not a group like (?: or an unknown flag like (?i)
                for (var f = 2; f < k; f++) dotAll |= p.charAt(f) == 's'; // MULTILINE only affects anchors, which are declined anyway
                head = k + 1;
            }
        }

        final var any = dotAll ? ANY_ALL : ANY;

        final var out = new StringBuilder(len * 2);

        final var entry = new boolean[MAX_DEPTH + 1]; // tail on entering the group
        final var alt = new boolean[MAX_DEPTH + 1]; // tail of any alternative of the group closed so far

        var quantified = false; // whether the last element is quantified: reluctant/possessive detection
        var atom = false; // whether the current alternative has an element: this engine has no empty alternative
        var tail = false; // whether a splittable element can end here
        var prev = false; // tail as of just before the last element, to let an optional element pass it through
        var depth = 0;
        var reps = 0;

        for (var i = head; i < len; i++) {
            final var c = p.charAt(i);

            switch (c) {
                case '\\' -> {
                    final var n = escape(p, i, out, false);
                    if (n < 0) return null;

                    final var split = NEGATED.indexOf(p.charAt(i + 1)) != -1;
                    if (split && tail) return null; // two splittable elements can share a surrogate pair

                    i = n;
                    quantified = false;
                    atom = true;
                    prev = tail;
                    tail = split;
                }
                case '.' -> {
                    if (tail) return null;

                    out.append(any);
                    quantified = false;
                    atom = true;
                    prev = tail;
                    tail = true;
                }
                case '[' -> {
                    final var n = charClass(p, i, out);
                    if (n < 0) return null;

                    final var split = p.charAt(i + 1) == '^';
                    if (split && tail) return null;

                    i = n;
                    quantified = false;
                    atom = true;
                    prev = tail;
                    tail = split;
                }
                case '(' -> {
                    if (p.startsWith("(?", i)) {
                        if (!p.startsWith("(?:", i)) return null; // lookaround, inline flag, named group, ...
                        i += 2;
                    }
                    if (MAX_DEPTH <= depth) return null;

                    out.append('(');
                    entry[++depth] = tail;
                    alt[depth] = false;
                    quantified = false;
                    atom = false;
                }
                case ')' -> {
                    if (!atom || (depth == 0)) return null; // empty or unbalanced group

                    out.append(c);
                    prev = entry[depth];
                    tail |= alt[depth--];
                    quantified = false;
                }
                case '|' -> {
                    if (!atom) return null; // empty alternative

                    out.append(c);
                    alt[depth] |= tail;
                    tail = entry[depth];
                    quantified = false;
                    atom = false;
                }
                case '?', '*', '+' -> {
                    if (!atom || quantified) return null; // dangling, reluctant or possessive

                    out.append(c);
                    if (c != '+') tail |= prev; // the element can be skipped altogether
                    quantified = true;
                }
                case '{' -> {
                    if (!atom || quantified) return null; // dangling, reluctant or possessive

                    var j = i + 1;
                    while ((j < len) && Character.isDigit(p.charAt(j))) j++;
                    if ((j == i + 1) || (9 < j - i - 1)) return null;
                    final var lo = Integer.parseInt(p, i + 1, j, 10);

                    var hi = lo;
                    if ((j < len) && (p.charAt(j) == ',')) {
                        final var k = ++j;
                        while ((j < len) && Character.isDigit(p.charAt(j))) j++;
                        if (9 < j - k) return null;
                        hi = (j == k) ? UNBOUND : Integer.parseInt(p, k, j, 10);
                    }
                    if ((len <= j) || (p.charAt(j) != '}')) return null;
                    if (REPEAT_BUDGET < (reps += (hi == UNBOUND) ? lo : hi)) return null; // DFA blowup
                    // a splittable element taken twice can eat a surrogate pair as two code points
                    if (tail && ((2 <= lo) || ((hi != UNBOUND) && (2 <= hi)))) return null;

                    out.append(p, i, j + 1);
                    i = j;
                    if (lo == 0) tail |= prev; // the element can be skipped altogether
                    quantified = true;
                }
                // an anchor is a no-op under matches() but not under find(), and this engine has none
                case '^', '$' -> { return null; }
                default -> {
                    if (Character.isSurrogate(c)) return null;

                    esc(out, c);
                    quantified = false;
                    atom = true;
                    prev = tail;
                    tail = false;
                }
            }
        }

        return (atom && (depth == 0)) ? out.toString() : null;
    }

    /**
     * translate a backslash escape.
     *
     * @param i index of the backslash
     * @param inClass whether the escape sits in a character class
     * @return index of the last character consumed, or -1 if not translatable
     */
    private static int escape(final String p, final int i, final StringBuilder out, final boolean inClass) {

        final var lit = classChar(p, i);
        if (0 <= lit) {
            esc(out, (char) lit);
            return i + 1;
        }

        if (p.length() <= i + 1) return -1;

        final var c = p.charAt(i + 1);

        if (c == 'p') { // \p{Alnum} and friends
            if ((p.length() <= i + 2) || (p.charAt(i + 2) != '{')) return -1;
            final var end = p.indexOf('}', i + 3);
            if (end < 0) return -1;
            final var posix = posix(p.substring(i + 3, end));
            if (posix == null) return -1;

            klass(out, posix, false, inClass);
            return end;
        }

        final var body = switch (c) {
            case 'w', 'W' -> WORD;
            case 'd', 'D' -> DIGIT;
            case 's', 'S' -> SPACE;
            case 'h', 'H' -> H_SPACE;
            case 'v', 'V' -> V_SPACE;
            // \A \b \B \G \z \Z \Q \E \k \X \R \N \P, back reference, octal/hex/unicode/control escape, ...
            default -> null;
        };
        if (body == null) return -1;

        final var negate = Character.isUpperCase(c);
        if (negate && inClass) return -1; // [\W] cannot be spliced

        klass(out, body, negate, inClass);
        return i + 1;
    }

    /**
     * translate a character class.
     *
     * @param start index of the opening bracket
     * @return index of the closing bracket, or -1 if not translatable
     */
    private static int charClass(final String p, final int start, final StringBuilder out) {

        final var len = p.length();

        var i = start + 1;
        final var negate = (i < len) && (p.charAt(i) == '^');
        if (negate) i++;
        if ((len <= i) || (p.charAt(i) == ']')) return -1; // JDK rejects an empty class

        final var body = new StringBuilder();

        while ((i < len) && (p.charAt(i) != ']')) {
            final var c = p.charAt(i);

            if (c == '[') return -1; // class union
            if (p.startsWith("&&", i)) return -1; // class intersection

            final var lo = classChar(p, i);

            if (lo < 0) { // has to be a shorthand to splice
                if (c != '\\') return -1;

                final var n = escape(p, i, body, true);
                if (n < 0) return -1;
                i = n + 1;

                // JDK gives no clear meaning to a range which starts at a shorthand
                if ((i + 1 < len) && (p.charAt(i) == '-') && (p.charAt(i + 1) != ']')) return -1;
                continue;
            }

            i += (c == '\\') ? 2 : 1;

            if ((i + 1 < len) && (p.charAt(i) == '-') && (p.charAt(i + 1) != ']')) {
                final var hi = classChar(p, i + 1);
                if (hi < 0) return -1;

                body.append(esc((char) lo)).append('-').append(esc((char) hi));
                i += 1 + ((p.charAt(i + 1) == '\\') ? 2 : 1);
            } else body.append(esc((char) lo));
        }

        if (len <= i) return -1; // unclosed

        klass(out, body.toString(), negate, false);

        return i;
    }

    /**
     * decode the character class atom which denotes a single character.
     *
     * @param i index of the atom
     * @return the character it denotes, or -1 if it denotes anything else
     */
    private static int classChar(final String p, final int i) {

        final var c = p.charAt(i);

        if (c != '\\') return ((c == '[') || (c == ']') || Character.isSurrogate(c)) ? -1 : c;
        if (p.length() <= i + 1) return -1;

        final var e = p.charAt(i + 1);
        if (Character.isSurrogate(e)) return -1;
        if (!Character.isLetterOrDigit(e)) return e; // an escaped symbol is a literal on both engines

        return switch (e) {
            case 't' -> '\t';
            case 'n' -> '\n';
            case 'r' -> '\r';
            case 'f' -> '\f';
            case 'a' -> '\u0007';
            case 'e' -> '\u001b';
            default -> -1;
        };
    }

    /** character class body of the POSIX class, or null if unknown. */
    private static String posix(final String name) {
        return switch (name) {
            case "Digit" -> DIGIT;
            case "Alpha" -> "A-Za-z";
            case "Alnum" -> "A-Za-z0-9";
            case "Upper" -> "A-Z";
            case "Lower" -> "a-z";
            case "XDigit" -> "0-9A-Fa-f";
            case "Blank" -> esc('\t') + esc(' ');
            case "Space" -> SPACE;
            case "Cntrl" -> range(Character.MIN_VALUE, '\u001f') + esc('\u007f');
            case "ASCII" -> range(Character.MIN_VALUE, '\u007f');
            case "Punct" -> range('!', '/') + range(':', '@') + range('[', '`') + range('{', '~');
            case "Graph" -> range('!', '~');
            case "Print" -> range(' ', '~');
            default -> null;
        };
    }

    private static void klass(final StringBuilder out, final String body, final boolean negate, final boolean inClass) {
        if (inClass) out.append(body);
        else if (!negate) out.append('[').append(body).append(']');
        else out.append("([^").append(body).append("]|").append(PAIR).append(')');
    }

    private static void esc(final StringBuilder out, final char c) { out.append('\\').append(c); }

    private static String esc(final char c) { return "\\" + c; }

    private static String range(final char lo, final char hi) { return esc(lo) + '-' + esc(hi); }
}

final class ReMatcherAutomaton
    implements ReMatcher<AutomatonTest, ReMatcherAutomaton> {

    private final AutomatonTest pattern;

    private CharSequence input;

    private AutomatonMatcher matcher;

    public ReMatcherAutomaton(final AutomatonTest pattern, final CharSequence input) {
        this.pattern = pattern;
        this.input = input;
    }

    private AutomatonMatcher matcher() {
        if (this.matcher == null) this.matcher = this.pattern.automaton.newMatcher(this.input);
        return this.matcher;
    }

    private AutomatonMatcher matcher(final int start) {
        if (this.matcher == null) this.matcher = this.pattern.automaton.newMatcher(this.input, start, this.input.length());
        return this.matcher;
    }

    @Override
    public AutomatonTest pattern() { return this.pattern; }

    @Override
    public MatchResult toMatchResult() { return this.matcher().toMatchResult(); }

    @Override
    public ReMatcherAutomaton usePattern(final AutomatonTest newPattern) { throw new UnsupportedOperationException(); }

    @Override
    public ReMatcherAutomaton reset() { return this.reset(this.input); }

    @Override
    public ReMatcherAutomaton reset(final CharSequence input) {
        this.input = input;
        this.matcher = null;
        return this;
    }

    @Override
    public int start(final String name) { throw new UnsupportedOperationException(); }

    @Override
    public int end(final String name) { throw new UnsupportedOperationException(); }

    @Override
    public String group(final String name) { throw new UnsupportedOperationException(); }

    @Override
    public boolean matches() {
        if (!this.find()) return false;
        return (this.matcher().start() == 0) && (this.matcher().end() == this.input.length());
    }

    @Override
    public boolean find() { return this.matcher().find(); }

    @Override
    public boolean find(final int start) { return this.matcher(start).find(); }

    @Override
    public boolean lookingAt() {
        this.reset();
        return this.find();
    }

    @Override
    public ReMatcherAutomaton appendReplacement(final StringBuilder sb, final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public ReMatcherAutomaton appendReplacement(final StringBuffer sb, final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public StringBuilder appendTail(final StringBuilder sb) { throw new UnsupportedOperationException(); }

    @Override
    public StringBuffer appendTail(final StringBuffer sb) { throw new UnsupportedOperationException(); }

    @Override
    public String replaceAll(final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public String replaceAll(final Function<MatchResult, String> replacement) { throw new UnsupportedOperationException(); }

    @Override
    public String replaceFirst(final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public String replaceFirst(final Function<MatchResult, String> replacement) { throw new UnsupportedOperationException(); }

    @Override
    public ReMatcherAutomaton region(final int start, final int end) { throw new UnsupportedOperationException(); }

    @Override
    public int regionStart() { throw new UnsupportedOperationException(); }

    @Override
    public int regionEnd() { throw new UnsupportedOperationException(); }

    @Override
    public boolean hasTransparentBounds() { throw new UnsupportedOperationException(); }

    @Override
    public ReMatcherAutomaton useTransparentBounds(final boolean b) { throw new UnsupportedOperationException(); }

    @Override
    public boolean hasAnchoringBounds() { throw new UnsupportedOperationException(); }

    @Override
    public ReMatcherAutomaton useAnchoringBounds(final boolean b) { throw new UnsupportedOperationException(); }

    @Override
    public boolean hitEnd() { throw new UnsupportedOperationException(); }

    @Override
    public boolean requireEnd() { throw new UnsupportedOperationException(); }

    @Override
    public String text() { return this.input.toString(); }

    @Override
    public int start() { return this.matcher().start(); }

    @Override
    public int start(final int group) { return this.matcher().start(group); }

    @Override
    public int end() { return this.matcher().end(); }

    @Override
    public int end(final int group) { return this.matcher().end(group); }

    @Override
    public String group() { return this.matcher().group(); }

    @Override
    public String group(final int group) { return this.matcher().group(group); }

    @Override
    public int groupCount() { return this.matcher().groupCount(); }
}
