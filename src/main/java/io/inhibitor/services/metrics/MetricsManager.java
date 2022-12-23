package io.inhibitor.services.metrics;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.ScheduledReporter;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Slf4jReporter.LoggingLevel;
import io.dropwizard.lifecycle.Managed;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsManager implements Managed {

  private final ScheduledReporter reporter;

  @Inject
  public MetricsManager(MetricRegistry metricRegistry) {
    this.reporter = Slf4jReporter.forRegistry(metricRegistry)
        .outputTo(log)
        .withLoggingLevel(LoggingLevel.INFO)
        .convertRatesTo(TimeUnit.SECONDS)
        .convertDurationsTo(TimeUnit.MILLISECONDS)
        .build();
  }

  @Override
  public void start() {
    reporter.start(10, TimeUnit.SECONDS);
  }

  @Override
  public void stop() {
    reporter.stop();
  }
}
