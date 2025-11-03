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
import com.muzic.dao.MusicDao;
import com.muzic.dao.MusicGenreDao;
import com.muzic.domain.AttachmentCategory;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicFormDto;
import com.muzic.service.AttachmentService;
import com.muzic.service.MusicService;
import com.muzic.vo.MusicUserVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/music")
public class MusicController {

	@Autowired
	private MusicGenreDao musicGenreDao;
	
	@Autowired
	private MusicService musicService;
	
	@Autowired
	private MusicDao musicDao;
	
	@Autowired
	private AttachmentService attachmentService;
	
	@GetMapping("/add")
	public String add(Model model) {
		model.addAttribute("genreList", musicGenreDao.getCachedGenres());
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
	public String list(Model model, HttpSession session, 
			@ModelAttribute SearchCondition searchCondition) {
		model.addAttribute("musicUserVO", musicService.findUserMusicList(searchCondition));
		model.addAttribute("searchCondition", searchCondition);
		model.addAttribute("pageURL", "/music/list");
		return "/WEB-INF/views/music/list.jsp";
	}

	@GetMapping("/detail")
	public String detail (Model model, HttpSession session, 
			@RequestParam int musicNo) {
		MusicUserVO musicUserVO = musicService.findDetail(musicNo);
		String uploaderId = musicDao.selectOne(musicNo).getMusicUploader();
//		int coverImageNo =
//				attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.COVER.getCategoryName());
//		int musicFileNo = 
//				attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.MUSIC.getCategoryName());
//		model.addAttribute("coverImageNo",coverImageNo);
//		model.addAttribute("musicFileNo",musicFileNo);
		model.addAttribute("musicUserVO", musicUserVO);
		model.addAttribute("uploaderId", uploaderId);
		
		return "/WEB-INF/views/music/detail.jsp";
	}
	
	// 음원 히스토리는 db에서 cascade로 자동 삭제
	// 음원-장르 조인 테이블은 통계를 위해서 null로 해놨고 db에서 자동 보존
	// 서버에서 처리 필요 x
	@PostMapping("/delete-request")
	public String delete(@RequestParam int musicNo, HttpSession session) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		musicService.requestDeleteMusic(loginMemberId, musicNo);
		return "redirect:./list";
	}
	
	@GetMapping("/edit")
	public String edit(@RequestParam int musicNo, Model model) {
		MusicDto musicDto = musicService.selectOneMusicDto(musicNo);
		MusicFormDto musicFormDto = 
				MusicFormDto
				.builder()
				.musicTitle(musicDto.getMusicTitle())
				.musicArtist(musicDto.getMusicArtist())
				.musicAlbum(musicDto.getMusicAlbum())
				.musicGenreSet(musicGenreDao.findGenresByMusicNo(musicNo))
				.build();
		int coverImageNo = attachmentService.getAttachmentNoByParent(musicNo, AttachmentCategory.COVER.getCategoryName());
		model.addAttribute("musicFormDto", musicFormDto);
		model.addAttribute("genreList", musicGenreDao.getCachedGenres());
		model.addAttribute("coverImageNo", coverImageNo);
		return "/WEB-INF/views/music/edit.jsp";
	}
	
	@PostMapping("/edit")
	public String edit(@ModelAttribute MusicFormDto musicFormDto, 
			@RequestParam int musicNo, HttpSession session) throws IOException {
		String memberId = (String) session.getAttribute("loginMemberId");
		String memberRole = (String) session.getAttribute("loginMemberRole");
		musicService.updateMusic(musicFormDto, memberId, memberRole, musicNo);
		return "redirect:./list";
	}
	
	@GetMapping("/file")
	public String file(@RequestParam int attachmentNo) {
		try {
			 if (attachmentNo <= 0) return "redirect:/images/error/music-no-image.png";
			 return "redirect:/attachment/download?attachmentNo="+attachmentNo;
		} catch (Exception e) {
			return "redirect:/images/error/no-image.pnSg";
		}
	}
	
	
}
