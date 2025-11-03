package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.condition.SearchCondition;
import com.muzic.dao.MusicDao;
import com.muzic.dto.MusicDto;
import com.muzic.service.AdminMusicService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/music")
public class AdminMusicController {

    @Autowired
    private MusicDao musicDao;

    @Autowired
    private AdminMusicService adminMusicService;

    @GetMapping("/list")
    public String list(
            SearchCondition searchCondition,
            @RequestParam(required = false, defaultValue = "전체") String status,
            Model model
    ) {
        if (searchCondition.getColumn() == null) {
            searchCondition.setColumn("music_title");
        }

        boolean isSearch = !searchCondition.isList(); // 검색 여부
        boolean isAll = status.equals("전체");        // 전체 상태 여부

        int totalCount;
        List<MusicDto> list;

        if (isAll && !isSearch) {
            totalCount = musicDao.adminCountAll();
            list = musicDao.adminListAll(searchCondition);
        }
        else if (isAll && isSearch) {
            totalCount = musicDao.adminCountAllSearch(searchCondition);
            list = musicDao.adminSearchAll(searchCondition);
        }
        else if (!isAll && !isSearch) {
            totalCount = musicDao.adminCountByStatus(status);
            list = musicDao.adminListByStatus(searchCondition, status);
        }
        else {
            totalCount = musicDao.adminCountByStatusSearch(searchCondition, status);
            list = musicDao.adminSearchByStatus(searchCondition, status);
        }

        searchCondition.setAllData(totalCount);

        model.addAttribute("searchCondition", searchCondition);
        model.addAttribute("status", status);
        model.addAttribute("list", list);

        return "/WEB-INF/views/admin/music/list.jsp";
    }


    @GetMapping("/detail")
    public String detail(@RequestParam int musicNo, Model model) {
        MusicDto musicDto = musicDao.selectOne(musicNo);
        if(musicDto == null) return "redirect:/admin/music/list";

        model.addAttribute("musicDto", musicDto);
        return "/WEB-INF/views/admin/music/detail.jsp";
    }

    @PostMapping("/approve")
    public String approve(int musicNo, String comment, HttpSession session) {
        String adminId = (String) session.getAttribute("loginMemberId");
        adminMusicService.approve(musicNo, adminId, comment);
        return "redirect:/admin/music/detail?musicNo=" + musicNo;
    }

    @PostMapping("/reject")
    public String reject(int musicNo, String comment, HttpSession session) {
        String adminId = (String) session.getAttribute("loginMemberId");
        adminMusicService.reject(musicNo, adminId, comment);
        return "redirect:/admin/music/detail?musicNo=" + musicNo;
    }

    @PostMapping("/approve-delete")
    public String approveDelete(int musicNo, String comment, HttpSession session) {
        String adminId = (String) session.getAttribute("loginMemberId");
        adminMusicService.approveDelete(musicNo, adminId, comment);
        return "redirect:/admin/music/detail?musicNo=" + musicNo;
    }

    @PostMapping("/approve-edit")
    public String approveUpdate(int musicNo, String comment, HttpSession session) {
        String adminId = (String) session.getAttribute("loginMemberId");
        adminMusicService.approveUpdate(musicNo, adminId, comment);
        return "redirect:/admin/music/detail?musicNo=" + musicNo;
    }
}
