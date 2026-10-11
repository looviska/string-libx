package strlib;


import java.util.Arrays;
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


    public static String join(String delimiter, Iterable<String> parts) {
        Args.notNull(delimiter, "delimiter");
        Args.notNull(parts, "parts");
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String part : parts) {
            Args.notNull(part, "элемент parts");
            if (!first) {
                sb.append(delimiter);
            }
            sb.append(part);
            first = false;
        }
        return sb.toString();
    }

    public static String join(String delimiter, String... parts) {
        Args.notNull(parts, "parts");
        return join(delimiter, Arrays.asList(parts));
    }


    public static String replace(String s, String target, String replacement) {
        char[] a = checkReplaceArgs(s, target, replacement);
        char[] t = target.toCharArray();
        StringBuilder sb = new StringBuilder(a.length);
        int pos = 0;
        int idx;
        while ((idx = find(a, t, pos)) >= 0) {
            sb.append(a, pos, idx - pos).append(replacement);
            pos = idx + t.length;
        }
        sb.append(a, pos, a.length - pos);
        return sb.toString();
    }

    public static String replaceFirst(String s, String target, String replacement) {
        char[] a = checkReplaceArgs(s, target, replacement);
        char[] t = target.toCharArray();
        int idx = find(a, t, 0);
        if (idx < 0) {
            return s;
        }
        StringBuilder sb = new StringBuilder(a.length);
        sb.append(a, 0, idx).append(replacement).append(a, idx + t.length, a.length - idx - t.length);
        return sb.toString();
    }

    private static char[] checkReplaceArgs(String s, String target, String replacement) {
        Args.notNull(s, "s");
        Args.notNull(target, "target");
        Args.notNull(replacement, "replacement");
        Args.nonEmpty(target, "target");
        return s.toCharArray();
    }

    private static int find(char[] text, char[] pattern, int from) {
        for (int i = from; i <= text.length - pattern.length; i++) {
            if (matchesAt(text, pattern, i)) {
                return i;
            }
        }
        return -1;
    }
 }
