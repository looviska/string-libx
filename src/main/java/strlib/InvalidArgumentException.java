package strlib;

/**
 * Некорректный аргумент операции: {@code null}, отрицательная длина,
 * пустой разделитель там, где он недопустим, и т. п.
 */
public class InvalidArgumentException extends StringLibException {

    /**
     * @param message что именно не так с аргументом
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
