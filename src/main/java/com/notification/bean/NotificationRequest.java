package com.notification.bean;

import com.notification.type.NotificationType;

public class NotificationRequest {
	
	private NotificationType type;
	
	NotificationRequest(NotificationType type){
		this.type = type;
	}

	public NotificationType getType() {
		return type;
	}

	public void setType(NotificationType type) {
		this.type = type;
	}
}
