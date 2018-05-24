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


/**
 * @author takahashikzn
 */
public final class Numeric {

    /** non private for subtyping. */
    protected Numeric() {}

    public static int intOf(final String s) { return Integer.valueOf(s); }

    public static long longOf(final String s) { return Long.valueOf(s); }

    public static short shortOf(final String s) { return Short.valueOf(s); }

    public static byte byteOf(final String s) { return Byte.valueOf(s); }

    public static float floatOf(final String s) { return Float.valueOf(s); }

    public static double doubleOf(final String s) { return Double.valueOf(s); }

    public static boolean boolOf(final String s) { return Boolean.valueOf(s); }

    public static char charOf(final String s) { return s.charAt(0); }

    public static BigDecimal decimal(final String s) { return new BigDecimal(s); }

    public static BigDecimal decimal(final long l) { return BigDecimal.valueOf(l); }

    public static BigDecimal decimal(final double d) { return BigDecimal.valueOf(d); }

    public static Function<String, Integer> toInt() { return Integer::valueOf; }

    public static Function<String, Long> toLong() { return Long::valueOf; }

    public static Function<String, Short> toShort() { return Short::valueOf; }

    public static Function<String, Byte> toByte() { return Byte::valueOf; }

    public static Function<String, Float> toFloat() { return Float::valueOf; }

    public static Function<String, Double> toDouble() { return Double::valueOf; }

    public static Function<String, Boolean> toBool() { return Boolean::valueOf; }

    public static Function<String, Character> toChar() { return x -> charOf(x); }

    public static Function<String, BigDecimal> toDecimal() { return x -> decimal(x); }
}
