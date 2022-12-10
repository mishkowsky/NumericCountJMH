import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.function.Function;

import static java.lang.Math.PI;
import static org.apache.commons.math3.util.Precision.round;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.spbstu.aleksandrov.NumericalCount.countIntegral;
import static org.spbstu.aleksandrov.NumericalCount.countIntegralParallel;

public class NumericalCountTest {

    private static final int threads = Runtime.getRuntime().availableProcessors();

    private final double a = 0;
    private final double b = 1;
    private final Function<Double, Double> piIntegral = (x) -> 4 / (1 + x * x);

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7})
    public void countIntegralTest(int precision) {
        assertEquals(round(PI, precision),
                countIntegral(a, b, precision, piIntegral, true));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    public void countIntegralParallelTest(int precision) {
        assertEquals(round(PI, precision),
                countIntegralParallel(a, b, precision, threads, piIntegral, threads));
    }
}
