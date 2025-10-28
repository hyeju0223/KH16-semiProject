package com.muzic.error;

public class DataPersistenceException extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public DataPersistenceException() {
		super();
	}
	
	public DataPersistenceException(String message) {
		super(message);
	}
}
