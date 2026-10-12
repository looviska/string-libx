package strlib;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Консольная демонстрация: 33 тестовые строки и все операции библиотеки.
 */
public class Main {

    /**
     * Точка входа.
     *
     * @param args не используются
     */
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        List<String> samples = List.of(
                "hello", "World", "  padded  ", "a,b,,c", "one--two--three", "", " ",
                "Java 21", "racecar", "UPPER", "lower", "MiXeD", "TITLE",
                "\u0130stanbul", "\u0131sparta", "caf\u00e9", "na\u00efve", "x", "12345",
                "a=b=c", "path/to/file.txt", "2024-10-08", "{name}", "tab\there",
                "\u041f\u0440\u0438\u0432\u0435\u0442, \u043c\u0438\u0440",
                "\u65e5\u672c\u8a9e", "emoji \uD83D\uDE00 end", "\uD83D\uDE00\uD83D\uDE01",
                "end.", "...", "a b  c   d", "Hello, World!", "repeat-me");
        System.out.println("Создано тестовых строк: " + samples.size());

        section("1. split");
        for (String s : List.of("a,b,,c", ",a,", "")) {
            show("split(\"" + s + "\", ',', KEEP_EMPTY)", StringUtils.split(s, ',', SplitMode.KEEP_EMPTY));
            show("split(\"" + s + "\", ',', SKIP_EMPTY)", StringUtils.split(s, ',', SplitMode.SKIP_EMPTY));
        }
        show("split(\"one--two--three\", \"--\")", StringUtils.split("one--two--three", "--", SplitMode.KEEP_EMPTY));
        show("split(\"aaa\", \"aa\")", StringUtils.split("aaa", "aa", SplitMode.KEEP_EMPTY));

        section("2. join");
        show("join(\"-\", [x, y, z])", StringUtils.join("-", List.of("x", "y", "z")));
        show("join(\", \", a, b, c)", StringUtils.join(", ", "a", "b", "c"));

        section("3. replace / replaceFirst");
        show("replace(\"a=b=c\", \"=\", \" == \")", StringUtils.replace("a=b=c", "=", " == "));
        show("replace(\"hello\", \"l\", \"\")", StringUtils.replace("hello", "l", ""));
        show("replaceFirst(\"a=b=c\", \"=\", \":\")", StringUtils.replaceFirst("a=b=c", "=", ":"));

        section("4. indexOf / lastIndexOf");
        show("indexOf(\"path/to/file.txt\", \"/\")", StringUtils.indexOf("path/to/file.txt", "/"));
        show("lastIndexOf(\"path/to/file.txt\", \"/\")", StringUtils.lastIndexOf("path/to/file.txt", "/"));
        show("indexOf(\"hello\", \"z\")", StringUtils.indexOf("hello", "z"));

        section("5. startsWith / endsWith");
        show("startsWith(\"Hello, World!\", \"Hello\")", StringUtils.startsWith("Hello, World!", "Hello"));
        show("endsWith(\"path/to/file.txt\", \".txt\")", StringUtils.endsWith("path/to/file.txt", ".txt"));

        section("6. trim (там, где что-то изменилось)");
        for (String s : samples) {
            String t = StringUtils.trim(s);
            if (!t.equals(s)) {
                show("trim(\"" + s + "\")", "\"" + t + "\"");
            }
        }

        section("7. equalsIgnoreCase");
        show("equalsIgnoreCase(\"MiXeD\", \"mixed\")", StringUtils.equalsIgnoreCase("MiXeD", "mixed"));
        show("equalsIgnoreCase(\"abc\", \"abcd\")", StringUtils.equalsIgnoreCase("abc", "abcd"));
        show("equalsIgnoreCase(\"İstanbul\", \"istanbul\")", StringUtils.equalsIgnoreCase("\u0130stanbul", "istanbul"));

        section("8. repeat / padLeft / padRight");
        show("repeat(\"ab\", 3)", StringUtils.repeat("ab", 3));
        show("padLeft(\"42\", 8, '0')", StringUtils.padLeft("42", 8, '0'));
        show("padRight(\"42\", 8, '.')", StringUtils.padRight("42", 8, '.'));

        section("9. reverse для всех образцов");
        for (String s : samples) {
            show("reverse(\"" + s + "\")", StringUtils.reverse(s));
        }

        section("10. Форматтеры");
        StringFormatter<List<?>> positional = new PositionalFormatter();
        StringFormatter<Map<String, ?>> named = new NamedFormatter();
        show("positional \"{0} + {1} = {2}\"", positional.format("{0} + {1} = {2}", List.of(2, 3, 5)));
        Map<String, Object> values = new LinkedHashMap<>();
        values.put("name", "Анна");
        values.put("city", "Казань");
        show("named \"Привет, {name} из {city}!\"", named.format("Привет, {name} из {city}!", values));

        section("11. Ловушки");
        String emoji = "a\uD83D\uDE00b";
        show("reverse(\"a😀b\") правильный", StringUtils.reverse(emoji));
        show("reverseByChars(\"a😀b\") наивный", StringUtils.reverseByChars(emoji));
        Locale old = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            show("tr-TR: \"TITLE\".toLowerCase() (наивно)", "TITLE".toLowerCase());
            show("tr-TR: equalsIgnoreCase(\"TITLE\", \"title\")", StringUtils.equalsIgnoreCase("TITLE", "title"));
        } finally {
            Locale.setDefault(old);
        }
        BenchmarkResult r = ConcatBenchmark.run(100_000);
        System.out.printf("  s += 'x' : %.1f мс%n", r.concatNanos() / 1_000_000.0);
        System.out.printf("  StringBuilder : %.1f мс%n", r.builderNanos() / 1_000_000.0);
        System.out.printf("  разница в %.0f раз%n", r.ratio());

        section("12. Собственные исключения");
        try {
            StringUtils.repeat("ab", -1);
        } catch (InvalidArgumentException e) {
            System.out.println("  InvalidArgumentException: " + e.getMessage());
        }
        try {
            StringUtils.split("abc", "", SplitMode.KEEP_EMPTY);
        } catch (InvalidArgumentException e) {
            System.out.println("  InvalidArgumentException: " + e.getMessage());
        }
        try {
            positional.format("{5}", List.of("a"));
        } catch (FormatException e) {
            System.out.println("  FormatException: " + e.getMessage());
        }
        try {
            named.format("{age}", Map.of("name", "x"));
        } catch (FormatException e) {
            System.out.println("  FormatException: " + e.getMessage());
        }
        System.out.println();
        System.out.println("Демонстрация завершена.");
    }

    private static void section(String title) {
        System.out.println();
        System.out.println(title);
    }

    private static void show(String call, Object result) {
        System.out.println("  " + call + " = " + result);
    }
}