// Copyright 2020 takahashikzn
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

import dk.brics.automaton.RegExp;
import dk.brics.automaton.RunAutomaton;


/**
 * @author takahashikzn
 */
public class AutomatonTest
    implements ReTest {

    private final RunAutomaton automaton;

    private final String pattern;

    public AutomatonTest(final RegExp re, final String pattern) { this(new RunAutomaton(re.toAutomaton()), pattern); }

    public AutomatonTest(final RunAutomaton automaton, final String pattern) {
        this.automaton = automaton;
        this.pattern = pattern;
    }

    @Override
    public String pattern() { return this.pattern; }

    @Override
    public boolean test(final CharSequence cs) { return this.automaton.run(cs.toString()); }

    @Override
    public String toString() { return this.pattern(); }
}
