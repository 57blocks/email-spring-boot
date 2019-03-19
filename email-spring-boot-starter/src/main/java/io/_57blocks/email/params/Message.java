package io._57blocks.email.params;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Message {

  private String from;
  private String template;
  private Locale locale;
  private Map<String, Object> context;
  private List<String> recipients;
  private List<Attachment> attachments;

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public Map<String, Object> getContext() {
    return context;
  }

  public void setContext(Map<String, Object> context) {
    this.context = context;
  }

  public List<String> getRecipients() {
    return recipients;
  }

  public void setRecipients(List<String> recipients) {
    this.recipients = recipients;
  }

  public List<Attachment> getAttachments() {
    return attachments;
  }

  public void setAttachments(List<Attachment> attachments) {
    this.attachments = attachments;
  }

  public static class MessageBuilder {
    private String from;
    private String template;
    private Locale locale = Locale.ENGLISH;
    private Map<String, Object> context;
    private List<String> recipients;
    private List<Attachment> attachments;

    public MessageBuilder from(String from) {
      this.from = from;
      return this;
    }

    public MessageBuilder template(String template) {
      this.template = template;
      return this;
    }

    public MessageBuilder locale(Locale locale) {
      this.locale = locale;
      return this;
    }

    public MessageBuilder context(Map<String, Object> context) {
      this.context = context;
      return this;
    }

    public MessageBuilder recipients(List<String> recipients) {
      this.recipients = recipients;
      return this;
    }

    public MessageBuilder attachments(List<Attachment> attachments) {
      this.attachments = attachments;
      return this;
    }

    public Message build() {
      Message message = new Message();

      message.setFrom(this.from);
      message.setTemplate(this.template);
      message.setLocale(this.locale);
      message.setContext(this.context);
      message.setAttachments(this.attachments);
      message.setRecipients(this.recipients);

      return message;
    }
  }
}
