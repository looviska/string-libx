package strlib;

/**
 * Базовое исключение библиотеки. Все ошибки библиотеки наследуются от него,
 * поэтому клиент может перехватить их одним {@code catch}.
 */
public class StringLibException extends RuntimeException {

    /**
     * Создаёт исключение с сообщением.
     *
     * @param message описание проблемы
     */
    public StringLibException(String message) {
        super(message);
    }

    /**
     * Создаёт исключение с сообщением и причиной.
     *
     * @param message описание проблемы
     * @param cause   исходная причина
     */
    public StringLibException(String message, Throwable cause) {
        super(message, cause);
    }
}
