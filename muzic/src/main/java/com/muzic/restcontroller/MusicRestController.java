package com.muzic.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dto.MusicLikeVO;
import com.muzic.service.MusicLikeService;
import com.muzic.service.MusicService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/rest/music")
public class MusicRestController {
	
	@Autowired
	private MusicLikeService musicLikeService;
	
	@Autowired
	private MusicService musicService;
	
	@GetMapping("/check")
	public MusicLikeVO check(@RequestParam int musicNo, HttpSession session) {
		String loginMemberId = (String)session.getAttribute("loginMemberId");
		return musicLikeService.checkLikeStatus(loginMemberId, musicNo);
	}
	
	@GetMapping("/toggle")
	public MusicLikeVO toggle(@RequestParam int musicNo, HttpSession session) {
		String loginMemberId = (String)session.getAttribute("loginMemberId");
		return musicLikeService.toggleLike(loginMemberId, musicNo);
	}
	
	@GetMapping("/play")
	public void play(@RequestParam int musicNo) {
	    musicService.updateMusicPlay(musicNo);
	}
	
}
