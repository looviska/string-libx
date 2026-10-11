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
}