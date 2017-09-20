# Spring Boot中使用JavaMailSender发送邮件

- 参考：[http://blog.didispace.com/springbootmailsender/](http://blog.didispace.com/springbootmailsender/)

# 快速入门
- pom.xml中引入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

- application.properties加入如下配置

```yml
spring: 
    mail:
        host: smtp.qq.com
        username: 916812579@qq.com
        # 腾讯邮箱授权码
        password: pphhdkjvbldzsbbdg
        properties:
            mail:
                smtp:
                    auth: true
                    starttls: 
                         enable: true
                         required: true
                    ssl:
                         enable: true
                    socketFactory:
                         class: javax.net.ssl.SSLSocketFactory
                   

 
```
注意：qq邮箱可能通过密码不能正常使用java发送邮件，此时需要把密码换成授权码，授权码获取地址[http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256](http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256)
- 测试代码

```java
package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Autowired
	private JavaMailSender mailSender;
	@Test
	public void sendSimpleMail() throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("916812579@qq.com");
		message.setTo("916812579@qq.com");
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容");
		mailSender.send(message);
	}
}
```
- 由于Spring Boot的starter模块提供了自动化配置，所以在引入了spring-boot-starter-mail依赖之后，会根据配置文件中的内容去创建`JavaMailSender`实例，因此我们可以直接在需要使用的地方直接`@Autowired`来引入邮件发送对象。


# 进阶使用

## 发送附件
使用`MimeMessage`发送复杂内容的邮件
```java
@Test
	public void sendAttachmentsMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("916812579@qq.com");
		helper.setTo("916812579@qq.com");
		helper.setSubject("主题：有附件");
		helper.setText("有附件的邮件");
		// UrlResource
		FileSystemResource file = new FileSystemResource(new File("test.png"));
		helper.addAttachment("附件.jpg", file);
		mailSender.send(mimeMessage);
	}
```

## 嵌入静态资源

邮件正文中嵌入静态资源
```java
@Test
	public void sendInlineMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom("916812579@qq.com");
		helper.setTo("916812579@qq.com");
		helper.setSubject("主题：嵌入静态资源");
		helper.setText("<html><body><img src=\"cid:test\" ></body></html>", true);
		FileSystemResource file = new FileSystemResource(new File("test.png"));
		helper.addInline("test", file);
		mailSender.send(mimeMessage);
	}
```


这里需要注意的是`addInline`函数中资源名称`tes`t需要与正文中`cid:test`对应起来

## 模板邮件

使用`freemarker`发送模板邮件

- pom.xml增加如下freemarker配置

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-freemarker</artifactId>
</dependency>
```

- 在`resources/templates/`下，创建一个模板页面`template.ftl`：

```html
<html>
<body>
    <h3>你好， ${username}, 这是一封模板邮件!</h3>
</body>
</html>
```
- 发送测试代码如下

```java
@Autowired  
private Configuration configuration; //freeMarker configuration  

@Test
public void sendTemplateMail() throws Exception {
	MimeMessage mimeMessage = mailSender.createMimeMessage();
	MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
	helper.setFrom("916812579@qq.com");
	helper.setTo("916812579@qq.com");
	helper.setSubject("主题：模板邮件");
	Map<String, Object> model = new HashMap<>();
	model.put("username", "test");
	Template t = configuration.getTemplate("template.ftl"); // freeMarker template  
    String content = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);  
	helper.setText(content, true);
	mailSender.send(mimeMessage);
}
```
> 自己测试使用velocity时，竟然没有`1.5.7.RELEASE`版本的`spring-boot-starter-freemarker`