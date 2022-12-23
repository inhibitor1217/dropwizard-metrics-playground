package io.inhibitor.resources;

import io.inhibitor.DropwizardMetricsPlaygroundConfiguration;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/ping")
public class PingResource {

  private final DropwizardMetricsPlaygroundConfiguration configuration;

  @Inject
  PingResource(
      DropwizardMetricsPlaygroundConfiguration configuration
  ) {
    this.configuration = configuration;
  }

  @GET
  public String ping() {
    return String.format("%s is up and running!", configuration.getApplicationName());
  }
}
