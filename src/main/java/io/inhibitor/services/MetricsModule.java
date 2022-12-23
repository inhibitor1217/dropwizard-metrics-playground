package io.inhibitor.services;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.dropwizard.setup.Environment;

public class MetricsModule extends AbstractModule {

  @Provides
  @Singleton
  public MetricRegistry provideMetricRegistry(Environment environment) {
    return environment.metrics();
  }
}
