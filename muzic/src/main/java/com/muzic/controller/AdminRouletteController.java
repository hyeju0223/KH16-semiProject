// src/main/java/com/muzic/controller/AdminRouletteController.java
package com.muzic.controller;

import com.muzic.dao.RouletteDao;
import com.muzic.dao.RouletteLogDao;
import com.muzic.dto.RouletteDto;
import com.muzic.dto.RouletteLogDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.PageVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/game")
public class AdminRouletteController {

    private final RouletteDao rouletteDao;
    private final RouletteLogDao rouletteLogDao;

    private void assertAdmin(HttpSession session) {
        String role = (String) session.getAttribute("loginMemberRole");
        if (!"관리자".equals(role)) throw new NeedPermissionException("허가받지 않은 접근입니다.");
    }

    // [1] 게임 목록 (이름 검색 + 페이징)
    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
                       @RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "20") int size,
                       Model model, HttpSession session) {
        assertAdmin(session);

        PageVO pageVO = PageVO.builder().page(page).size(size).keyword(keyword).build();
        pageVO.setAllData(rouletteDao.countByName(pageVO.getKeyword()));

        List<RouletteDto> list = rouletteDao.selectList(pageVO.getStr(), pageVO.getEnd(), pageVO.getKeyword());

        model.addAttribute("pageVO", pageVO);
        model.addAttribute("list", list);
        return "/WEB-INF/views/admin/game/list.jsp";
    }

    // [2] 상세 + 로그 페이징 (lpage/lsize/ltype/lkeyword)
    @GetMapping("/detail")
    public String detail(@RequestParam int rouletteNo,
                         @RequestParam(defaultValue = "1") int lpage,
                         @RequestParam(defaultValue = "20") int lsize,
                         @RequestParam(required = false) String ltype,
                         @RequestParam(required = false) String lkeyword,
                         Model model, HttpSession session) {
        assertAdmin(session);

        RouletteDto game = rouletteDao.selectOne(rouletteNo);
        if (game == null) throw new TargetNotFoundException("존재하지 않는 게임입니다.");

        PageVO logPage = PageVO.builder().page(lpage).size(lsize).column(ltype).keyword(lkeyword).build();
        logPage.setAllData(rouletteLogDao.countLogs(rouletteNo, logPage.getColumn(), logPage.getKeyword()));

        List<RouletteLogDto> logList =
                rouletteLogDao.selectLogs(rouletteNo, logPage.getStr(), logPage.getEnd(),
                        logPage.getColumn(), logPage.getKeyword());

        model.addAttribute("game", game);
        model.addAttribute("logPage", logPage);
        model.addAttribute("logList", logList);
        return "/WEB-INF/views/admin/game/detail.jsp";
    }

    // [3] 수정 폼 (이름만 수정)
    @GetMapping("/edit")
    public String editForm(@RequestParam int rouletteNo, Model model, HttpSession session) {
        assertAdmin(session);
        RouletteDto game = rouletteDao.selectOne(rouletteNo);
        if (game == null) throw new TargetNotFoundException("존재하지 않는 게임입니다.");
        model.addAttribute("game", game);
        return "/WEB-INF/views/admin/game/edit.jsp";
    }

    // [3] 수정 처리
    @PostMapping("/edit")
    public String edit(@RequestParam int rouletteNo,
                       @RequestParam String rouletteName,
                       HttpSession session) {
        assertAdmin(session);
        boolean ok = rouletteDao.updateName(rouletteNo, rouletteName);
        if (!ok) throw new TargetNotFoundException("수정 대상 게임이 없습니다.");
        return "redirect:/admin/game/detail?rouletteNo=" + rouletteNo;
    }
}
