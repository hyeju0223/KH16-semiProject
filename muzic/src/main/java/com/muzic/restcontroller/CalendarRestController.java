package com.muzic.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dao.CalendarDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.CalendarDto;

@CrossOrigin
@RestController
@RequestMapping("/mypage/calendar")
public class CalendarRestController {
	
	@Autowired
	private CalendarDao calendarDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
//	private CalendarPointService calendarPointService;
	
	//일정 조회
	@PostMapping("/home")
	public List<CalendarDto> selectByMemberId(@RequestParam String memberId) {
		
		return calendarDao.selectByMemberId(memberId);
		
	}
	
	//출석 체크
	@PostMapping("/check")
	public String check(@ModelAttribute CalendarDto calendarDto) {
		
		//출석 체크 여부 조회
		String attendance = calendarDao.selectAttendance(calendarDto);
		
		if("Y".equals(attendance)) { //출석 상태라면
			//출석 상태라면 이미 출석된 상태라고 안내 필요
			return "already";
		}else if("N".equals(attendance)) { //미출석 상태라면
			calendarDao.updateAttendance(calendarDto);
			return "success";
		}
		else {
			int sequence = calendarDao.sequence();
			calendarDto.setCalendarNo(sequence);
			calendarDao.checkAttendance(calendarDto); //일정+출석 체크 추가
			return "success"; 
		}
		
		
	}
	
//	//출석체크 선물
//	@PostMapping("/gift")
//	public String gift(HttpSession session) {
//		
////		return calendarPointService.giftPoint(session);
//		
//		
//	}
	

}
