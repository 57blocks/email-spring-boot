package io._57blocks.email;

import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;

public interface EmailService {

  void sendTextEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String... recipientEmails)
      throws MessagingException;

  void sendTextEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException;

  void sendHtmlEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String... recipientEmails)
      throws MessagingException;

  void sendHtmlEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException;
}
