package com.muzic.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dto.GoodsCartDto;
import com.muzic.dto.GoodsDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.service.GoodsCartService;

import jakarta.servlet.http.HttpSession;

@CrossOrigin
@RestController
@RequestMapping("/rest/store/cart")
public class GoodsCartRestController {
	@Autowired
	private GoodsCartService goodsCartService;

	// 체크된 상품 삭제
	@PostMapping("/deleteMultiple")
	public void deleteMultiple(@RequestParam List<String> goodsNos, HttpSession session) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		if (loginMemberId == null) {
			throw new NeedPermissionException("로그인이 필요합니다.");
		}
		goodsCartService.deleteSelectedGoods(loginMemberId, goodsNos);
	}

	// 체크된 상품 구매
	@PostMapping("/buyMultiple")
	public void buyMultiple(@RequestParam List<String> goodsNos, HttpSession session) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		if (loginMemberId == null) {
			throw new NeedPermissionException("로그인이 필요합니다.");
		}
		goodsCartService.buySelectedGoods(loginMemberId, goodsNos);
	}

	// 장바구니 수량 변경
	@PostMapping("/update")
	public void update(HttpSession session, @ModelAttribute GoodsCartDto goodsCartDto) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		if (loginMemberId == null) {
			throw new NeedPermissionException("로그인이 필요합니다.");
		}
		goodsCartService.updateQuantity(loginMemberId, goodsCartDto);
	}

	// 장바구니 담기
	@PostMapping("/add")
	public void add(HttpSession session, @ModelAttribute GoodsDto goodsDto) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		if (loginMemberId == null) {
	        throw new NeedPermissionException("로그인이 필요합니다.");
	    }
		goodsCartService.addGoodsToCart(loginMemberId, goodsDto);
	}

}
