// Copyright 2018 takahashikzn
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

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;


/**
 * @author takahashikzn
 */
public final class Numeric {

    /** non private for subtyping. */
    protected Numeric() {}

    public static int asInt(final String s) { return Integer.valueOf(s); }

    public static long asLong(final String s) { return Long.valueOf(s); }

    public static short asShort(final String s) { return Short.valueOf(s); }

    public static byte asByte(final String s) { return Byte.valueOf(s); }

    public static float asFloat(final String s) { return Float.valueOf(s); }

    public static double asDouble(final String s) { return Double.valueOf(s); }

    public static boolean asBool(final String s) { return Boolean.valueOf(s); }

    // public static char asChar(final String s) { return s.charAt(0); }

    public static BigDecimal decimal(final String s) { return new BigDecimal(s); }

    public static BigDecimal decimal(final long l) { return BigDecimal.valueOf(l); }

    public static BigDecimal decimal(final double d) { return BigDecimal.valueOf(d); }

    public static Function<String, Integer> asInt() { return Numeric::asInt; }

    public static ToIntFunction<String> toInt() { return Numeric::asInt; }

    public static Function<String, Long> asLong() { return Numeric::asLong; }

    public static ToLongFunction<String> toLong() { return Numeric::asLong; }

    public static Function<String, Short> asShort() { return Numeric::asShort; }

    public static Function<String, Byte> asByte() { return Numeric::asByte; }

    public static Function<String, Float> asFloat() { return Numeric::asFloat; }

    public static Function<String, Double> asDouble() { return Numeric::asDouble; }

    public static ToDoubleFunction<String> toDouble() { return Numeric::asDouble; }

    public static Function<String, Boolean> asBool() { return Numeric::asBool; }

    public static Predicate<String> toBool() { return Numeric::asBool; }

    // public static Function<String, Character> toChar() { return x -> asChar(x); }

    public static Function<String, BigDecimal> decimal() { return x -> decimal(x); }
}
