package io._57blocks.email.config;

import io._57blocks.email.DummyEmailServiceImpl;
import io._57blocks.email.EmailService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(EmailService.class)
@ConditionalOnProperty(prefix = "io.57blocks.email", name = "enabled", havingValue = "false")
public class DummyEmailServiceAutoConfig {

  @Bean
  public EmailService emailService() {
    return new DummyEmailServiceImpl();
  }

}
