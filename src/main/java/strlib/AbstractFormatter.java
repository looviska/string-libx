package strlib;

/**
 * Общий каркас форматтеров (шаблонный метод): разбор шаблона, обработка
 * экранированных скобок и сборка результата. Подклассы определяют только,
 * как по ключу из скобок найти значение.
 *
 * @param <A> тип набора аргументов
 */
public abstract class AbstractFormatter<A> implements StringFormatter<A> {

    /**
     * {@inheritDoc}
     *
     * <p>Один проход по char[] шаблона: обычные символы копируются, удвоенная скобка
     * даёт одну скобку, конструкция от открывающей до закрывающей скобки
     * передаётся в {@link #resolve(String, Object)}.
     */
    @Override
    public final String format(String template, A args) {
        Args.notNull(template, "template");
        Args.notNull(args, "args");
        char[] t = template.toCharArray();
        StringBuilder out = new StringBuilder(t.length + 16);
        int i = 0;
        while (i < t.length) {
            char c = t[i];
            if (c == '{') {
                if (i + 1 < t.length && t[i + 1] == '{') {
                    out.append('{');
                    i += 2;
                    continue;
                }
                int end = i + 1;
                while (end < t.length && t[end] != '}') {
                    end++;
                }
                if (end == t.length) {
                    throw new FormatException("Незакрытая '{' в позиции " + i);
                }
                StringBuilder key = new StringBuilder();
                for (int j = i + 1; j < end; j++) {
                    key.append(t[j]);
                }
                out.append(resolve(key.toString(), args));
                i = end + 1;
            } else if (c == '}') {
                if (i + 1 < t.length && t[i + 1] == '}') {
                    out.append('}');
                    i += 2;
                } else {
                    throw new FormatException("Непарная '}' в позиции " + i);
                }
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }

    /**
     * Находит значение для placeholder-а.
     *
     * @param key  текст между скобками
     * @param args набор аргументов
     * @return значение (null выводится как «null»)
     * @throws FormatException если ключ некорректен или значения нет
     */
    protected abstract Object resolve(String key, A args);
}