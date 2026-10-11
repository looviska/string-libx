package strlib;

import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
        throw new AssertionError("Утилитный класс не предназначен для создания экземпляров");
    }

    public static List<String> split(String s, char separator, SplitMode mode) {
        return splitChars(s, new char[]{separator}, mode);
    }

    public static List<String> split(String s, String separator, SplitMode mode) {
        Args.notNull(separator, "separator");
        Args.nonEmpty(separator, "separator");
        return splitChars(s, separator.toCharArray(), mode);
    }

    private static List<String> splitChars(String s, char[] sep, SplitMode mode) {
        Args.notNull(s, "s");
        Args.notNull(mode, "mode");
        char[] a = s.toCharArray();
        List<String> parts = new ArrayList<>();
        int start = 0;
        int i = 0;
        while (i <= a.length - sep.length) {
            if (matchesAt(a, sep, i)) {
                addPart(parts, a, start, i, mode);
                i += sep.length;
                start = i;
            } else {
                i++;
            }
        }
        addPart(parts, a, start, a.length, mode);
        return parts;
    }
    private static void  addPart(List<String> parts, char[] a, int from, int to, SplitMode mode) {
        if (from == to && !mode.keepsEmpty()) {
            return;
        }
        parts.add(new String(a, from, to - from));
    }

    private static boolean matchesAt(char[] text, char[] pattern, int pos) {
        if (pos < 0 || pos + pattern.length > text.length) {
            return false;
        }
        for (int k = 0; k < pattern.length; k ++) {
            if (text[pos + k] != pattern[k]) {
                return  false;
            }
        }
        return  true;
    }
 }
