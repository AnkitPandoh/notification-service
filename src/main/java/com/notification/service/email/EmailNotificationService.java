package com.notification.service.email;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import com.notification.bean.EmailRequest;
import com.notification.exception.NotificationException;
import com.notification.service.INotification;

public class EmailNotificationService implements INotification<EmailRequest> {

	private JavaMailSender emailSender;

	public EmailNotificationService(JavaMailSender emailSender) {
		this.emailSender = emailSender;
	}

	@Override
	public void sendNotification(final EmailRequest request) throws NotificationException {
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
