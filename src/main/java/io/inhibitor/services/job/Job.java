package io.inhibitor.services.job;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public interface Job {
  public CompletableFuture<Void> run(Runnable runnable);
  public <T> CompletableFuture<T> supply(Supplier<T> supplier);
}
