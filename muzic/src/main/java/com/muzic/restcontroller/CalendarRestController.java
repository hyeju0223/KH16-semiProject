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
import com.muzic.dao.MemberPointLogDao;
import com.muzic.dto.CalendarDto;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MemberPointLogDto;
import com.muzic.error.TargetNotFoundException;
//import com.muzic.service.CalendarPointService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/rest/mypage/calendar")
public class CalendarRestController {
	
	@Autowired
	private CalendarDao calendarDao;
	@Autowired
	private MemberDao memberDao;
//	@Autowired
//	private CalendarPointService calendarPointService;
	@Autowired
	private MemberPointLogDao memberPointLogDao;
	
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
	
	//출석체크 선물
	@PostMapping("/gift")
	public String gift(HttpSession session) {
		
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");

		//총 출석체크 개수 구하기
		int totalAttendance = calendarDao.selectTotalAttendance(loginId);

		boolean already = memberPointLogDao.selectReasonByMonthly(loginId);

		
		int point = 0;
		if(already){
			return "already";					
		}
		else if(totalAttendance >= 30 ) {
			point = 1000;

		}else if(totalAttendance >= 20 ) {
			point = 300;
		}
		else if(totalAttendance >= 5 ) {
			point = 100;
		} else {
			return "fail";
		}
		
		//포인트 로그 등록을 위한 Dto(회원 아이디, 증감 포인트, 변경사유)
		MemberPointLogDto memberPointLogDto = new MemberPointLogDto();
		memberPointLogDto.setPointLogNo(memberPointLogDao.sequence());
		memberPointLogDto.setPointLogMember(loginId);
		memberPointLogDto.setPointLogReason("출석체크");
		
		memberDao.addPoint(point,loginId);
		memberPointLogDto.setPointLogChange(point);	
		memberPointLogDao.insertByGiftpoint(memberPointLogDto);
		return "success";
		
		
	}
	
	
	

}
