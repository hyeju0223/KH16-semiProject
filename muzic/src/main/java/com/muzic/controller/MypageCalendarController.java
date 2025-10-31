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
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;

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
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		//해당 회원의 캘린더를 보여주기
		List<CalendarDto> calendarList = calendarDao.selectByMemberId(findDto.getMemberId());
		
		//캘린더 정보 모델로 전송
		model.addAttribute("calendarList",calendarList);
		model.addAttribute("memberDto", findDto);
		
		return "/WEB-INF/views/mypage/calendar/home.jsp";
	}
	
	@GetMapping("/add")
	public String add(HttpSession session) {
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");

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
		
		return "redirect:detail?calendarNo=" + sequence ;
	}	
	
	@GetMapping("/detail")
	public String detail(@RequestParam int calendarNo,
									HttpSession session, Model model) {
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		//CalendarDto정보 가져오기
		CalendarDto calendarDto = calendarDao.selectOne(calendarNo);
		model.addAttribute("calendarDto",calendarDto);
		
		//일정이 없을 경우 예외처리
		if(calendarDto == null) throw new TargetNotFoundException("존재하지 않는 일정입니다");

		//일정을 만든 회원과 로그인 회원이 다를 경우 예외처리
		boolean isOwner = calendarDto.getCalendarMember().equals(findDto.getMemberId());
		if(! isOwner ) throw new NeedPermissionException("본인의 일정이 아닐 경우 조회 불가능합니다");
		
		return "/WEB-INF/views/mypage/calendar/detail.jsp";
		
	}
	
	
	@GetMapping("/edit")
	public String edit(HttpSession session, @RequestParam int calendarNo,
									Model model) {
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");

		//CalendarDto정보 가져오기
		CalendarDto calendarDto = calendarDao.selectOne(calendarNo);
		//일정이 없을 경우 예외처리
		if(calendarDto == null) throw new TargetNotFoundException("존재하지 않는 일정입니다");
		model.addAttribute("calendarDto",calendarDto);
		
		//일정을 만든 회원과 로그인 회원이 다를 경우 예외처리
		boolean isOwner = calendarDto.getCalendarMember().equals(findDto.getMemberId());
		if(! isOwner ) throw new NeedPermissionException("본인의 일정이 아닐 경우 수정 불가능합니다");
		
		return "/WEB-INF/views/mypage/calendar/edit.jsp";
		
	}
	
	@PostMapping("/edit")
	public String edit(HttpSession session,@ModelAttribute CalendarDto calendarDto) {
		
		//세션으로 회원 찾기 및 예외처리
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");

		//CalendarDto정보 가져오기
		CalendarDto findCalendarDto = calendarDao.selectOne(calendarDto.getCalendarNo());
		//일정이 없을 경우 예외처리
		if(calendarDto == null) throw new TargetNotFoundException("존재하지 않는 일정입니다");
				
		//일정을 만든 회원과 로그인 회원이 다를 경우 예외처리
		boolean isOwner = findCalendarDto.getCalendarMember().equals(findDto.getMemberId());
		if(! isOwner ) throw new NeedPermissionException("본인의 일정이 아닐 경우 수정 불가능합니다");		
		
		calendarDao.update(calendarDto);
		
		return "redirect:detail?calendarNo=" + calendarDto.getCalendarNo() ;
	}
	
	@GetMapping("/delete")
	public String delete(HttpSession session,@RequestParam int calendarNo) {
		
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		//일정이 없을 경우 예외처리
		CalendarDto calendarDto = calendarDao.selectOne(calendarNo);
		if(calendarDto == null) throw new TargetNotFoundException("존재하지 않는 일정입니다");
		
		//일정을 만든 회원과 로그인 회원이 다를 경우 예외처리
		boolean isOwner = calendarDto.getCalendarMember().equals(findDto.getMemberId());
		if(! isOwner ) throw new NeedPermissionException("본인의 일정이 아닐 경우 삭제 불가능합니다");
		
		calendarDao.delete(calendarNo);
		
		return "redirect:/mypage/calendar/";
	}
}
