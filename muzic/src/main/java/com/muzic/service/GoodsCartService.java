package com.muzic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dao.GoodsCartDao;
import com.muzic.dao.GoodsDao;
import com.muzic.dao.GoodsOrderDao;
import com.muzic.dto.GoodsCartDto;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.GoodsOrderDto;
import com.muzic.error.TargetNotFoundException;
import com.muzic.vo.GoodsCartViewVO;

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
	public void deleteSelectedGoods(String loginMemberId, List<String> goodsNos) {
		System.out.println("삭제 대상: " + goodsNos); // <- 여기에 추가
		for (String goodsNo : goodsNos) {
			goodsCartDao.delete(loginMemberId, Integer.parseInt(goodsNo));
		}
	}

	// 선택 상품 구매
	@Transactional
	public void buySelectedGoods(String loginMemberId, List<String> goodsNos) {
		for (String goodsNoStr : goodsNos) {
			int goodsNo = Integer.parseInt(goodsNoStr);// 문자열을 숫자로

			// 장바구니에서 구매할 수량 확인
			GoodsCartDto goodsCartDto = goodsCartDao.selectOne(loginMemberId, goodsNo);
			if (goodsCartDto == null) {
				// 구매하려는 상품이 장바구니에 없으면 에러
				throw new TargetNotFoundException("장바구니에서 해당 상품을 찾을 수 없습니다.");
			}
			int quantityToBuy = goodsCartDto.getCartQuantity();

			// 상품 정보 확인
			GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
			if (goodsDto == null) {
				throw new TargetNotFoundException("존재하지 않는 상품입니다.");
			}

			// 재고 확인
			if (goodsDto.getGoodsQuantity() < quantityToBuy) {
				throw new RuntimeException("재고 부족");
			}

			// 재고 감소
			int updated = goodsDao.updateQuantity(goodsNo, goodsDto.getGoodsQuantity() - quantityToBuy);
			if (updated == 0) {
				throw new RuntimeException();
			}

			// goodsorder에 구매 기록 추가
			GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
			goodsOrderDto.setOrderGoods(goodsNo);
			goodsOrderDto.setOrderMember(loginMemberId);
			goodsOrderDto.setOrderQuantity(quantityToBuy);
			goodsOrderDto.setOrderPoint(goodsDto.getGoodsPoint() * quantityToBuy);
			goodsOrderDao.insert(goodsOrderDto);

			// 구매완료 후 장바구니에서 해당 상품 삭제
			goodsCartDao.delete(loginMemberId, goodsNo);
		}

	}

	// 장바구니 수량 변경
	@Transactional
	public void updateQuantity(String loginMemberId, GoodsCartDto goodsCartDto) {
		int goodsNo = goodsCartDto.getCartGoods();
		int quantity = goodsCartDto.getCartQuantity();

		goodsCartDto.setCartMember(loginMemberId);

		int price = goodsDao.selectPrice(goodsNo);

		// 새로운 총액
		int newTotal = price * quantity;

		goodsCartDto.setCartTotal(newTotal);

		goodsCartDao.updateQuantity(goodsCartDto);

	}

	// 장바구니 목록 + 상품 정보 가져오기
	public List<GoodsCartViewVO> getCartListWithDetails(String loginMemberId) {
		return goodsCartDao.selectCartViewByMember(loginMemberId);
	}

	@Transactional
	public void addGoodsToCart(String loginMemberId, GoodsDto goodsDto) {
		// GoodsDto에서 필요한 정보를 추출
		int goodsNo = goodsDto.getGoodsNo();
		int goodsQuantity = goodsDto.getGoodsQuantity();
		// 1. 상품 가격 조회
		int price = goodsDao.selectPrice(goodsNo);

		// 2. DAO로 보낼 DTO 생성
		GoodsCartDto goodsCartDto = new GoodsCartDto();
		goodsCartDto.setCartMember(loginMemberId);
		goodsCartDto.setCartGoods(goodsNo);
		goodsCartDto.setCartQuantity(goodsQuantity);
		goodsCartDto.setCartTotal(goodsQuantity * price);

		// 3. DAO의 insert 메서드 호출
		goodsCartDao.insert(goodsCartDto);
	}

	@Transactional
	public boolean delete(String loginMemberId, int goodsNo) {
		return goodsCartDao.delete(loginMemberId, goodsNo);
	}

}
