package com.muzic.service;

import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dao.GoodsCartDao;
import com.muzic.dao.GoodsDao;
import com.muzic.dao.GoodsOrderDao;
import com.muzic.dto.GoodsCartDto;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.GoodsOrderDto;

@Service
public class GoodsCartService {
	@Autowired
	private GoodsCartDao goodsCartDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private GoodsOrderDao goodsOrderDao;

	// 선택 상품 삭제
	@Transactional
	public void deleteSelectedGoods(String memberId, List<String> goodsNos) {
		System.out.println("삭제 대상: " + goodsNos); // <- 여기에 추가
		for (String goodsNo : goodsNos) {
			goodsCartDao.delete(memberId, Integer.parseInt(goodsNo));
		}
	}

	// 선택 상품 구매
	@Transactional
	public void buySelectedGoods(String memberId, List<String> goodsNos) {
		for (String goodsNoStr : goodsNos) {
			int goodsNo = Integer.parseInt(goodsNoStr);// 문자열을 숫자로

			// 장바구니에서 구매할 수량 확인
			GoodsCartDto goodsCartDto = goodsCartDao.selectOne(memberId, goodsNo);
			if (goodsCartDto == null) {
				// 구매하려는 상품이 장바구니에 없으면 에러
				throw new RuntimeException();
			}
			int quantityToBuy = goodsCartDto.getCartQuantity();

			// 상품 정보 확인
			GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
			if (goodsDto == null) {
				throw new RuntimeException();
			}

			// 재고 확인
			if (goodsDto.getGoodsQuantity() < quantityToBuy) {
				throw new RuntimeException();
			}

			// 재고 감소
			int updated = goodsDao.updateQuantity(goodsNo, goodsDto.getGoodsQuantity() - quantityToBuy);
			if (updated == 0) {
				throw new RuntimeException();
			}

			// goodsorder에 구매 기록 추가
			GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
			goodsOrderDto.setOrderGoods(goodsNo);
			goodsOrderDto.setOrderMember(memberId);
			goodsOrderDto.setOrderQuantity(quantityToBuy);
			goodsOrderDto.setOrderPoint(goodsDto.getGoodsPoint() * quantityToBuy);
			goodsOrderDao.insert(goodsOrderDto);

			// 구매완료 후 장바구니에서 해당 상품 삭제
			goodsCartDao.delete(memberId, goodsNo);
		}

	}

	// 장바구니 수량 변경
	@Transactional
	public void updateQuantity(String memberId, int goodsNo, int quantity) {
		int price = goodsDao.selectPrice(goodsNo);
		
		//새로운 총액
		int newTotal = price * quantity;
		
		GoodsCartDto goodsCartDto = new GoodsCartDto();
		goodsCartDto.setCartMember(memberId);
		goodsCartDto.setCartGoods(goodsNo);
		goodsCartDto.setCartQuantity(quantity);
		goodsCartDto.setCartTotal(newTotal);
		
		goodsCartDao.updateQuantity(goodsCartDto);
		
	}

}
