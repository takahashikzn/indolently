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

import java.util.regex.MatchResult;

import dk.brics.automaton.AutomatonMatcher;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;


/**
 * @author takahashikzn
 */
public class AutomatonTest
    implements ReTest, ReFindable {

    private final RegExp re;

    final RunAutomaton automaton;

    private final String pattern;

    public AutomatonTest(final RegExp re, final String pattern) {
        this(re, new RunAutomaton(re.toAutomaton()), pattern);
    }

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

    public ReMatcherAutomaton matcher(final CharSequence cs) { return new ReMatcherAutomaton(this, cs); }

    @Override
    public String toString() { return this.pattern(); }
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
        if (this.matcher == null)
            this.matcher = this.pattern.automaton.newMatcher(this.input, start, this.input.length());
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
    public ReMatcherAutomaton appendReplacement(final StringBuilder sb,
        final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public ReMatcherAutomaton appendReplacement(final StringBuffer sb,
        final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public StringBuilder appendTail(final StringBuilder sb) { throw new UnsupportedOperationException(); }

    @Override
    public StringBuffer appendTail(final StringBuffer sb) { throw new UnsupportedOperationException(); }

    @Override
    public String replaceAll(final String replacement) { throw new UnsupportedOperationException(); }

    @Override
    public String replaceFirst(final String replacement) { throw new UnsupportedOperationException(); }

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
