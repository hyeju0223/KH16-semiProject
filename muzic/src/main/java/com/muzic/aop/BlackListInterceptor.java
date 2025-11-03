package com.muzic.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.muzic.dao.BlacklistDao;
import com.muzic.error.NeedPermissionException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class BlackListInterceptor implements HandlerInterceptor {
	// 1. BlacklistDao를 직접 주입받습니다.
	@Autowired
	private BlacklistDao blacklistDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		String currentLoggedInId = (String) session.getAttribute("loginMemberId");
		
		if (currentLoggedInId != null) {//로그인 상태일 때만 블랙리스트 검사

			//DAO를 통해 회원이 현재 활성 블랙리스트 상태인지 확인
			boolean isBlacklisted = blacklistDao.existsActive(currentLoggedInId);

			if (isBlacklisted) {//블랙리스트 사용자일 경우 접근 차단

				// 강제 로그아웃
				session.invalidate();

				// 리다이렉트 대신 예외를 발생시켜 공통 에러 핸들러로 이동
				throw new NeedPermissionException("이용이 제한된 사용자입니다. 관리자에게 문의하세요.");

			}
		}

		// 블랙리스트가 아니거나 로그아웃 상태면 다음 단계로 진행
		return true;
	}
}