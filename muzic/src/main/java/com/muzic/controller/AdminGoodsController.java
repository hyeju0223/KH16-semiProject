package com.muzic.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.muzic.dao.GoodsDao;
import com.muzic.dto.GoodsDto;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;
import com.muzic.service.GoodsService;
import com.muzic.vo.PageVO;

@Controller
@RequestMapping("/admin/goods")
public class AdminGoodsController {
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsDao goodsDao;

	// 굿즈 목록
	@RequestMapping("/list")
	public String list(Model model,
			@ModelAttribute PageVO pageVO,
			@RequestParam(required=false) String goodsCategory,
			@RequestParam(required=false, defaultValue = "regdate-desc") String sort) {
//		List<GoodsDto> goodsList = goodsDao.selectList();
		
		int count = goodsDao.countGoods(pageVO, goodsCategory);
		pageVO.setAllData(count);
		
		List<GoodsDto> goodsList = goodsDao.selectGoodsList(pageVO, goodsCategory, sort);
		
		model.addAttribute("goodsList", goodsList);
		model.addAttribute("pageVO", pageVO);
        model.addAttribute("goodsCategory", goodsCategory);
        model.addAttribute("sort", sort);
		return "/WEB-INF/views/admin/goods/list.jsp";
	}

	// 굿즈 등록
	@GetMapping("/add")
	public String add() {
		return "/WEB-INF/views/admin/goods/add.jsp";
	}

	@PostMapping("/add")
	public String add(GoodsDto goodsDto, @RequestParam MultipartFile attach) throws IOException {

		goodsService.insert(goodsDto, attach);

		return "redirect:list";
	}

	// 굿즈 삭제
	@GetMapping("/delete")
	public String delete(@RequestParam int goodsNo) {
		boolean isDeleted = goodsService.delete(goodsNo);
		if (!isDeleted) {
			throw new TargetNotFoundException("존재하지 않는 상품입니다.");
		}
		return "redirect:list"; // 삭제 후 목록 페이지로 이동
	}
	
	//굿즈 상세
	@GetMapping("/detail")
	public String detail(Model model, @RequestParam int goodsNo) {
	    
	    // 1. GoodsDao를 사용하여 상품 번호(goodsNo)에 해당하는 단일 상품 정보를 조회합니다.
	    GoodsDto goodsDto = goodsDao.selectOne(goodsNo); // goodsDao에 selectOne(int goodsNo) 메소드가 있다고 가정
	    
	    // 2. 조회된 상품 정보가 없으면 오류 처리 또는 목록 페이지로 리다이렉트
	    if (goodsDto == null) {
	        throw new TargetNotFoundException("존재하지 않는 상품입니다.");
	    }
	    
	    // 3. 모델에 조회된 상품 정보를 담아 detail.jsp로 전달
	    model.addAttribute("goodsDto", goodsDto);
	    
	    // 4. View 반환: detail.jsp의 실제 경로를 반환합니다.
	    return "/WEB-INF/views/admin/goods/detail.jsp";
	}

	// 굿즈 수정
	@GetMapping("/edit")
	public String edit(@RequestParam int goodsNo, Model model) {
		GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
		if (goodsDto == null)
			throw new TargetNotFoundException("존재하지 않는 상품입니다.");
		model.addAttribute("goodsDto", goodsDto);
		return "/WEB-INF/views/admin/goods/edit.jsp";
	}

	@PostMapping("/edit")
	public String edit(@ModelAttribute GoodsDto goodsDto, @RequestParam MultipartFile attach) throws IOException {
		goodsService.edit(goodsDto, attach);
		return "redirect:list"; // 수정 완료 후 목록 페이지로 리다이렉트
	}
}
