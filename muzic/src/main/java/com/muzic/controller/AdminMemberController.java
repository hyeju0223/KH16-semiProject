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
import com.muzic.vo.PageVO;
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

    @GetMapping("/list")
    public String list(
            @RequestParam(defaultValue = "all") String type,     // id/email/name/nickname/all
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model, HttpSession session) {

        assertAdmin(session);

        // 1) PageVO 구성
        PageVO pageVO = PageVO.builder()
                .page(page)
                .size(size)
                .blockNum(10)
                .column(type)           // PageVO는 column/keyword 사용
                .keyword(keyword)
                .build();

        // 2) 총 건수
        int total = adminMemberDao.count(pageVO.getColumn(), pageVO.getKeyword());
        pageVO.setAllData(total);

        // 3) 목록 (ROWNUM between getStr() and getEnd())
        List<AdminMemberListVO> rows = adminMemberDao.selectList(
                pageVO.getColumn(), pageVO.getKeyword(),
                pageVO.getStr(), pageVO.getEnd()
        );

        // 4) 모델
        model.addAttribute("pageVO", pageVO);
        model.addAttribute("list", rows);

        return "/WEB-INF/views/admin/member/list.jsp";
    }

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

    @GetMapping("/edit")
    public String editForm(@RequestParam String memberId, Model model, HttpSession session) {
        assertAdmin(session);
        MemberDto dto = adminMemberDao.selectOne(memberId);
        if (dto == null) throw new TargetNotFoundException("존재하지 않는 회원입니다.");
        model.addAttribute("memberDto", dto);
        return "/WEB-INF/views/admin/member/edit.jsp";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute MemberDto form, HttpSession session) {
        assertAdmin(session);
        boolean ok = adminMemberDao.updateByAdmin(form);
        if (!ok) throw new TargetNotFoundException("수정 대상 회원이 없습니다.");
        return "redirect:/admin/member/detail?memberId=" + form.getMemberId();
    }

    @PostMapping("/blacklist/add")
    public String blacklistAdd(@RequestParam String memberId,
                               @RequestParam String reason,
                               HttpSession session) {
        assertAdmin(session);
        blacklistDao.insert(memberId, reason);
        return "redirect:/admin/member/detail?memberId=" + memberId;
    }

    @PostMapping("/blacklist/release")
    public String blacklistRelease(@RequestParam String memberId, HttpSession session) {
        assertAdmin(session);
        blacklistDao.releaseActive(memberId);
        return "redirect:/admin/member/detail?memberId=" + memberId;
    }

    @PostMapping("/drop")
    public String drop(@RequestParam String memberId, HttpSession session) {
        assertAdmin(session);
        boolean ok = adminMemberDao.drop(memberId);
        if (!ok) throw new TargetNotFoundException("대상 회원이 없습니다.");
        return "redirect:/admin/member/list";
    }
}
