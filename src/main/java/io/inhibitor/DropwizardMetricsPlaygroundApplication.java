package io.inhibitor;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.inhibitor.services.job.JobModule;
import io.inhibitor.services.metrics.MetricsModule;
import ru.vyarus.dropwizard.guice.GuiceBundle;

public class DropwizardMetricsPlaygroundApplication extends
    Application<DropwizardMetricsPlaygroundConfiguration> {

  public static void main(String[] args) throws Exception {
    new DropwizardMetricsPlaygroundApplication().run(args);
  }

  @Override
  public String getName() {
    return "dropwizard-metrics-playground";
  }

  @Override
  public void initialize(Bootstrap<DropwizardMetricsPlaygroundConfiguration> bootstrap) {
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor(false)));

    bootstrap.addBundle(
        GuiceBundle.builder()
            .enableAutoConfig("io.inhibitor.resources", "io.inhibitor.services")
            .modules(
                new JobModule(),
                new MetricsModule()
            )
            .build()
    );
  }

  @Override
  public void run(DropwizardMetricsPlaygroundConfiguration dropwizardMetricsPlaygroundConfiguration,
      Environment environment) throws Exception {
  }
}
