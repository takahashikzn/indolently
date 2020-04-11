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
import java.util.Optional;
import java.util.function.Function;


/**
 * @author takahashikzn
 */
public final class Numeric {

    /** non private for subtyping. */
    protected Numeric() {}

    @SuppressWarnings("UnnecessaryBoxing")
    public static int asInt(final String s) { return Integer.valueOf(s); }

    @SuppressWarnings("UnnecessaryBoxing")
    public static long asLong(final String s) { return Long.valueOf(s); }

    @SuppressWarnings("UnnecessaryBoxing")
    public static short asShort(final String s) { return Short.valueOf(s); }

    @SuppressWarnings("UnnecessaryBoxing")
    public static byte asByte(final String s) { return Byte.valueOf(s); }

    @SuppressWarnings("UnnecessaryBoxing")
    public static float asFloat(final String s) { return Float.valueOf(s); }

    @SuppressWarnings("UnnecessaryBoxing")
    public static double asDouble(final String s) { return Double.valueOf(s); }

    @SuppressWarnings("UnnecessaryBoxing")
    public static boolean asBool(final String s) { return Boolean.valueOf(s); }

    private static <T> Optional<T> parseX(final String s, final Function<String, T> f) {
        try {
            return (s == null) ? Optional.empty() : Optional.of(f.apply(s));
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Optional<Integer> parseInt(final String s) { return parseX(s, Integer::parseInt); }

    public static Optional<Long> parseLong(final String s) { return parseX(s, Long::parseLong); }

    public static Optional<Short> parseShort(final String s) { return parseX(s, Short::parseShort); }

    public static Optional<Byte> parseByte(final String s) { return parseX(s, Byte::parseByte); }

    public static Optional<Float> parseFloat(final String s) { return parseX(s, Float::parseFloat); }

    public static Optional<Double> parseDouble(final String s) { return parseX(s, Double::parseDouble); }

    public static Optional<Boolean> parseBool(final String s) { return parseX(s, Boolean::parseBoolean); }

    // public static char asChar(final String s) { return s.charAt(0); }

    public static BigDecimal decimal(final String s) { return new BigDecimal(s); }

    public static BigDecimal decimal(final long l) { return BigDecimal.valueOf(l); }

    public static BigDecimal decimal(final double d) { return BigDecimal.valueOf(d); }

    public static Function<String, Integer> asInt() { return Numeric::asInt; }

    public static Function<String, Long> asLong() { return Numeric::asLong; }

    public static Function<String, Short> asShort() { return Numeric::asShort; }

    public static Function<String, Byte> asByte() { return Numeric::asByte; }

    public static Function<String, Float> asFloat() { return Numeric::asFloat; }

    public static Function<String, Double> asDouble() { return Numeric::asDouble; }

    public static Function<String, Boolean> asBool() { return Numeric::asBool; }

    // public static Function<String, Character> toChar() { return x -> asChar(x); }

    public static Function<String, BigDecimal> decimal() { return x -> decimal(x); }

    public static Function<Number, Integer> toInt() { return Number::intValue; }

    public static Function<Number, Long> toLong() { return Number::longValue; }

    public static Function<Number, Short> toShort() { return Number::shortValue; }

    public static Function<Number, Byte> toByte() { return Number::byteValue; }

    public static Function<Number, Float> toFloat() { return Number::floatValue; }

    public static Function<Number, Double> toDouble() { return Number::doubleValue; }
}
