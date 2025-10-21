package com.muzic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/music")
public class MusicController {
	
	@GetMapping("/add")
	public String add() {
		return "/WEB-INF/views/music/add.jsp";
	}
	
//	@PostMapping("/add")
//	public String add() {
//		
//	}
	
}
