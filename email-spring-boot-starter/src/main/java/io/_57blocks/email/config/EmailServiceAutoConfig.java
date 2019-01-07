package io._57blocks.email.config;

import io._57blocks.email.EmailService;
import io._57blocks.email.EmailServiceImpl;
import io._57blocks.email.config.properties.EmailServiceProperties;
import io._57blocks.email.config.properties.EmailServiceProperties.TemplateResolverProperties;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
@ConditionalOnMissingBean(EmailService.class)
@ConditionalOnBean(JavaMailSender.class)
@AutoConfigureAfter(DummyEmailServiceAutoConfig.class)
@EnableConfigurationProperties(EmailServiceProperties.class)
public class EmailServiceAutoConfig {

  @Autowired
  private EmailServiceProperties properties;

  @Autowired
  private JavaMailSender mailSender;

  private ResourceBundleMessageSource emailMessageSource() {
    final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(properties.getTemplate().getFullMessageBaseName());

    return messageSource;
  }

  private TemplateEngine emailTemplateEngine() {
    final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
    // Resolver for TEXT emails
    templateEngine.addTemplateResolver(textTemplateResolver());
    // Resolver for HTML emails (except the editable one)
    templateEngine.addTemplateResolver(htmlTemplateResolver());
    // Resolver for Subject
    templateEngine.addTemplateResolver(subjectTemplateResolver());
    // Message source, internationalization specific to emails
    templateEngine.setTemplateEngineMessageSource(emailMessageSource());

    return templateEngine;
  }

  private ITemplateResolver textTemplateResolver() {
    return createTemplateResolver(properties.getTemplate().getText(), TemplateMode.TEXT);
  }

  private ITemplateResolver htmlTemplateResolver() {
    return createTemplateResolver(properties.getTemplate().getHtml(), TemplateMode.HTML);
  }

  private ITemplateResolver subjectTemplateResolver() {
    return createTemplateResolver(properties.getTemplate().getSubject(), TemplateMode.TEXT);
  }

  private ITemplateResolver createTemplateResolver(TemplateResolverProperties props,
      TemplateMode templateMode) {
    final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
    templateResolver.setOrder(props.getOrder());
    templateResolver.setResolvablePatterns(Collections.singleton(props.getPattern()));
    templateResolver.setPrefix(properties.getTemplate().getPrefix());
    templateResolver.setSuffix(props.getSuffix());
    templateResolver.setTemplateMode(templateMode);
    templateResolver.setCharacterEncoding(props.getCharacterEncoding());
    templateResolver.setCacheable(props.getCacheable());
    return templateResolver;
  }

  @Bean
  @ConditionalOnMissingBean
  public EmailService htmlEmailService() {
    return new EmailServiceImpl(mailSender, emailTemplateEngine(), properties);
  }
}
