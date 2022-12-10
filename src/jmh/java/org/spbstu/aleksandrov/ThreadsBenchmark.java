package org.spbstu.aleksandrov;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 5)
public class ThreadsBenchmark {

    @Param({"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"})
    private int threads = 2;

    private final Function<Double, Double> piIntegral = (x) -> 4 / (1 + x * x);

    @Benchmark
    public void threads(Blackhole bh) {
        int precision = 5;
        double a = 0d;
        double b = 1d;
        double result = NumericCount.countIntegralParallel(
                a, b, precision, threads, piIntegral, threads);
        bh.consume(result);
    }
}
