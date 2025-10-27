package com.muzic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.condition.SearchCondition;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicViewDao;
import com.muzic.dao.PostDao;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MusicDto;
import com.muzic.dto.PostDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.MusicService;
import com.muzic.vo.MusicUserVO;
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
	@Autowired
	private MusicService musicService;
	@Autowired
	private MusicViewDao musicViewDao;
	
	@RequestMapping("/free/list")
	public String freelist(Model model, @ModelAttribute SearchCondition searchCondition) {
		
		//공지사항 검색
//		List<PostVO> postNotice = postDao.selectListNotice(pageVO);
//		size()으로 공지사항 개수 전달
//		model.addAttribute("postNotice", postNotice.size());
		
		List<PostVO> postList = postDao.selectFreeList(searchCondition);
		List<PostVO> result = new ArrayList<>();
		
//		result.addAll(postNotice);
		result.addAll(postList);
		
		model.addAttribute("postList", result);
		
		int dataCount = postDao.count(searchCondition);
		searchCondition.setAllData(dataCount);
		
		model.addAttribute("pageVO", searchCondition);
		
		return "/WEB-INF/views/post/free/list.jsp";
	}

	@RequestMapping("/mbti/list")
	public String mbtilist(Model model, @ModelAttribute SearchCondition searchCondition, HttpSession session) {
		
		//세션에서 로그인 아이디 및 회원 mbti 불러오기
	    String memberMbti = (String) session.getAttribute("loginMemberMbti");
	    String loginId = (String) session.getAttribute("loginMemberId");
	    
	    // MBTI 게시판은 MBTI 값이 없으면 접근할 수 없으므로, 값이 없는 경우 처리 로직 추가 (필요하다면 리다이렉트)
	    if (memberMbti == null || memberMbti.isBlank()) {
	        // memberMbti가 없으면 오류 페이지나 자유 게시판으로 리다이렉트 처리
	        // 여기서는 임시로 자유 게시판으로 리다이렉트. 실제 앱에서는 사용자 경험에 맞게 수정
//	        return "redirect:/post/free/list"; 
	    	throw new NeedPermissionException("권한이 부족합니다");
	    }
	    
	    MemberDto findDto = memberDao.selectByMemberId(loginId);
	    model.addAttribute("memberDto",findDto);
	    
	    List<PostVO> postList;

		if (searchCondition.getKeyword() != null && !searchCondition.getKeyword().isBlank()) {
			postList = postDao.selectMbtiListSearch(memberMbti, searchCondition);
		}
		else {
			postList = postDao.selectMbtiList(memberMbti, searchCondition);
		}
		
		model.addAttribute("postList", postList);

		int dataCount = postDao.countMbti(memberMbti, searchCondition);
		searchCondition.setAllData(dataCount);
		
		model.addAttribute("pageVO", searchCondition);
		
		return "/WEB-INF/views/post/mbti/list.jsp";
	}
	
	@GetMapping("/write")
	public String redirectToFreeWrite() {
	    return "redirect:free/write";
	}
	
	// URL 경로의 {boardType} 변수를 받아 처리
	// 예: /post/free/write, /post/mbti/write
	@GetMapping("/{postMbti}/write")
	public String unifiedWrite(@PathVariable String postMbti, Model model, HttpSession session) {
		
		List<MusicUserVO> musicList = musicService.findUserMusicList(new SearchCondition());
	    model.addAttribute("musicList", musicList);
		
		// 1. DTO 객체 생성 (View에서 postDto를 사용하므로 필요)
		PostDto postDto = new PostDto();
		
		// 2. MBTI 게시판일 경우에만 DTO에 MBTI 정보 설정
		if ("mbti".equals(postMbti)) {
			String memberMbti = (String) session.getAttribute("loginMemberMbti");
			
			// MBTI 정보가 없으면 글쓰기 불가 처리
			if (memberMbti == null || memberMbti.isBlank()) {
			    return "redirect:/post/free/list"; 
			}
			
			// DTO에 MBTI 값을 설정하여 View(write.jsp)로 전달 -> Hidden 필드와 목록보기 경로 설정에 사용됨
			postDto.setPostMbti(memberMbti);
		} 
		// "free"일 경우 postDto.postMbti는 null로 유지
		
		// 3. DTO를 View에 전달
		model.addAttribute("postDto", postDto);
		
		return "/WEB-INF/views/post/write.jsp";
	}
	
	//PostMapping으로 사용자가 작성 폼에서 입력한 데이터를 받아서 실제로 처리하고 저장
	@PostMapping("/write")
	public String write(@ModelAttribute PostDto postDto, HttpSession session) {
		
		//공지사항 처리 로직 (기존과 동일)
		String notice = postDto.getPostNotice();
		
	    if(notice == null || notice.isBlank()) { 
	        notice = "N";
	    }
	    notice = notice.toUpperCase(); 
	    postDto.setPostNotice(notice);
		
		// 관리자 권한 체크 (기존과 동일)
		String memberRole = (String) session.getAttribute("loginMemberRole");
		//만약 관리자가 아니고, 공지사항으로 등록한다면
		if(memberRole.equals("관리자") == false && postDto.getPostNotice().equals("Y")) {
			throw new NeedPermissionException("공지글 작성 권한이 없습니다");
		}
		
		// 세션에서 작성자 ID를 가져와 설정
		String loginId = (String) session.getAttribute("loginMemberId");
		if (loginId == null) {
			// 로그인하지 않은 경우 처리 필요
		    throw new NeedPermissionException("로그인 후 글 작성이 가능합니다.");
		}
        postDto.setPostWriter(loginId);
        
		// MBTI 값은 폼의 hidden 필드에서 넘어오거나 (MBTI 게시판) null/빈 문자열
		// 세션에서 강제로 덮어쓰지 않고, DTO에 담겨온 값을 그대로 사용
		String postMbti = postDto.getPostMbti();
		if (postMbti == null || postMbti.isBlank()) {
		    postDto.setPostMbti(null); // DB에 NULL로 저장되도록 명시적으로 설정
		} else {
		    // MBTI 게시판 글인 경우, DTO에 담겨온 값을 그대로 사용
		}
		
		int postNo = postDao.sequence();
		postDto.setPostNo(postNo);
		
		Integer musicNo = postDto.getPostMusic();

		// musicNo가 null일 수 있으므로, DAO insert에서 null 허용 확인
		postDto.setPostMusic(musicNo);
		
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
		
		MemberDto memberDto = memberDao.selectByMemberId(postDto.getPostWriter());
		
		// 만약 조회한 게시글이 null이라면 에러메세지 출력
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");
		
		Integer musicNo = postDto.getPostMusic();
	    
	    if (musicNo != null) {

	        MusicUserVO musicUserVO = musicViewDao.selectOneMusicUserVO(musicNo); 

	        model.addAttribute("musicUserVO", musicUserVO); 
	    }
		
		//회된 게시글 정보(postDto)를 postDto라는 이름으로 View에 전달
		//model에 일시적으로 데이터를 담아두고,
		//addAttribute로 데이터를 컨트롤러에서 뷰로 이동시키는 메서드
		model.addAttribute("postDto", postDto);
		model.addAttribute("memberDto", memberDto);
		
		return "/WEB-INF/views/post/detail.jsp";
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
