package strlib;

/**
 * Замер разницы между конкатенацией в цикле и StringBuilder.
 *
 * <p>Строка неизменяема: каждое s += 'x' создаёт новую строку и копирует в неё все
 * предыдущие символы. На n итерациях это 1 + 2 + ... + n = n(n+1)/2 копирований,
 * то есть O(n²); для n = 100 000 порядка 5·10⁹ копирований. StringBuilder хранит
 * изменяемый массив с запасом ёмкости и при переполнении увеличивает его
 * геометрически, поэтому append в среднем O(1), а весь цикл — O(n).
 */
public final class ConcatBenchmark {

    private ConcatBenchmark() {
        throw new AssertionError("Утилитный класс не предназначен для создания экземпляров");
    }

    /**
     * Выполняет оба цикла и замеряет время.
     *
     * @param iterations число итераций (положительное)
     * @return результат замера
     * @throws InvalidArgumentException если iterations не положительно
     */
    public static BenchmarkResult run(int iterations) {
        if (iterations <= 0) {
            throw new InvalidArgumentException("iterations должно быть > 0: " + iterations);
        }
        long t0 = System.nanoTime();
        String s = "";
        for (int i = 0; i < iterations; i++) {
            s += 'x';
        }
        long concat = System.nanoTime() - t0;

        long t1 = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append('x');
        }
        String built = sb.toString();
        long builder = System.nanoTime() - t1;

        if (s.length() != built.length()) {
            throw new IllegalStateException("Результаты не совпали");
        }
        return new BenchmarkResult(iterations, concat, builder, built.length());
    }
}