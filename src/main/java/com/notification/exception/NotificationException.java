package com.notification.exception;

public class NotificationException extends Exception{
	
	private static final long serialVersionUID = -1052417864842135385L;

	public NotificationException(){
		super();
	}
	
	public NotificationException(String message){
		super(message);
	}
	
	public NotificationException(String message, Throwable cause){
		super(message,cause);
	}

}
