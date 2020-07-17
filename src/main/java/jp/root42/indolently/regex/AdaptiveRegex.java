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

import java.util.Collections;
import java.util.List;

import jp.root42.indolently.$list;
import jp.root42.indolently.Indolently;


/**
 * @author takahashikzn.
 */
public final class AdaptiveRegex
    implements RegexBase<Regex.Ptrn, ReMatcher<?, ?>> {

    private final List<? extends RegexBase<?, ?>> patterns;

    private final List<Long> times;

    private final int trial;

    private int current;

    private RegexBase<?, ?> fastest;

    public AdaptiveRegex(final List<? extends RegexBase<?, ?>> patterns) {
        this(patterns, 100);
    }

    public AdaptiveRegex(final List<? extends RegexBase<?, ?>> patterns, final int trial) {
        this.patterns = patterns;
        this.times = Indolently.list(patterns).map(x -> 0L);
        this.trial = trial;
    }

    @Override
    public String toString() {
        return this.patterns.get(0).pattern();
    }

    private RegexBase<?, ?> select() {
        this.determineFastest();

        return (this.fastest == null) ? this.patterns.get(this.current % this.patterns.size()) : this.fastest;
    }

    private void determineFastest() {
        if ((this.fastest == null) && (this.trial < this.current)) {
            this.fastest = this.patterns.get(this.times.indexOf(Collections.min(this.times)));
        }
    }

    @Override
    public boolean test(final CharSequence cs) {
        this.determineFastest();

        if (this.fastest != null) {
            return this.fastest.test(cs);
        }

        final var now = System.nanoTime();
        final int pos = this.current % this.patterns.size();
        final var result = this.patterns.get(pos).test(cs);
        final var time = System.nanoTime() - now;

        this.times.set(pos, this.times.get(pos) + time);

        this.current++;

        return result;
    }

    @Override
    public Regex.Ptrn ptrn() { return this::pattern; }

    @Override
    public ReMatcher<?, ?> matcher(final CharSequence cs) {
        return this.select().matcher(cs);
    }

    @Override
    public String pattern() { return this.select().pattern(); }

    @Override
    public $list<String> split(final CharSequence cs, final int limit) {
        return this.select().split(cs, limit);
    }
}
