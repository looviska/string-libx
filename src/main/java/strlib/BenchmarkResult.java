package strlib;

/**
 * Результат замера: конкатенация в цикле против StringBuilder.
 * Неизменяемое значение; equals, hashCode и toString генерирует компилятор.
 *
 * @param iterations   число итераций цикла
 * @param concatNanos  время цикла со s += ..., нс
 * @param builderNanos время цикла со StringBuilder.append, нс
 * @param resultLength длина итоговой строки
 */
public record BenchmarkResult(int iterations, long concatNanos, long builderNanos, int resultLength) {

    /**
     * Проверяет инварианты значения.
     *
     * @throws InvalidArgumentException если iterations не положительно или время отрицательно
     */
    public BenchmarkResult {
        if (iterations <= 0) {
            throw new InvalidArgumentException("iterations должно быть > 0: " + iterations);
        }
        if (concatNanos < 0 || builderNanos < 0) {
            throw new InvalidArgumentException("Время не может быть отрицательным");
        }
    }

    /**
     * @return во сколько раз конкатенация медленнее StringBuilder
     *         (бесконечность, если время билдера равно нулю)
     */
    public double ratio() {
        return builderNanos == 0 ? Double.POSITIVE_INFINITY : (double) concatNanos / builderNanos;
    }
}
