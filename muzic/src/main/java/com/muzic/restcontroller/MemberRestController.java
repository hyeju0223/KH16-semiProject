package com.muzic.restcontroller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.muzic.dao.CertDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.CertDto;
//import com.muzic.service.EmailService;

@CrossOrigin
@RestController
@RequestMapping("/rest/member")
public class MemberRestController {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private CertDao certDao;
//    @Autowired
//    private EmailService emailService;

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
//        emailService.sendCertNumber(certEmail);
    }

    // ✅ 이메일 인증번호 확인 (유효시간 5분)
    @PostMapping("/certCheck")
    public boolean certCheck(@ModelAttribute CertDto certDto) {
        var findDto = certDao.selectOne(certDto.getCertEmail());
        if (findDto == null) return false;

        LocalDateTime current = LocalDateTime.now();
        LocalDateTime sent = findDto.getCertTime().toLocalDateTime();
        Duration diff = Duration.between(sent, current);
        if (diff.toSeconds() > 300) return false;

        boolean valid = certDto.getCertNumber().equals(findDto.getCertNumber());
        if (!valid) return false;

        certDao.delete(certDto.getCertEmail());
        return true;
    }
}
