// Copyright 2019 takahashikzn
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

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.root42.indolently.Indolently;
import jp.root42.indolently.SList;


/**
 * {@link SPtrn} implementation.
 *
 * @author takahashikzn
 */
public final class SPtrnJDK
    implements SPtrnBase<Pattern, SMatcherJDK> {

    private final Pattern pattern;

    public SPtrnJDK(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Pattern ptrn() {
        return this.pattern;
    }

    @Override
    public String pattern() {
        return this.ptrn().pattern();
    }

    @Override
    public SMatcherJDK matcher(final CharSequence cs) {
        return new SMatcherJDK(this.ptrn().matcher(cs), cs);
    }

    @Override
    public SList<String> split(final CharSequence cs, final int limit) {
        return Indolently.list(this.ptrn().split(cs, limit));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof SPtrn)) {
            return false;
        }

        return this.ptrn().equals(((SPtrn) o).ptrn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.pattern);
    }

    @Override
    public String toString() {
        return this.pattern.toString();
    }
}

final class SMatcherJDK
    implements SMatcher<Pattern, Matcher> {

    private final Matcher matcher;

    private final String text;

    public SMatcherJDK(final Matcher matcher, final CharSequence text) {
        this.matcher = matcher;
        this.text = text.toString();
    }

    @Override
    public int hashCode() {
        return this.matcher.hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof SMatcherJDK)) {
            return false;
        }

        return ((SMatcherJDK) o).matcher.equals(this.matcher);
    }

    @Override
    public String toString() { return this.matcher.toString(); }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public Pattern pattern() {
        return this.matcher.pattern();
    }

    @Override
    public MatchResult toMatchResult() {
        return this.matcher.toMatchResult();
    }

    @Override
    public Matcher usePattern(final Pattern newPattern) {
        return this.matcher.usePattern(newPattern);
    }

    @Override
    public Matcher reset() {
        return this.matcher.reset();
    }

    @Override
    public Matcher reset(final CharSequence input) {
        return this.matcher.reset(input);
    }

    @Override
    public int start() {
        return this.matcher.start();
    }

    @Override
    public int start(final int group) {
        return this.matcher.start(group);
    }

    @Override
    public int start(final String name) {
        return this.matcher.start(name);
    }

    @Override
    public int end() {
        return this.matcher.end();
    }

    @Override
    public int end(final int group) {
        return this.matcher.end(group);
    }

    @Override
    public int end(final String name) {
        return this.matcher.end(name);
    }

    @Override
    public String group() {
        return this.matcher.group();
    }

    @Override
    public String group(final int group) {
        return this.matcher.group(group);
    }

    @Override
    public String group(final String name) {
        return this.matcher.group(name);
    }

    @Override
    public int groupCount() {
        return this.matcher.groupCount();
    }

    @Override
    public boolean matches() {
        return this.matcher.matches();
    }

    @Override
    public boolean find() {
        return this.matcher.find();
    }

    @Override
    public boolean find(final int start) {
        return this.matcher.find(start);
    }

    @Override
    public boolean lookingAt() {
        return this.matcher.lookingAt();
    }

    @Override
    public Matcher appendReplacement(final StringBuilder sb, final String replacement) {
        return this.matcher.appendReplacement(sb, replacement);
    }

    @Override
    public Matcher appendReplacement(final StringBuffer sb, final String replacement) {
        return this.matcher.appendReplacement(sb, replacement);
    }

    @Override
    public StringBuilder appendTail(final StringBuilder sb) {
        return this.matcher.appendTail(sb);
    }

    @Override
    public StringBuffer appendTail(final StringBuffer sb) {
        return this.matcher.appendTail(sb);
    }

    @Override
    public String replaceAll(final String replacement) {
        return this.matcher.replaceAll(replacement);
    }

    @Override
    public String replaceFirst(final String replacement) {
        return this.matcher.replaceFirst(replacement);
    }

    @Override
    public Matcher region(final int start, final int end) {
        return this.matcher.region(start, end);
    }

    @Override
    public int regionStart() {
        return this.matcher.regionStart();
    }

    @Override
    public int regionEnd() {
        return this.matcher.regionEnd();
    }

    @Override
    public boolean hasTransparentBounds() {
        return this.matcher.hasTransparentBounds();
    }

    @Override
    public Matcher useTransparentBounds(final boolean b) {
        return this.matcher.useTransparentBounds(b);
    }

    @Override
    public boolean hasAnchoringBounds() {
        return this.matcher.hasAnchoringBounds();
    }

    @Override
    public Matcher useAnchoringBounds(final boolean b) {
        return this.matcher.useAnchoringBounds(b);
    }

    @Override
    public boolean hitEnd() {
        return this.matcher.hitEnd();
    }

    @Override
    public boolean requireEnd() {
        return this.matcher.requireEnd();
    }
}
