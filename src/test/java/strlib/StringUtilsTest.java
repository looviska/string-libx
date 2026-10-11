package strlib;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class StringUtilsTest {

    // ---------- split

    @Test
    void splitCharKeepEmpty() {
        assertEquals(List.of("a", "b", "", "c"), StringUtils.split("a,b,,c", ',', SplitMode.KEEP_EMPTY));
    }

    @Test
    void splitCharSkipEmpty() {
        assertEquals(List.of("a", "b", "c"), StringUtils.split("a,b,,c", ',', SplitMode.SKIP_EMPTY));
    }

    @Test
    void splitSeparatorsAtEdges() {
        assertEquals(List.of("", "a", ""), StringUtils.split(",a,", ',', SplitMode.KEEP_EMPTY));
        assertEquals(List.of("a"), StringUtils.split(",a,", ',', SplitMode.SKIP_EMPTY));
    }

    @Test
    void splitEmptyInput() {
        assertEquals(List.of(""), StringUtils.split("", ',', SplitMode.KEEP_EMPTY));
        assertEquals(List.of(), StringUtils.split("", ',', SplitMode.SKIP_EMPTY));
    }

    @Test
    void splitSeparatorNotFound() {
        assertEquals(List.of("abc"), StringUtils.split("abc", ',', SplitMode.KEEP_EMPTY));
    }

    @Test
    void splitBySubstring() {
        assertEquals(List.of("one", "two", "three"),
                StringUtils.split("one--two--three", "--", SplitMode.KEEP_EMPTY));
    }

    @Test
    void splitOverlappingMatches() {
        assertEquals(List.of("", "a"), StringUtils.split("aaa", "aa", SplitMode.KEEP_EMPTY));
    }

    @Test
    void splitInvalidArgumentsThrow() {
        assertThrows(InvalidArgumentException.class, () -> StringUtils.split("abc", "", SplitMode.KEEP_EMPTY));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.split(null, ',', SplitMode.KEEP_EMPTY));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.split("a", ',', null));
    }

    @Test
    void joinVariants() {
        assertEquals("x-y-z", StringUtils.join("-", List.of("x", "y", "z")));
        assertEquals("a, b", StringUtils.join(", ", "a", "b"));
        assertEquals("", StringUtils.join("-", List.of()));
        assertEquals("solo", StringUtils.join("-", "solo"));
    }

    @Test
    void joinNullsThrow() {
        assertThrows(InvalidArgumentException.class, () -> StringUtils.join("-", "a", null));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.join(null, "a"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.join("-", (Iterable<String>) null));
    }

    // ---------- replace / replaceFirst

    @Test
    void replaceAllOccurrences() {
        assertEquals("a == b == c", StringUtils.replace("a=b=c", "=", " == "));
        assertEquals("heo", StringUtils.replace("hello", "l", ""));
    }

    @Test
    void replaceEdgeCases() {
        assertEquals("hello", StringUtils.replace("hello", "z", "!"));
        assertEquals("ba", StringUtils.replace("aaa", "aa", "b"));
        assertEquals("", StringUtils.replace("", "a", "b"));
    }

    @Test
    void replaceInvalidArgumentsThrow() {
        assertThrows(InvalidArgumentException.class, () -> StringUtils.replace("abc", "", "x"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.replace("abc", "a", null));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.replaceFirst(null, "a", "b"));
    }

    @Test
    void replaceFirstOnlyFirst() {
        assertEquals("a:b=c", StringUtils.replaceFirst("a=b=c", "=", ":"));
        assertEquals("hello", StringUtils.replaceFirst("hello", "z", "!"));
        assertEquals("Xbc", StringUtils.replaceFirst("abc", "a", "X"));
        assertEquals("abX", StringUtils.replaceFirst("abc", "c", "X"));
    }
    // ---------- indexOf / lastIndexOf / startsWith / endsWith / trim

    @Test
    void indexOfAndLastIndexOf() {
        assertEquals(4, StringUtils.indexOf("path/to/file", "/"));
        assertEquals(7, StringUtils.lastIndexOf("path/to/file", "/"));
        assertEquals(-1, StringUtils.indexOf("hello", "z"));
        assertEquals(-1, StringUtils.lastIndexOf("hello", "z"));
        assertEquals(-1, StringUtils.indexOf("ab", "abc"));
    }

    @Test
    void searchWithEmptyTarget() {
        assertEquals(0, StringUtils.indexOf("hello", ""));
        assertEquals(5, StringUtils.lastIndexOf("hello", ""));
        assertEquals(0, StringUtils.indexOf("", ""));
    }

    @Test
    void startsAndEndsWith() {
        assertTrue(StringUtils.startsWith("Hello", "Hel"));
        assertFalse(StringUtils.startsWith("Hi", "Hello"));
        assertTrue(StringUtils.startsWith("abc", ""));
        assertTrue(StringUtils.endsWith("file.txt", ".txt"));
        assertFalse(StringUtils.endsWith("x", "long"));
        assertTrue(StringUtils.endsWith("abc", ""));
    }

    @Test
    void searchNullsThrow() {
        assertThrows(InvalidArgumentException.class, () -> StringUtils.indexOf(null, "a"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.lastIndexOf("a", null));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.startsWith(null, "a"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.endsWith("a", null));
    }

    @Test
    void trimRemovesEdgeWhitespace() {
        assertEquals("a b", StringUtils.trim("  \t a b \n "));
        assertEquals("", StringUtils.trim("   "));
        assertEquals("", StringUtils.trim(""));
        assertEquals("x", StringUtils.trim("x"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.trim(null));
    }

    @Test
    void equalsIgnoreCaseBasics() {
        assertTrue(StringUtils.equalsIgnoreCase("MiXeD", "mixed"));
        assertTrue(StringUtils.equalsIgnoreCase("", ""));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "abcd"));
        assertFalse(StringUtils.equalsIgnoreCase("abc", "abd"));
        assertTrue(StringUtils.equalsIgnoreCase("\u041f\u0440\u0438\u0432\u0435\u0442", "\u043f\u0420\u0418\u0412\u0415\u0422"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.equalsIgnoreCase(null, "a"));
    }

    @Test
    void repeatBasics() {
        assertEquals("ababab", StringUtils.repeat("ab", 3));
        assertEquals("", StringUtils.repeat("ab", 0));
        assertEquals("", StringUtils.repeat("", 1000));
    }

    @Test
    void repeatInvalidThrows() {
        assertThrows(InvalidArgumentException.class, () -> StringUtils.repeat("ab", -1));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.repeat(null, 2));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.repeat("abc", Integer.MAX_VALUE));
    }

    @Test
    void padding() {
        assertEquals("00042", StringUtils.padLeft("42", 5, '0'));
        assertEquals("42...", StringUtils.padRight("42", 5, '.'));
    }

    @Test
    void paddingBoundaries() {
        assertEquals("hello", StringUtils.padLeft("hello", 5, '*'));
        assertEquals("hello", StringUtils.padRight("hello", 2, '*'));
        assertEquals("**", StringUtils.padRight("", 2, '*'));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.padLeft("a", -1, '*'));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.padRight(null, 3, '*'));
    }

    @Test
    void reverseBasics() {
        assertEquals("olleh", StringUtils.reverse("hello"));
        assertEquals("", StringUtils.reverse(""));
        assertEquals("racecar", StringUtils.reverse("racecar"));
        assertThrows(InvalidArgumentException.class, () -> StringUtils.reverse(null));
    }

    @Test
    void utilityClassConstructorIsPrivate() throws Exception {
        Constructor<StringUtils> ctor = StringUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(ctor.getModifiers()));
        ctor.setAccessible(true);
        InvocationTargetException e = assertThrows(InvocationTargetException.class, ctor::newInstance);
        assertTrue(e.getCause() instanceof AssertionError);
    }
}