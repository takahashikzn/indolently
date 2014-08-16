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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;


/**
 * Extended {@link Matcher} class for indolent person.
 * It's name comes from "Sugared Matcher".
 *
 * @author takahashikzn
 */
public interface SMatcher
    extends MatcherDelegate {

    /**
     * replace matched character sequence.
     *
     * @param f replace operator
     * @return replaced string
     * @see #replaceAll(String)
     */
    default String replace(final Function<String, String> f) {
        Objects.requireNonNull(f);

        return this.replace((m, s) -> f.apply(s));
    }

    /**
     * replace matched character sequence.
     *
     * @param f replace operator
     * @return replaced string
     * @see #replaceAll(String)
     */
    default String replace(final BiFunction<SMatcher, String, String> f) {
        Objects.requireNonNull(f);

        final StringBuffer sb = new StringBuffer();

        while (this.find()) {
            this.appendReplacement(sb, f.apply(this, this.group()));
        }

        return this.appendTail(sb).toString();
    }
}
