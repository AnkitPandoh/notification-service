package com.notification.service;

import com.notification.bean.NotificationRequest;
import com.notification.exception.NotificationException;

public interface INotification<T extends NotificationRequest> {
	
	public void sendNotification(T request) throws NotificationException;
}
