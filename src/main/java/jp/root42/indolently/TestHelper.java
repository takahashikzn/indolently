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
package jp.root42.indolently;

import java.util.Optional;

import jp.root42.indolently.ref.$;

import org.assertj.core.api.Assertions;
import org.assertj.core.api.OptionalAssert;


/**
 * @author root42 Inc.
 * @version $
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class TestHelper {

    private TestHelper() { }

    public static <VALUE> OptionalAssert<VALUE> assertThat(final Optional<VALUE> actual) {
        return Assertions.assertThat(actual);
    }

    public static <VALUE> OptionalAssert<VALUE> assertThat(final $<VALUE> actual) { return assertThat$(actual); }

    public static <VALUE> OptionalAssert<VALUE> assertThat$(final $<VALUE> actual) {
        return new OptionalAssert<>(actual.unwrap()) {

            @SuppressWarnings("rawtypes")
            @Override
            public OptionalAssert<VALUE> isEqualTo(final Object expected) {
                if (expected instanceof $) {
                    this.objects.assertEqual(this.info, this.actual, (($) expected).unwrap());
                } else {
                    this.objects.assertEqual(this.info, this.actual, expected);
                }

                return this.myself;
            }

            @SuppressWarnings("rawtypes")
            @Override
            public OptionalAssert<VALUE> isNotEqualTo(final Object other) {
                if (other instanceof $) {
                    this.objects.assertNotEqual(this.info, this.actual, (($) other).unwrap());
                } else {
                    this.objects.assertNotEqual(this.info, this.actual, other);
                }

                return this.myself;
            }
        };
    }
}
