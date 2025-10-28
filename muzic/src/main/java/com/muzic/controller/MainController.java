package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muzic.dao.MemberDao;
import com.muzic.dao.PostCommentsDao;
import com.muzic.dto.MemberDto;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.PostCommentsVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private PostCommentsDao postCommentsDao;

	@RequestMapping("/")
	public String main(Model model, HttpSession session) {
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		model.addAttribute("memberDto", findDto);
		
		//게시글 목록(댓글 포함) 가져오기
		List<PostCommentsVO> freePostList = postCommentsDao.selectListByFree();
		model.addAttribute("freePostList",freePostList);
		
		List<PostCommentsVO> mbtiPostList = postCommentsDao.selectListByMbti();
		model.addAttribute("mbtiPostList",mbtiPostList);
		
		return "/WEB-INF/views/home.jsp";
	}
}
