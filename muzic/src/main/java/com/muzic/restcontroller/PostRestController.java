package com.muzic.restcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.PostDao;
import com.muzic.dao.PostLikeDao;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;
import com.muzic.vo.LikeVO;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/rest/post")
public class PostRestController {

	@Autowired
	private PostDao postDao;
	
	@Autowired
	private PostLikeDao postLikeDao;
	
//	@Autowired
//	private AttachmentService attachmentService;
//	
//	//임시 파일
//	@PostMapping("/temp")
//	public int temp(@RequestParam MultipartFile attach) throws IllegalStateException, IOException {
//		if(attach.isEmpty()) {
//			throw new TargetNotFoundException("파일이 존재하지 않습니다.");
//		}
//		return attachmentService.saveTemp(attach, "0");
//	}
//	
//	@PostMapping("/temps")
//	public List<Integer> temps(@RequestParam(value = "attach") List<MultipartFile> attachList) 
//			throws IllegalStateException, IOException {
//		List<Integer> number = new ArrayList<>();
//		
//		for(MultipartFile attach : attachList) {
//			if(attach.isEmpty() == false) {
//				int attachmentNo = attachmentService.saveTemp(attach, "0");
//				number.add(attachmentNo);
//			}
//		}
//		
//		return number;
//	}
	
	@GetMapping("/check")
	public LikeVO check(HttpSession session, @RequestParam int postNo) {
		String loginId = (String) session.getAttribute("loginMemberId");
		boolean result = postLikeDao.check(loginId, postNo);
		int count = postLikeDao.countPostNo(postNo);
		LikeVO likeVO = new LikeVO();
		likeVO.setLike(result);
		likeVO.setCount(count);
		
		return likeVO;
	}
	
	@GetMapping("/action")
	public LikeVO action(HttpSession session, @RequestParam int postNo) {
		String loginId = (String) session.getAttribute("loginMemberId");
		LikeVO likeVO = new LikeVO();
		
		if(postLikeDao.check(loginId, postNo)) {
			postLikeDao.delete(loginId, postNo);
			likeVO.setLike(false);
		}
		else {
			postLikeDao.insert(loginId, postNo);
			likeVO.setLike(true);
		}
		
		int count = postLikeDao.countPostNo(postNo);
		postDao.updatePostLike(postNo, count);
		likeVO.setCount(count);
		return likeVO;
	}
}
