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

import java.util.function.Predicate;
import java.util.stream.Stream;

import jp.root42.indolently.bridge.StreamDelegate;


/**
 * Extended {@link Stream} class for indolent person.
 * It's name comes from "Sugared iterator".
 *
 * @param <T> value type
 * @author takahashikzn
 */
final class $stream_impl<T>
    extends StreamDelegate<T>
    implements $stream<T> {

    public $stream_impl(final Stream<T> store) { super(store); }

    @Override
    public $stream<T> filter(final Predicate<? super T> f) { return this.take(f); }

    @Override
    public $stream<T> take(final Predicate<? super T> f) { return new $stream_impl<>(super.filter(f)); }
}
