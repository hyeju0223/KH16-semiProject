package com.muzic.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dao.CommentsDao;
import com.muzic.dao.CommentsLikeDao;
import com.muzic.dao.PostDao;
import com.muzic.dto.CommentsDto;
import com.muzic.dto.PostDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.CommentsVO;
import com.muzic.vo.LikeVO;

import jakarta.servlet.http.HttpSession;

//댓글 및 댓글 좋아요 api요청 처리하는 컨트롤러
@CrossOrigin
@RestController
@RequestMapping("/rest/comments")
public class CommentsRestComtroller {

	@Autowired
	private CommentsDao commentsDao;
	@Autowired
	private PostDao postDao;
	@Autowired
	private CommentsLikeDao commentsLikeDao;
	
	//게시글에 달린 댓글 목록 조회
	@GetMapping("/list")
	public List<CommentsVO> list(@RequestParam int commentPost, HttpSession session) {
		String loginId = (String)session.getAttribute("loginMemberId");
		
		//게시글 확인
		PostDto postDto = postDao.selectOne(commentPost);
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 글 정보 입니다");
		
		//댓글 목록 조회
		List<CommentsDto> list = commentsDao.selectList(commentPost);
		//빈 목록 생성 (VO에 반환)
		List<CommentsVO> result = new ArrayList<>();
		
		//boolean ownwe, writer;
		
		//반복문으로 목록 돌려보며 플래그 설정
		for(CommentsDto commentsDto : list) {
			boolean owner = loginId != null 
					&& commentsDto.getCommentWriter() != null 
					&& loginId.equals(commentsDto.getCommentWriter());
			
			boolean writer = postDto.getPostWriter() != null
					&& commentsDto.getCommentWriter() != null
					&& postDto.getPostWriter().equals(commentsDto.getCommentWriter());
			
			// CommentsDto를 CommentsVO로 변환하여 result 리스트에 추가
			result.add(CommentsVO.builder()
					.commentNo(commentsDto.getCommentNo())
					.commentPost(commentsDto.getCommentPost())
					.commentWriter(commentsDto.getCommentWriter())
					.commentContent(commentsDto.getCommentContent())
					.commentWtime(commentsDto.getCommentWtime())
					.commentEtime(commentsDto.getCommentEtime())
					.commentLike(commentsDto.getCommentLike())
					.commentOrigin(commentsDto.getCommentOrigin())
					.commentDepth(commentsDto.getCommentDepth())
					.owner(owner)
					.writer(writer)
				.build());
		}
		return result;
	}
	
	//댓글 작성
	@PostMapping("/write")
	public void write(@ModelAttribute CommentsDto commentsDto, HttpSession session) {
		int sequence = commentsDao.sequence();
		
		//시퀀스로 댓글 번호 생성 및 설정
		commentsDto.setCommentNo(sequence);
		String loginId = (String) session.getAttribute("loginMemberId");
		
		//세션 정보가 null이라면 에외
		if (loginId == null) {
	        throw new NeedPermissionException("로그인 후 이용 가능합니다."); 
	    }
		
		commentsDto.setCommentWriter(loginId);
		
//		commentsDto.setCommentOrigin(0); 
//	    commentsDto.setCommentDepth(0);
		
		commentsDao.insert(commentsDto);
	}
	
	//댓글 삭제
	@PostMapping("/delete")
	public void delete(@RequestParam int commentNo, HttpSession session) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		//댓글 확인
		CommentsDto commentsDto = commentsDao.selectOne(commentNo);
		if(commentsDto == null) throw new TargetNotFoundException("존재하지 않는 댓글입니다");
		
		// 작성자가 맞는지 확인
		boolean owner = loginId.equals(commentsDto.getCommentWriter());
		if(owner == false) throw new NeedPermissionException("권한이 부족합니다");
		
		commentsDao.delete(commentNo);
	}
	
	//댓글 수정
	@PostMapping("/edit")
	public void edit(@ModelAttribute CommentsDto commentsDto, HttpSession session) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		//기존 댓글 정보 확인
		CommentsDto addDto = commentsDao.selectOne(commentsDto.getCommentNo());
		if(addDto == null) throw new TargetNotFoundException("존재하지 않는 댓글입니다");
		
		//작성자가 맞는지 확인
		boolean owner = loginId.equals(addDto.getCommentWriter());
		if(owner == false) throw new NeedPermissionException("권한이 부족합니다");
		
		commentsDao.update(commentsDto);
	}
	
	//댓글 좋아요 상태 및 개수 조회
	@GetMapping("/check")
	public LikeVO check(HttpSession session, @RequestParam int postNo) {
		String memberId = (String) session.getAttribute("loginMemberId");
		
		//좋아요 확인
		boolean result = commentsLikeDao.check(memberId, postNo);
		//좋아요 개수 확인
		int count = commentsLikeDao.countCommentNo(postNo);
		
		//likeVO에 담아 반환
		LikeVO likeVO = new LikeVO();
		
		likeVO.setLike(result);
		likeVO.setCount(count);
		
		return likeVO;
	}
	
	//댓글 좋아요/취소 상태 반환
	@PostMapping("/action")
	public LikeVO action(HttpSession session, @RequestParam int commentNo) {
		String memberId = (String) session.getAttribute("loginMemberId");
		
		LikeVO likeVO = new LikeVO();
		
		//현재 좋아요 상태 확인
		if(commentsLikeDao.check(memberId, commentNo)) {
			//좋아요 → 좋아요 취소
			commentsLikeDao.delete(memberId, commentNo);
			likeVO.setLike(false);
		}
		else {
			//좋아요 아님 → 좋아요
			commentsLikeDao.insert(memberId, commentNo);
			likeVO.setLike(true);
		}
		
		//변경된 좋아요 개수 카운트
		int count = commentsLikeDao.countCommentNo(commentNo);
		//좋아요 수 업데이트
		commentsDao.updateCommentsLike(commentNo, count);
		likeVO.setCount(count);
		
		return likeVO;
	}
}
