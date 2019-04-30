package com.notification.service;

import com.notification.exception.NotificationException;

public interface INotification<T> {
	
	public void sendNotification(T request) throws NotificationException;
}
