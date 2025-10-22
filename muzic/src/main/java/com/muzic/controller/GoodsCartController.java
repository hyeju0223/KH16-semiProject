package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.MemberDao;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.MemberDto;
import com.muzic.service.GoodsCartService;
import com.muzic.vo.GoodsCartViewVO;

@Controller
@RequestMapping("/store/cart")
public class GoodsCartController {
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private GoodsCartService goodsCartService;

	// 장바구니 목록
	@GetMapping("/list")
	public String list(Model model, @RequestParam String memberId) {
		MemberDto memberDto = memberDao.selectOne(memberId);
		model.addAttribute("memberDto", memberDto);

		List<GoodsCartViewVO> cartList = goodsCartService.getCartListWithDetails(memberId);
		model.addAttribute("cartList", cartList);
		model.addAttribute("memberId", memberId);

		return "/WEB-INF/views/store/cart/list.jsp";
	}

	// 장바구니 담기 -->ajax로 바꿈
//	@PostMapping("/add")
//	public String add(Model model, @ModelAttribute GoodsDto goodsDto, @RequestParam String memberId) {
//		// 회원
//		MemberDto memberDto = memberDao.selectOne(memberId);
//		model.addAttribute("memberDto", memberDto);
//
//		// 직접 호출 대신 Service
//		goodsCartService.addGoodsToCart(memberId, goodsDto.getGoodsNo(), goodsDto.getGoodsQuantity());
//		return "/WEB-INF/views/store/cart/cartFinish.jsp";
//	}

	@PostMapping("/delete")
	public String delete(@RequestParam String memberId, @RequestParam int goodsNo) {
		memberId = "testuser2";// 세션 추후 구현
		// [수정] DAO 직접 호출 대신 Service로 위임
		boolean success = goodsCartService.delete(memberId, goodsNo);

		if (success == false) {
			return "/store/cart/error.jsp";
		}
		return "redirect:/store/cart/list?memberId=" + memberId;
	}

}
