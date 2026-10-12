package strlib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BenchmarkResultTest {

    private final BenchmarkResult a = new BenchmarkResult(10, 200, 100, 10);
    private final BenchmarkResult sameAsA = new BenchmarkResult(10, 200, 100, 10);
    private final BenchmarkResult sameAsA2 = new BenchmarkResult(10, 200, 100, 10);
    private final BenchmarkResult other = new BenchmarkResult(10, 300, 100, 10);

    @Test
    void equalsContract() {
        assertEquals(a, a);
        assertEquals(a, sameAsA);
        assertEquals(sameAsA, a);
        assertEquals(sameAsA, sameAsA2);
        assertEquals(a, sameAsA2);
    }

    @Test
    void equalsNegative() {
        assertNotEquals(null, a);
        assertNotEquals("строка", a);
        assertNotEquals(a, other);
    }

    @Test
    void hashCodeContract() {
        assertEquals(a.hashCode(), sameAsA.hashCode());
        Set<BenchmarkResult> set = new HashSet<>();
        set.add(a);
        set.add(sameAsA);
        set.add(other);
        assertEquals(2, set.size());
    }

    @Test
    void invariants() {
        assertThrows(InvalidArgumentException.class, () -> new BenchmarkResult(0, 1, 1, 0));
        assertThrows(InvalidArgumentException.class, () -> new BenchmarkResult(1, -1, 1, 1));
        assertThrows(InvalidArgumentException.class, () -> new BenchmarkResult(1, 1, -1, 1));
    }

    @Test
    void ratio() {
        assertEquals(2.0, a.ratio(), 1e-9);
        assertEquals(Double.POSITIVE_INFINITY, new BenchmarkResult(1, 5, 0, 1).ratio());
    }

    @Test
    void splitModeEnum() {
        assertTrue(SplitMode.KEEP_EMPTY.keepsEmpty());
        assertFalse(SplitMode.SKIP_EMPTY.keepsEmpty());
        assertEquals(2, SplitMode.values().length);
        assertEquals(SplitMode.KEEP_EMPTY, SplitMode.valueOf("KEEP_EMPTY"));
    }

    @Test
    void exceptionHierarchy() {
        assertTrue(new InvalidArgumentException("x") instanceof StringLibException);
        assertTrue(new FormatException("x") instanceof StringLibException);
        assertEquals("msg", new FormatException("msg").getMessage());
    }
}