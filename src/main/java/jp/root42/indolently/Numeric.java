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

import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;
import static jp.root42.indolently.Iterative.*;


/**
 * @author takahashikzn
 */
public final class Numeric {

    /** non private for subtyping. */
    protected Numeric() {}

    private static final $map<String, Integer> intCache = range(0, 256).list().mapmap(x -> "" + x, it()).freeze();

    public static int asInt(final String s) {
        switch (s) {
            case "0": { return 0; }
            case "1": { return 1; }
            case "2": { return 2; }
            case "3": { return 3; }
            case "4": { return 4; }
            case "5": { return 5; }
            case "6": { return 6; }
            case "7": { return 7; }
            case "8": { return 8; }
            case "9": { return 9; }
            case "10": { return 10; }
            case "11": { return 11; }
            case "12": { return 12; }
            case "13": { return 13; }
            case "14": { return 14; }
            case "15": { return 15; }
            case "16": { return 16; }
            case "17": { return 17; }
            case "18": { return 18; }
            case "19": { return 19; }
            case "20": { return 20; }
            case "21": { return 21; }
            case "22": { return 22; }
            case "23": { return 23; }
            case "24": { return 24; }
            case "25": { return 25; }
            case "26": { return 26; }
            case "27": { return 27; }
            case "28": { return 28; }
            case "29": { return 29; }
            case "30": { return 30; }
            case "31": { return 31; }
            default:
                final var i = intCache.get(s);
                return (i != null) ? i : Integer.parseInt(s);
        }
    }

    public static long asLong(final String s) { return Long.parseLong(s); }

    public static short asShort(final String s) { return Short.parseShort(s); }

    public static byte asByte(final String s) { return Byte.parseByte(s); }

    public static float asFloat(final String s) { return Float.parseFloat(s); }

    public static double asDouble(final String s) { return Double.parseDouble(s); }

    public static boolean asBool(final String s) { return Boolean.parseBoolean(s); }

    private static <T> $<T> parseX(final String s, final Function<String, T> f) {
        try { return empty(s) ? $.none() : $.of(f.apply(s)); } //
        catch (final IllegalArgumentException e) { return $.none(); }
    }

    public static $<Integer> parseInt(final String s) {
        final var i = intCache.get(s);
        return (i != null) ? $.of(i) : parseX(s, Integer::parseInt);
    }

    public static $<Long> parseLong(final String s) { return parseX(s, Long::parseLong); }

    public static $<Short> parseShort(final String s) { return parseX(s, Short::parseShort); }

    public static $<Byte> parseByte(final String s) { return parseX(s, Byte::parseByte); }

    public static $<Float> parseFloat(final String s) { return parseX(s, Float::parseFloat); }

    public static $<Double> parseDouble(final String s) { return parseX(s, Double::parseDouble); }

    public static $<Boolean> parseBool(final String s) { return parseX(s, Boolean::parseBoolean); }

    public static $<BigDecimal> parseDecimal(final String s) { return parseX(s, BigDecimal::new); }

    public static Function<String, $<Integer>> parseInt() { return Numeric::parseInt; }

    public static Function<String, $<Long>> parseLong() { return Numeric::parseLong; }

    public static Function<String, $<Short>> parseShort() { return Numeric::parseShort; }

    public static Function<String, $<Byte>> parseByte() { return Numeric::parseByte; }

    public static Function<String, $<Float>> parseFloat() { return Numeric::parseFloat; }

    public static Function<String, $<Double>> parseDouble() { return Numeric::parseDouble; }

    public static Function<String, $<Boolean>> parseBool() { return Numeric::parseBool; }

    public static Function<String, $<BigDecimal>> parseDecimal() { return Numeric::parseDecimal; }

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

    public static Function<String, BigDecimal> asDecimal() { return Numeric::decimal; }

    public static Function<Number, Integer> toInt() { return Number::intValue; }

    public static Function<Number, Long> toLong() { return Number::longValue; }

    public static Function<Number, Short> toShort() { return Number::shortValue; }

    public static Function<Number, Byte> toByte() { return Number::byteValue; }

    public static Function<Number, Float> toFloat() { return Number::floatValue; }

    public static Function<Number, Double> toDouble() { return Number::doubleValue; }

    public static Function<Number, BigDecimal> toDecimal() {
        return x -> (x instanceof BigDecimal)
            ? cast(x)
            : (x instanceof Float || x instanceof Double) ? decimal(x.doubleValue()) : decimal(x.longValue());
    }
}
