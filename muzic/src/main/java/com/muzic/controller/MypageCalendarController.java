package com.muzic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.CalendarDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.CalendarDto;
import com.muzic.dto.MemberDto;

@Controller
@CrossOrigin
@RequestMapping("/mypage/calendar")
public class MypageCalendarController {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private CalendarDao calendarDao;
	
	@GetMapping("/")
	public String home(@RequestParam String memberId, Model model) {
		
		//memberId를 찾고
		MemberDto findDto = memberDao.selectByMemberId(memberId);
		
		//해당 회원의 캘린더를 보여주기
		CalendarDto calendarDto = calendarDao.selectByMemberId(findDto.getMemberId());
		
		//캘린더 정보 모델로 전송
		model.addAttribute("calendarDto",calendarDto);

		
		return "/WEB-INF/views/mypage/calendar/home.jsp";
	}
	
	@GetMapping("/add")
	public String add(@RequestParam String memberId) {
		
		//예외 처리 진행

		return "/WEB-INF/views/mypage/calendar/add.jsp";
	}
	
	@PostMapping("/add")
	public String add(@ModelAttribute CalendarDto calendarDto) {
		
		//시퀀스를 구해서
		int sequence = calendarDao.sequence();
		
		//캘린더에 추가한다
		calendarDto.setCalendarNo(sequence);
		
		//작성된 캘린더 정보를 DB에 저장
		calendarDao.insert(calendarDto);
		
		return "redirect:/";
	}	
}
