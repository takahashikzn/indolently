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
package jp.root42.indolently.bridge;

import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Implementation of {@link RegexMatcher}.
 *
 * @author takahashikzn
 */
public class RegexMatcherDelegate
    extends ObjDelegate<Matcher>
    implements RegexMatcher {

    private final Matcher matcher;

    /**
     * constructor.
     *
     * @param matcher matcher
     */
    public RegexMatcherDelegate(final Matcher matcher) {
        Objects.requireNonNull(matcher, "matcher");

        this.matcher = matcher;
    }

    @Override
    protected Matcher getDelegate() {
        return this.matcher;
    }

    @Override
    public int hashCode() {
        return this.getDelegate().hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof RegexMatcherDelegate)) {
            return false;
        }

        return ((RegexMatcherDelegate) o).matcher.equals(this.getDelegate());
    }

    @Override
    public Pattern pattern() {
        return this.getDelegate().pattern();
    }

    @Override
    public MatchResult toMatchResult() {
        return this.getDelegate().toMatchResult();
    }

    @Override
    public Matcher usePattern(final Pattern newPattern) {
        return this.getDelegate().usePattern(newPattern);
    }

    @Override
    public Matcher reset() {
        return this.getDelegate().reset();
    }

    @Override
    public Matcher reset(final CharSequence input) {
        return this.getDelegate().reset(input);
    }

    @Override
    public int start() {
        return this.getDelegate().start();
    }

    @Override
    public int start(final int group) {
        return this.getDelegate().start(group);
    }

    @Override
    public int start(final String name) {
        return this.getDelegate().start(name);
    }

    @Override
    public int end() {
        return this.getDelegate().end();
    }

    @Override
    public int end(final int group) {
        return this.getDelegate().end(group);
    }

    @Override
    public int end(final String name) {
        return this.getDelegate().end(name);
    }

    @Override
    public String group() {
        return this.getDelegate().group();
    }

    @Override
    public String group(final int group) {
        return this.getDelegate().group(group);
    }

    @Override
    public String group(final String name) {
        return this.getDelegate().group(name);
    }

    @Override
    public int groupCount() {
        return this.getDelegate().groupCount();
    }

    @Override
    public boolean matches() {
        return this.getDelegate().matches();
    }

    @Override
    public boolean find() {
        return this.getDelegate().find();
    }

    @Override
    public boolean find(final int start) {
        return this.getDelegate().find(start);
    }

    @Override
    public boolean lookingAt() {
        return this.getDelegate().lookingAt();
    }

    @Override
    public Matcher appendReplacement(final StringBuffer sb, final String replacement) {
        return this.getDelegate().appendReplacement(sb, replacement);
    }

    @Override
    public StringBuffer appendTail(final StringBuffer sb) {
        return this.getDelegate().appendTail(sb);
    }

    @Override
    public String replaceAll(final String replacement) {
        return this.getDelegate().replaceAll(replacement);
    }

    @Override
    public String replaceFirst(final String replacement) {
        return this.getDelegate().replaceFirst(replacement);
    }

    @Override
    public Matcher region(final int start, final int end) {
        return this.getDelegate().region(start, end);
    }

    @Override
    public int regionStart() {
        return this.getDelegate().regionStart();
    }

    @Override
    public int regionEnd() {
        return this.getDelegate().regionEnd();
    }

    @Override
    public boolean hasTransparentBounds() {
        return this.getDelegate().hasTransparentBounds();
    }

    @Override
    public Matcher useTransparentBounds(final boolean b) {
        return this.getDelegate().useTransparentBounds(b);
    }

    @Override
    public boolean hasAnchoringBounds() {
        return this.getDelegate().hasAnchoringBounds();
    }

    @Override
    public Matcher useAnchoringBounds(final boolean b) {
        return this.getDelegate().useAnchoringBounds(b);
    }

    @Override
    public boolean hitEnd() {
        return this.getDelegate().hitEnd();
    }

    @Override
    public boolean requireEnd() {
        return this.getDelegate().requireEnd();
    }
}
