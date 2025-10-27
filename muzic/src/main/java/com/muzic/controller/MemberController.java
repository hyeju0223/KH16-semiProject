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
import com.muzic.service.AttachmentService;
import com.muzic.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired private MemberDao memberDao;
    @Autowired private MemberLoginDao memberLoginDao;
    @Autowired private CertDao certDao;
    @Autowired private EmailService emailService;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private AttachmentService attachmentService;

    // ======================
    // 회원가입
    // ======================

    @GetMapping("/join")
    public String join() { return "/WEB-INF/views/member/join.jsp"; }

    @PostMapping("/join")
    public String join(@ModelAttribute MemberDto memberDto,
                       @RequestParam(required = false) MultipartFile attach)
            throws IOException, MessagingException {

        memberDao.insert(memberDto);

        if (attach != null && !attach.isEmpty()) {
            attachmentService.save(attach, memberDto.getMemberId(), "member");
        }

        emailService.sendWelcomeMail(memberDto);
        memberLoginDao.insert(memberDto.getMemberId());

        return "redirect:/member/joinFinish";
    }

    @RequestMapping("/joinFinish")
    public String joinFinish() { return "/WEB-INF/views/member/joinFinish.jsp"; }

    // ======================
    // 로그인 / 로그아웃
    // ======================

    @GetMapping("/login")
    public String login() { return "/WEB-INF/views/member/login.jsp"; }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDto memberDto, HttpSession session) {

        MemberDto findDto = memberDao.selectOne(memberDto.getMemberId());
        if (findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        MemberLoginDto loginInfo = memberLoginDao.selectOne(memberDto.getMemberId());
        if (loginInfo == null) {
            memberLoginDao.insert(memberDto.getMemberId());
            loginInfo = memberLoginDao.selectOne(memberDto.getMemberId());
        }

        if ("Y".equals(loginInfo.getLoginLocked())) {
            throw new NeedPermissionException("5회 이상 로그인 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.");
        }

        boolean isLogin = passwordEncoder.matches(memberDto.getMemberPw(), findDto.getMemberPw());
        if (!isLogin) {
            memberLoginDao.increaseFailCount(memberDto.getMemberId());
            MemberLoginDto updated = memberLoginDao.selectOne(memberDto.getMemberId());
            if (updated.getLoginFailCount() >= 5) {
                memberLoginDao.lockAccount(memberDto.getMemberId());
                throw new NeedPermissionException("비밀번호 5회 이상 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.");
            }
            throw new NeedPermissionException("비밀번호가 일치하지 않습니다.");
        }

        memberLoginDao.resetFailCount(memberDto.getMemberId());

        session.setAttribute("loginMemberId", findDto.getMemberId());
        session.setAttribute("loginMemberMbti", findDto.getMemberMbti());
        session.setAttribute("loginMemberRole", findDto.getMemberRole());
        session.setAttribute("loginMemberNickname", findDto.getMemberNickname());

        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ======================
    // 아이디 찾기 (이름+생년월일+이메일)
    // ======================

    @GetMapping("/findMemberId")
    public String findMemberId() { return "/WEB-INF/views/member/findMemberId.jsp"; }

    @PostMapping("/findMemberId")
    public String findMemberId(@RequestParam String memberName,
                               @RequestParam String memberBirth,
                               @RequestParam String memberEmail,
                               @RequestParam String certNumber,
                               Model model) {

        // 1) 이메일 인증번호 검증
        CertDto cert = certDao.selectOne(memberEmail);
        if (cert == null || !cert.getCertNumber().equals(certNumber))
            throw new NeedPermissionException("이메일 인증번호가 일치하지 않습니다.");

        // 2) 회원 조회
        MemberDto dto = memberDao.findIdByNameBirthEmail(memberName, memberBirth, memberEmail);
        model.addAttribute("memberId", dto != null ? dto.getMemberId() : null);

        // 3) 인증 소모
        certDao.delete(memberEmail);

        return "/WEB-INF/views/member/findMemberIdFinish.jsp";
    }

    @RequestMapping("/findMemberIdFinish")
    public String findMemberIdFinish() { return "/WEB-INF/views/member/findMemberIdFinish.jsp"; }

    // ======================
    // 비밀번호 찾기(본인확인 → 비밀번호 변경)
    //  - 페이지는 GET만; 인증은 REST에서 진행 후
    //    /member/changeMemberPw?memberId=...&certNumber=... 로 이동
    // ======================

    @GetMapping("/findMemberPw")
    public String findMemberPw() { return "/WEB-INF/views/member/findMemberPw.jsp"; }

    // (구)POST /findMemberPw 는 더 이상 사용하지 않음
    // 프론트에서 인증 성공 시 changeMemberPw 로 이동

    // ======================
    // 비밀번호 재설정
    // ======================

    @GetMapping("/changeMemberPw")
    public String changeMemberPw(@RequestParam String memberId,
                                 @RequestParam String certNumber,
                                 Model model) {

        MemberDto memberDto = memberDao.selectOne(memberId);
        if (memberDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        CertDto certDto = certDao.selectOne(memberDto.getMemberEmail());
        if (certDto == null) throw new NeedPermissionException("허가받지 않은 접근입니다.");

        // 인증번호 + 5분 제한
        if (!certDto.getCertNumber().equals(certNumber))
            throw new NeedPermissionException("인증번호가 일치하지 않습니다.");

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime created = certDto.getCertTime().toLocalDateTime();
        boolean timeValid = Duration.between(created, current).toSeconds() <= 300; // 5분
        if (!timeValid) throw new NeedPermissionException("인증정보가 만료되었습니다.");

        model.addAttribute("memberId", memberId);
        model.addAttribute("certNumber", certNumber);

        return "/WEB-INF/views/member/changeMemberPw.jsp";
    }

    @PostMapping("/changeMemberPw")
    public String changeMemberPw(@ModelAttribute MemberDto memberDto,
                                 @RequestParam String certNumber) {

        MemberDto findDto = memberDao.selectOne(memberDto.getMemberId());
        if (findDto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        CertDto certDto = certDao.selectOne(findDto.getMemberEmail());
        if (certDto == null) throw new NeedPermissionException("허가받지 않은 접근입니다.");

        if (!certDto.getCertNumber().equals(certNumber))
            throw new NeedPermissionException("인증번호가 일치하지 않습니다.");

        // 새 비밀번호 저장
        String encPw = passwordEncoder.encode(memberDto.getMemberPw());
        findDto.setMemberPw(encPw);
        memberDao.updateMemberPw(findDto);

        // 인증 소모
        certDao.delete(findDto.getMemberEmail());

        // ✅ 변경 후 비번찾기 페이지로 이동 (알림용 파라미터 포함)
        return "/WEB-INF/views/member/changeMemberPwFinish.jsp";
    }

    @RequestMapping("/changeMemberPwFinish")
    public String changeMemberPwFinish() {
        return "/WEB-INF/views/member/changeMemberPwFinish.jsp";
    }
}
