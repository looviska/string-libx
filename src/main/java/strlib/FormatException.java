package strlib;

/**
 * Ошибка форматирования шаблона: незакрытая скобка, отсутствующий
 * аргумент, нечисловой индекс в позиционном шаблоне и т. д.
 */
public class FormatException extends StringLibException {

    /**
     * @param message описание ошибки в шаблоне или в наборе аргументов
     */
    public FormatException(String message) {
        super(message);
    }
}
