package com.muzic.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "작업 실패: 요청 내용을 확인해주세요.")
public class OperationFailedException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public OperationFailedException() {
		super();
	}
	
	public OperationFailedException(String message) {
		super(message);
	}
	
}	
