package io.inhibitor.services.job;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.RatioGauge;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.inject.Inject;

public class JobQueue implements Job {

  private static final int MIN_THREADS = 16;
  private static final int MAX_THREADS = 32;

  private final ThreadPoolExecutor threadPoolExecutor;

  private final Meter runMeter;

  private final Meter successMeter;

  private final Meter failureMeter;

  private final Counter succeededJobsCounter;

  private final Counter failedJobsCounter;

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
    this.successMeter = metricRegistry.meter(MetricRegistry.name(JobQueue.class, "success"));
    this.failureMeter = metricRegistry.meter(MetricRegistry.name(JobQueue.class, "failure"));
    this.succeededJobsCounter = metricRegistry.counter(MetricRegistry.name(JobQueue.class, "succeeded-jobs"));
    this.failedJobsCounter = metricRegistry.counter(MetricRegistry.name(JobQueue.class, "failed-jobs"));

    metricRegistry.register(
        MetricRegistry.name(JobQueue.class, "thread-pool", "active-threads"),
        (Gauge<Integer>) threadPoolExecutor::getActiveCount
    );

    metricRegistry.register(
        MetricRegistry.name(JobQueue.class, "thread-pool", "completed-tasks"),
        (Gauge<Long>) threadPoolExecutor::getCompletedTaskCount
    );

    metricRegistry.register(
        MetricRegistry.name(JobQueue.class, "thread-pool", "core-thread-utilization"),
        new CoreThreadUtilizationGauge()
    );

    metricRegistry.register(
        MetricRegistry.name(JobQueue.class, "thread-pool", "job-success-rate"),
        new JobSuccessRateGauge()
    );
  }

  @Override
  public CompletableFuture<Void> run(Runnable runnable) {
    runMeter.mark();
    return CompletableFuture.runAsync(() -> {
      try {
        runnable.run();
        successMeter.mark();
        succeededJobsCounter.inc();
      } catch (Exception e) {
        failureMeter.mark();
        failedJobsCounter.inc();
      }
    }, threadPoolExecutor);
  }

  @Override
  public <T> CompletableFuture<T> supply(Supplier<T> supplier) {
    runMeter.mark();
    return CompletableFuture.supplyAsync(() -> {
      try {
        T result = supplier.get();
        successMeter.mark();
        succeededJobsCounter.inc();
        return result;
      } catch (Exception e) {
        failureMeter.mark();
        failedJobsCounter.inc();
        throw e;
      }
    }, threadPoolExecutor);
  }

  private class CoreThreadUtilizationGauge extends RatioGauge {
    @Override
    protected Ratio getRatio() {
      return Ratio.of(
          threadPoolExecutor.getActiveCount(),
          threadPoolExecutor.getCorePoolSize()
      );
    }
  }

  private class JobSuccessRateGauge extends RatioGauge {
    @Override
    protected Ratio getRatio() {
      return Ratio.of(
          successMeter.getCount(),
          succeededJobsCounter.getCount() + failedJobsCounter.getCount()
      );
    }
  }
}
