package org.spbstu.aleksandrov;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static org.apache.commons.math3.util.Precision.round;

public class NumericCount {

    public static double countIntegral(
            double a, double b, int precision, Function<Double, Double> function, boolean round) {
        long steps = 2;
        double s = 0;
        double sLast;
        do {
            steps *= 2;
            sLast = s;
            double h = (b - a) / steps; // h = (b - a) / pow(2,n);
            double x = a;
            for (int i = 0; i < steps; i++) {
                s += function.apply(x); // s = s + f(x);
                x += h; // x = x + h;
            }
            s *= h; // s = s*h;
        }
        // чтобы корректно округлить до precision цифры после запятой, нужно знать precision+1 цифру после запятой
        while (abs(s - sLast) >= pow(10, -precision - 1));
        if (round) return round(s, precision);
        else return s;
    }

    public static double countIntegralParallel(
            double a, double b, int precision, int segments,
            Function<Double, Double> piIntegral, int threadAmount) {
        double result = 0;
        double h = (b - a) / segments;
        double ai = a;
        double bi = a + h;
        List<Future<Double>> futures = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(threadAmount);
        for (int i = 0; i < segments; i++) {
            double aConst = ai;
            double bConst = bi;
            futures.add(executor.submit(() ->
                    NumericCount.countIntegral(aConst, bConst, precision + 1, piIntegral, false)));
            ai += h;
            bi += h;
        }
        for (Future<Double> future : futures) {
            try {
                result += future.get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("ERROR!");
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return round(result, precision);
    }
}
