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

import java.util.List;

import jp.root42.indolently.bridge.ObjFactory;
import jp.root42.indolently.regex.AdaptiveSPtrn;
import jp.root42.indolently.regex.SPtrn;
import jp.root42.indolently.regex.SPtrnBase;
import jp.root42.indolently.regex.SPtrnJDK;
import jp.root42.indolently.regex.SPtrnRE2;


/**
 * @author takahashikzn
 */
public class Regexive {

    private static final boolean RE2_AVAIL = ObjFactory.isPresent("com.google.re2j.Pattern");

    /** non private for subtyping. */
    protected Regexive() {}

    /**
     * create pattern instance using JDK regex library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static SPtrn regex(final String pattern) {
        if (!RE2_AVAIL) {
            regex1(pattern);
        }

        final List<SPtrnBase<?, ?>> patterns = Indolently.list();

        try {
            patterns.add(regex2(pattern));
        } catch (final RuntimeException e) {
            if (!e.getClass().getName().equals("com.google.re2j.PatternSyntaxException")) {
                throw e;
            }
        }

        patterns.add(regex1(pattern));

        if (patterns.size() == 1) {
            return new SPtrn(patterns.get(0));
        } else {
            return new SPtrn(new AdaptiveSPtrn(patterns));
        }
    }

    /**
     * create pattern instance using JDK regex library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static SPtrnJDK regex1(final String pattern) {
        return regex1(java.util.regex.Pattern.compile(pattern));
    }

    /**
     * create pattern instance using RE2J library.
     *
     * @param pattern pattern string
     * @return enhanced Pattern instance
     */
    public static SPtrnRE2 regex2(final String pattern) {
        return regex2(com.google.re2j.Pattern.compile(pattern));
    }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static SPtrnJDK regex1(final java.util.regex.Pattern pattern) {
        return new SPtrnJDK(pattern);
    }

    /**
     * create pattern instance.
     *
     * @param pattern pattern object
     * @return enhanced Pattern instance
     */
    public static SPtrnRE2 regex2(final com.google.re2j.Pattern pattern) {
        return new SPtrnRE2(pattern);
    }
}
