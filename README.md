# String Library

Учебный проект (Java 21, Maven, JUnit 5): собственная реализация строковых операций.
Из String используются только length, charAt, toCharArray.

## Запуск
- Тесты: `mvn test`
- Демонстрация: запустить класс `strlib.Main`

## Модель
| Элемент | Тип | Назначение |
|---|---|---|
| StringUtils | utility-класс | все операции |
| StringFormatter<A> | интерфейс | format(template, args) |
| AbstractFormatter<A> | абстрактный класс | общий разбор шаблона |
| PositionalFormatter | класс | {0}, {1} |
| NamedFormatter | класс | {name} |
| SplitMode | enum | KEEP_EMPTY / SKIP_EMPTY |
| BenchmarkResult | record | результат замера |
| ConcatBenchmark | utility-класс | замер конкатенации |
| StringLibException, InvalidArgumentException, FormatException | исключения | ошибки библиотеки |

## Операции
split (по символу и подстроке), join, replace, replaceFirst, indexOf, lastIndexOf,
startsWith, endsWith, trim, equalsIgnoreCase, repeat, padLeft, padRight, reverse.

## Три ловушки
1. Суррогатные пары: ... (объясните своими словами, почему наивный reverse ломает эмодзи)
2. Турецкая «i»: ...
3. Конкатенация в цикле: ... (вставьте ваши цифры замера из Main и объяснение O(n²) против O(n))

## Тесты
Опишите, что покрыто: границы, исключения, equals/hashCode, закрытый конструктор.