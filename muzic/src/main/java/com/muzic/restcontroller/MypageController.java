//package com.muzic.restcontroller;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.muzic.dao.AttachmentDao;
//import com.muzic.service.AttachmentService;
//
//import jakarta.servlet.http.HttpSession;
//
//@Service
//@CrossOrigin
//@RequestMapping("/mypage/edit")
//public class MypageController {
//	
//	@Autowired
//	private AttachmentService attachmentService;
//	@Autowired
//	private AttachmentDao attachmentDao;
//	
//	//프로필 변경
//	@RequestMapping("/profile")
//	public void profile(HttpSession Session, MultipartFile attach) throws IOException {
//		//로그인 아이디 구하기
//		String loginId = (String) Session.getAttribute("loginMemberId");
//		
//		
//		
//		
//		//신규 파일 등록
//		if(attach.isEmpty() == false) { 
//			String categoty = attach.getOriginalFilename(); //카테고리는 파일 원본 이름으로 저장
//			attachmentService.save(attach, categoty, loginId);
//		}
//	} 
//}
