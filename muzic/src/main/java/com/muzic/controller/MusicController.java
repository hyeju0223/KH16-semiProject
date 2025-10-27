package com.muzic.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.condition.SearchCondition;
import com.muzic.domain.AttachmentCategory;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicFormDto;
import com.muzic.service.AttachmentService;
import com.muzic.service.MusicService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/music")
public class MusicController {

	@Autowired
	private MusicService musicService;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@GetMapping("/add")
	public String add() {
		return "/WEB-INF/views/music/add.jsp";
	}
	
	// 매개변수가 없을 경우에 대한 예외처리나 코드가 필요(프론트 상태객체로 해결예정)
	@PostMapping("/add")
	public String add(@ModelAttribute MusicFormDto musicFormDto, HttpSession session) 
			throws IOException {
		// 정지회원 및 비회원은 인터셉터로 차단
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		String loginMemberRole = (String) session.getAttribute("loginMemberRole");
		
		musicService.registerMusic(musicFormDto, loginMemberId, loginMemberRole);
	
		return "redirect:./list";
	}
	
	@GetMapping("/list")
	public String list(Model model, @ModelAttribute SearchCondition searchCondition) {
		model.addAttribute("musicUserVO", musicService.findUserMusicList(searchCondition));
		return "/WEB-INF/views/music/list.jsp";
	}

	// 음원 히스토리는 db에서 cascade로 자동 삭제
	// 음원-장르 조인 테이블은 통계를 위해서 null로 해놨고 db에서 자동 보존
	// 서버에서 처리 필요 x
	@GetMapping("/detail")
	public String detail (Model model, @RequestParam int musicNo) {
		MusicDto musicDto = musicService.selectOneMusicDto(musicNo);
		int coverImageNo =
				attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.COVER.getCategoryName());
		int musicFileNo = 
				attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.MUSIC.getCategoryName());
		model.addAttribute("musicDto", musicDto);
		model.addAttribute("coverImageNo",coverImageNo);
		model.addAttribute("musicFileNo",musicFileNo);
		
		return "/WEB-INF/views/music/detail.jsp";
	}
	
	@PostMapping("/delete")
	public String delete(@RequestParam int musicNo, HttpSession session) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		musicService.requestDeleteMusic(loginMemberId, musicNo);
		return "redirect:/mypage/music/list?musicNo="+musicNo;
	}
	
	@GetMapping("/file")
	public String file(@RequestParam int attachmentNo) {
		try {
			 if (attachmentNo == -1) return "redirect:/images/error/no-image.png";
			 return "redirect:/attachment/download?attachmentNo="+attachmentNo;
		} catch (Exception e) {
			return "redirect:/images/error/no-image.png";
		}
	}
	
}
