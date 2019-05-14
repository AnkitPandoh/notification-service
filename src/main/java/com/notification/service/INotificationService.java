package com.notification.service;

import com.notification.exception.NotificationException;

public interface INotificationService {
	
	public void sendNotification() throws NotificationException;
}
