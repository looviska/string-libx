package strlib;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public final class StringUtils {

    private StringUtils() {
        throw new AssertionError("Утилитный класс не предназначен для создания экземпляров");
    }


    /**
     * Делит строку по одному символу-разделителю.
     *
     * @param s         исходная строка
     * @param separator символ-разделитель
     * @param mode      сохранять ли пустые части
     * @return новый изменяемый список частей
     * @throws InvalidArgumentException если s или mode равны null
     */

    public static List<String> split(String s, char separator, SplitMode mode) {
        return splitChars(s, new char[]{separator}, mode);
    }

    /**
     * Делит строку по подстроке-разделителю. Совпадения ищутся слева направо
     * и не перекрываются: "aaa" по "aa" даёт ["", "a"].
     *
     * @param s         исходная строка
     * @param separator разделитель (не пустой)
     * @param mode      сохранять ли пустые части
     * @return новый изменяемый список частей
     * @throws InvalidArgumentException при null или пустом разделителе
     */

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

    private static void addPart(List<String> parts, char[] a, int from, int to, SplitMode mode) {
        if (from == to && !mode.keepsEmpty()) {
            return;
        }
        parts.add(new String(a, from, to - from));
    }

    /**
     * Совпадает ли pattern с text, начиная с позиции pos.
     */
    private static boolean matchesAt(char[] text, char[] pattern, int pos) {
        if (pos < 0 || pos + pattern.length > text.length) {
            return false;
        }
        for (int k = 0; k < pattern.length; k++) {
            if (text[pos + k] != pattern[k]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Склеивает части через разделитель.
     *
     * @param delimiter разделитель (может быть пустым)
     * @param parts     части
     * @return склеенная строка; для пустой коллекции — пустая строка
     * @throws InvalidArgumentException если разделитель, коллекция или любая часть равны null
     */
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

    /**
     * Вариант join с переменным числом аргументов.
     *
     * @param delimiter разделитель
     * @param parts     части
     * @return склеенная строка
     * @throws InvalidArgumentException при null
     */
    public static String join(String delimiter, String... parts) {
        Args.notNull(parts, "parts");
        return join(delimiter, Arrays.asList(parts));
    }

    /**
     * Заменяет все непересекающиеся вхождения target (поиск слева направо).
     *
     * @param s           исходная строка
     * @param target      что искать (не пустое)
     * @param replacement чем заменять (может быть пустым — это удаление)
     * @return новая строка; если вхождений нет — равная исходной
     * @throws InvalidArgumentException при null или пустом target
     */
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

    /**
     * Заменяет только первое вхождение target.
     *
     * @param s           исходная строка
     * @param target      что искать (не пустое)
     * @param replacement чем заменять
     * @return новая строка; если вхождений нет — исходная
     * @throws InvalidArgumentException при null или пустом target
     */
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

    /**
     * Индекс первого совпадения pattern в text, начиная с from; -1, если нет.
     */
    private static int find(char[] text, char[] pattern, int from) {
        for (int i = from; i <= text.length - pattern.length; i++) {
            if (matchesAt(text, pattern, i)) {
                return i;
            }
        }
        return -1;
    }
    /**
     * Индекс первого вхождения подстроки.
     *
     * @param s      где искать
     * @param target что искать; пустая подстрока найдена в позиции 0
     * @return индекс или -1
     * @throws InvalidArgumentException при null
     */
    public static int indexOf(String s, String target) {
        Args.notNull(s, "s");
        Args.notNull(target, "target");
        return find(s.toCharArray(), target.toCharArray(), 0);
    }

    /**
     * Индекс последнего вхождения подстроки.
     *
     * @param s      где искать
     * @param target что искать; пустая подстрока найдена в позиции s.length()
     * @return индекс или -1
     * @throws InvalidArgumentException при null
     */
    public static int lastIndexOf(String s, String target) {
        Args.notNull(s, "s");
        Args.notNull(target, "target");
        char[] a = s.toCharArray();
        char[] t = target.toCharArray();
        for (int i = a.length - t.length; i >= 0; i--) {
            if (matchesAt(a, t, i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Проверяет, начинается ли строка с префикса.
     *
     * @param s      строка
     * @param prefix префикс (пустой всегда подходит)
     * @return true, если s начинается с prefix
     * @throws InvalidArgumentException при null
     */
    public static boolean startsWith(String s, String prefix) {
        Args.notNull(s, "s");
        Args.notNull(prefix, "prefix");
        return matchesAt(s.toCharArray(), prefix.toCharArray(), 0);
    }

    /**
     * Проверяет, заканчивается ли строка суффиксом.
     *
     * @param s      строка
     * @param suffix суффикс (пустой всегда подходит)
     * @return true, если s заканчивается на suffix
     * @throws InvalidArgumentException при null
     */
    public static boolean endsWith(String s, String suffix) {
        Args.notNull(s, "s");
        Args.notNull(suffix, "suffix");
        char[] a = s.toCharArray();
        char[] p = suffix.toCharArray();
        return matchesAt(a, p, a.length - p.length);
    }

    /**
     * Удаляет пробельные символы с обоих концов строки.
     *
     * @param s строка
     * @return строка без крайних пробелов
     * @throws InvalidArgumentException если s равна null
     */
    public static String trim(String s) {
        Args.notNull(s, "s");
        char[] a = s.toCharArray();
        int lo = 0;
        int hi = a.length;
        while (lo < hi && Character.isWhitespace(a[lo])) {
            lo++;
        }
        while (hi > lo && Character.isWhitespace(a[hi - 1])) {
            hi--;
        }
        return new String(a, lo, hi - lo);
    }
    // ------------------------------------------------------- equalsIgnoreCase

    /**
     * Сравнение без учёта регистра, не зависящее от локали.
     * Идёт по кодовым точкам, поэтому символы вне BMP (эмодзи) не ломаются.
     * Турецкие İ и ı считаются равными i и I. Многосимвольные отображения
     * (например, ß и SS) не поддерживаются.
     *
     * @param a первая строка
     * @param b вторая строка
     * @return true, если строки совпадают с точностью до регистра
     * @throws InvalidArgumentException при null
     */
    public static boolean equalsIgnoreCase(String a, String b) {
        Args.notNull(a, "a");
        Args.notNull(b, "b");
        char[] x = a.toCharArray();
        char[] y = b.toCharArray();
        int i = 0;
        int j = 0;
        while (i < x.length && j < y.length) {
            int cx = Character.codePointAt(x, i);
            int cy = Character.codePointAt(y, j);
            if (!sameIgnoringCase(cx, cy)) {
                return false;
            }
            i += Character.charCount(cx);
            j += Character.charCount(cy);
        }
        return i == x.length && j == y.length;
    }

    private static boolean sameIgnoringCase(int a, int b) {
        if (a == b) {
            return true;
        }
        int ua = Character.toUpperCase(a);
        int ub = Character.toUpperCase(b);
        if (ua == ub) {
            return true;
        }
        return Character.toLowerCase(ua) == Character.toLowerCase(ub);
    }

    // ---------------------------------------------------------- repeat / pad

    /**
     * Повторяет строку count раз.
     *
     * @param s     строка
     * @param count число повторов (0 даёт пустую строку)
     * @return результат повторения
     * @throws InvalidArgumentException при null, отрицательном count
     *                                  или слишком длинном результате
     */
    public static String repeat(String s, int count) {
        Args.notNull(s, "s");
        Args.nonNegative(count, "count");
        long total = (long) s.length() * count;
        if (total > Integer.MAX_VALUE - 8) {
            throw new InvalidArgumentException("Результат слишком длинный: " + total + " символов");
        }
        StringBuilder sb = new StringBuilder((int) total);
        for (int i = 0; i < count; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    /**
     * Дополняет строку слева до заданной длины.
     *
     * @param s      строка
     * @param length целевая длина; если она не больше текущей, строка не меняется
     * @param pad    символ-заполнитель
     * @return дополненная строка
     * @throws InvalidArgumentException при null или отрицательной длине
     */
    public static String padLeft(String s, int length, char pad) {
        return pad(s, length, pad, true);
    }

    /**
     * Дополняет строку справа до заданной длины.
     *
     * @param s      строка
     * @param length целевая длина; если она не больше текущей, строка не меняется
     * @param pad    символ-заполнитель
     * @return дополненная строка
     * @throws InvalidArgumentException при null или отрицательной длине
     */
    public static String padRight(String s, int length, char pad) {
        return pad(s, length, pad, false);
    }

    private static String pad(String s, int length, char pad, boolean left) {
        Args.notNull(s, "s");
        Args.nonNegative(length, "length");
        int missing = length - s.length();
        if (missing <= 0) {
            return s;
        }
        StringBuilder sb = new StringBuilder(length);
        if (!left) {
            sb.append(s);
        }
        for (int i = 0; i < missing; i++) {
            sb.append(pad);
        }
        if (left) {
            sb.append(s);
        }
        return sb.toString();
    }

    // --------------------------------------------------------------- reverse

    /**
     * Разворачивает строку.
     *
     * @param s строка
     * @return развёрнутая строка
     * @throws InvalidArgumentException если s равна null
     */
    public static String reverse(String s) {
        Args.notNull(s, "s");
        char[] a = s.toCharArray();
        StringBuilder sb = new StringBuilder(a.length);
        for (int i = a.length - 1; i >= 0; i--) {
            sb.append(a[i]);
        }
        return sb.toString();
    }
}