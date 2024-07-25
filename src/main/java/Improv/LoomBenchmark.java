package Improv;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Fork(value = 1, jvmArgs = {"-Xms512m", "-Xmx4096m"})
@BenchmarkMode({Mode.AverageTime})
@Warmup(time = 20)
@Measurement(iterations = 10, time = 30)
@State(Scope.Benchmark)
@Timeout(time = 60)
public class LoomBenchmark {

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LoomBenchmark.class.getSimpleName())
                .forks(1)
                .shouldDoGC(false)
                .build();

        new Runner(opt).run();
    }

    //@Benchmark
    public long virtualThreadExecutor(Blackhole blackhole, ExecutionPlan plan) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            benchmarkingMethod(blackhole, plan, executor);
        }
        return System.currentTimeMillis();
    }

    @Benchmark
    public long FixedPlatformPool(Blackhole blackhole, ExecutionPlan plan) {
        try (var executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
            benchmarkingMethod(blackhole, plan, executor);
        }
        return System.currentTimeMillis();
    }

    private void benchmarkingMethod(Blackhole blackhole, ExecutionPlan plan, ExecutorService executor) {
        IntStream.range(0, 10_000).forEach(i ->
                executor.submit(() -> {
                    try {
                        String response1 =  fetchURL(URI.create("http://192.168.1.122:8080/v1/crawl/delay/" + plan.delay).toURL());
                        blackhole.consume(response1);

                        if ("2".equals(plan.numberOfCalls)) {
                            String response2 =  fetchURL(URI.create("http://192.168.1.17:8080/v1/crawl/delay/" + plan.delay).toURL());
                            blackhole.consume(response2);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
    }

    String fetchURL(URL url) throws IOException {
        try (var in = url.openStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }


}

