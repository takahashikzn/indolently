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
package jp.root42.indolently;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;


/**
 * Enhanced {@link Pattern} object
 *
 * @author takahashikzn.
 */
public interface SPtrn
    extends Predicate<CharSequence> {

    /**
     * Get {@link Pattern} instance which this object contains.
     *
     * @return {@link Pattern} instance which this object contains
     */
    Pattern ptrn();

    /**
     * An shortcut of {@code ptrn.pattern().pattern()}.
     *
     * @return regex string
     * @see Pattern#pattern()
     */
    default String pattern() {
        return this.ptrn().pattern();
    }

    @Override
    default boolean test(final CharSequence cs) {
        return this.matcher(cs).matches();
    }

    /**
     * Create a {@link SMatcher} instance.
     *
     * @param cs the string to match
     * @return created {@link SMatcher} instance
     */
    default SMatcher matcher(final CharSequence cs) {
        return new SMatcherImpl(this.ptrn().matcher(cs), cs);
    }

    /**
     * Tokenize string by the regex pattern which this object expresses.
     * This method is equivalent to {@code ptrn.split(cs, 0)}.
     *
     * @param cs the string to tokenize
     * @return token list
     */
    default SList<String> split(final CharSequence cs) {
        return this.split(cs, 0);
    }

    /**
     * Tokenize string by the regex pattern which this object expresses.
     *
     * @param cs the string to tokenize
     * @param limit The result threshold
     * @return token list
     * @see Pattern#split(CharSequence, int)
     */
    default SList<String> split(final CharSequence cs, final int limit) {
        return Indolently.list(this.ptrn().split(cs, limit));
    }

    /**
     * delegate for {@link Matcher#replaceAll(String)}
     *
     * @param cs the string to replace
     * @param replacement replacement string
     * @return replaced string
     */
    default String replaceAll(final CharSequence cs, final String replacement) {
        return this.matcher(cs).replaceAll(replacement);
    }

    /**
     * delegate for {@link Matcher#replaceFirst(String)}
     *
     * @param cs the string to replace
     * @param replacement replacement string
     * @return replaced string
     */
    default String replaceFirst(final CharSequence cs, final String replacement) {
        return this.matcher(cs).replaceFirst(replacement);
    }

    /**
     * delegate for {@link SMatcher#replace(Function)}
     *
     * @param cs the string to replace
     * @param f replace operator
     * @return replaced string
     * @see #replaceAll(String)
     */
    default String replace(final CharSequence cs, final Function<String, String> f) {
        return this.matcher(cs).replace(f);
    }

    /**
     * delegate for {@link SMatcher#replace(BiFunction)}
     *
     * @param cs the string to replace
     * @param f replace operator
     * @return replaced string
     * @see #replaceAll(String)
     */
    default String replace(final CharSequence cs, final BiFunction<? super SMatcher, String, String> f) {
        return this.matcher(cs).replace(f);
    }
}
