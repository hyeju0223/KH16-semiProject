package com.muzic.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.muzic.dao.MemberDao;
import com.muzic.dto.MemberDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class PreventAdminInterceptor implements HandlerInterceptor {

	@Autowired
	private MemberDao memberDao;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		// 1. 세션에서 로그인한 관리자 ID를 'loginMemberId' 키로 가져옵니다. (사용자 요청 반영)
		String currentLoggedInId = (String) session.getAttribute("loginMemberId");

		// 로그인 ID가 없으면 (loginInterceptor에서 걸러져야 하므로 통과)
		if (currentLoggedInId == null) {
			return true;
		}

		// 2. URL 파라미터에서 조회/수정/삭제 대상 회원의 ID를 가져옵니다.
		String targetMemberId = request.getParameter("memberId");

		// 대상 ID가 파라미터로 제공되지 않았다면 예외 처리
		if (targetMemberId == null || targetMemberId.isEmpty()) {
			throw new IllegalArgumentException("대상 회원 ID가 요청에 포함되지 않았습니다.");
		}

		// 3. 대상 회원의 정보를 DB에서 조회합니다.
		// (이전 코드의 오류 수정: 문자열 "loginMemberId" 대신 변수 targetMemberId 사용)
		MemberDto targetMemberDto = memberDao.selectOne(targetMemberId);

		// 대상 회원이 존재하지 않으면 예외 처리
		if (targetMemberDto == null) {
			throw new TargetNotFoundException("존재하지 않는 회원입니다.");
		}

		// 4. 대상 회원이 관리자인지 확인합니다.
		boolean isTargetAdmin = targetMemberDto.getMemberRole().equals("관리자");

		// -------------------------------------------------------------------------

		String requestURI = request.getRequestURI();

		// 5. 관리자 상세 조회(/detail)는 무조건 허용하여 기존 문제 해결
		if (requestURI.endsWith("/detail")) {
			return true;
		}

		// 6. 관리자 수정/삭제 차단 로직 (최고 관리자 없음 가정)
		if (isTargetAdmin) {
			// 대상 회원이 관리자이고, 요청이 수정(/edit)이나 삭제(/drop)일 경우
			if (requestURI.endsWith("/drop") || requestURI.endsWith("/edit")) {

				// (a) 자기 자신의 계정을 수정/삭제하는 것은 금지 (모든 관리자에게 적용)
				if (currentLoggedInId.equals(targetMemberId)) {
					throw new NeedPermissionException("본인의 관리자 계정은 수정/삭제할 수 없습니다.");
				}

				// (b) 다른 관리자 계정의 수정/삭제도 모두 금지
				throw new NeedPermissionException("다른 관리자 계정에 대한 수정/삭제는 금지됩니다.");
			}
		}

		return true;
	}
}
