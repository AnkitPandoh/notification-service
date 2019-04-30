package com.notification.service.email;

import java.io.StringWriter;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.notification.bean.EmailRequest;

public class EmailRequestGenerator {
	
	private static final String TO = "to";
	private static final String CC = "cc";
	private static final String BCC = "bcc";
	private static final String SUBJECT = "subject";
	private static final String TEMPLATE = "template";
	
	private VelocityEngine velocityEngine;
	
	private EmailConfig config;
	
	
	public EmailRequestGenerator(EmailConfig config, VelocityEngine velocityEngine) {
		this.config = config;
		this.velocityEngine = velocityEngine;
	}
	
	public EmailRequest generateEmailRequest(Map<String, Object> emailModel){

		EmailRequest request = new EmailRequest();
		
		VelocityContext context = new VelocityContext(emailModel);
		
		request.setFrom(config.getUsername());
		request.setTo((String) emailModel.get(TO));
		request.setCc((String) emailModel.get(CC));
		request.setBcc((String) emailModel.get(BCC));
		request.setSubject((String) emailModel.get(SUBJECT));
		
		StringWriter stringWriter = new StringWriter();
	    velocityEngine.mergeTemplate("email-templates/"+(String)emailModel.get(TEMPLATE)+".vm", "UTF-8", context, stringWriter);
	    String message = stringWriter.toString();
		request.setMessage(message);

		return request;
	
	}
}
