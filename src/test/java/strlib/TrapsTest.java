package strlib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Locale;
import org.junit.jupiter.api.Test;

class TrapsTest {

    private static final String SMILE = "\uD83D\uDE00";
    private static final String GRIN = "\uD83D\uDE01";

    //ловушка 1: суррогатные пары

    @Test
    void reverseKeepsSurrogatePairs() {
        assertEquals("b" + SMILE + "a", StringUtils.reverse("a" + SMILE + "b"));
        assertEquals(GRIN + SMILE, StringUtils.reverse(SMILE + GRIN));
    }

    @Test
    void naiveReverseBreaksPairs() {
        String broken = StringUtils.reverseByChars("a" + SMILE + "b");
        assertNotEquals("b" + SMILE + "a", broken);
        assertTrue(Character.isLowSurrogate(broken.charAt(1)));
        assertTrue(Character.isHighSurrogate(broken.charAt(2)));
    }

    @Test
    void reverseHandlesLoneSurrogates() {
        assertEquals("a\uD83D", StringUtils.reverse("\uD83Da"));
        assertEquals("\uDE00a", StringUtils.reverse("a\uDE00"));
    }

    @Test
    void doubleReverseIsIdentity() {
        String original = "x" + SMILE + "y" + GRIN + "z";
        assertEquals(original, StringUtils.reverse(StringUtils.reverse(original)));
    }

    //ловушка 2: турецкая i

    @Test
    void equalsIgnoreCaseIsLocaleIndependent() {
        Locale old = Locale.getDefault();
        try {
            Locale turkish = Locale.forLanguageTag("tr-TR");
            Locale.setDefault(turkish);
            // ловушка: в турецкой локали I превращается в бесточечную ı
            assertNotEquals("title", "TITLE".toLowerCase(turkish));
            // наша реализация от локали не зависит
            assertTrue(StringUtils.equalsIgnoreCase("TITLE", "title"));
            assertTrue(StringUtils.equalsIgnoreCase("title", "TITLE"));
        } finally {
            Locale.setDefault(old);
        }
    }

    @Test
    void turkishDottedAndDotlessI() {
        assertTrue(StringUtils.equalsIgnoreCase("\u0130stanbul", "istanbul"));
        assertTrue(StringUtils.equalsIgnoreCase("\u0131sparta", "ISPARTA"));
        assertFalse(StringUtils.equalsIgnoreCase("istanbul", "istanbuk"));
    }

    // ловушка 3: конкатенация против StringBuilder

    @Test
    void builderBeatsConcatenation() {
        BenchmarkResult r = ConcatBenchmark.run(100_000);
        assertEquals(100_000, r.resultLength());
        assertTrue(r.builderNanos() < r.concatNanos(),
                "StringBuilder должен быть быстрее: concat=" + r.concatNanos()
                        + " нс, builder=" + r.builderNanos() + " нс");
        assertTrue(r.ratio() > 1.0);
    }

    @Test
    void benchmarkInvalidIterationsThrow() {
        assertThrows(InvalidArgumentException.class, () -> ConcatBenchmark.run(0));
        assertThrows(InvalidArgumentException.class, () -> ConcatBenchmark.run(-5));
    }
}