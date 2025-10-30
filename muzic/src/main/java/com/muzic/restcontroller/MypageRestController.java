package com.muzic.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.AttachmentDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.MemberDto;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;

import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin
@RequestMapping("/mypage")
public class MypageRestController {
	
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	//프로필 변경
//	@RequestMapping("/profile")
//	public void profile(HttpSession Session, MultipartFile attach) throws IOException {
//		//로그인 아이디 구하기
//		String loginId = (String) Session.getAttribute("loginMemberId");
//		
//		
//		//신규 파일 등록
//		if(attach.isEmpty() == false) { 
//			String categoty = attach.getOriginalFilename(); //카테고리는 파일 원본 이름으로 저장
//			attachmentService.save(attach, categoty, loginId);
//		}
//	} 
	
	//회원 탈퇴를 위한 비밀번호 확인
	@RequestMapping("/withDraw")
	public String withDraw(HttpSession session,String memberId, String memberPw) {
		//정보를 받으면 아이디, 비밀번호 확인
		//세션으로 아이디 가지고 오기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
//		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		//세션의 아이디와 memberId 같은지 조회
		if(loginId == null || ! loginId.equals(memberId)) {
			return "unauthorized"; //세션불일치
		};
		
		//비밀번호 같은지 조회
		if(!bCryptPasswordEncoder.matches(memberPw, findDto.getMemberPw())) {
			return "fail";
		}
		
		//회원 삭제
		memberDao.delete(memberId);
		//캘린더 정보 삭제
		//세션 삭제
		session.invalidate();
		
		return "redirect:/mypage/withDraw/buy";
		
	}
	
	//비밀번호 변경
	@RequestMapping("/currentPwCheck")
	public String password(HttpSession session,
			String memberId, String currentPw) {
		//정보를 받으면 아이디, 비밀번호 확인
		//세션으로 아이디 가지고 오기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		if(findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다");
		
		//세션의 아이디와 memberId 같은지 조회
		if(loginId == null || ! loginId.equals(memberId)) {
			return "unauthorized"; //세션불일치
		};
		
		//비밀번호 같은지 조회
		if(!bCryptPasswordEncoder.matches(currentPw, findDto.getMemberPw())) {
			return "fail";
		}
		
		return "success";

	}
		
	

	
}
