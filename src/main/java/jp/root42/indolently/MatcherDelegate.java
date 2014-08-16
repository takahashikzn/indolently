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

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * delegate interface of {@link Matcher}
 * 
 * @author takahashikzn
 */
interface MatcherDelegate
    extends MatchResult {

    /**
     * delegate for {@link Matcher#pattern()}
     *
     * @return -
     */
    Pattern pattern();

    /**
     * delegate for {@link Matcher#toMatchResult()}
     *
     * @return -
     */
    MatchResult toMatchResult();

    /**
     * delegate for {@link Matcher#usePattern(Pattern)}
     *
     * @param newPattern -
     * @return -
     */
    Matcher usePattern(final Pattern newPattern);

    /**
     * delegate for {@link Matcher#reset()}
     *
     * @return -
     */
    Matcher reset();

    /**
     * delegate for {@link Matcher#reset(CharSequence)}
     *
     * @param input -
     * @return -
     */
    Matcher reset(final CharSequence input);

    /**
     * delegate for {@link Matcher#start(String)}
     *
     * @param name -
     * @return -
     */
    int start(final String name);

    /**
     * delegate for {@link Matcher#end(String)}
     *
     * @param name -
     * @return -
     */
    int end(final String name);

    /**
     * delegate for {@link Matcher#group(String)}
     *
     * @param name -
     * @return -
     */
    String group(final String name);

    /**
     * delegate for {@link Matcher#matches()}
     *
     * @return -
     */
    boolean matches();

    /**
     * delegate for {@link Matcher#find()}
     *
     * @return -
     */
    boolean find();

    /**
     * delegate for {@link Matcher#find(int)}
     *
     * @param start -
     * @return -
     */
    boolean find(final int start);

    /**
     * delegate for {@link Matcher#lookingAt()}
     *
     * @return -
     */
    boolean lookingAt();

    /**
     * delegate for {@link Matcher#appendReplacement(StringBuffer, String)}
     *
     * @param sb -
     * @param replacement -
     * @return -
     */
    Matcher appendReplacement(final StringBuffer sb, final String replacement);

    /**
     * delegate for {@link Matcher#appendTail(StringBuffer)}
     *
     * @param sb -
     * @return -
     */
    StringBuffer appendTail(final StringBuffer sb);

    /**
     * delegate for {@link Matcher#replaceAll(String)}
     *
     * @param replacement -
     * @return -
     */
    String replaceAll(final String replacement);

    /**
     * delegate for {@link Matcher#replaceFirst(String)}
     *
     * @param replacement -
     * @return -
     */
    String replaceFirst(final String replacement);

    /**
     * delegate for {@link Matcher#region(int, int)}
     *
     * @param start -
     * @param end -
     * @return -
     */
    Matcher region(final int start, final int end);

    /**
     * delegate for {@link Matcher#regionStart()}
     *
     * @return -
     */
    int regionStart();

    /**
     * delegate for {@link Matcher#regionEnd()}
     *
     * @return -
     */
    int regionEnd();

    /**
     * delegate for {@link Matcher#hasTransparentBounds()}
     *
     * @return -
     */
    boolean hasTransparentBounds();

    /**
     * delegate for {@link Matcher#useTransparentBounds(boolean)}
     *
     * @param b -
     * @return -
     */
    Matcher useTransparentBounds(final boolean b);

    /**
     * delegate for {@link Matcher#hasAnchoringBounds()}
     *
     * @return -
     */
    boolean hasAnchoringBounds();

    /**
     * delegate for {@link Matcher#useAnchoringBounds(boolean)}
     *
     * @param b -
     * @return -
     */
    Matcher useAnchoringBounds(final boolean b);

    /**
     * delegate for {@link Matcher#hitEnd()}
     *
     * @return -
     */
    boolean hitEnd();

    /**
     * delegate for {@link Matcher#requireEnd()}
     *
     * @return -
     */
    boolean requireEnd();
}