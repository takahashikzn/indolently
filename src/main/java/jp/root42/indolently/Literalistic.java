// Copyright 2024 takahashikzn
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

import java.util.function.IntPredicate;

import jp.root42.indolently.ref.$;

import static jp.root42.indolently.Indolently.*;


/**
 * @author takahashikzn
 */
public final class Literalistic {

    private Literalistic() { }

    public static int char2int(final char c) {

        return switch (c) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;

            default -> throw new NumberFormatException("" + c);
        };
    }

    public static byte hex2byte(final char c) {

        return i2b(switch (c) {
            case '0' -> 0;
            case '1' -> 1;
            case '2' -> 2;
            case '3' -> 3;
            case '4' -> 4;
            case '5' -> 5;
            case '6' -> 6;
            case '7' -> 7;
            case '8' -> 8;
            case '9' -> 9;
            case 'a', 'A' -> 10;
            case 'b', 'B' -> 11;
            case 'c', 'C' -> 12;
            case 'd', 'D' -> 13;
            case 'e', 'E' -> 14;
            case 'f', 'F' -> 15;

            default -> throw new NumberFormatException("" + c);
        });
    }

    public static boolean isHex(final char c) {
        return switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F' -> true;
            default -> false;
        };
    }

    public static boolean isNum(final char c) {
        return switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> true;
            default -> false;
        };
    }

    public static char toZenkakuUpper(final char c) {

        return switch (c) {
            case 'A', 'ａ' -> 'Ａ';
            case 'B', 'ｂ' -> 'Ｂ';
            case 'C', 'ｃ' -> 'Ｃ';
            case 'D', 'ｄ' -> 'Ｄ';
            case 'E', 'ｅ' -> 'Ｅ';
            case 'F', 'ｆ' -> 'Ｆ';
            case 'G', 'ｇ' -> 'Ｇ';
            case 'H', 'ｈ' -> 'Ｈ';
            case 'I', 'ｉ' -> 'Ｉ';
            case 'J', 'ｊ' -> 'Ｊ';
            case 'K', 'ｋ' -> 'Ｋ';
            case 'L', 'ｌ' -> 'Ｌ';
            case 'M', 'ｍ' -> 'Ｍ';
            case 'N', 'ｎ' -> 'Ｎ';
            case 'O', 'ｏ' -> 'Ｏ';
            case 'P', 'ｐ' -> 'Ｐ';
            case 'Q', 'ｑ' -> 'Ｑ';
            case 'R', 'ｒ' -> 'Ｒ';
            case 'S', 'ｓ' -> 'Ｓ';
            case 'T', 'ｔ' -> 'Ｔ';
            case 'U', 'ｕ' -> 'Ｕ';
            case 'V', 'ｖ' -> 'Ｖ';
            case 'W', 'ｗ' -> 'Ｗ';
            case 'X', 'ｘ' -> 'Ｘ';
            case 'Y', 'ｙ' -> 'Ｙ';
            case 'Z', 'ｚ' -> 'Ｚ';

            default -> c;
        };
    }

    public static char toZenkakuLower(final char c) {

        return switch (c) {
            case 'a', 'Ａ' -> 'ａ';
            case 'b', 'Ｂ' -> 'ｂ';
            case 'c', 'Ｃ' -> 'ｃ';
            case 'd', 'Ｄ' -> 'ｄ';
            case 'e', 'Ｅ' -> 'ｅ';
            case 'f', 'Ｆ' -> 'ｆ';
            case 'g', 'Ｇ' -> 'ｇ';
            case 'h', 'Ｈ' -> 'ｈ';
            case 'i', 'Ｉ' -> 'ｉ';
            case 'j', 'Ｊ' -> 'ｊ';
            case 'k', 'Ｋ' -> 'ｋ';
            case 'l', 'Ｌ' -> 'ｌ';
            case 'm', 'Ｍ' -> 'ｍ';
            case 'n', 'Ｎ' -> 'ｎ';
            case 'o', 'Ｏ' -> 'ｏ';
            case 'p', 'Ｐ' -> 'ｐ';
            case 'q', 'Ｑ' -> 'ｑ';
            case 'r', 'Ｒ' -> 'ｒ';
            case 's', 'Ｓ' -> 'ｓ';
            case 't', 'Ｔ' -> 'ｔ';
            case 'u', 'Ｕ' -> 'ｕ';
            case 'v', 'Ｖ' -> 'ｖ';
            case 'w', 'Ｗ' -> 'ｗ';
            case 'x', 'Ｘ' -> 'ｘ';
            case 'y', 'Ｙ' -> 'ｙ';
            case 'z', 'Ｚ' -> 'ｚ';

            default -> c;
        };
    }

    public static char toAscii(final char c) {

        return switch (c) {
            case 'Ａ' -> 'A';
            case 'Ｂ' -> 'B';
            case 'Ｃ' -> 'C';
            case 'Ｄ' -> 'D';
            case 'Ｅ' -> 'E';
            case 'Ｆ' -> 'F';
            case 'Ｇ' -> 'G';
            case 'Ｈ' -> 'H';
            case 'Ｉ' -> 'I';
            case 'Ｊ' -> 'J';
            case 'Ｋ' -> 'K';
            case 'Ｌ' -> 'L';
            case 'Ｍ' -> 'M';
            case 'Ｎ' -> 'N';
            case 'Ｏ' -> 'O';
            case 'Ｐ' -> 'P';
            case 'Ｑ' -> 'Q';
            case 'Ｒ' -> 'R';
            case 'Ｓ' -> 'S';
            case 'Ｔ' -> 'T';
            case 'Ｕ' -> 'U';
            case 'Ｖ' -> 'V';
            case 'Ｗ' -> 'W';
            case 'Ｘ' -> 'X';
            case 'Ｙ' -> 'Y';
            case 'Ｚ' -> 'Z';

            case 'ａ' -> 'a';
            case 'ｂ' -> 'b';
            case 'ｃ' -> 'c';
            case 'ｄ' -> 'd';
            case 'ｅ' -> 'e';
            case 'ｆ' -> 'f';
            case 'ｇ' -> 'g';
            case 'ｈ' -> 'h';
            case 'ｉ' -> 'i';
            case 'ｊ' -> 'j';
            case 'ｋ' -> 'k';
            case 'ｌ' -> 'l';
            case 'ｍ' -> 'm';
            case 'ｎ' -> 'n';
            case 'ｏ' -> 'o';
            case 'ｐ' -> 'p';
            case 'ｑ' -> 'q';
            case 'ｒ' -> 'r';
            case 'ｓ' -> 's';
            case 'ｔ' -> 't';
            case 'ｕ' -> 'u';
            case 'ｖ' -> 'v';
            case 'ｗ' -> 'w';
            case 'ｘ' -> 'x';
            case 'ｙ' -> 'y';
            case 'ｚ' -> 'z';

            case '０' -> '0';
            case '１' -> '1';
            case '２' -> '2';
            case '３' -> '3';
            case '４' -> '4';
            case '５' -> '5';
            case '６' -> '6';
            case '７' -> '7';
            case '８' -> '8';
            case '９' -> '9';

            case '！' -> '!';
            case '＂' -> '"';
            case '＃' -> '#';
            case '＄' -> '$';
            case '％' -> '%';
            case '＆' -> '&';
            case '＇' -> '\'';
            case '（' -> '(';
            case '）' -> ')';
            case '＊' -> '*';
            case '＋' -> '+';
            case '，' -> ',';
            case '－' -> '-';
            case '．' -> '.';
            case '／' -> '/';
            case '：' -> ':';
            case '；' -> ';';
            case '＜' -> '<';
            case '＝' -> '=';
            case '＞' -> '>';
            case '？' -> '?';
            case '＠' -> '@';
            case '［' -> '[';
            case '＼' -> '\\';
            case '］' -> ']';
            case '＾' -> '^';
            case '＿' -> '_';
            case '｀' -> '`';
            case '｛' -> '{';
            case '｜' -> '|';
            case '｝' -> '}';
            case '～' -> '~';

            default -> c;
        };
    }

    public static char toZenkaku(final char c) {

        return switch (c) {
            case 'A' -> 'Ａ';
            case 'B' -> 'Ｂ';
            case 'C' -> 'Ｃ';
            case 'D' -> 'Ｄ';
            case 'E' -> 'Ｅ';
            case 'F' -> 'Ｆ';
            case 'G' -> 'Ｇ';
            case 'H' -> 'Ｈ';
            case 'I' -> 'Ｉ';
            case 'J' -> 'Ｊ';
            case 'K' -> 'Ｋ';
            case 'L' -> 'Ｌ';
            case 'M' -> 'Ｍ';
            case 'N' -> 'Ｎ';
            case 'O' -> 'Ｏ';
            case 'P' -> 'Ｐ';
            case 'Q' -> 'Ｑ';
            case 'R' -> 'Ｒ';
            case 'S' -> 'Ｓ';
            case 'T' -> 'Ｔ';
            case 'U' -> 'Ｕ';
            case 'V' -> 'Ｖ';
            case 'W' -> 'Ｗ';
            case 'X' -> 'Ｘ';
            case 'Y' -> 'Ｙ';
            case 'Z' -> 'Ｚ';

            case 'a' -> 'ａ';
            case 'b' -> 'ｂ';
            case 'c' -> 'ｃ';
            case 'd' -> 'ｄ';
            case 'e' -> 'ｅ';
            case 'f' -> 'ｆ';
            case 'g' -> 'ｇ';
            case 'h' -> 'ｈ';
            case 'i' -> 'ｉ';
            case 'j' -> 'ｊ';
            case 'k' -> 'ｋ';
            case 'l' -> 'ｌ';
            case 'm' -> 'ｍ';
            case 'n' -> 'ｎ';
            case 'o' -> 'ｏ';
            case 'p' -> 'ｐ';
            case 'q' -> 'ｑ';
            case 'r' -> 'ｒ';
            case 's' -> 'ｓ';
            case 't' -> 'ｔ';
            case 'u' -> 'ｕ';
            case 'v' -> 'ｖ';
            case 'w' -> 'ｗ';
            case 'x' -> 'ｘ';
            case 'y' -> 'ｙ';
            case 'z' -> 'ｚ';

            case '0' -> '０';
            case '1' -> '１';
            case '2' -> '２';
            case '3' -> '３';
            case '4' -> '４';
            case '5' -> '５';
            case '6' -> '６';
            case '7' -> '７';
            case '8' -> '８';
            case '9' -> '９';

            case '!' -> '！';
            case '"' -> '＂';
            case '#' -> '＃';
            case '$' -> '＄';
            case '%' -> '％';
            case '&' -> '＆';
            case '\'' -> '＇';
            case '(' -> '（';
            case ')' -> '）';
            case '*' -> '＊';
            case '+' -> '＋';
            case ',' -> '，';
            case '-' -> '－';
            case '.' -> '．';
            case '/' -> '／';
            case ':' -> '：';
            case ';' -> '；';
            case '<' -> '＜';
            case '=' -> '＝';
            case '>' -> '＞';
            case '?' -> '？';
            case '@' -> '＠';
            case '[' -> '［';
            case '\\' -> '＼';
            case ']' -> '］';
            case '^' -> '＾';
            case '_' -> '＿';
            case '`' -> '｀';
            case '{' -> '｛';
            case '|' -> '｜';
            case '}' -> '｝';
            case '~' -> '～';

            default -> c;
        };
    }

    public static boolean isZenkakuAlnum(final char c) { return isZenkakuDigit(c) || isZenkakuAlpha(c); }

    public static boolean isZenkakuDigit(final char c) { return ('０' <= c) && (c <= '９'); }

    public static boolean isZenkakuAlpha(final char c) { return (('ａ' <= c) && (c <= 'ｚ')) || (('Ａ' <= c) && (c <= 'Ｚ')); }

    public static boolean isAlnum(final char c) { return isDigit(c) || isAlpha(c); }

    public static boolean isDigit(final char c) { return '0' <= c && c <= '9'; }

    public static boolean isAlpha(final char c) { return (('a' <= c) && (c <= 'z')) || (('A' <= c) && (c <= 'Z')); }

    public static char toLower(final char c) {

        return switch (c) {
            case 'A' -> 'a';
            case 'B' -> 'b';
            case 'C' -> 'c';
            case 'D' -> 'd';
            case 'E' -> 'e';
            case 'F' -> 'f';
            case 'G' -> 'g';
            case 'H' -> 'h';
            case 'I' -> 'i';
            case 'J' -> 'j';
            case 'K' -> 'k';
            case 'L' -> 'l';
            case 'M' -> 'm';
            case 'N' -> 'n';
            case 'O' -> 'o';
            case 'P' -> 'p';
            case 'Q' -> 'q';
            case 'R' -> 'r';
            case 'S' -> 's';
            case 'T' -> 't';
            case 'U' -> 'u';
            case 'V' -> 'v';
            case 'W' -> 'w';
            case 'X' -> 'x';
            case 'Y' -> 'y';
            case 'Z' -> 'z';

            default -> c;
        };
    }

    public static char toUpper(final char c) {

        return switch (c) {
            case 'a' -> 'A';
            case 'b' -> 'B';
            case 'c' -> 'C';
            case 'd' -> 'D';
            case 'e' -> 'E';
            case 'f' -> 'F';
            case 'g' -> 'G';
            case 'h' -> 'H';
            case 'i' -> 'I';
            case 'j' -> 'J';
            case 'k' -> 'K';
            case 'l' -> 'L';
            case 'm' -> 'M';
            case 'n' -> 'N';
            case 'o' -> 'O';
            case 'p' -> 'P';
            case 'q' -> 'Q';
            case 'r' -> 'R';
            case 's' -> 'S';
            case 't' -> 'T';
            case 'u' -> 'U';
            case 'v' -> 'V';
            case 'w' -> 'W';
            case 'x' -> 'X';
            case 'y' -> 'Y';
            case 'z' -> 'Z';

            default -> c;
        };
    }

    public static String toAscii(final String s) {

        final var sb = new StringBuilder(s.length());

        for (final var c: s.toCharArray())
            sb.append(toAscii(c));

        return sb.toString();
    }

    public static $<String> firstLine(final String s) {
        if (empty(s)) return none();
        final int idx = s.indexOf('\n');
        return idx < 0 ? none() : opt(s.substring(0, idx));
    }

    public static $<String> lastLine(final String s) {
        if (empty(s)) return none();
        final int idx = s.lastIndexOf('\n');
        return idx < 0 ? none() : opt(s.substring(idx + 1));
    }

    public static int countWhile(final String s, final IntPredicate test) {
        if (empty(s)) return 0;
        final int len = s.length();
        for (int i = 0; i < len; i++)
            if (!test.test(s.charAt(i))) return i;
        return len;
    }
}
