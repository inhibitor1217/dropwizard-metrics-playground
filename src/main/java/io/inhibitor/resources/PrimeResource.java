package io.inhibitor.resources;

import io.inhibitor.behaviors.PrimeBehavior;
import java.util.List;
import java.util.stream.IntStream;
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

  @Inject
  public PrimeResource(PrimeBehavior primeBehavior) {
    this.primeBehavior = primeBehavior;
  }

  @GET
  public List<Integer> getPrimes(
      @QueryParam("length") @NonNull Integer length
  ) {
    return IntStream.range(0, length)
        .map(i -> i * 1000)
        .map(primeBehavior::findSmallestPrimeAfter)
        .boxed()
        .toList();
  }
}
