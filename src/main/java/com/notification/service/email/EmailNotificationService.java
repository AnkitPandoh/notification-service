package com.notification.service.email;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.notification.bean.EmailRequest;
import com.notification.exception.NotificationException;
import com.notification.service.INotificationService;

public class EmailNotificationService implements INotificationService {

	private final JavaMailSender emailSender;
	private final EmailRequest request;

	public EmailNotificationService(JavaMailSender emailSender, EmailRequest request) {
		this.emailSender = emailSender;
		this.request = request;
	}

	@Override
	public void sendNotification() throws NotificationException {
		try {
			MimeMessagePreparator preparator = (mimeMessage) -> {
				MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
				mailMessage.setFrom(request.getFrom());
				mailMessage.setSubject(request.getSubject());
				mailMessage.setText(request.getMessage(), true);
				mailMessage.setTo(request.getTo());
			};
			emailSender.send(preparator);
		} catch (MailException ex) {
			throw new NotificationException("something went wrong while sending email:", ex);
		}
	}
}
