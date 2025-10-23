package com.muzic.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.muzic.dao.MemberDao;
import com.muzic.dto.MemberDto;
import com.muzic.error.NeedPermissionException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class LoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private MemberDao memberDao;

	@Override
	public boolean preHandle(
			HttpServletRequest request,// 요청(사용자의 정보)
			HttpServletResponse response, //응답(사용자에게 나갈 정보)
			Object handler)
			throws Exception {
		
		//로그인 된 회원인지 확인 (HttpServletRequest에서 세션 먼저 가져오기)
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("loginMemberId");
	
		boolean isLogin = loginId != null;
		
		if(isLogin) { //로그인 된 경우
			return true; //통과			
		}else {
			throw new NeedPermissionException("로그인이 필요합니다"); //예외 문구 생성
		}
		
		
	}
	
}
