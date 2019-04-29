package com.notification.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import com.notification.bean.EmailRequest;
import com.notification.service.INotification;

@Component
public class EmailNotificationService implements INotification<EmailRequest> {

	@Autowired
	private JavaMailSender emailSender;
	
	@Override
	public void sendNotification(EmailRequest request) {
		MimeMessagePreparator preparator = (mimeMessage) -> {
			MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage);
			mailMessage.setFrom(request.getFrom());
			mailMessage.setSubject(request.getSubject());
			mailMessage.setText(request.getMessage(), true);
			mailMessage.setTo(request.getTo());
		};
		emailSender.send(preparator);
	}

}
