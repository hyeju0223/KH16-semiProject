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
public class MemberCheckInterceptor implements HandlerInterceptor {
	
	@Autowired
	private MemberDao memberDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws Exception {
		
		//세션 및 아이디로 회원 정보 조회
		HttpSession session = request.getSession();
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		
		if(findDto == null) { //회원이 아닐 경우
			throw new NeedPermissionException("존재하지 않는 회원입니다");
		}
		
		//회원 정보 전달
		request.setAttribute("findDto", findDto);
		
			return true; //통과
		}
}
