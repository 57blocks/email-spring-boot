package io._57blocks.email;

import io._57blocks.email.config.properties.EmailServiceProperties;
import io._57blocks.email.params.Attachment;
import io._57blocks.email.params.Message;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final TemplateEngine templateEngine;
  private final EmailServiceProperties properties;

  public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine,
      EmailServiceProperties properties) {
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
    this.properties = properties;
  }

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
      Map<String, Object> ctx,
      String... recipientEmails) throws MessagingException {
    sendTextEmailWithAttachments(fromEmail, template, locale, ctx, null, recipientEmails);
  }

  @Override
  public void sendTextEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException {

    MimeMessage attachmentMimeMessage = createMimeMessage(fromEmail, template, locale,
        ctx, recipientEmails, attachments, false);

    sendMail(attachmentMimeMessage);
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

    MimeMessage attachmentMimeMessage = createMimeMessage(fromEmail, template, locale,
        ctx, recipientEmails, attachments, true);

    sendMail(attachmentMimeMessage);
  }

  public void sendMail(MimeMessage mimeMessage) {
    this.mailSender.send(mimeMessage);
  }

  private MimeMessage createMimeMessage(String fromEmail, String template,
      Locale locale,
      Map<String, Object> vars, String[] recipientEmails, Attachment[] attachments, boolean isHtml)
      throws MessagingException {

    final boolean hasAttachments = attachments != null && attachments.length > 0;
    final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    final MimeMessageHelper messageHelper =
        new MimeMessageHelper(mimeMessage, hasAttachments, "UTF-8");

    // Prepare context variables
    final Context ctx = new Context(locale);
    vars.forEach(ctx::setVariable);

    // Set from and to addresses, format can be: Mr. Smith <smith@email.com> or smith@email.com
    messageHelper.setFrom(fromEmail);
    messageHelper.setTo(recipientEmails);

    // Create subject using Thymeleaf
    String subjectTemplate = getSubjectTemplate(template);
    final String subject = this.templateEngine.process(subjectTemplate, ctx);
    messageHelper.setSubject(subject);

    // Create the HTML body using Thymeleaf
    final String content = this.templateEngine.process(getContentTemplate(template, isHtml), ctx);
    messageHelper.setText(content, isHtml);

    if (hasAttachments) {
      processAttachments(messageHelper, attachments);
    }

    return mimeMessage;
  }

  private void processAttachments(MimeMessageHelper messageHelper, Attachment[] attachments)
      throws MessagingException {
    if (attachments == null || attachments.length == 0) {
      return;
    }

    for (Attachment attachment : attachments) {
      final InputStreamSource attachmentSource = new ByteArrayResource(
          attachment.getAttachmentBytes());
      messageHelper
          .addAttachment(attachment.getFilename(), attachmentSource, attachment.getContentType());
    }
  }


  private String getSubjectTemplate(String template) {

    String subjectPattern = properties.getTemplate().getSubject().getPattern();
    return concatWithPrefix(subjectPattern, template);
  }

  private String getContentTemplate(String template, boolean isHtml) {

    String templatePattern = properties.getTemplate().getText().getPattern();
    if (isHtml) {
      templatePattern = properties.getTemplate().getHtml().getPattern();
    }

    return concatWithPrefix(templatePattern, template);
  }

  private String concatWithPrefix(String prefix, String template) {
    if (prefix.indexOf('/') > 0) {
      prefix = prefix.substring(0, prefix.indexOf('/'));
    }
    return prefix + "/" + template;
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
