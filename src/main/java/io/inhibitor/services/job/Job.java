package io.inhibitor.services.job;

import java.util.concurrent.CompletableFuture;

public interface Job {
  public CompletableFuture<Void> run(Runnable runnable);
}
