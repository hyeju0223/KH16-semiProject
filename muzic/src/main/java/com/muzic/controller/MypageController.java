package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicDao;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MusicDto;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage")
public class MypageController {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private MusicDao musicDao;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
	@GetMapping("/profile")
	public String profile(Model model,HttpSession session) {
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto memberDto = memberDao.selectByMemberId(loginId);
		//아이디로 음원 리스트 조회
		List<MusicDto> musicList = musicDao.selectBymemberId(memberDto.getMemberId());
		//memberDto와 musicList를 jsp로 넘길 수 있도록 model 설정 
		model.addAttribute("memberDto",memberDto);
		model.addAttribute("musicList",musicList);
		return "/WEB-INF/views/mypage/profile.jsp";
	}
	
	//회원 탈퇴
	@GetMapping("/withDraw") 
	public String withDraw(HttpSession session, Model model) {
		
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
//		if(findDto == null) throw new 
		
		//회원 정보를 jsp에 넘김
		model.addAttribute("findDto",findDto);
		
		return "/WEB-INF/views/mypage/withDraw.jsp";
	}
	
	@PostMapping("/withDraw")
	public String withDraw(@RequestParam String memberPw, HttpSession session
										,@RequestParam String agree) {
		
		//세션으로 loginId 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		
		//회원 탈퇴 (비밀번호 확인 후 회원 탈퇴)

		boolean  isVaild  = bCryptPasswordEncoder.matches(memberPw, findDto.getMemberPw());
		
		if(!isVaild) { //비밀번호가 틀리다면
			return "redirect:withDraw?error"; //파라미터에 error을 추가해서 '비밀번호를 확인해주세요' 문구 추가 (프론트)
		}
		//동의 버튼을 체크하지 않으면 다시 동일한 페이지로 반환
		if(!"on".equals(agree)) return "/mypage/withDraw.jsp";
		
//		memberDao.delete(findDto.getMemberId()); //DB에서 회원 정보 삭제 - 회원가입 귀찮아서 우선 주석처리..
//		session.removeAttribute("loginMemberId");
//		session.removeAttribute("loginMemberMbti");
//		session.removeAttribute("loginMemberRole");
		
		//회원 탈퇴 완료 후 굿바이 페이지로 리다이랙트
		return "redirect:bye";
	}
	
	@GetMapping("/bye")
	public String buy() {
		return "/WEB-INF/views/mypage/bye.jsp";
	}
	
	//비밀번호 변경 페이지
	
	@GetMapping("/password")
	public String password() {
		
		//예외처리

		return "/WEB-INF/views/mypage/password.jsp";
	}
	
	@PostMapping("/password")
	public String password(HttpSession session,
			@RequestParam String loginPw,
			@RequestParam String changePw,
			@RequestParam String changePw2) {
		
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		
		//현재 비밀번호와 입력한 현재 비밀번호가 일치하지 않는다면
		if(! bCryptPasswordEncoder.matches(loginPw, findDto.getMemberPw())) { 
			return "redirect:password?loginPwError";
		}
		
		//현재 비밀번호와 입력한 변경 비밀번호가 같다면
		if(loginPw.equals(changePw)||loginPw.equals(changePw2)) {
			return "redirect:password?samePwError";
		}
		
		//입력한 두 변경 비밀번호가 일치하지 않는다면
		if(! changePw.equals(changePw2)) {
			return "redirect:password?changePwError";
		}
		
		//해시 비밀번호로 변환 후 신규 비밀번호로 교체
		String hashed = bCryptPasswordEncoder.encode(changePw);		
		findDto.setMemberPw(hashed);
		//DB에 업데이트
		memberDao.updateMemberPw(findDto);
		
		return "redirect:profile";
	}
	
	@GetMapping("/edit")
	public String edit(HttpSession session, Model model) {
		
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		
		model.addAttribute("memberDto",findDto);

		return "/WEB-INF/views/mypage/edit.jsp";
	}
	
	@PostMapping("/edit")
	public String edit(HttpSession session,
								@ModelAttribute MemberDto memberDto) {
		
		//세션으로 회원 찾기
		String loginId = (String) session.getAttribute("loginMemberId");
		MemberDto findDto = memberDao.selectByMemberId(loginId);
		
		memberDto.setMemberId(loginId);
		memberDao.update(memberDto);
		
		return "redirect:profile";
	}
	
	
}
