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
import com.muzic.dao.PostDao;
import com.muzic.dto.CommentsDto;
import com.muzic.dto.PostDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.CommentsVO;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/rest/comments")
public class CommentsRestComtroller {

	@Autowired
	private CommentsDao commentsDao;
	
	@Autowired
	private PostDao postDao;
	
//	@PostMapping("/list")
	@GetMapping("/list")
	public List<CommentsVO> list(@RequestParam int commentPost, HttpSession session) {
		String loginId = (String)session.getAttribute("loginMemberId");
		
		PostDto postDto = postDao.selectOne(commentPost);
		
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 글 정보 입니다");
		
		List<CommentsDto> list = commentsDao.selectList(commentPost);
		List<CommentsVO> result = new ArrayList<>(); //빈 목록 생성
		
		//boolean ownwe, writer;
		
		for(CommentsDto commentsDto : list) {
			boolean owner = loginId != null 
					&& commentsDto.getCommentWriter() != null 
					&& loginId.equals(commentsDto.getCommentWriter());
			
			boolean writer = postDto.getPostWriter() != null
					&& commentsDto.getCommentWriter() != null
					&& postDto.getPostWriter().equals(commentsDto.getCommentWriter());
			
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
	
	@PostMapping("/write")
	public void write(@ModelAttribute CommentsDto commentsDto, HttpSession session) {
		int sequence = commentsDao.sequence();
		
		commentsDto.setCommentNo(sequence);
		String loginId = (String) session.getAttribute("loginMemberId");
		
		if (loginId == null) {
	        throw new NeedPermissionException("로그인 후 이용 가능합니다."); 
	    }
		
		commentsDto.setCommentWriter(loginId);
		
		commentsDto.setCommentOrigin(0); 
	    commentsDto.setCommentDepth(0);
		
		commentsDao.insert(commentsDto);
	}
	
	@PostMapping("/delete")
	public void delete(@RequestParam int commentNo, HttpSession session) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		CommentsDto commentsDto = commentsDao.selectOne(commentNo);
		if(commentsDto == null) throw new TargetNotFoundException("존재하지 않는 댓글입니다");
		
		boolean owner = loginId.equals(commentsDto.getCommentWriter());
		if(owner == false) throw new NeedPermissionException("권한이 부족합니다");
		
		commentsDao.delete(commentNo);
	}
	
	@PostMapping("/edit")
	public void edit(@ModelAttribute CommentsDto commentsDto, HttpSession session) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		CommentsDto addDto = commentsDao.selectOne(commentsDto.getCommentNo());
		if(addDto == null) throw new TargetNotFoundException("존재하지 않는 댓글입니다");
		
		boolean owner = loginId.equals(addDto.getCommentWriter());
		if(owner == false) throw new NeedPermissionException("권한이 부족합니다");
		
		commentsDao.update(commentsDto);
	}
	
}
