package com.notification.service.email;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.notification.bean.EmailRequest;
import com.notification.exception.NotificationException;
import com.notification.service.INotificationService;

public class EmailNotificationServiceTest {

	private static final String TEST_PROPERTIES_FILE = "application-test.properties";

	private JavaMailSenderImpl emailSender;

	private Properties properties;

	@Before
	public void setup() throws IOException {
		properties = new Properties();
		properties.load(EmailNotificationServiceTest.class.getClassLoader().getResourceAsStream(TEST_PROPERTIES_FILE));

		emailSender = new JavaMailSenderImpl();
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
		//emailService = new EmailNotificationService(emailSender, new EmailRequest());
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenFromIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setTo("xyz@test.com");
		request.setSubject("test");
		INotificationService emailService = new EmailNotificationService(emailSender, request);
		emailService.sendNotification();
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenToIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom(properties.getProperty("email.username"));
		request.setSubject("test");
		INotificationService emailService = new EmailNotificationService(emailSender, request);
		emailService.sendNotification();
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenSubjectIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom(properties.getProperty("email.username"));
		request.setTo("xyz@gmail.com");
		request.setMessage("test message");
		INotificationService emailService = new EmailNotificationService(emailSender, request);
		emailService.sendNotification();
	}

	@Test(expected = NotificationException.class)
	public void testSendNotification_whenMessageIsMissing() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom(properties.getProperty("email.username"));
		request.setTo("xyz@gmail.com");
		request.setSubject("test");
		INotificationService emailService = new EmailNotificationService(emailSender, request);
		emailService.sendNotification();
	}

	@Test(expected = Test.None.class)
	public void testSendNotification_whenNoMissingField() throws NotificationException {
		EmailRequest request = new EmailRequest();
		request.setFrom(properties.getProperty("email.username"));
		request.setTo("xyz@gmail.com");
		request.setSubject("test");
		request.setMessage("Hi This is test message");
		INotificationService emailService = new EmailNotificationService(emailSender, request);
		emailService.sendNotification();
	}
}
