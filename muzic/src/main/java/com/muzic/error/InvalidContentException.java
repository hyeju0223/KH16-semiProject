package com.muzic.error;

public class InvalidContentException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public InvalidContentException() {
		super();
	}
	
	public InvalidContentException(String message) {
		super(message);
	}
}
