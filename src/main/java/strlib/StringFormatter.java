package strlib;

/**
 * Форматтер: подставляет значения в шаблон со скобочными placeholder-ами.
 * Способ адресации значений зависит от реализации (по позиции или по имени).
 *
 * @param <A> тип набора аргументов
 */
public interface StringFormatter<A> {

    /**
     * Подставляет аргументы в шаблон.
     *
     * @param template шаблон; удвоенные скобки означают одну буквальную скобку
     * @param args     значения для подстановки
     * @return готовая строка
     * @throws InvalidArgumentException если шаблон или аргументы равны null
     * @throws FormatException          если шаблон некорректен или для placeholder-а нет значения
     */
    String format(String template, A args);
}