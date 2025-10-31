package com.muzic.aop;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;

import com.muzic.dao.PostDao;
import com.muzic.dto.PostDto;
import com.muzic.error.TargetNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//게시글 조회할때 조회수 증가를 위한 인터셉터
@Service
public class PostReadInterceptor implements HandlerInterceptor{

	@Autowired
	private PostDao postDao;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		//현재 로그인 된 ID 세션 가져오기
		HttpSession session = request.getSession(); 
		String loginId = (String) session.getAttribute("loginMemberId");
		
		//파라미터로 게시글 번호 가져오기
		int postNo = Integer.parseInt(request.getParameter("postNo"));
		
		//DB에서 해당 글 벙조 조회
		PostDto postDto = postDao.selectOne(postNo);
		
		//만약 DB에 게시글이 존재하지 않으면 에러
		if(postDto == null) throw new TargetNotFoundException("게시글 정보가 존재하지 않습니다");
		
		//세션 정보가 null이 아니고, 작성자가 null이 아니라면
		if(loginId != null && postDto.getPostWriter() != null) {
			//세션에 담긴 id가 작성자라면
			if(loginId.equals(postDto.getPostWriter())) {
				//조회수 증가X, 그냥 통과
				return true;
			}
		}
		
		//이미 읽었던 게시글이라면
		//현재 사용자의 세션에 읽은 게시물 번호 목록 가져오기
		//Set :  중복을 허용하지 않는 데이들의 집합
		//(Set<Integer>) : 세션에 저장된 객체의 타입을 Object → Integer 로 형변환
		Set<Integer> history = (Set<Integer>)session.getAttribute("history");
		//만약 세션에 기록이 없을시 새로 생성
		if(history == null) history = new HashSet<>();
		
		 //저장소에 글 번호가 있다면  통과
		if(history.contains(postNo)) return true;
		//글 번호가 없다면 
		else
			//현재 글 번호를 추가
			history.add(postNo);
		//변경된 정보를 세션에 다시 저장
		session.setAttribute("history", history);
		
		//위에서 차단되지 않았다면 조회수 증가
		postDao.postRead(postNo);
		
		return true;
	}
}
