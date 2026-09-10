// Copyright 2026 takahashikzn
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

import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.root42.indolently.io.LinuxMemInfo;
import org.junit.Test;

import static jp.root42.indolently.Indolently.*;
import static org.assertj.core.api.Assertions.*;


public class RegexiveTest {

    @Test
    public void jdkEntryPointsWorkWithoutOptionalLibraries() throws Exception {
        assertThat(withoutOptionalLibraries("match"))
            .containsExactly("value", "value", "value", "true", "loaded");
    }

    @Test
    public void jdkSyntaxErrorsWorkWithoutOptionalLibraries() throws Exception {
        assertThat(withoutOptionalLibraries("syntaxErrors"))
            .containsExactly(PatternSyntaxException.class.getName(), PatternSyntaxException.class.getName());
    }

    @Test
    public void re2StillMatchesWithItsLibraryPresent() {
        assertThat(re2("(foo)[0-9]+").group1("foo42")).isEqualTo(just("foo"));
        assertThat(Regexive.regex2(com.google.re2j.Pattern.compile("foo[0-9]+")).test("foo42")).isTrue();
    }

    @Test
    public void re2SyntaxErrorsPreserveJdkExceptionAndDetails() {
        // Lookbehind is valid in JDK regex but unsupported by RE2.
        final var pattern = "(?<=prefix:)value";
        assertThat(Pattern.compile(pattern).matcher("prefix:value").find()).isTrue();
        final var expected = catchThrowableOfType(com.google.re2j.PatternSyntaxException.class,
            () -> com.google.re2j.Pattern.compile(pattern));
        final var actual = catchThrowableOfType(PatternSyntaxException.class, () -> re2(pattern));

        assertThat(expected).isNotNull();
        assertThat(actual).isNotNull();
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getPattern()).isEqualTo(expected.getPattern());
        assertThat(actual.getIndex()).isEqualTo(expected.getIndex());
    }

    private static String[] withoutOptionalLibraries(final String method) throws Exception {
        final var mainClasses = Indolently.class.getProtectionDomain().getCodeSource().getLocation();
        final var testClasses = RegexiveTest.class.getProtectionDomain().getCodeSource().getLocation();

        // Do not inherit the test runner's classpath, which normally includes RE2 and automaton.
        try (var loader = new URLClassLoader(new URL[] { mainClasses, testClasses }, ClassLoader.getPlatformClassLoader())) {
            assertThatThrownBy(() -> loader.loadClass("com.google.re2j.Pattern")).isInstanceOf(ClassNotFoundException.class);
            assertThatThrownBy(() -> loader.loadClass("dk.brics.automaton.RegExp")).isInstanceOf(ClassNotFoundException.class);
            final var probe = Class.forName(JdkOnlyProbe.class.getName(), true, loader);
            return (String[]) probe.getMethod(method).invoke(null);
        }
    }

    /** Invoked in isolation; this class must not depend on test or optional libraries. */
    public static final class JdkOnlyProbe {

        public static String[] match() {
            final var pattern = "(?<=prefix:)value";
            final var text = "prefix:value";
            final var results = new String[] {
                re(pattern).group(text, 0).get(),
                re1(pattern).group(text, 0).get(),
                re(Pattern.compile(pattern)).group(text, 0).get(),
                Boolean.toString(retest("value").test("value")),
                "loaded"
            };
            LinuxMemInfo.available(); // Initialization also compiles the /proc/meminfo pattern with re().
            return results;
        }

        public static String[] syntaxErrors() {
            final var results = new String[2];
            try { re("("); } catch (PatternSyntaxException e) { results[0] = e.getClass().getName(); }
            try { re1("("); } catch (PatternSyntaxException e) { results[1] = e.getClass().getName(); }
            return results;
        }
    }
}
