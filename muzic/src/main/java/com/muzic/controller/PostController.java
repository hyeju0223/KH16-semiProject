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
import com.muzic.dao.AttachmentDao;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicViewDao;
import com.muzic.dao.PostDao;
import com.muzic.dto.MemberDto;
import com.muzic.dto.PostDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;
import com.muzic.service.MusicService;
import com.muzic.vo.MusicUserVO;
import com.muzic.vo.PostVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/post")
public class PostController {

	@Autowired
	private PostDao postDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MusicService musicService;
	@Autowired
	private MusicViewDao musicViewDao;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
	private AttachmentService attachmentService;
	
	@RequestMapping("/free/list")
	public String freelist(Model model, @ModelAttribute SearchCondition searchCondition) {
		
		//검색 조건에 맞는 자유게시판 목록 조회
		List<PostVO> postList = postDao.selectFreeList(searchCondition);
		//빈 목록 생성 (공지사항, 게시글을 하나의 목록으로 합쳐서 보여주기 위해서)
		List<PostVO> result = new ArrayList<>();
		
		//공지사항과 자유게시판 목록 합치기
		result.addAll(postList);
		model.addAttribute("postList", result);
		
		//전체 데이터 개수를 조회하여 페이징 조건에 설정
		int dataCount = postDao.count(searchCondition);
		//전체 데이터 수를 설정해 체이지 계산
		searchCondition.setAllData(dataCount);
		
		//페이징, 검색 조건 뷰에 전당
		model.addAttribute("searchCondition", searchCondition);
		
		return "/WEB-INF/views/post/free/list.jsp";
	}

	@RequestMapping("/mbti/list")
	public String mbtilist(Model model, @ModelAttribute SearchCondition searchCondition, HttpSession session) {
		
		//세션에서 로그인 아이디 및 회원 mbti 불러오기
	    String memberMbti = (String) session.getAttribute("loginMemberMbti");
	    String loginId = (String) session.getAttribute("loginMemberId");
	    
	    // MBTI 게시판은 MBTI 값이 없으면 접근할 수 없으므로, 값이 없는 경우 처리 로직 추가 (필요하다면 리다이렉트)
	    if (memberMbti == null || memberMbti.isBlank()) {
	    	//리다이렉트 → 권한 부족 예외 발생으로 로직 수정
	    	throw new NeedPermissionException("권한이 부족합니다");
	    }
	    
	    //추가 회원 정보 조회
	    MemberDto findDto = memberDao.selectByMemberId(loginId);
	    model.addAttribute("memberDto",findDto);
	    
	    //빈 리스트 변수 하나 생성
	    List<PostVO> postList;

	    //만약 검색 키워드가 null이거나 값이 있다면
		if (searchCondition.getKeyword() != null && !searchCondition.getKeyword().isBlank()) {
			//검색어 O: MBTI 필터링 + 검색 적용
			postList = postDao.selectMbtiListSearch(memberMbti, searchCondition);
		}
		else {
			//검색어 X: MBTI 필터링 + 검색 적용
			postList = postDao.selectMbtiList(memberMbti, searchCondition);
		}
		
		//뷰에 검색겂 전달
		model.addAttribute("postList", postList);

		// MBTI 조건에 맞는 전체 데이터 개수 조회 및 페이징 조건 설정
		int dataCount = postDao.countMbti(memberMbti, searchCondition);
		searchCondition.setAllData(dataCount);
		
		model.addAttribute("searchCondition", searchCondition);
		
		return "/WEB-INF/views/post/mbti/list.jsp";
	}
	
	@GetMapping("/write")
	public String redirectToFreeWrite() {
//		사용자가 /post/write로 접속했을 때, 기본적으로 자유 게시판과 글쓰기 폼으로 연결해 주기 위한 코드
		return "redirect:free/write";
	}
	
	// {postMbti} 부분을 postMbti 변수로 받아서 사용 (free/ mbti)
	// ex: /post/free/write, /post/mbti/write
	@GetMapping("/{postMbti}/write")
	public String unifiedWrite(@PathVariable String postMbti, Model model, HttpSession session) {
		
		//사용 가능한 음악 목록을 조회(글에 음악 첨부용)
		List<MusicUserVO> musicList = musicService.findUserMusicList(new SearchCondition());
		//뷰에 전달
	    model.addAttribute("musicList", musicList);
		
		//DTO 객체 생성
		PostDto postDto = new PostDto();
		
		//MBTI 게시판일 경우에만 DTO에 MBTI 정보 설정
		if ("mbti".equals(postMbti)) {
			//세션에 담긴 회원 mbti정보 가져오기
			String memberMbti = (String) session.getAttribute("loginMemberMbti");
			
			//MBTI 정보가 없으면 글쓰기 불가 처리
			if (memberMbti == null || memberMbti.isBlank()) {
				//예외처리
				throw new TargetNotFoundException("로그인이 필요합니다");
				//자유게시판으로 우회
//			    return "redirect:/post/free/list"; 
			}
			
			// DTO에 MBTI 값을 설정하여 뷰로 전달
			postDto.setPostMbti(memberMbti);
		} 
		// free일 경우 postDto.postMbti는 null로 유지
		
		//DTO를 뷰에 전달
		model.addAttribute("postDto", postDto);
		
		return "/WEB-INF/views/post/write.jsp";
	}
	
	//PostMapping으로 사용자가 작성 폼에서 입력한 데이터를 받아서 실제로 처리하고 저장
	@PostMapping("/write")
	public String write(@ModelAttribute PostDto postDto,
			@RequestParam(required = false) String postType,
			@RequestParam(required = false) List<Integer> attachmentNo, HttpSession session) {
		
		//공지사항 처리 로직 (기존과 동일)
		String notice = postDto.getPostNotice();
		//값이 null이거나 빈 문자열일시 공지사항 'N'
	    if(notice == null || notice.isBlank()) { 
	        notice = "N";
	    }
	    //대소문자 신경X
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
			// 로그인하지 않은 경우 예외
		    throw new NeedPermissionException("로그인 후 글 작성이 가능합니다.");
		}
        postDto.setPostWriter(loginId);
        
//		String postMbti = postDto.getPostMbti();
		
        //회원 mbti 세션 가져오기
		String postMbti = (String) session.getAttribute("loginMemberMbti");
		
		//만약 게시판 타입이 mbti라면 세션 mbti정보 가져오기
		if ("mbti".equals(postType)) {
	        String memberMbti = (String) session.getAttribute("loginMemberMbti");
	        postDto.setPostMbti(memberMbti);
	    }
		else {
			//아니라면 자유게시판
	        postDto.setPostMbti(null);
	    }
		
		//게시글 조회 시퀀스 조회 및 설정
		int postNo = postDao.sequence();
		postDto.setPostNo(postNo);
		
		//음악 번호 설정
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
		//게시글 작성자 정보 조회
		MemberDto memberDto = memberDao.selectByMemberId(postDto.getPostWriter());
		
		// 만약 조회한 게시글이 null이라면 에러메세지 출력
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");
		
		//첨부된 음악 정보 조회
		Integer musicNo = postDto.getPostMusic();
	    
		//만약 정보가 null이 아니라면
	    if (musicNo != null) {

	    	//음악 번호로 음악 정보 조회
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
	public String edit(Model model, @RequestParam int postNo, HttpSession session) {
	    
		//회원Id 세션 정보 조회
		String loginId = (String) session.getAttribute("loginMemberId");
		
	    //게시글 번호로 DB에서 해당 게시글 정보를 조회
	    PostDto postDto = postDao.selectOne(postNo);
	    
	    //만약 작성자 정보가 세션정보와 일치하지 않는다면
	    if(!postDto.getPostWriter().equals(loginId)) {
	    	//예외처리
		    throw new NeedPermissionException("본인 글만 수정이 가능합니다.");
		}
	    
	    // 만약 조회한 게시글이 null이라면 에러메세지 출력
	    if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");
	            
	    // 전체 음악 목록을 조회하여 Model에 추가
	    List<MusicUserVO> musicList = musicService.findUserMusicList(new SearchCondition()); 
	    model.addAttribute("musicList", musicList);
	    
	    //회된 게시글 정보(postDto)를 postDto라는 이름으로 View에 전달
	    model.addAttribute("postDto", postDto);
	    
	    return "/WEB-INF/views/post/edit.jsp";
	}
	
	//PostMapping으로 사용자가 수정 폼에서 입력한 데이터를 받아서 처리하고 저장
	@PostMapping("/edit")
	public String edit(@ModelAttribute PostDto postDto) {
		
		//DB 업데이트
		postDao.update(postDto);
		
		return "redirect:detail?postNo="+postDto.getPostNo();
	}
	
	//삭제
	@RequestMapping("/delete")
	public String delete(@RequestParam int postNo, HttpSession session) {
		
		//회원 ID 세션 정보 조회
		String loginId = (String) session.getAttribute("loginMemberId");
		
		//게시글 번호로 DB에서 해당 게시글 정보를 조회
		PostDto postDto = postDao.selectOne(postNo);
		
		//만약 작성자가 세션 정보와 일치하지 않는다면
		if(!postDto.getPostWriter().equals(loginId)) {
			//예외처리
		    throw new NeedPermissionException("본인 글만 삭제가 가능합니다.");
		}
		
		// 만약 조회한 게시글이 null이라면 에러메세지 출력
		if(postDto == null) throw new TargetNotFoundException("존재하지 않는 게시물입니다");
		
		//DB업데이트
		postDao.delete(postNo);
		
		//만약 mbti정보가 null이 아니라면
		if(postDto.getPostMbti() != null) {
			//mbti 게시판으로 리다이렉트
			return "redirect:mbti/list";
		}
		else {
			//자유게시판으로 리다이렉트
			return "redirect:free/list";			
		}
	}
}
