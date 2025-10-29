package com.muzic.aop;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.muzic.error.AlreadyRequestedException;
import com.muzic.error.DataPersistenceException;
import com.muzic.error.InvalidContentException;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.NotKoreanException;
import com.muzic.error.NotOwnerException;
import com.muzic.error.OperationFailedException;
import com.muzic.error.TargetNotFoundException;

@ControllerAdvice
public class ExceptionControllerAdvice {

	@ExceptionHandler(AlreadyRequestedException.class)
	public String alreadyRequested(AlreadyRequestedException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/alreadyRequested.jsp";
	}

	@ExceptionHandler(DataPersistenceException.class)
	public String dataPersist(DataPersistenceException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/dataPersist.jsp";
	}

	@ExceptionHandler(InvalidContentException.class)
	public String invalidContent(InvalidContentException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/invalidContent.jsp";
	}

	@ExceptionHandler(NeedPermissionException.class)
	public String needPermission(NeedPermissionException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/needPermission.jsp";
	}

//	@ExceptionHandler(NotKoreanException.class)
//	public String notKorean(NotKoreanException e, Model model) {
//		model.addAttribute("title", e.getMessage());
//		return "/WEB-INF/views/error/notKorean.jsp";
//	}

	@ExceptionHandler(NotOwnerException.class)
	public String notOwner(NotOwnerException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/notOwner.jsp";
	}

	@ExceptionHandler(OperationFailedException.class)
	public String operationFailed(OperationFailedException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/operationFailed.jsp";
	}

	@ExceptionHandler(TargetNotFoundException.class)
	public String notFound(TargetNotFoundException e, Model model) {
		model.addAttribute("title", e.getMessage());
		return "/WEB-INF/views/error/notFound.jsp";
	}

	// 나머지 모든 예외
	// -사용자에게는 별거 아닌 것처럼, 개발자에게는 아주 상세한 정보를 남긴다
	@ExceptionHandler(Exception.class)
	public String all(Exception e) {
		e.printStackTrace();
		return "/WEB-INF/views/error/all.jsp";
	}

}
