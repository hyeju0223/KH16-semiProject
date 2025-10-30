package com.muzic.restcontroller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.muzic.dao.CertDao;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MemberLoginDao;
import com.muzic.dto.CertDto;
import com.muzic.dto.MemberDto;
import com.muzic.dto.MemberLoginDto;
import com.muzic.service.EmailService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/rest/member")
public class MemberRestController {

    @Autowired private MemberDao memberDao;
    @Autowired private CertDao certDao;
    @Autowired private EmailService emailService;

    @Autowired private MemberLoginDao memberLoginDao;
    @Autowired private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/checkMemberId")
    public boolean checkMemberId(@RequestParam String memberId) {
        return memberDao.existsById(memberId);
    }

    @GetMapping("/checkMemberNickname")
    public boolean checkMemberNickname(@RequestParam String memberNickname) {
        return memberDao.existsByNickname(memberNickname);
    }

    @PostMapping("/certSend")
    public void certSend(@RequestParam String certEmail) {
        emailService.sendCertNumber(certEmail);
    }

    // 인증번호 확인(삭제는 컨트롤러 최종 단계에서)
    @PostMapping("/certCheck")
    public boolean certCheck(@ModelAttribute CertDto certDto) {
        var stored = certDao.selectOne(certDto.getCertEmail());
        if (stored == null) return false;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sent = stored.getCertTime().toLocalDateTime();
        if (Duration.between(sent, now).toSeconds() > 300) return false;

        return certDto.getCertNumber().equals(stored.getCertNumber());
    }

    // 아이디 찾기: 이름+생년월일+이메일 일치 시 인증번호 발송
    @PostMapping("/findIdCertSend")
    public boolean findIdCertSend(@RequestParam String memberName,
                                  @RequestParam String memberBirth,
                                  @RequestParam String memberEmail) {
        var found = memberDao.findIdByNameBirthEmail(memberName, memberBirth, memberEmail);
        if (found == null) return false;
        emailService.sendCertNumber(memberEmail);
        return true;
    }

    // 비밀번호 변경: 아이디+이름+생년월일+이메일 일치 시 인증번호 발송
    @PostMapping("/findPwCertSend")
    public boolean findPwCertSend(@RequestParam String memberId,
                                  @RequestParam String memberName,
                                  @RequestParam String memberBirth,
                                  @RequestParam String memberEmail) {
        var found = memberDao.findByIdNameBirthEmail(memberId, memberName, memberBirth, memberEmail);
        if (found == null) return false;
        emailService.sendCertNumber(memberEmail);
        return true;
    }
 // MemberRestController.java  (/rest/member/login)
    @GetMapping(value="/checkMemberEmail", produces="application/json")
    public boolean checkMemberEmail(@RequestParam String memberEmail){
        return memberDao.existsByEmail(memberEmail); // true = 중복
    }
 // MemberRestController.java  (/rest/member/login)
    @PostMapping("/login")
    public Map<String, Object> loginRest(
            @RequestParam String memberId,
            @RequestParam String memberPw,
            HttpSession session) {

        Map<String, Object> res = new HashMap<>();

        MemberDto user = memberDao.selectOne(memberId);
        if (user == null) {
            res.put("ok", false);
            res.put("code", "no_user");
            res.put("msg", "존재하지 않는 아이디입니다."); // ← 문구 변경
            // remain 안 보냄!
            return res;
        }

        MemberLoginDto loginInfo = memberLoginDao.selectOne(memberId);
        if (loginInfo == null) {
            memberLoginDao.insert(memberId);
            loginInfo = memberLoginDao.selectOne(memberId);
        }

        if ("Y".equals(loginInfo.getLoginLocked())) {
            res.put("ok", false);
            res.put("code", "locked");
            res.put("msg", "5회 이상 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.");
            // remain 안 보내도 됨 (보내고 싶으면 0)
            return res;
        }

        boolean match = passwordEncoder.matches(memberPw, user.getMemberPw());
        if (!match) {
            memberLoginDao.increaseFailCount(memberId);
            MemberLoginDto after = memberLoginDao.selectOne(memberId);
            int fail = after.getLoginFailCount(); // (DAO에서 NULL 안 나오도록 보장)

            if (fail >= 5) {
                memberLoginDao.lockAccount(memberId);
                res.put("ok", false);
                res.put("code", "locked");
                res.put("msg", "5회 이상 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.");
                return res;
            }

            int remain = Math.max(5 - fail, 0);
            res.put("ok", false);
            res.put("code", "bad_pw");
            res.put("msg", "비밀번호가 일치하지 않습니다.");
            res.put("remain", remain); // ← 남은 횟수는 '비번 틀림'일 때만 보냄
            return res;
        }

        memberLoginDao.resetFailCount(memberId);
        session.setAttribute("loginMemberId", user.getMemberId());
        session.setAttribute("loginMemberMbti", user.getMemberMbti());
        session.setAttribute("loginMemberRole", user.getMemberRole());
        session.setAttribute("loginMemberNickname", user.getMemberNickname());

        res.put("ok", true);
        return res;
    }
}