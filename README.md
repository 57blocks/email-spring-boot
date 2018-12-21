# Email Spring Boot Starter
Configure an email service ready for sending emails. Supporting templating with [thymeleaf](https://www.thymeleaf.org/).

## Getting Started
### Add the Starter in Maven Dependency
Edit `pom.xml`, add the starter:
```xml
<dependency>
  <groupId>io.57blocks</groupId>
  <artifactId>email-spring-boot-starter</artifactId>
  <version>${io.57blocks.email.version}</version>
</dependency>
```
### Configure the JavaMailSender
By default, this starter can benefit from `spring-boot-email-starter` by using `SMTP` protocol 
or `JNDI` to sending out emails, without other dependencies.

#### Default `spring-boot-email-starter` Configuration
Edit `application.yml`, add the following properties:
```yaml
spring.mail:
  host: email-smtp.us-west-2.amazonaws.com
  username: username
  password: password
  properties:
    mail.transport.protocol: smtp
    mail.smtp.port: 25
    mail.smtp.auth: true
    mail.smtp.starttls.enable: true
    mail.smtp.starttls.required: true
```
#### AWS SES Integration
If you want to use AWS SES sdk to send email via `http(s)` protocol, you need to import some additional
dependencies:
```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-aws</artifactId>
    <version>2.0.1.RELEASE</version>
  </dependency>
  <dependency>
    <groupId>com.amazonaws</groupId>
    <artifactId>aws-java-sdk-ses</artifactId>
    <version>1.11.415</version>
  </dependency>
</dependencies>
```

Then update the configuration by removing `spring.mail` and adding AWS cloud basic configuration:
```yaml
cloud:
  aws:
    region:
      static: us-east-2
    credentials:
      accessKey: accessKey
      secretKey: secretKey
```

### Create Email Template

Default layout in `/src/main/resources`:

```
email/
  text/
    greeting.txt
  html/
    greeting.html
  subject/
    greeting.txt
  messages.properties
  messages_zh.properties
```

To configure template file layout, edit `application.yml`:

```yaml
io.57blocks.email:
  enabled: true
  template:
    prefix: /email/
    message_base_name: messages
    html:
      pattern: html/*
      suffix: .html
      character_encoding: UTF-8
      cacheable: false
    text:
      pattern: text/*
      suffix: .txt
      character_encoding: UTF-8
      cacheable: false
    subject:
      pattern: subject/*
      suffix: .txt
      character_encoding: UTF-8
      cacheable: false
```

### Send Mail

To send email, inject `EmailService` into the bean.

```java
public class GreetingService {

  @Autowired
  private EmailService emailService;

  public void sendEmail() {
    try {
      Map<String, Object> ctx = ImmutableMap.of("name", "Mr. Smith");
      emailService.sendHtmlMail("Sender <sender@somecompany.com>", "html/greeting", Locale.CHINESE, ctx, "Mr. Smith <smith@somedomain.com>");
    } catch (MessagingException e) {
      // ignored
    }
  }
}
```

### Example
- [spring-mail-example](./email-spring-boot-example/spring-mail-example): build on `spring-boot-mail-starter`.
- [aws-ses-example](./email-spring-boot-example/aws-ses-example): build on `spring-cloud-aws-starter` and `aws-sdk-java-ses`.





