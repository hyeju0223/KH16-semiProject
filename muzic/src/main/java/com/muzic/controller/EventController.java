package com.muzic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muzic.dao.MemberDao;
import com.muzic.dto.MemberDto;
import com.muzic.error.TargetNotFoundException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/event")
public class EventController {
	
	@Autowired
	private MemberDao memberDao;

	@RequestMapping("/roulette")
	public String roulette(HttpSession session) {
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto memberDto = memberDao.selectByMemberId(loginId);
//		if(memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		return "/WEB-INF/views/event/roulette.jsp";
	}
}
