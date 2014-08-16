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

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implementation of {@MatcherDelegate}.
 *
 * @author takahashikzn
 */
class MatcherDelegateImpl
    implements MatcherDelegate {

    private final Matcher matcher;

    /**
     * constructor.
     *
     * @param matcher matcher
     */
    public MatcherDelegateImpl(final Matcher matcher) {
        Objects.requireNonNull(matcher, "matcher");

        this.matcher = matcher;
    }

    @Override
    public int hashCode() {
        return this.matcher.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof MatcherDelegateImpl)) {
            return false;
        }

        return ((MatcherDelegateImpl) obj).matcher.equals(this.matcher);
    }

    @Override
    public String toString() {
        return this.matcher.toString();
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
    public Matcher appendReplacement(final StringBuffer sb, final String replacement) {
        return this.matcher.appendReplacement(sb, replacement);
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
