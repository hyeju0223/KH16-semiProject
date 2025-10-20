package com.muzic.error;

//대상을 찾지 못했을때 사용하는 에러메세지
public class TargetNotFoundException extends RuntimeException{
	
	//클래스가 파일에 저장되거나 네트워크로 전송될 때 안정성 확보를 위한 작성
	private static final long serialVersionUID = 1L;
	
	public TargetNotFoundException() {
		super(); //부모클래스의 기본 생성자를 호출
	}
	public TargetNotFoundException(String message) {
		super(message); // 부모 클래스의 생성자에게 오류 메시지를 전달
	}
}
