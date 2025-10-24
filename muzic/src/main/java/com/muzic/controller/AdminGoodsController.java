package com.muzic.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dto.GoodsDto;
import com.muzic.service.GoodsService;

@Controller
@RequestMapping("/admin/goods")
public class AdminGoodsController {
	@Autowired
	private GoodsService goodsService;


	@GetMapping("/add")
	public String add() {
		return "/WEB-INF/views/admin/goods/add.jsp";
	}
	
	@PostMapping("/add")
	public String add( GoodsDto goodsDto,@RequestParam MultipartFile attach ) throws IOException {
	    
		goodsService.insert(goodsDto, attach);
	    
	    return "redirect:/store/list"; 
	}
}
