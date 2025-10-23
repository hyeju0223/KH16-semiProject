package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.service.GoodsCartService;
import com.muzic.vo.GoodsCartViewVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store/cart")
public class GoodsCartController {
	@Autowired
	private GoodsCartService goodsCartService;

	// 장바구니 목록
	@GetMapping("/list")
	public String list(Model model, HttpSession session){
		String loginMemberId = (String) session.getAttribute("loginMemberId");
	    if (loginMemberId == null) {
	        return "redirect:/member/login";
	    }
		

		List<GoodsCartViewVO> cartList = goodsCartService.getCartListWithDetails(loginMemberId);
		model.addAttribute("cartList", cartList);

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
	public String delete(HttpSession session, @RequestParam int goodsNo) {
		
		String loginMemberId = (String) session.getAttribute("loginMemberId");
	    if (loginMemberId == null) {
	        return "redirect:/member/login";
	    }
		
		//서비스로 삭제
		boolean success = goodsCartService.delete(loginMemberId, goodsNo);

		if (success == false) {
			return "/store/cart/error.jsp";
		}
		return "redirect:/store/cart/list";
	}

}
