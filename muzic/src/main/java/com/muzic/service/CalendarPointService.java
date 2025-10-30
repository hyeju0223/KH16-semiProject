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
//import com.muzic.dao.MemberPointLogDao;
//import com.muzic.dto.MemberDto;
//import com.muzic.dto.MemberPointLogDto;
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
//	@Autowired
//	private MemberPointLogDao memberPointLogDao;
//
//	@Transactional
//	public String giftPoint(HttpSession session) {
//		//세션으로 회원 찾기 및 예외처리
//				String loginId = (String) session.getAttribute("loginMemberId");
//				MemberDto findDto = memberDao.selectByMemberId(loginId);
//				if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
//
//				//총 출석체크 개수 구하기
//				int totalAttendance = calendarDao.selectTotalAttendance(loginId);
//				//포인트 로그 등록을 위한 Dto(회원 아이디, 증감 포인트, 변경사유)
//				MemberPointLogDto memberPointLogDto = new MemberPointLogDto();
//				memberPointLogDto.setPointLogNo(memberPointLogDao.sequence());
//				memberPointLogDto.setPointLogMember(loginId);
//				memberPointLogDto.setPointLogReason("출석체크");
//				String reason = memberPointLogDao.selectReasonByMonthly(loginId);
//				if(reason.equals("출석체크")){
//					return "already";					
//				}
//				
//				
//				if(totalAttendance >= 30 ) {
//					int point = 1000;
//					//포인트 추가
//					memberDao.addPoint(point, findDto.getMemberId());
//					//로그 등록
//					memberPointLogDto.setPointLogChange(point);	
//					memberPointLogDao.insertByGiftpoint(memberPointLogDto);
//					return "success";
//				}else if(totalAttendance >= 20 ) {
//					int point = 300;
//					memberDao.addPoint(point, findDto.getMemberId());
//					memberPointLogDto.setPointLogChange(point);	
//					memberPointLogDao.insertByGiftpoint(memberPointLogDto);
//					return "success";
//				}
//				else if(totalAttendance >= 10 ) {
//					int point = 100;
//					memberDao.addPoint(point, findDto.getMemberId());
//					memberPointLogDto.setPointLogChange(point);	
//					memberPointLogDao.insertByGiftpoint(memberPointLogDto);
//					return "success";
//				} 
//				
//				
//				
//
//	}
//}
