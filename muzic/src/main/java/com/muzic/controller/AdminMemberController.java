// src/main/java/com/muzic/controller/AdminMemberController.java
package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.muzic.dao.AdminMemberDao;
import com.muzic.dao.BlacklistDao;
import com.muzic.dto.BlacklistDto;
import com.muzic.dto.MemberDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.AdminMemberListVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberController {

    @Autowired private AdminMemberDao adminMemberDao;
    @Autowired private BlacklistDao blacklistDao;

    private void assertAdmin(HttpSession session) {
        String role = (String) session.getAttribute("loginMemberRole");
        if (!"관리자".equals(role)) throw new NeedPermissionException("허가받지 않은 접근입니다.");
    }

    // [1] 리스트 (검색: id/email/name/nickname/all) / 최신가입순
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue="all") String type,
                       @RequestParam(required=false) String keyword,
                       @RequestParam(defaultValue="1") int page,
                       @RequestParam(defaultValue="20") int size,
                       Model model, HttpSession session) {
        assertAdmin(session);

        int total = adminMemberDao.count(type, keyword);
        int begin = (page - 1) * size + 1;     // Oracle rownum 시작
        int end   = page * size;

        List<AdminMemberListVO> rows = adminMemberDao.selectList(type, keyword, begin, end);

        model.addAttribute("rows", rows);
        model.addAttribute("total", total);
        model.addAttribute("page", page);
        model.addAttribute("size", size);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "/WEB-INF/views/admin/member/list.jsp";
    }

    // [2] 상세
    @GetMapping("/detail")
    public String detail(@RequestParam String memberId, Model model, HttpSession session) {
        assertAdmin(session);

        MemberDto dto = adminMemberDao.selectOne(memberId);
        if (dto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");

        boolean blacklisted = blacklistDao.existsActive(memberId);
        List<BlacklistDto> active = blacklistDao.selectActiveByMember(memberId);

        model.addAttribute("memberDto", dto);
        model.addAttribute("blacklistYn", blacklisted ? "Y" : "N");
        model.addAttribute("activeBlacklist", active);
        return "/WEB-INF/views/admin/member/detail.jsp";
    }

    // [3] 수정 화면
    @GetMapping("/edit")
    public String editForm(@RequestParam String memberId, Model model, HttpSession session) {
        assertAdmin(session);
        MemberDto dto = adminMemberDao.selectOne(memberId);
        if (dto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");
        model.addAttribute("memberDto", dto);
        return "/WEB-INF/views/admin/member/edit.jsp";
    }

    // [3] 수정 처리
    @PostMapping("/edit")
    public String edit(@ModelAttribute MemberDto form, HttpSession session) {
        assertAdmin(session);
        boolean ok = adminMemberDao.updateByAdmin(form);
        if (!ok) throw new TargetNotFoundException("수정 대상 회원이 없습니다.");
        return "redirect:/admin/member/detail?memberId=" + form.getMemberId();
    }

    // [4] 블랙리스트 등록
    @PostMapping("/blacklist/add")
    public String blacklistAdd(@RequestParam String memberId,
                               @RequestParam String reason,
                               HttpSession session) {
        assertAdmin(session);
        blacklistDao.insert(memberId, reason); // status='Y'
        return "redirect:/admin/member/detail?memberId=" + memberId;
    }

    // [4] 블랙리스트 해제(활성건 일괄/최신 우선)
    @PostMapping("/blacklist/release")
    public String blacklistRelease(@RequestParam String memberId, HttpSession session) {
        assertAdmin(session);
        blacklistDao.releaseActive(memberId);  // status='N'
        return "redirect:/admin/member/detail?memberId=" + memberId;
    }

    // [5] 하드 삭제 (drop)
    @PostMapping("/drop")
    public String drop(@RequestParam String memberId, HttpSession session) {
        assertAdmin(session);
        boolean ok = adminMemberDao.drop(memberId);
        if (!ok) throw new TargetNotFoundException("대상 회원이 없습니다.");
        return "redirect:/admin/member/list";
    }
}
