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
import com.muzic.service.GoodsCartService;

@CrossOrigin
@RestController
@RequestMapping("/rest/store/cart")
public class GoodsCartRestController {
	@Autowired
	private GoodsCartService goodsCartService;

	// 체크된 상품 삭제
	@PostMapping("/deleteMultiple")
	public void deleteMultiple(@RequestParam List<String> goodsNos, @RequestParam String memberId) {
		goodsCartService.deleteSelectedGoods(memberId, goodsNos);
	}

	// 체크된 상품 구매
	@PostMapping("/buyMultiple")
	public void buyMultiple(@RequestParam List<String> goodsNos, @RequestParam String memberId) {
		goodsCartService.buySelectedGoods(memberId, goodsNos);
	}

	// 장바구니 수량 변경
	@PostMapping("/update")
	public void update(@RequestParam String memberId, @ModelAttribute GoodsCartDto goodsCartDto) {
		goodsCartService.updateQuantity(memberId, goodsCartDto);
	}

	// 장바구니 담기
	@PostMapping("/add")
	public void add(@RequestParam String memberId, @ModelAttribute GoodsDto goodsDto) {
		goodsCartService.addGoodsToCart(memberId, goodsDto);
	}

}
