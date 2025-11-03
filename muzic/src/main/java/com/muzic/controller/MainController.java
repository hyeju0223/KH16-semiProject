package com.muzic.controller;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muzic.condition.SearchCondition;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicStatsDao;
import com.muzic.dao.MusicViewDao;
import com.muzic.dao.PostCommentsDao;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MusicDto;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.MusicUserVO;
import com.muzic.vo.PostCommentsVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {
	
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private PostCommentsDao postCommentsDao;
	@Autowired
	private MusicDao musicDao;
	@Autowired
	private MusicViewDao musicViewDao;
	@Autowired
	private MusicStatsDao musicStatsDao;

	@RequestMapping("/")
	public String main(Model model, HttpSession session) {
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		String mbti = (String) session.getAttribute("loginMemberMbti");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
//		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		model.addAttribute("memberDto", findDto);
		
		//게시글 목록(댓글 포함) 가져오기
		List<PostCommentsVO> freePostList = postCommentsDao.selectListByFree();
		model.addAttribute("freePostList",freePostList);
		
		List<PostCommentsVO> mbtiPostList = postCommentsDao.selectListByMbti();
		model.addAttribute("mbtiPostList",mbtiPostList);
		
		// mbti별로 8개 음원 가져오기
		SearchCondition searchCondition = new SearchCondition();
		searchCondition.setPage(1);
		searchCondition.setSize(8);
		List<MusicUserVO>  hearderList = musicStatsDao.findTopByMbti(mbti, searchCondition);
		model.addAttribute("hearderList",hearderList);
		
		//음원 목록(인기순) 가져오기
		List<MusicUserVO> musicRankList = musicViewDao.selectListByRank();
		model.addAttribute("musicRankList",musicRankList);
		
	    //jsp에 util.Date or calendar만 전송 가능
		LocalDate now = LocalDate.now();
		Date current = java.sql.Date.valueOf(now);
	    
	    model.addAttribute("now", current);
	    
		
		return "/WEB-INF/views/home.jsp";
	}


}

