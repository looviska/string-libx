package strlib;

/**
 * Внутренние проверки аргументов. Все нарушения превращаются
 * в {@link InvalidArgumentException} с понятным сообщением.
 */
final class Args {

    private Args() {
        throw new AssertionError("Утилитный класс не предназначен для создания экземпляров");
    }

    static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new InvalidArgumentException("Аргумент '" + name + "' не должен быть null");
        }
        return value;
    }

    static void nonEmpty(String value, String name) {
        if (value.length() == 0) {
            throw new InvalidArgumentException("Аргумент '" + name + "' не должен быть пустым");
        }
    }

    static void nonNegative(int value, String name) {
        if (value < 0) {
            throw new InvalidArgumentException("Аргумент '" + name + "' не может быть отрицательным: " + value);
        }
    }
}
