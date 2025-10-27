package com.muzic.restcontroller;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.muzic.dao.CertDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.CertDto;
import com.muzic.service.EmailService;

@CrossOrigin
@RestController
@RequestMapping("/rest/member")
public class MemberRestController {

    @Autowired private MemberDao memberDao;
    @Autowired private CertDao certDao;
    @Autowired private EmailService emailService;

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

    // ✅ 인증번호 확인만 (삭제는 하지 않음!)
    @PostMapping("/certCheck")
    public boolean certCheck(@ModelAttribute CertDto certDto) {
        var stored = certDao.selectOne(certDto.getCertEmail());
        if (stored == null) return false;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sent = stored.getCertTime().toLocalDateTime();
        if (Duration.between(sent, now).toSeconds() > 300) return false;

        return certDto.getCertNumber().equals(stored.getCertNumber());
    }

    // ===== 아이디 찾기: 이름+생년월일+이메일 일치 시 인증번호 발송
    @PostMapping("/findIdCertSend")
    public boolean findIdCertSend(@RequestParam String memberName,
                                  @RequestParam String memberBirth,
                                  @RequestParam String memberEmail) {
        var found = memberDao.findIdByNameBirthEmail(memberName, memberBirth, memberEmail);
        if (found == null) return false;
        emailService.sendCertNumber(memberEmail);
        return true;
    }

    // ===== 비밀번호 변경: 아이디+이름+생년월일+이메일 일치 시 인증번호 발송
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
}
