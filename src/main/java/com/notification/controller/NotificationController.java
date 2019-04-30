package com.notification.controller;

import java.io.IOException;
import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.bean.EmailRequest;
import com.notification.exception.NotificationException;
import com.notification.service.INotification;
import com.notification.service.email.EmailConfig;
import com.notification.service.email.EmailNotificationService;
import com.notification.service.email.EmailRequestGenerator;
import com.notification.type.NotificationType;

@RestController
@RequestMapping("/notify")
public class NotificationController {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private EmailConfig config;

	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;

	@RequestMapping(value = "/v1/{type}", method = RequestMethod.POST)
	public ResponseEntity<Void> sendMessage(@RequestBody String request, @PathVariable String type)
			throws NotificationException {

		NotificationType notifyType = NotificationType.valueOf(type.toUpperCase());

		try {
			switch (notifyType) {
			case EMAIL:
				Map<String, Object> requestMap = mapper.readValue(request, new TypeReference<Map<String, Object>>() {
				});
				EmailRequestGenerator requestGen = new EmailRequestGenerator(config, velocityEngine);
				EmailRequest emailRequest = requestGen.generateEmailRequest(requestMap);
				INotification<EmailRequest> emailService = new EmailNotificationService(emailSender);
				emailService.sendNotification(emailRequest);
				break;
			case SMS:
				// placeholder for sms notification
				break;
			default:
				throw new NotificationException("Unknown notification type..");
			}
		} catch (IOException iex) {
			// TODO : Log exception
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (NotificationException nex) {
			// TODO : Log exception
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

}