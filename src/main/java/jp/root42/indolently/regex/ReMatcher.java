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
package jp.root42.indolently.regex;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;

import jp.root42.indolently.$iter;
import jp.root42.indolently.$list;
import jp.root42.indolently.Iterative;
import jp.root42.indolently.bridge.RegexMatcher;
import jp.root42.indolently.ref.$void;
import jp.root42.indolently.trait.Loopable;

import static jp.root42.indolently.Expressive.*;
import static jp.root42.indolently.Indolently.*;


/**
 * Extended {@link Matcher} class for indolent person.
 * The name is came from "Sugared Matcher".
 *
 * @author takahashikzn
 */
public interface ReMatcher<P, M>
    extends RegexMatcher<P, M>, Iterable<String>, Loopable<String, ReMatcher<P, M>> {

    @Override
    default $iter<String> iterator() {

        final $void<Boolean> found = ref();

        return Iterative.iterator( //
            () -> opt(found.$) //
                .or(() -> found.$ = this.find()), //
            () -> prog1( //
                () -> this.group(), //
                () -> found.$ = null));
    }

    @Override
    default ReMatcher<P, M> each(final Consumer<? super String> f) { return this.each((m, s) -> f.accept(s)); }

    /**
     * Return original matching target text.
     *
     * @return target text
     */
    String text();

    /**
     * Return matching target text if this matcher matches whole text otherwise return alternative text.
     *
     * @param altText the alternative text
     * @return matching target text or alternative text
     */
    default String orElse(final String altText) { return this.matches() ? this.text() : altText; }

    /**
     * Consume each matched token then return {@code this} instance which {@link Matcher#reset() reset} was called after
     * iteration finished.
     *
     * @param f consumer
     * @return {@code this} instance which is reset
     * @see Matcher#reset()
     */
    default ReMatcher<P, M> each(final BiConsumer<? super ReMatcher<P, M>, String> f) {

        this.iterator().each(x -> f.accept(this, x));

        this.reset();

        return this;
    }

    /**
     * replace matched character sequence.
     *
     * @param f replace operator
     * @return replaced string
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
     */
    default String replace(final BiFunction<? super ReMatcher<?, ?>, String, String> f) {
        Objects.requireNonNull(f);

        final var sb = new StringBuilder();
        while (this.find()) {
            this.appendReplacement(sb, f.apply(this, this.group()));
        }
        return this.appendTail(sb).toString();
    }

    default String subst(final Function<String, String> f) {
        Objects.requireNonNull(f);
        return this.subst((m, s) -> f.apply(s));
    }

    default String subst(final BiFunction<? super ReMatcher<?, ?>, String, String> f) {
        Objects.requireNonNull(f);
        return this.replace((m, s) -> {
            final var repl = f.apply(m, s);
            return contains(repl, '$') ? repl.replaceAll("(?<!\\\\)\\$", "\\\\\\$") : repl;
        });
    }

    default $list<String> groups() {

        final $list<String> found = list();

        for (int i = 0, Z = this.groupCount(); i < Z; i++)
            found.add(this.group(i + 1));

        return found;
    }
}
