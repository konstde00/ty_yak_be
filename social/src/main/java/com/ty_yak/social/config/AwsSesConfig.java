package com.ty_yak.social.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableAutoConfiguration(exclude = {ContextStackAutoConfiguration.class})
public class AwsSesConfig {

  @Value("${aws.region}")
  private String region;

  @Bean
  @Primary
  public AmazonSimpleEmailService getAwsSesClient() {
    if (region == null) {
      throw new IllegalStateException("Environment is missing required property - AWS region");
    }

    return AmazonSimpleEmailServiceClientBuilder
            .standard()
            .withRegion(Regions.EU_CENTRAL_1)
            .build();
  }
}
