package io._57blocks.example;

import io._57blocks.example.service.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SesExampleApplication {

  public static void main(String[] args) {
    SpringApplication.run(SesExampleApplication.class, args);
  }


  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext context) {
    return args -> {
      GreetingService greetingService = context.getBean(GreetingService.class);

      // send by html template
      greetingService.sendHtmlEmail();

      // send by text template
      greetingService.sendTextEmail();

      // send by text template in message format
      greetingService.sendTextEmailByMessage();

    };
  }
}
