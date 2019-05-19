package com.notification.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.notification.bean.EmailRequest;
import com.notification.config.NotificationConfig;
import com.notification.exception.NotificationException;
import com.notification.response.ApiWrapper;
import com.notification.service.INotificationService;
import com.notification.service.email.EmailNotificationService;
import com.notification.type.NotificationType;

@RestController
@RequestMapping("/notify")
public class NotificationApi {
	
	private static final String FROM_ADDRESS = "from";
	
	@Autowired
	private ApiWrapper wrapper;

	@Autowired
	private NotificationConfig config;
	
	@RequestMapping(path = "/v1/{type}", method = RequestMethod.POST)
	public ResponseEntity<String> sendNotification(@RequestBody String requestBody, @PathVariable String type) throws NotificationException {
		
		return wrapper.execute(new TypeReference<Map<String, Object>>() {
		}, requestBody, (emailModel) -> {
			NotificationType notifyType = NotificationType.valueOf(type.toUpperCase());
			switch (notifyType) {
			case EMAIL:
				Map<String, Object> model = (Map<String, Object>) emailModel;
				model.put(FROM_ADDRESS, config.getUsername());
				EmailRequest emailRequest = config.requestGenerator(model).generateEmailRequest();
				INotificationService notifyService = new EmailNotificationService(config.emailSender(),emailRequest);
				notifyService.sendNotification();
				break;
			case SMS:
				throw new NotificationException("Not Implemented yet");
			default:
				throw new NotificationException("Unknown Type");
			}
			return "Message sent successfully";
		});
	}
}
