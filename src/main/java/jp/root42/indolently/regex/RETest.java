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
import java.util.function.Supplier;


/**
 * @author takahashikzn
 */
public interface RETest
    extends Predicate<CharSequence> {

    static RETest of(final SPtrnBase<?, ?> ptrn) { return of(ptrn, ptrn.pattern()); }

    static RETest of(final Predicate<CharSequence> pred, final String pattern) {
        return new RETest() {

            @Override
            public boolean test(final CharSequence s) { return pred.test(s); }

            @Override
            public String toString() { return pattern; }
        };
    }

    static RETest ofShared(final Supplier<Predicate<CharSequence>> pred, final String pattern) {

        final var local = ThreadLocal.withInitial(pred);

        return new RETest() {

            @Override
            public boolean test(final CharSequence s) { return pred.get().test(s); }

            @Override
            public String toString() { return pattern; }
        };
    }
}
