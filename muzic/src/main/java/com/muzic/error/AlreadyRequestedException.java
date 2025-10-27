package com.muzic.error;

public class AlreadyRequestedException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public AlreadyRequestedException() {
		super();
	}
	
	public AlreadyRequestedException(String message) {
		super(message);
	}
}
