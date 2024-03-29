// Copyright 2016 takahashikzn
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

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;

import jp.root42.indolently.$list;


/**
 * Enhanced regex pattern object
 *
 * @author takahashikzn.
 */
public final class Regex
    implements RegexBase<Regex.Ptrn, ReMatcher<?, ?>> {

    interface Ptrn {

        String pattern();
    }

    private final RegexBase<?, ?> pattern;

    public Regex(final RegexBase<?, ?> pattern) { this.pattern = pattern; }

    public <T extends RegexBase<?, ?>> T unwrap() {
        //noinspection unchecked
        return (T) this.pattern;
    }

    @Override
    public String toString() { return this.pattern.toString(); }

    @Override
    public int hashCode() { return this.pattern.hashCode(); }

    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof RegexBase<?, ?> that && this.pattern().equals(that.pattern()));
    }

    @Override
    public Ptrn ptrn() { return this::pattern; }

    @Override
    public ReMatcher<?, ?> matcher(final CharSequence cs) { return this.pattern.matcher(cs); }

    @Override
    public boolean test(final CharSequence cs) { return this.pattern.test(cs); }

    @Override
    public String pattern() { return this.pattern.pattern(); }

    @Override
    public $list<String> split(final CharSequence cs, final int limit) { return this.pattern.split(cs, limit); }
}

interface RegexBase<P, M extends ReMatcher<?, ?>>
    extends ReTest, ReFindable {

    /**
     * Get Pattern instance which this object contains.
     *
     * @return Pattern instance which this object contains
     */
    P ptrn();

    /**
     * Create a matcher instance.
     *
     * @param cs the string to match
     * @return created matcher instance
     */
    M matcher(CharSequence cs);

    @Override
    default boolean test(final CharSequence cs) { return this.matcher(cs).matches(); }

    @Override
    default boolean find(final CharSequence cs) { return this.matcher(cs).find(); }

    /**
     * Tokenize string by the regex pattern which this object expresses.
     * This method is equivalent to {@code ptrn.split(cs, 0)}.
     *
     * @param cs the string to tokenize
     * @return token list
     */
    default $list<String> split(final CharSequence cs) { return this.split(cs, 0); }

    /**
     * Tokenize string by the regex pattern which this object expresses.
     *
     * @param cs the string to tokenize
     * @param limit The result threshold
     * @return token list
     * @see Pattern#split(CharSequence, int)
     */
    $list<String> split(CharSequence cs, int limit);

    /**
     * delegate for {@link java.util.regex.Matcher#replaceAll(String)}
     *
     * @param cs the string to replace
     * @param replacement replacement string
     * @return replaced string
     */
    default String replaceAll(final CharSequence cs, final String replacement) {
        return this.matcher(cs).replaceAll(replacement);
    }

    /**
     * delegate for {@link java.util.regex.Matcher#replaceFirst(String)}
     *
     * @param cs the string to replace
     * @param replacement replacement string
     * @return replaced string
     */
    default String replaceFirst(final CharSequence cs, final String replacement) {
        return this.matcher(cs).replaceFirst(replacement);
    }

    /**
     * replace matched character sequence.
     *
     * @param cs the string to replace
     * @param f replace operator
     * @return replaced string
     * @see java.util.regex.Matcher#replaceAll(String)
     */
    default String replace(final CharSequence cs, final Function<String, String> f) {
        return this.matcher(cs).replace(f);
    }

    /**
     * replace matched character sequence.
     *
     * @param cs the string to replace
     * @param f replace operator
     * @return replaced string
     * @see java.util.regex.Matcher#replaceAll(String)
     */
    default String replace(final CharSequence cs, final BiFunction<? super ReMatcher<?, ?>, String, String> f) {
        return this.matcher(cs).replace(f);
    }

    default String subst(final CharSequence cs, final Function<String, String> f) { return this.matcher(cs).subst(f); }

    default String subst(final CharSequence cs, final BiFunction<? super ReMatcher<?, ?>, String, String> f) {
        return this.matcher(cs).subst(f);
    }
}
