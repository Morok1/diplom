//package oldapi.cachesize;
//
//
//
//
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.runner.Runner;
//import org.openjdk.jmh.runner.RunnerException;
//import org.openjdk.jmh.runner.options.Options;
//import org.openjdk.jmh.runner.options.OptionsBuilder;
//
//import java.util.concurrent.TimeUnit;
//
//
//@State(Scope.Thread)
//@OutputTimeUnit(TimeUnit.NANOSECONDS)
//@BenchmarkMode(Mode.AverageTime)
//public class CacheL1 {
//
//    public static final int SIZE = 2 * 1024; // double[SIZE] ~ 16K
//
//    private double[] array;
//    private int[] ordered;
//    private int[] shuffled;
//
//    @Setup
//    public void init() {
//        array = Utils.newRandomDoubleArray(SIZE);
//        ordered = new int[SIZE];
//        for (int i = 0; i < ordered.length; i++) {
//            ordered[i] = i;
//        }
//        shuffled = Utils.shuffledCopyOf(ordered);
//    }
//
//    @Benchmark
//    @OperationsPerInvocation(SIZE)
//    public double ordered1() {
//        return Sums.sum1(array, ordered);
//    }
//
//    @Benchmark
//    @OperationsPerInvocation(SIZE)
//    public double ordered2() {
//        return Sums.sum2(array, ordered);
//    }
//
//    @Benchmark
//    @OperationsPerInvocation(SIZE)
//    public double ordered4() {
//        return Sums.sum4(array, ordered);
//    }
//
//    @Benchmark
//    @OperationsPerInvocation(SIZE)
//    public double shuffled1() {
//        return Sums.sum1(array, shuffled);
//    }
//
//    @Benchmark
//    @OperationsPerInvocation(SIZE)
//    public double shuffled2() {
//        return Sums.sum2(array, shuffled);
//    }
//
//    @Benchmark
//    @OperationsPerInvocation(SIZE)
//    public double shuffled4() {
//        return Sums.sum4(array, shuffled);
//    }
//
//    public static void main(String[] args) throws RunnerException {
//        Options opt = new OptionsBuilder()
//                //.mode(Mode.Throughput)
//                .mode(Mode.AverageTime)
//                .build();
//        new Runner(opt).run();
//    }
//
//}
//
