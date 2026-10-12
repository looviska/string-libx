package strlib;

import java.util.Map;

/**
 * Форматтер по имени: placeholder-ы {name}, {city}.
 * Значение null допустимо, если ключ присутствует в Map.
 */
public class NamedFormatter extends AbstractFormatter<Map<String, ?>> {

    /**
     * {@inheritDoc}
     *
     * @throws FormatException если имя пустое или его нет в args
     */
    @Override
    protected Object resolve(String key, Map<String, ?> args) {
        if (key.length() == 0) {
            throw new FormatException("Пустой placeholder {}: ожидалось имя");
        }
        if (!args.containsKey(key)) {
            throw new FormatException("Нет значения для {" + key + "}");
        }
        return args.get(key);
    }
}