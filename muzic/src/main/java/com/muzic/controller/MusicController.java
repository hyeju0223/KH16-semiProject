package com.muzic.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.MemberDao;
import com.muzic.dao.MusicGenreDao;
import com.muzic.dto.MusicDto;
import com.muzic.dto.MusicFormDto;
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
	
	// 매개변수가 없을 경우에 대한 예외처리나 코드가 필요(프론트 상태객체로 해결예정)
	@PostMapping("/add")
	public String add(@ModelAttribute MusicFormDto musicFormDto, HttpSession session) 
			throws IOException {
		// 정지회원 및 비회원은 인터셉터로 차단
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		String loginMemberRole = (String) session.getAttribute("loginMemberRole");
		
		// 장르 검증
		Set<String> selectedGenres = musicFormDto.getMusicGenreSet();
	    if( selectedGenres == null || selectedGenres.isEmpty()) 
	    		throw new IllegalArgumentException("장르는 최소 1개 선택해야 합니다.");
		if(!musicGenreDao.areAllGenresValid(selectedGenres)) 
				throw new IllegalArgumentException("장르가 선택되지 않았거나 유효하지 않은 장르입니다.");
		
		// 음악파일 검증
		MultipartFile inputMusicFile = musicFormDto.getMusicFile();
		if(inputMusicFile == null || inputMusicFile.isEmpty()) 
				throw new IllegalArgumentException("음악파일은 필수로 첨부되어야 합니다.");
		
		musicService.registerMusic(musicFormDto, loginMemberId, loginMemberRole);
	
		return "redirect:./list";
	}
	
	@GetMapping("/list")
	public String list() {
		return "/WEB-INF/views/music/list.jsp";
	}
	
//	@GetMapping("/image")
//	public String image(@RequestParam int musicNo, String category) {
//		
//	}
	
}
