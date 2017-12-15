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

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * delegate interface of {@link Matcher}
 *
 * @author takahashikzn
 */
public interface RegexMatcher
    extends MatchResult {

    /**
     * delegate for {@link Matcher#pattern()}
     */
    Pattern pattern();

    /**
     * delegate for {@link Matcher#toMatchResult()}
     */
    MatchResult toMatchResult();

    /**
     * delegate for {@link Matcher#usePattern(Pattern)}
     */
    Matcher usePattern(Pattern newPattern);

    /**
     * delegate for {@link Matcher#reset()}
     */
    Matcher reset();

    /**
     * delegate for {@link Matcher#reset(CharSequence)}
     */
    Matcher reset(CharSequence input);

    /**
     * delegate for {@link Matcher#start(String)}
     */
    int start(String name);

    /**
     * delegate for {@link Matcher#end(String)}
     */
    int end(String name);

    /**
     * delegate for {@link Matcher#group(String)}
     */
    String group(String name);

    /**
     * delegate for {@link Matcher#matches()}
     */
    boolean matches();

    /**
     * delegate for {@link Matcher#find()}
     */
    boolean find();

    /**
     * delegate for {@link Matcher#find(int)}
     */
    boolean find(int start);

    /**
     * delegate for {@link Matcher#lookingAt()}
     */
    boolean lookingAt();

    /**
     * delegate for {@link Matcher#appendReplacement(StringBuilder, String)}
     */
    Matcher appendReplacement(StringBuilder sb, String replacement);

    /**
     * delegate for {@link Matcher#appendReplacement(StringBuffer, String)}
     */
    Matcher appendReplacement(StringBuffer sb, String replacement);

    /**
     * delegate for {@link Matcher#appendTail(StringBuilder)}
     */
    StringBuilder appendTail(StringBuilder sb);

    /**
     * delegate for {@link Matcher#appendTail(StringBuffer)}
     */
    StringBuffer appendTail(StringBuffer sb);

    /**
     * delegate for {@link Matcher#replaceAll(String)}
     */
    String replaceAll(String replacement);

    /**
     * delegate for {@link Matcher#replaceFirst(String)}
     */
    String replaceFirst(String replacement);

    /**
     * delegate for {@link Matcher#region(int, int)}
     */
    Matcher region(int start, int end);

    /**
     * delegate for {@link Matcher#regionStart()}
     */
    int regionStart();

    /**
     * delegate for {@link Matcher#regionEnd()}
     */
    int regionEnd();

    /**
     * delegate for {@link Matcher#hasTransparentBounds()}
     */
    boolean hasTransparentBounds();

    /**
     * delegate for {@link Matcher#useTransparentBounds(boolean)}
     */
    Matcher useTransparentBounds(boolean b);

    /**
     * delegate for {@link Matcher#hasAnchoringBounds()}
     */
    boolean hasAnchoringBounds();

    /**
     * delegate for {@link Matcher#useAnchoringBounds(boolean)}
     */
    Matcher useAnchoringBounds(boolean b);

    /**
     * delegate for {@link Matcher#hitEnd()}
     */
    boolean hitEnd();

    /**
     * delegate for {@link Matcher#requireEnd()}
     */
    boolean requireEnd();
}
