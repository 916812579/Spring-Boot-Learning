package com.example.demo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ApplicationTests {
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired  
	private Configuration configuration; //freeMarker configuration  
	
	@Test
	public void sendSimpleMail() throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("916812579@qq.com");
		message.setTo("916812579@qq.com");
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容");
		mailSender.send(message);
	}
	
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
}
