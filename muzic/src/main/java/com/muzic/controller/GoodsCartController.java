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
import org.springframework.web.bind.annotation.ResponseBody;

import com.muzic.dao.GoodsCartDao;
import com.muzic.dao.GoodsDao;
import com.muzic.dao.MemberDao;
import com.muzic.dto.GoodsCartDto;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.MemberDto;
import com.muzic.service.GoodsCartService;

@Controller
@RequestMapping("/store/cart")
public class GoodsCartController {
	@Autowired
	private GoodsCartDao goodsCartDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private MemberDao memberDao;
	@Autowired
	private GoodsCartService goodsCartService;

	// 장바구니 목록
	@GetMapping("/list")
	public String list(Model model, @RequestParam String memberId) {
		MemberDto memberDto = memberDao.selectOne(memberId);
		model.addAttribute("memberDto", memberDto);

		List<GoodsCartDto> cartList = goodsCartDao.selectByMember(memberId);
		model.addAttribute("cartList", cartList);

		return "/WEB-INF/views/store/cart/list.jsp";
	}

	// 장바구니 담기
	@PostMapping("/add")
	public String add(Model model, @ModelAttribute GoodsDto goodsDto, @RequestParam String memberId) {
		// 회원
		MemberDto memberDto = memberDao.selectOne(memberId);
		model.addAttribute("memberDto", memberDto);

		int goodsNo = goodsDto.getGoodsNo();
		int quantity = goodsDto.getGoodsQuantity();
		int price = goodsDao.selectPrice(goodsNo);

		// 1. DAO로 보낼 DTO 생성
		GoodsCartDto goodsCartDto = new GoodsCartDto();
		goodsCartDto.setCartMember(memberId);
		goodsCartDto.setCartGoods(goodsNo);
		goodsCartDto.setCartQuantity(quantity); // 새로 추가할 수량
		goodsCartDto.setCartTotal(quantity * price); // 새로 추가할 금액

		// 2. DAO의 insert 메서드 호출
		// (DAO가 알아서 기존 상품이 있는지 확인하고 UPDATE 또는 INSERT 실행)
		goodsCartDao.insert(goodsCartDto);

//		GoodsCartDto existing = goodsCartDao.selectOne(memberId, goodsNo);
//
//		if (existing != null) {// 이미 존재한다면 -> 수량과 금액만 업데이트
//			existing.setCartQuantity(quantity);
//			existing.setCartTotal(quantity * price);
//			goodsCartDao.insert(existing);
//		} else {// 없다면 -> 상품 담기
//			GoodsCartDto goodsCartDto = new GoodsCartDto();
//			goodsCartDto.setCartMember(memberId);
//			goodsCartDto.setCartGoods(goodsNo);
//			goodsCartDto.setCartQuantity(quantity);
//			goodsCartDto.setCartTotal(quantity * price);
//			goodsCartDao.insert(goodsCartDto);
//		}

//		return "redirect:/store/cart/list?memberId=" + memberId;
		return "/WEB-INF/views/store/cart/cartFinish.jsp";
	}

	@PostMapping("/delete")
	public String delete(@RequestParam String memberId, @RequestParam int goodsNo) {
		memberId = "testuser2";// 세션 추후 구현
		boolean success = goodsCartDao.delete(memberId, goodsNo);
		if (success == false) {
//			throw targetnot //에러
			return "/store/cart/error.jsp";
		}
		return "redirect:/store/cart/list?memberId=" + memberId;
	}

	// 체크된 상품 삭제
	@PostMapping("/deleteMultiple")
	@ResponseBody
	public void deleteMultiple(@RequestParam List<String> goodsNos, @RequestParam String memberId) {
		goodsCartService.deleteSelectedGoods(memberId, goodsNos);
	}

	// 체크된 상품 구매
	@PostMapping("/buyMultiple")
	@ResponseBody
	public void buyMultiple(@RequestParam List<String> goodsNos, @RequestParam String memberId) {
		goodsCartService.buySelectedGoods(memberId, goodsNos);
	}

	// 장바구니 수량 변경
	@PostMapping("/update")
	@ResponseBody
	public void update(@RequestParam String memberId, @RequestParam int goodsNo, @RequestParam int quantity) {
		goodsCartService.updateQuantity(memberId, goodsNo, quantity);
	}

}
