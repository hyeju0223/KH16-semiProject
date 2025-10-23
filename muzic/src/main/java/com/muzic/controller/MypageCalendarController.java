package com.muzic.controller;

import java.util.List;

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

import jakarta.servlet.http.HttpSession;

@Controller
@CrossOrigin
@RequestMapping("/mypage/calendar")
public class MypageCalendarController {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private CalendarDao calendarDao;
	
	@GetMapping("/")
	public String home(HttpSession session, Model model) {
		
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		
		//해당 회원의 캘린더를 보여주기
		List<CalendarDto> calendarList = calendarDao.selectByMemberId(findDto.getMemberId());
		
		//캘린더 정보 모델로 전송
		model.addAttribute("calendarList",calendarList);
		
		return "/WEB-INF/views/mypage/calendar/home.jsp";
	}
	
	@GetMapping("/add")
	public String add(HttpSession session) {
		
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
		
		return "redirect:/mypage/calendar/";
	}	
	
	@GetMapping("/detail")
	public String detail(@RequestParam int calendarNo,
									HttpSession session, Model model) {

		//CalendarDto정보 가져오기
		CalendarDto calendarDto = calendarDao.selectOne(calendarNo);
		model.addAttribute("calendarDto",calendarDto);
		
		return "/WEB-INF/views/mypage/calendar/detail.jsp";
		
	}
	
	
	@GetMapping("/edit")
	public String edit(@RequestParam int calendarNo,
									Model model) {

		//CalendarDto정보 가져오기
		CalendarDto calendarDto = calendarDao.selectOne(calendarNo);
		model.addAttribute("calendarDto",calendarDto);
		
		return "/WEB-INF/views/mypage/calendar/edit.jsp";
		
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute CalendarDto calendarDto) {
		
		calendarDao.update(calendarDto);
		
		return "redirect:/mypage/calendar/";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam int calendarNo) {
		
		calendarDao.delete(calendarNo);
		
		return "redirect:/mypage/calendar/";
	}
}
