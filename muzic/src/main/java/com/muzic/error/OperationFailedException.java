package com.muzic.error;

public class OperationFailedException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public OperationFailedException() {
		super();
	}
	
	public OperationFailedException(String message) {
		super(message);
	}
}	
