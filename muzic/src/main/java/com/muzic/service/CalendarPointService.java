//package com.muzic.service;
//
//import java.time.LocalDate;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.muzic.dao.CalendarDao;
//import com.muzic.dao.MemberDao;
//import com.muzic.dto.MemberDto;
//import com.muzic.error.TargetNotFoundException;
//
//import jakarta.servlet.http.HttpSession;
//
//@Service
//public class CalendarPointService {
//	
//	@Autowired
//	private MemberDao memberDao;
//	@Autowired
//	private CalendarDao calendarDao;
//
//	@Transactional
//	public String giftPoint(HttpSession session) {
//		//세션으로 회원 찾기 및 예외처리
//				String loginId = (String) session.getAttribute("loginMemberId");
//				MemberDto findDto = memberDao.selectByMemberId(loginId);
//				if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
//				
////				int year = LocalDate.now().getYear();
////				int month = LocalDate.now().getMonth().toString();
////				String dateToChar = year + month
////				
////				System.out.println(month + "," + year);
////				int totalAttendance = calendarDao.selectTotalAttendance(loginId, month);
////				
////				if(totalAttendance >= 5 ) {
////					int point = 100;
////					memberDao.addPoint(point, findDto.getMemberId());
////					return "success";
////				}
////				if(totalAttendance >= 20 ) {
////					int point = 300;
////					memberDao.addPoint(point, findDto.getMemberId());
////					return "success";
////				}
////				if(totalAttendance >= 30 ) {
////					int point = 1000;
////					memberDao.addPoint(point, findDto.getMemberId());
////					return "success";
////				} else {
////					return "fail";
////				}
//	}
//}
