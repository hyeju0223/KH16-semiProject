package com.muzic.restcontroller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	@PostMapping("/list")
	public List<CommentsVO> list(@RequestParam int commentsPost, HttpSession session) {
		String loginId = (String)session.getAttribute("loginMemberId");
		
		PostDto postDto = postDao.selectOne(commentsPost);
		
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 글 정보 입니다");
		
		List<CommentsDto> list = commentsDao.selectList(commentsPost);
		List<CommentsVO> result = new ArrayList<>(); //빈 목록 생성
		
		//boolean ownwe, writer;
		
		for(CommentsDto commentsDto : list) {
			boolean owner = loginId != null 
					&& commentsDto.getCommentsWriter() != null 
					&& loginId.equals(commentsDto.getCommentsWriter());
			boolean writer = postDto.getPostWriter() != null
					&& commentsDto.getCommentsWriter() != null
					&& postDto.getPostWriter().equals(commentsDto.getCommentsWriter());
			
			result.add(CommentsVO.builder()
					.commentsNo(commentsDto.getCommentsNo())
					.commentsPost(commentsDto.getCommentsPost())
					.commentsWriter(commentsDto.getCommentsWriter())
					.commentsContent(commentsDto.getCommentsContent())
					.commentsWtime(commentsDto.getCommentsWtime())
					.commentsEtime(commentsDto.getCommentsEtime())
					.commentsLike(commentsDto.getCommentsLike())
					.commentsOrigin(commentsDto.getCommentsOrigin())
					.commentsDepth(commentsDto.getCommentsDepth())
					.owner(owner)
					.writer(writer)
				.build());
		}
		return result;
	}
	
	@PostMapping("/write")
	public void write(@ModelAttribute CommentsDto commentsDto, HttpSession session) {
		int sequence = commentsDao.sequence();
		
		commentsDto.setCommentsNo(sequence);
		String loginId = (String) session.getAttribute("loginMemberId");
		commentsDto.setCommentsWriter(loginId);
		
		commentsDao.insert(commentsDto);
	}
	
	@PostMapping("/delete")
	public void delete(@RequestParam int commentsNo, HttpSession session) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		CommentsDto commentsDto = commentsDao.selectOne(commentsNo);
		if(commentsDto == null) throw new TargetNotFoundException("존재하지 않는 댓글입니다");
		
		boolean owner = loginId.equals(commentsDto.getCommentsWriter());
		if(owner == false) throw new NeedPermissionException("권한이 부족합니다");
		
		commentsDao.delete(commentsNo);
	}
	
	@PostMapping("/edit")
	public void edit(@ModelAttribute CommentsDto commentsDto, HttpSession session) {
		String loginId = (String) session.getAttribute("loginMemberId");
		
		CommentsDto addDto = commentsDao.selectOne(commentsDto.getCommentsNo());
		if(addDto == null) throw new TargetNotFoundException("존재하지 않는 댓글입니다");
		
		boolean owner = loginId.equals(addDto.getCommentsWriter());
		if(owner == false) throw new NeedPermissionException("권한이 부족합니다");
		
		commentsDao.update(commentsDto);
	}
	
}
