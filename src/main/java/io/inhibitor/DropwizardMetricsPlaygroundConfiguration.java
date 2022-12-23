package io.inhibitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class DropwizardMetricsPlaygroundConfiguration extends Configuration {

  @JsonProperty
  @NotEmpty
  private String applicationName = "Dropwizard Metrics Playground";
}
