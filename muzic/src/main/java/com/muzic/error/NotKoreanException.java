package com.muzic.error;

// 한글이 아닌경우의 예외처리 클래스인데 2025-10-21 개발과정까지는 사용없음
public class NotKoreanException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public NotKoreanException() {
		super();
	}
	
	public NotKoreanException(String message) {
		super(message);
	}
	
}
