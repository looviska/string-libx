package strlib;

import java.util.Arrays;
import java.util.List;

/**
 * Форматтер по позиции: placeholder-ы {0}, {1}, ...
 * Один индекс можно использовать несколько раз и в любом порядке.
 */
public class PositionalFormatter extends AbstractFormatter<List<?>> {

    /** Максимум цифр в индексе — защита от переполнения int. */
    private static final int MAX_DIGITS = 9;

    /**
     * {@inheritDoc}
     *
     * @throws FormatException если ключ пуст, не число или индекс вне списка
     */
    @Override
    protected Object resolve(String key, List<?> args) {
        if (key.length() == 0) {
            throw new FormatException("Пустой placeholder {}: ожидался числовой индекс");
        }
        if (key.length() > MAX_DIGITS) {
            throw new FormatException("Слишком длинный индекс в placeholder {" + key + "}");
        }
        int index = 0;
        for (char c : key.toCharArray()) {
            if (c < '0' || c > '9') {
                throw new FormatException("Placeholder {" + key + "} не является числом");
            }
            index = index * 10 + (c - '0');
        }
        if (index >= args.size()) {
            throw new FormatException("Нет аргумента для {" + index + "}: передано только " + args.size());
        }
        return args.get(index);
    }

    /**
     * Удобная обёртка с переменным числом аргументов.
     *
     * @param template шаблон
     * @param values   значения по порядку
     * @return готовая строка
     * @throws InvalidArgumentException если values равен null
     */
    public String formatWith(String template, Object... values) {
        Args.notNull(values, "values");
        return format(template, Arrays.asList(values));
    }
}