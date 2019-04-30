package com.notification.service.email;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.notification.bean.EmailRequest;
import com.notification.exception.NotificationException;

public class EmailNotificationServiceTest {

	private static final String TEST_PROPERTIES_FILE = "application-test.properties";

	private EmailNotificationService emailService;

	private Properties properties;

	@Before
	public void setup() throws IOException {
		properties = new Properties();
		properties.load(EmailNotificationServiceTest.class.getClassLoader().getResourceAsStream(TEST_PROPERTIES_FILE));

		JavaMailSenderImpl emailSender = new JavaMailSenderImpl();
		emailSender.setHost(properties.getProperty("email.host"));
		emailSender.setPort(Integer.parseInt(properties.getProperty("email.port")));
		emailSender.setUsername(properties.getProperty("email.username"));
		emailSender.setPassword(properties.getProperty("email.password"));

		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtp.auth", "true");
		prop.setProperty("mail.smtp.starttls.enable", "true");
		prop.setProperty("mail.debug", "true");

		emailSender.setJavaMailProperties(prop);
		emailService = new EmailNotificationService(emailSender);
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenFromIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setTo("xyz@test.com");
		request.setSubject("test");
		emailService.sendNotification(request);
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenToIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom("xyz@test.com");
		request.setSubject("test");
		emailService.sendNotification(request);
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenSubjectIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom("srvcacct007@gmail.com");
		request.setTo("pandoh.2007@gmail.com");
		request.setMessage("test message");
		emailService.sendNotification(request);
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenMessageIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom("srvcacct007@gmail.com");
		request.setTo("pandoh.2007@gmail.com");
		request.setSubject("test");
		emailService.sendNotification(request);
	}

	@Test(expected = Test.None.class)
	public void testSendNotification_whenNoMissingField() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom("servacct007@gmail.com");
		request.setTo("pandoh.2007@gmail.com");
		request.setSubject("test");
		request.setMessage("Hi This is test message");
		emailService.sendNotification(request);
	}
}
