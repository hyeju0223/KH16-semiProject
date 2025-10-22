package com.muzic.controller;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.CertDao;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MemberLoginDao;
import com.muzic.dto.CertDto;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MemberLoginDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberLoginDao memberLoginDao;
    @Autowired
    private CertDao certDao;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    
    // 회원가입 (멀티페이지)
    
    @GetMapping("/join")
    public String join() {
        return "/WEB-INF/views/member/join.jsp";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDto memberDto,
                       @RequestParam(required = false) MultipartFile attach)
            throws IOException, MessagingException {

        memberDao.insert(memberDto);
        emailService.sendWelcomeMail(memberDto);

    
        memberLoginDao.insert(memberDto.getMemberId());

        return "redirect:/music/add";
    }

    @RequestMapping("/joinFinish")
    public String joinFinish() {
        return "/WEB-INF/views/member/joinFinish.jsp";
    }

    // ======================
    // 로그인
    // ======================
    @GetMapping("/login")
    public String login() {
        return "/WEB-INF/views/member/login.jsp";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDto memberDto, HttpSession session) {

        // 회원 존재 여부 확인
        MemberDto findDto = memberDao.selectOne(memberDto.getMemberId());
        if (findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        // 로그인 상태 조회
        MemberLoginDto loginInfo = memberLoginDao.selectOne(memberDto.getMemberId());
        if (loginInfo == null) {
            memberLoginDao.insert(memberDto.getMemberId());
            loginInfo = memberLoginDao.selectOne(memberDto.getMemberId());
        }

        // 계정 잠금 여부 확인
        if ("Y".equals(loginInfo.getLoginLocked())) {
            throw new NeedPermissionException("5회 이상 로그인 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.");
        }

        //  비밀번호 검증
        boolean isLogin = passwordEncoder.matches(memberDto.getMemberPw(), findDto.getMemberPw());

        if (!isLogin) {
            // 실패 횟수 증가
            memberLoginDao.increaseFailCount(memberDto.getMemberId());

            // 5회 이상 실패 시 계정 잠금
            MemberLoginDto updated = memberLoginDao.selectOne(memberDto.getMemberId());
            if (updated.getLoginFailCount() >= 5) {
                memberLoginDao.lockAccount(memberDto.getMemberId());
                throw new NeedPermissionException("비밀번호 5회 이상 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.");
            }
            throw new NeedPermissionException("비밀번호가 일치하지 않습니다.");
        }

        //  로그인 성공 → 실패 기록 초기화
        memberLoginDao.resetFailCount(memberDto.getMemberId());

        // 세션 저장
        session.setAttribute("loginMemberId", findDto.getMemberId());
     
      
        session.setAttribute("loginMemberMbti", findDto.getMemberMbti());
    
      
        session.setAttribute("loginMemberRole", findDto.getMemberRole());
        
        session.setAttribute("loginMemberNickName", findDto.getMemberNickname());

        return "redirect:/music/add";
    }

    
    // 로그아웃

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    // 아이디 찾기

    @GetMapping("/findMemberId")
    public String findMemberId() {
        return "/WEB-INF/views/member/findMemberId.jsp";
    }

    @PostMapping("/findMemberId")
    public String findMemberId(@ModelAttribute MemberDto memberDto) {
        MemberDto findDto = memberDao.findIdByNicknameAndEmail(
                memberDto.getMemberNickname(),
                memberDto.getMemberEmail()
        );

        if (findDto == null)
            throw new TargetNotFoundException("해당 닉네임과 이메일 조합의 회원이 존재하지 않습니다.");

        emailService.sendEmail(
                findDto.getMemberEmail(),
                "[muzic] 아이디 찾기 결과",
                "안녕하세요.\n회원님의 아이디는 [" + findDto.getMemberId() + "] 입니다."
        );

        return "redirect:findMemberIdFinish";
    }

    @RequestMapping("/findMemberIdFinish")
    public String findMemberIdFinish() {
        return "/WEB-INF/views/member/findMemberIdFinish.jsp";
    }


    // 비밀번호 찾기
  
    @GetMapping("/findMemberPw")
    public String findMemberPw() {
        return "/WEB-INF/views/member/findMemberPw.jsp";
    }

    @PostMapping("/findMemberPw")
    public String findMemberPw(@ModelAttribute MemberDto memberDto)
            throws MessagingException, IOException {

        MemberDto findDto = memberDao.findPwByIdNicknameEmail(
                memberDto.getMemberId(),
                memberDto.getMemberNickname(),
                memberDto.getMemberEmail()
        );

        if (findDto == null)
            throw new TargetNotFoundException("일치하는 회원 정보를 찾을 수 없습니다.");

        emailService.sendResetPassword(findDto);
        return "redirect:findMemberPwFinish";
    }

    @RequestMapping("/findMemberPwFinish")
    public String findMemberPwFinish() {
        return "/WEB-INF/views/member/findMemberPwFinish.jsp";
    }

    // ======================
    // 비밀번호 재설정
    // ======================
    @GetMapping("/changeMemberPw")
    public String changeMemberPw(@RequestParam String memberId,
                                 @RequestParam String certNumber,
                                 Model model) {

        MemberDto memberDto = memberDao.selectOne(memberId);
        if (memberDto == null)
            throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        CertDto certDto = certDao.selectOne(memberDto.getMemberEmail());
        if (certDto == null)
            throw new NeedPermissionException("허가받지 않은 접근입니다.");

        boolean numberValid = certDto.getCertNumber().equals(certNumber);
        if (!numberValid)
            throw new NeedPermissionException("인증번호가 일치하지 않습니다.");

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime created = certDto.getCertTime().toLocalDateTime();
        Duration duration = Duration.between(created, current);
        boolean timeValid = duration.toSeconds() <= 600;
        if (!timeValid)
            throw new NeedPermissionException("인증정보가 만료되었습니다.");

        model.addAttribute("memberId", memberId);
        model.addAttribute("certNumber", certNumber);

        return "/WEB-INF/views/member/changeMemberPw.jsp";
    }

    @PostMapping("/changeMemberPw")
    public String changeMemberPw(@ModelAttribute MemberDto memberDto,
                                 @RequestParam String certNumber) {

        MemberDto findDto = memberDao.selectOne(memberDto.getMemberId());
        if (findDto == null)
            throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        CertDto certDto = certDao.selectOne(findDto.getMemberEmail());
        if (certDto == null)
            throw new NeedPermissionException("허가받지 않은 접근입니다.");

        boolean numberValid = certDto.getCertNumber().equals(certNumber);
        if (!numberValid)
            throw new NeedPermissionException("인증번호가 일치하지 않습니다.");

        String encPw = passwordEncoder.encode(memberDto.getMemberPw());
        findDto.setMemberPw(encPw);
        memberDao.updateMemberPw(findDto);

        certDao.delete(findDto.getMemberEmail());

        return "redirect:changeMemberPwFinish";
    }

    @RequestMapping("/changeMemberPwFinish")
    public String changeMemberPwFinish() {
        return "/WEB-INF/views/member/changeMemberPwFinish.jsp";
    }
}
