package io.inhibitor.services;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.MetricRegistry;
import io.dropwizard.lifecycle.Managed;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class MetricsManager implements Managed {

  private final ConsoleReporter consoleReporter;

  @Inject
  public MetricsManager(MetricRegistry metricRegistry) {
    this.consoleReporter = ConsoleReporter.forRegistry(metricRegistry)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
  }

  @Override
  public void start() {
    consoleReporter.start(1, TimeUnit.SECONDS);
  }

  @Override
  public void stop() {
    consoleReporter.stop();
  }
}
