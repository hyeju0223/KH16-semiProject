package com.muzic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muzic.error.AlreadyRequestedException;
import com.muzic.error.DataPersistenceException;
import com.muzic.error.InvalidContentException;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.NotOwnerException;
import com.muzic.error.OperationFailedException;
import com.muzic.error.TargetNotFoundException;

@Controller
@RequestMapping("/test") // 테스트 전용 기본 경로
public class TestErrorController {

	// 1. AlreadyRequestedException 테스트
	@GetMapping("/already-requested")
	public void testAlreadyRequested() {
		throw new AlreadyRequestedException("개발자 테스트: 이미 처리된 요청입니다.");
	}

	// 2. DataPersistenceException 테스트
	@GetMapping("/data-persist")
	public void testDataPersistence() {
		throw new DataPersistenceException("개발자 테스트: 데이터베이스 저장/수정 중 오류가 발생했습니다.");
	}

	// 3. InvalidContentException 테스트
	@GetMapping("/invalid-content")
	public void testInvalidContent() {
		throw new InvalidContentException("개발자 테스트: 원본 파일 이름은 null이거나 비어있을 수 없습니다.");
	}

	// 4. NeedPermissionException 테스트
	@GetMapping("/need-permission")
	public void testNeedPermission() {
		throw new NeedPermissionException("개발자 테스트: 이 작업을 수행할 권한이 필요합니다.");
	}

	// 5. NotKoreanException 테스트
//	@GetMapping("/not-korean")
//	public void testNotKorean() {
//		throw new NotKoreanException("개발자 테스트: 입력은 한글만 허용됩니다.");
//	}

	// 6. NotOwnerException 테스트
	@GetMapping("/not-owner")
	public void testNotOwner() {
		throw new NotOwnerException("해당 리소스의 소유자가 아닙니다.");
	}

	// 7. OperationFailedException 테스트
	@GetMapping("/operation-failed")
	public void testOperationFailed() {
		throw new OperationFailedException("음원 정보 수정에 실패했습니다.");
	}

	@GetMapping("/not-found")
	public void testTargetNotFound() {
		throw new TargetNotFoundException("개발자 테스트: 요청하신 대상을 찾을 수 없습니다.");
	}

	@GetMapping("/general")
	public void testGeneralException() throws Exception {
		// NullPointerException, IOException 등 예상치 못한 일반 예외를 simulate합니다.
		throw new Exception("개발자 테스트: 예상치 못한 일반 Exception이 발생했습니다.");
	}
}