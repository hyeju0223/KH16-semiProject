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

@Service
public class PreventAdminInterceptor implements HandlerInterceptor{
	
	@Autowired
	private MemberDao memberDao;
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception{
		
		String loginMemberId = request.getParameter("loginMemberId");
		if(loginMemberId == null) throw new NeedPermissionException("허용되지 않은 접근");
		MemberDto memberDto = memberDao.selectOne("loginMemberId");
		if(memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원");
		boolean isAdmin = memberDto.getMemberRole().equals("관리자");
		if(isAdmin) throw new NeedPermissionException("관리자에 대한 접근 금지");
		return true;
	}

}
