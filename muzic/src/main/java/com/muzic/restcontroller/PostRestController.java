package com.muzic.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dao.PostDao;
import com.muzic.dao.PostLikeDao;
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
	
	//게시글 좋아요 상태 및 개수 체크
	@GetMapping("/check")
	public LikeVO check(HttpSession session, @RequestParam int postNo) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		//게시글에 좋아요 체크
		boolean result = postLikeDao.check(loginId, postNo);
		//총 좋아요 개수 카운트
		int count = postLikeDao.countPostNo(postNo);
		
		//likeVO에 담아 반환
		LikeVO likeVO = new LikeVO();
		likeVO.setLike(result);
		likeVO.setCount(count);
		
		return likeVO;
	}
	
	//게시글 좋아요/취소 상태 반환
	@GetMapping("/action")
	public LikeVO action(HttpSession session, @RequestParam int postNo) {
		String loginId = (String) session.getAttribute("loginMemberId");
		LikeVO likeVO = new LikeVO();
		
		//현재 좋아요 상태 확인
		if(postLikeDao.check(loginId, postNo)) {
			//좋아요 → 좋아요 취소
			postLikeDao.delete(loginId, postNo);
			likeVO.setLike(false);
		}
		else {
			//좋아요 아님 → 좋아요
			postLikeDao.insert(loginId, postNo);
			likeVO.setLike(true);
		}
		
		//변경된 좋아요 개수 카운트
		int count = postLikeDao.countPostNo(postNo);
		//좋아요 수 업데이트
		postDao.updatePostLike(postNo, count);
		likeVO.setCount(count);
		return likeVO;
	}
}
