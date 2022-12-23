package io.inhibitor.services.job;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.inject.Inject;

public class JobQueue implements Job {

  private static final int MIN_THREADS = 16;
  private static final int MAX_THREADS = 32;

  private final ThreadPoolExecutor threadPoolExecutor;

  private final Meter runMeter;

  @Inject
  public JobQueue(
      MetricRegistry metricRegistry
  ) {
    this.threadPoolExecutor = new ThreadPoolExecutor(
        MIN_THREADS,
        MAX_THREADS,
        5L,
        TimeUnit.SECONDS,
        new ArrayBlockingQueue<>(1_048_576),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    this.runMeter = metricRegistry.meter(MetricRegistry.name(JobQueue.class, "run"));

    metricRegistry.register(
        MetricRegistry.name(JobQueue.class, "thread-pool", "active-threads"),
        (Gauge<Integer>) threadPoolExecutor::getActiveCount
    );

    metricRegistry.register(
        MetricRegistry.name(JobQueue.class, "thread-pool", "completed-tasks"),
        (Gauge<Long>) threadPoolExecutor::getCompletedTaskCount
    );
  }

  @Override
  public CompletableFuture<Void> run(Runnable runnable) {
    runMeter.mark();
    return CompletableFuture.runAsync(runnable, threadPoolExecutor);
  }

  @Override
  public <T> CompletableFuture<T> supply(Supplier<T> supplier) {
    runMeter.mark();
    return CompletableFuture.supplyAsync(supplier, threadPoolExecutor);
  }
}
