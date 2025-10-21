package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.GoodsDao;
import com.muzic.dao.GoodsOrderDao;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.GoodsOrderDto;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store")
public class GoodsController {
	@Autowired
	private GoodsOrderDao goodsOrderDao;
	@Autowired
	private GoodsDao goodsDao;

	// 목록
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(required = false) String goodsCategory) {
		List<GoodsDto> goodsList;

		if (goodsCategory == null || goodsCategory.isEmpty()) {
			goodsList = goodsDao.selectList(); // 전체 상품
		} else {
			goodsList = goodsDao.selectByCategory(goodsCategory); // 카테고리별 상품만
		}
		model.addAttribute("goodsList", goodsList);
		model.addAttribute("goodsCategory", goodsCategory);
		return "/WEB-INF/views/store/list.jsp";
	}

	// 상세 정보
	@GetMapping("/detail")
	public String detail(@RequestParam int goodsNo, Model model) {
		GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
		model.addAttribute("goodsDto", goodsDto);
		return "/WEB-INF/views/store/detail.jsp";
	}

	// 바로구매
	@PostMapping("/buy")
	@Transactional
	public String buy(@RequestParam int goodsNo, @RequestParam int goodsQuantity, HttpSession session) {
//		//1.상품 정보 조회
		GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
		if (goodsDto == null || goodsDto.getGoodsQuantity() < goodsQuantity) {
			return "/WEB-INF/views/store/error.jsp";
		}
		// 2.재고 감소
		int updatedQuantity = goodsDao.updateQuantity(goodsNo, goodsDto.getGoodsQuantity() - goodsQuantity);
		if (updatedQuantity == 0) {
			throw new RuntimeException("재고 감소 실패");
		}

		// 3.주문 기록 추가
//		String loginId = (String) session.getAttribute("loginId");//세션 추후 구현
		GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
		goodsOrderDto.setOrderGoods(goodsNo);
		goodsOrderDto.setOrderMember("testuser2");
		goodsOrderDto.setOrderQuantity(goodsQuantity);
		goodsOrderDto.setOrderPoint(goodsDto.getGoodsPoint() * goodsQuantity);
		goodsOrderDao.insert(goodsOrderDto);

		return "redirect:buyFinish";
	}

	@GetMapping("/buyFinish")
	public String buyFinish() {
		return "/WEB-INF/views/store/buyFinish.jsp";
	}

}
