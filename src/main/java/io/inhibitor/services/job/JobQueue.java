package io.inhibitor.services.job;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class JobQueue implements Job {

  private static final int MIN_THREADS = 8;
  private static final int MAX_THREADS = 16;

  private final ThreadPoolExecutor threadPoolExecutor;

  public JobQueue() {
    this.threadPoolExecutor = new ThreadPoolExecutor(
        MIN_THREADS,
        MAX_THREADS,
        5L,
        TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );
  }

  @Override
  public CompletableFuture<Void> run(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, threadPoolExecutor);
  }

  @Override
  public <T> CompletableFuture<T> supply(Supplier<T> supplier) {
    return CompletableFuture.supplyAsync(supplier, threadPoolExecutor);
  }
}
