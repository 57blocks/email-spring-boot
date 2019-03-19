package io._57blocks.email;

import io._57blocks.email.params.Attachment;
import io._57blocks.email.params.Message;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;

public interface EmailService {

  void sendTextEmail(Message message) throws MessagingException;

  void sendTextEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String... recipientEmails)
      throws MessagingException;

  void sendTextEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException;

  void sendHtmlEmail(Message message) throws MessagingException;

  void sendHtmlEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String... recipientEmails)
      throws MessagingException;

  void sendHtmlEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException;
}
