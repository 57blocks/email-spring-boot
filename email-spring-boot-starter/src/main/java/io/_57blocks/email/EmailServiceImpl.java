package io._57blocks.email;

import io._57blocks.email.config.properties.EmailServiceProperties;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
  public void sendTextEmail(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx,
      String... recipientEmails) throws MessagingException {
    sendTextEmailWithAttachments(fromEmail, template, locale, ctx, null, recipientEmails);
  }

  @Override
  public void sendTextEmailWithAttachments(String fromEmail, String template, Locale locale,
      Map<String, Object> ctx, Attachment[] attachments, String... recipientEmails)
      throws MessagingException {

    MimeMessage attachmentMimeMessage = createMimeMessage(fromEmail, template,
        this::getTextSubjectTemplate, locale,
        ctx, recipientEmails, attachments, false);

    sendMail(attachmentMimeMessage);
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

    MimeMessage attachmentMimeMessage = createMimeMessage(fromEmail, template,
        this::getHtmlSubjectTemplate, locale,
        ctx, recipientEmails, attachments, true);

    sendMail(attachmentMimeMessage);
  }

  public void sendMail(MimeMessage mimeMessage) {
    this.mailSender.send(mimeMessage);
  }

  private MimeMessage createMimeMessage(String fromEmail, String template,
      Function<String, String> subjectTemplateFunction,
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
    String subjectTemplate = subjectTemplateFunction.apply(template);
    final String subject = this.templateEngine.process(subjectTemplate, ctx);
    messageHelper.setSubject(subject);

    // Create the HTML body using Thymeleaf
    final String htmlContent = this.templateEngine.process(template, ctx);
    messageHelper.setText(htmlContent, isHtml);

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

  private String getSubjectTemplate(String template, String contentPattern,
      String subjectPattern) {
    contentPattern = contentPattern.substring(0, contentPattern.indexOf('/'));
    subjectPattern = subjectPattern.substring(0, subjectPattern.indexOf('/'));

    return template.replace(contentPattern, subjectPattern);
  }

  private String getHtmlSubjectTemplate(String template) {

    String htmlPattern = properties.getTemplate().getHtml().getPattern();
    String subjectPattern = properties.getTemplate().getSubject().getPattern();

    return getSubjectTemplate(template, htmlPattern, subjectPattern);
  }

  private String getTextSubjectTemplate(String template) {

    String textPattern = properties.getTemplate().getText().getPattern();
    String subjectPattern = properties.getTemplate().getSubject().getPattern();

    return getSubjectTemplate(template, textPattern, subjectPattern);
  }
}
