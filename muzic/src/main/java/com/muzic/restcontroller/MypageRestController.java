package com.muzic.restcontroller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping("/rest/mypage")
public class MypageRestController {
	
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
//	//프로필 사진 등록하기
	@PostMapping("/profile")
	public void profile(HttpSession Session,
									String memberId, MultipartFile attach) throws IOException {
		
		//로그인 아이디 구하기
		String loginId = (String) Session.getAttribute("loginMemberId");
		
		//기존 파일 삭제
		try {//사용자에게 예외 알려줘야해서 예외처리
			int attachmentNo = memberDao.findAttachment(loginId); //회원 아이디로 이미지 PK 찾기
			attachmentService.delete(attachmentNo);
		}catch(Exception e) { /*아무것도 안함*/} //다음 단계로 가야하니 try-catch 씀
		
		//신규 파일 등록 
		if(attach.isEmpty() == false) {
			String categoty = "profile";
			attachmentService.save(attach, categoty, loginId);//파일+DB 저장
		}
		
	}
//			int a = attachmentDao.findAttachmentNoByMemberId(memberId, categoty);//부모의 pk를 넣어야함. 아이디의 경우 파일 아이디로 저장할 때 반환되는 No가 있으면 됨

	
	//회원 탈퇴를 위한 비밀번호 확인
	@PostMapping("/withDraw")
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
	@PostMapping("/currentPwCheck")
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
