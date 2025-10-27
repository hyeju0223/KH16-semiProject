package com.muzic.error;

public class NotOwnerException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public NotOwnerException() {
		super();
	}
	
	public NotOwnerException(String message) {
		super(message);
	}
	
}
