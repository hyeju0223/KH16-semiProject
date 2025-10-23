package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.MemberDao;
import com.muzic.dao.PostDao;
import com.muzic.dto.MemberDto;
import com.muzic.dto.PostDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
//import com.muzic.vo.PageVO;
import com.muzic.vo.PostVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
//컨트롤러에 일 많이 시키지 말기..........
public class PostController {

	@Autowired
	private PostDao postDao;
	@Autowired
	private MemberDao memberDao;
	
	
//	@RequestMapping("/free/list")
//	public String freelist(Model model, @ModelAttribute PageVO pageVO) {
//		
//		List<PostVO> postNotice = postDao.selectListNotice();
//		
//		return "/WEB-INF/views/post/free/list.jsp";
//	}
//
//	@RequestMapping("/mbti/list")
//	public String mbtilist(Model model, @RequestParam(required = false) String column,
//			@RequestParam(required = false) String keyword, HttpSession session) {
//		
//		//세션에서 로그인 아이디 및 회원 mbti 불러오기
//	    String memberMbti = (String) session.getAttribute("loginMemberMbti");
//	    String loginId = (String) session.getAttribute("loginMemberId");
//	    
//	    MemberDto findDto = memberDao.selectByMemberId(loginId);
//	    model.addAttribute("memberDto",findDto);
//	    
//		//column, keyword가 null이 아닐시 true
//		boolean isSearch = column != null && keyword != null;
//		
//		if(!isSearch) { 
//			//!isSearch가 false일 시 파라미터 전달 X
//			List<PostDto> postList = postDao.selectMbtiList(memberMbti);
//			// DB에서 조회한 게시글 목록을 postList에 담아 View(JSP 등)에 전달
//			model.addAttribute("postList", postList);
//
//		}
//		else {
//			//!isSearch 가 true일시 파라미터 전달
//			List<PostDto> postList = postDao.selectMbtiList(memberMbti, column, keyword);
//			// DB에서 조회한 게시글 목록을 postList에 담아 View(JSP 등)에 전달
//			model.addAttribute("postList", postList);
//		}
//		
//		return "/WEB-INF/views/post/mbti/list.jsp";
//	}
	
	//GetMapping으로 작성하기 폼 요청
	@GetMapping("/write")
	public String write() {
		return "/WEB-INF/views/post/write.jsp";
	}
	
	//PostMapping으로 사용자가 작성 폼에서 입력한 데이터를 받아서 실제로 처리하고 저장
	@PostMapping("/write")
	public String write(@ModelAttribute PostDto postDto, HttpSession session) {
		//공지사항
		//notice변수에 dto에 있는 PostNotice정보 담기
		String notice = postDto.getPostNotice();
		
		//notice가 null이라면 공지사항 X
		if(notice == null) {notice = "no";}
		notice = notice.toLowerCase();// 데이터를 소문자로 통일하여 저장
		
		//관리자가 아닐시 권한 부족
		//session에 있는 로그인 등급 꺼내서
		String memberRole = (String) session.getAttribute("memberRole");
		//만약 관리자가 아니고, 공지사항으로 등록한다면
		if(memberRole.equals("관리자") == false && postDto.getPostNotice().equals("Y")) {
			//권한 부족 에러 메세지 출력
			throw new NeedPermissionException("공지글 작성 권한이 없습니다");
		}
		
		//session에서 값 꺼내기
		String loginId = (String) session.getAttribute("loginMemberId");
		String memberMbti = (String) session.getAttribute("loginMemberMbti");
		
		//만약 로그인 session에 담긴 정보가 null이 아니라면
		if (loginId != null) {
			// DTO에 작성자 아이디와 MBTI 설정
	        postDto.setPostWriter(loginId);
	        postDto.setPostMbti(memberMbti);
	    }
		
		int postNo = postDao.sequence();
		postDto.setPostNo(postNo);
		
		//postDto를 데이터베이스에 삽입
		postDao.insert(postDto);
		
		return "redirect:detail?postNo="+postNo;
	}
	
	//상세는 단일
	//상세보기
	@RequestMapping("detail")
	public String detail(Model model, @RequestParam int postNo) {
		
		//게시글 번호로 DB에서 해당 게시글 정보를 조회
		PostDto postDto = postDao.selectOne(postNo);
		
		// 만약 조회한 게시글이 null이라면 에러메세지 출력
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");
		
		//회된 게시글 정보(postDto)를 postDto라는 이름으로 View에 전달
		//model에 일시적으로 데이터를 담아두고,
		//addAttribute로 데이터를 컨트롤러에서 뷰로 이동시키는 메서드
		model.addAttribute("postDto", postDto);

		
		 return "/WEB-INF/views/post/detail.jsp";
//		return "redirect:detail?postNo=" + postNo;
	}
	
	//수정하기
	//GetMapping으로 수정 폼 달라고 요청
	@GetMapping("/edit")
	public String edit(Model model, @RequestParam int postNo) {
		
		//게시글 번호로 DB에서 해당 게시글 정보를 조회
		PostDto postDto = postDao.selectOne(postNo);
		
		// 만약 조회한 게시글이 null이라면 에러메세지 출력
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");

		//회된 게시글 정보(postDto)를 postDto라는 이름으로 View에 전달
		model.addAttribute("postDto", postDto);
		
		return "/WEB-INF/views/post/edit.jsp";
	}
	
	//PostMapping으로 사용자가 수정 폼에서 입력한 데이터를 받아서 실제로 처리하고 저장
	@PostMapping("/edit")
	public String edit(@ModelAttribute PostDto postDto) {
		
		//postDto를 데이터베이스에 삽입
		postDao.update(postDto);
		
		return "redirect:detail?postNo="+postDto.getPostNo();
	}
	
	//삭제
	@RequestMapping("/delete")
	public String delete(@RequestParam int postNo) {
		
		//게시글 번호로 DB에서 해당 게시글 정보를 조회
		PostDto postDto = postDao.selectOne(postNo);
		
		// 만약 조회한 게시글이 null이라면 에러메세지 출력
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");
		
		//postNo를 데이터베이스에 삽입
		postDao.delete(postNo);
		
		if(postDto.getPostMbti() != null) {
			return "redirect:mbti/list";
		}
		else {
			return "redirect:free/list";			
		}
	}
}
