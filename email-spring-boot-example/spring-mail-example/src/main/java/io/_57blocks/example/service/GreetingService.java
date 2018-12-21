package io._57blocks.example.service;

import io._57blocks.email.EmailService;
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
          .sendHtmlEmail("57blocks <57blocks@tokenpad.io>", "html/greeting", Locale.ENGLISH, ctx,
              "Yongzhi Yang <yongzhi.yang@57blocks.io>");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  public void sendTextEmail() {
    try {
      Map<String, Object> ctx = Collections.singletonMap("name", "Mr. Smith");
      emailService
          .sendTextEmail("57blocks <57blocks@tokenpad.io>", "text/greeting", Locale.ENGLISH, ctx,
              "Yongzhi Yang <yongzhi.yang@57blocks.io>");
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }
}
