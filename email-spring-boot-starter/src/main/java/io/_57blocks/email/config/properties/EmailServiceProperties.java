package io._57blocks.email.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@Data
@ConfigurationProperties("io.57blocks.email")
public class EmailServiceProperties {

  private Boolean enabled = Boolean.TRUE;
  private TemplateProperties template = new TemplateProperties();

  @Data
  public static class TemplateProperties {

    private String prefix = "/email/";
    private String messageBaseName = "messages";
    private TemplateResolverProperties html = new TemplateResolverProperties(1, "html/*", ".html");
    private TemplateResolverProperties text = new TemplateResolverProperties(2, "text/*", ".txt");
    private TemplateResolverProperties subject = new TemplateResolverProperties(3, "subject/*",
        ".txt");

    public String getFullMessageBaseName() {
      return StringUtils.applyRelativePath(prefix, messageBaseName);
    }
  }

  @Data
  public static class TemplateResolverProperties {

    private Integer order;
    private String pattern;
    private String suffix;
    private String characterEncoding = "UTF-8";
    private Boolean cacheable = Boolean.FALSE;

    public TemplateResolverProperties(Integer order, String pattern, String suffix) {
      this.order = order;
      this.pattern = pattern;
      this.suffix = suffix;
    }
  }
}
