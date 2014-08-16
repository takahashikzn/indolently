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

import java.util.regex.Pattern;


/**
 * @author takahashikzn
 */
public class Regexive {

    /** non private for subtyping. */
    protected Regexive() {}

    /**
     * create matcher instance.
     *
     * @param pattern pattern string
     * @param target target string
     * @return enhanced Matcher instance
     */
    public static SMatcher regex(final String pattern, final String target) {
        return regex(Pattern.compile(pattern), target);
    }

    /**
     * create matcher instance.
     *
     * @param pattern pattern object
     * @param target target string
     * @return enhanced Matcher instance
     */
    public static SMatcher regex(final Pattern pattern, final String target) {
        return new SMatcherImpl(pattern.matcher(target));
    }
}
