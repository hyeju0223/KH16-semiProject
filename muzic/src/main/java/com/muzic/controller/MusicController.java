package com.muzic.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicGenreDao;
import com.muzic.dto.MusicDto;
import com.muzic.service.MusicService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/music")
public class MusicController {
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private MusicService musicService;
	
	@Autowired
	private MusicGenreDao musicGenreDao;
	
	@GetMapping("/add")
	public String add() {
		return "/WEB-INF/views/music/add.jsp";
	}
	
	@PostMapping("/add")
	public String add(@ModelAttribute MusicDto musicDto, List<MultipartFile> attaches, List<String> musicGenres, HttpSession session) 
			throws IOException {
		// 정지회원 및 비회원은 인터셉터로 차단
		String loginMemberRole = (String) session.getAttribute("loginMemberRole");
		String loginMemberNickname = (String) session.getAttribute("loginMemberNickname");
		
		List<String> genreList = musicGenreDao.selectAllGenres();
		
		
		musicService.registerMusic(musicDto, attaches, musicGenres, loginMemberNickname, loginMemberRole);
		
		return "redirect:./list";
	}
	
//	@PostMapping("/")
	
}
