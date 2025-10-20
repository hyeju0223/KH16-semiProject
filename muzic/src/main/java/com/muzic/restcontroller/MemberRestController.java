package com.muzic.restcontroller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dao.CertDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.CertDto;
import com.muzic.dto.MemberDto;
import com.muzic.service.EmailService;

@CrossOrigin
@RestController
@RequestMapping("/rest/member")
public class MemberRestController {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private CertDao certDao;

    @Autowired
    private EmailService emailService;

    // ✅ 아이디 중복 검사
    @GetMapping("/checkMemberId")
    public boolean checkMemberId(@RequestParam String memberId) {
        return memberDao.existsById(memberId);
    }

    // ✅ 닉네임 중복 검사
    @GetMapping("/checkMemberNickname")
    public boolean checkMemberNickname(@RequestParam String memberNickname) {
        return memberDao.existsByNickname(memberNickname);
    }

    // ✅ 이메일 인증번호 발송
    @PostMapping("/certSend")
    public void certSend(@RequestParam String certEmail) {
        emailService.sendCertNumber(certEmail);
    }

    // ✅ 이메일 인증번호 확인 (유효시간 5분)
    @PostMapping("/certCheck")
    public boolean certCheck(@ModelAttribute CertDto certDto) {
        // 1️⃣ 이메일로 인증정보 조회
        CertDto findDto = certDao.selectOne(certDto.getCertEmail());
        if (findDto == null) return false; // 인증메일을 보낸 적 없음

        // 2️⃣ 유효시간 검사 (5분 제한)
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime sent = findDto.getCertTime().toLocalDateTime();
        Duration duration = Duration.between(sent, current);
        if (duration.toSeconds() > 300) return false; // 5분 초과

        // 3️⃣ 인증번호 일치 확인
        boolean isValid = certDto.getCertNumber().equals(findDto.getCertNumber());
        if (!isValid) return false; // 불일치 시 실패

        // 4️⃣ 인증 완료 시 인증기록 삭제
        certDao.delete(certDto.getCertEmail());
        return true;
    }

    // ✅ 회원가입 (DAO insert 그대로 사용)
    @PostMapping("/join")
    public void join(@ModelAttribute MemberDto memberDto) {
        memberDao.insert(memberDto);
    }
}
