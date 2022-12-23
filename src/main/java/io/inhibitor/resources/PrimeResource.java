package io.inhibitor.resources;

import io.inhibitor.behaviors.PrimeBehavior;
import io.inhibitor.services.job.Job;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.LongStream;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import lombok.NonNull;

@Path("/prime")
@Produces(MediaType.APPLICATION_JSON)
public class PrimeResource {

  private final PrimeBehavior primeBehavior;

  private final Job job;

  @Inject
  public PrimeResource(
      PrimeBehavior primeBehavior,
      Job job
  ) {
    this.primeBehavior = primeBehavior;
    this.job = job;
  }

  @GET
  public List<Long> getPrimes(
      @QueryParam("length") @NonNull Integer length
  ) {
    final var futures = LongStream.range(0L, length)
        .map(i -> i * 1_000_000_000L)
        .mapToObj(i -> job.supply(() -> primeBehavior.findSmallestPrimeAfter(i)))
        .toList();

    CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));

    return futures.stream()
        .map(CompletableFuture::join)
        .toList();
  }
}
