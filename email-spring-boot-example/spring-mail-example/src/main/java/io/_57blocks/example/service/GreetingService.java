package io._57blocks.example.service;

import io._57blocks.email.EmailService;
import io._57blocks.email.params.Message;
import io._57blocks.email.params.Message.MessageBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

  private final EmailService emailService;

  public GreetingService(EmailService emailService) {
    this.emailService = emailService;
  }

  public void sendHtmlEmail() {
    try {
      Map<String, Object> ctx = Collections.singletonMap("name", "Mr. Smith");
      emailService
          .sendHtmlEmail("57blocks <57blocks@tokenpad.io>", "greeting", Locale.ENGLISH, ctx,
              "Yongzhi Yang <yongzhi.yang@57blocks.io>");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public void sendTextEmail() {
    try {
      Map<String, Object> ctx = Collections.singletonMap("name", "Mr. Smith");
      emailService
          .sendTextEmail("57blocks <57blocks@tokenpad.io>", "greeting", Locale.ENGLISH, ctx,
              "Yongzhi Yang <yongzhi.yang@57blocks.io>");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public void sendTextEmailByMessage() {
    try {
      Map<String, Object> ctx = Collections.singletonMap("name", "Mr. Smith");

      Message message = new MessageBuilder()
          .from("57blocks <57blocks@tokenpad.io>")
          .template("greeting")
          .context(ctx)
          .recipients(Arrays.asList("Yongzhi Yang <yongzhi.yang@57blocks.io>"))
          .build();

      emailService
          .sendTextEmail(message);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
