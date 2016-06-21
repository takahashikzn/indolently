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
package jp.root42.indolently;

import java.util.Objects;
import java.util.regex.Pattern;


/**
 * {@link SPtrn} implementation.
 *
 * @author takahashikzn
 */
final class SPtrnImpl
    implements SPtrn {

    private final Pattern pattern;

    SPtrnImpl(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public Pattern ptrn() {
        return this.pattern;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof SPtrn)) {
            return false;
        }

        return this.ptrn().equals(((SPtrn) o).ptrn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.pattern);
    }

    @Override
    public String toString() {
        return this.pattern.toString();
    }
}
