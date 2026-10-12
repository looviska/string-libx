package strlib;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FormatterTest {

    private final StringFormatter<List<?>> positional = new PositionalFormatter();
    private final StringFormatter<Map<String, ?>> named = new NamedFormatter();

    @Test
    void positionalBasic() {
        assertEquals("2 + 3 = 5", positional.format("{0} + {1} = {2}", List.of(2, 3, 5)));
    }

    @Test
    void positionalOrderAndRepeat() {
        assertEquals("bab", positional.format("{1}{0}{1}", List.of("a", "b")));
    }

    @Test
    void positionalNoPlaceholders() {
        assertEquals("plain", positional.format("plain", List.of()));
        assertEquals("", positional.format("", List.of()));
    }

    @Test
    void escapedBraces() {
        assertEquals("{0} = 7", positional.format("{{0}} = {0}", List.of(7)));
    }

    @Test
    void positionalIndexOutOfRange() {
        FormatException e = assertThrows(FormatException.class, () -> positional.format("{2}", List.of("a")));
        assertEquals("Нет аргумента для {2}: передано только 1", e.getMessage());
    }

    @Test
    void positionalBadKeys() {
        assertThrows(FormatException.class, () -> positional.format("{abc}", List.of("a")));
        assertThrows(FormatException.class, () -> positional.format("{}", List.of("a")));
        assertThrows(FormatException.class, () -> positional.format("{-1}", List.of("a")));
        assertThrows(FormatException.class, () -> positional.format("{99999999999}", List.of("a")));
    }

    @Test
    void unbalancedBraces() {
        assertThrows(FormatException.class, () -> positional.format("{0", List.of("a")));
        assertThrows(FormatException.class, () -> positional.format("0}", List.of("a")));
    }

    @Test
    void namedBasic() {
        assertEquals("Привет, Анна из Казани",
                named.format("Привет, {name} из {city}", Map.of("name", "Анна", "city", "Казани")));
    }

    @Test
    void namedNullValue() {
        Map<String, Object> values = new HashMap<>();
        values.put("x", null);
        assertEquals("x=null", named.format("x={x}", values));
    }

    @Test
    void namedMissingKey() {
        assertThrows(FormatException.class, () -> named.format("{age}", Map.of("name", "x")));
        assertThrows(FormatException.class, () -> named.format("{}", Map.of("name", "x")));
    }

    @Test
    void nullArgumentsThrow() {
        assertThrows(InvalidArgumentException.class, () -> positional.format(null, List.of()));
        assertThrows(InvalidArgumentException.class, () -> named.format("{a}", null));
    }

    @Test
    void formatWithVarargs() {
        PositionalFormatter f = new PositionalFormatter();
        assertEquals("1 and null", f.formatWith("{0} and {1}", 1, null));
        assertThrows(InvalidArgumentException.class, () -> f.formatWith("{0}", (Object[]) null));
    }

    @Test
    void polymorphism() {
        assertInstanceOf(AbstractFormatter.class, positional);
        assertInstanceOf(AbstractFormatter.class, named);
    }
}