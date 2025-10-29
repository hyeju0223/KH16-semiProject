package com.muzic.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.muzic.dao.PostDao;
import com.muzic.dto.PostDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class PostOwnerInterceptor implements HandlerInterceptor{
	@Autowired
	private PostDao postDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception{
		HttpSession session = request.getSession();
		String loginMemberRole = (String)session.getAttribute("loginMemberRole");
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		
		//관리자가 삭제 페이지로 가는 경우 통과
		String uri = request.getRequestURI();
		if (loginMemberRole.equals("관리자") && uri.equals("/post/delete")) {
			return true;
		}
		
		//게시글 작성자가 아닌 경우 차단
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		PostDto postDto = postDao.selectOne(postNo);
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시글");
		if(loginMemberId.equals(postDto.getPostWriter()) == false) throw new NeedPermissionException("본인 글만 수정 및 삭제가 가능합니다");
		
		return true; //나머지 통과
	}

}
