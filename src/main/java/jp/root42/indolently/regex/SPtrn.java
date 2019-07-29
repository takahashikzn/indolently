// Copyright 2016 takahashikzn
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

import jp.root42.indolently.SList;


/**
 * Enhanced regex pattern object
 *
 * @author takahashikzn.
 */
public final class SPtrn
    implements SPtrnBase<SPtrn.Ptrn, SMatcher<?, ?>> {

    public interface Ptrn {

        String pattern();
    }

    private final SPtrnBase<?, ?> pattern;

    public SPtrn(final SPtrnBase<?, ?> pattern) {
        this.pattern = pattern;
    }

    public <T extends SPtrnBase<?, ?>> T unwrap() {
        //noinspection unchecked
        return (T) this.pattern;
    }

    @Override
    public Ptrn ptrn() { return this::pattern; }

    @Override
    public SMatcher<?, ?> matcher(final CharSequence cs) { return this.pattern.matcher(cs); }

    @Override
    public boolean test(final CharSequence cs) {
        return this.pattern.test(cs);
    }

    @Override
    public String pattern() { return this.pattern.pattern(); }

    @Override
    public SList<String> split(final CharSequence cs, final int limit) { return this.pattern.split(cs, limit); }
}
