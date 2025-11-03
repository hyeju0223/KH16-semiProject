package com.muzic.restcontroller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
	
	@PostMapping("/toggle")
	public MusicLikeVO toggle(@RequestParam int musicNo, HttpSession session) {
		String loginMemberId = (String)session.getAttribute("loginMemberId");
		return musicLikeService.toggleLike(loginMemberId, musicNo);
	}
	
	@PostMapping("/play")
	public boolean play(@RequestParam int musicNo, HttpSession session) {
	    String memberId = (String) session.getAttribute("loginMemberId");
	    String mbti = (String) session.getAttribute("loginMemberMbti");

	    // 세션에 재생 기록 set 가져오기
	    @SuppressWarnings("unchecked")
	    Set<Integer> playedSet = (Set<Integer>) session.getAttribute("playedMusicSet");

	    if (playedSet == null) {
	        playedSet = new HashSet<>();
	        session.setAttribute("playedMusicSet", playedSet);
	    }

	    // 이미 들은 곡이면 false (프론트 UI 업데이트 X)
	    if (playedSet.contains(musicNo)) {
	        return false;
	    }

	    // 처음 재생 → 서비스 호출
	    musicService.updateMusicPlay(mbti, memberId, musicNo);

	    // 세션에 기록
	    playedSet.add(musicNo);
	    
	    return true; // 최초 재생이면 true 반환 → JS가 UI 업데이트
	}
	
}
