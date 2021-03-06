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

import java.util.function.Predicate;


/**
 * @author takahashikzn
 */
public interface ReTest
    extends Predicate<CharSequence> {

    /**
     * get pattern string.
     *
     * @return regex string
     */
    String pattern();

    static ReTest of(final RegexBase<?, ?> ptrn) { return of(ptrn, ptrn.pattern()); }

    static ReTest of(final Predicate<CharSequence> pred, final String pattern) {
        return new ReTest() {

            @Override
            public String pattern() { return pattern; }

            @Override
            public boolean test(final CharSequence s) { return pred.test(s); }

            @Override
            public String toString() { return pattern; }
        };
    }
}
