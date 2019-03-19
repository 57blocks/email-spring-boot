package io._57blocks.email;

import io._57blocks.email.params.Attachment;
import io._57blocks.email.params.Message;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public class DummyEmailServiceImpl implements EmailService {

  private static final String HTML_EMAIL = "HTML";
  private static final String TEXT_EMAIL = "Text";

  @Override
  public void sendTextEmail(Message message) throws MessagingException {
    if (message == null) {
      throw new IllegalArgumentException("Message can not be null.");
    }

    String[] recipients = getRecipients(message);
    Attachment[] attachments = getAttachments(message);

    sendTextEmailWithAttachments(message.getFrom(), message.getTemplate(), message.getLocale(),
        message.getContext(), attachments,
        recipients);
  }

  @Override
  public void sendTextEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String... recipientEmails) throws MessagingException {
    sendHtmlEmailWithAttachments(fromEmail, template, locale, ctx, null, recipientEmails);
  }

  @Override
  public void sendTextEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException {
    printLog(fromEmail, template, locale, ctx, HTML_EMAIL, attachments,
        recipientEmails);
  }

  @Override
  public void sendHtmlEmail(Message message) throws MessagingException {
    if (message == null) {
      throw new IllegalArgumentException("Message can not be null.");
    }

    String[] recipients = getRecipients(message);
    Attachment[] attachments = getAttachments(message);

    sendHtmlEmailWithAttachments(message.getFrom(), message.getTemplate(), message.getLocale(),
        message.getContext(), attachments,
        recipients);
  }

  @Override
  public void sendHtmlEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String... recipientEmails) throws MessagingException {
    sendHtmlEmailWithAttachments(fromEmail, template, locale, ctx, null, recipientEmails);
  }

  @Override
  public void sendHtmlEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException {
    printLog(fromEmail, template, locale, ctx, TEXT_EMAIL, attachments,
        recipientEmails);
  }

  private void printLog(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, String emailType, Attachment[] attachments,
      String[] recipientEmails) {

    log.warn("Currently email is not sent out due to application is using dummy EmailService. "
        + "If you want to send email out, please set 'io.57blocks.email.enabled=true'.");

    log.info("From: '{}' send {} email to: '{}', using template: '{}' with locale: '{}'", fromEmail,
        emailType,
        recipientEmails, template, locale);
    log.info("=====================");
    log.info("Variables: {}", ctx);
    log.info("Attachments: {}", extractAttachmentNames(attachments));
    log.info("=====================");

  }

  private String[] extractAttachmentNames(Attachment[] attachments) {
    List<String> attachmentNames = new ArrayList<>();
    if (attachments != null) {
      attachmentNames = Arrays.asList(attachments).stream()
          .map(Attachment::getFilename)
          .collect(Collectors.toList());
    }
    return attachmentNames.toArray(new String[attachmentNames.size()]);
  }

  private Attachment[] getAttachments(Message message) {
    List<Attachment> attachments = message.getAttachments();
    if (CollectionUtils.isEmpty(attachments)) {
      return null;
    }

    return attachments.toArray(new Attachment[attachments.size()]);
  }

  private String[] getRecipients(Message message) {
    List<String> recipients = message.getRecipients();
    if (CollectionUtils.isEmpty(recipients)) {
      throw new IllegalArgumentException("Recipients can not be empty.");
    }

    return recipients.toArray(new String[recipients.size()]);
  }
}
