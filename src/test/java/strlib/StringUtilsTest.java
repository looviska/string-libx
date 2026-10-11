package strlib;

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
}