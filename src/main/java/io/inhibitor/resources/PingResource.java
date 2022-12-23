package io.inhibitor.resources;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import io.inhibitor.DropwizardMetricsPlaygroundConfiguration;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Path("/ping")
public class PingResource {

  private final DropwizardMetricsPlaygroundConfiguration configuration;

  private final Meter requestsMeter;

  @Inject
  PingResource(
      DropwizardMetricsPlaygroundConfiguration configuration,
      MetricRegistry metricRegistry
  ) {
    this.configuration = configuration;
    this.requestsMeter = metricRegistry
        .meter(MetricRegistry.name(PingResource.class, "requests"));
  }

  @GET
  public String ping() {
    requestsMeter.mark();
    return String.format("%s is up and running!", configuration.getApplicationName());
  }
}
