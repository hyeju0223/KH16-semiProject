package com.muzic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dao.GoodsCartDao;
import com.muzic.dao.GoodsDao;
import com.muzic.dao.GoodsOrderDao;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MemberPointLogDao;
import com.muzic.dto.GoodsCartDto;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.GoodsOrderDto;
import com.muzic.error.OperationFailedException;
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
	@Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberPointLogDao memberPointLogDao;

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
	    
	    // 1. 총 필요 포인트 계산 및 유효성 검사
	    int totalRequiredPoints = 0;
	    
	    // 구매할 상품 정보를 미리 담아둘 리스트 (재사용을 위해)
	    // GoodsCartViewVO를 가져오는 getCartListWithDetails 메서드를 활용하여 필터링합니다.
	    List<GoodsCartViewVO> allCartItems = getCartListWithDetails(loginMemberId);
	    
	    // 선택된 상품만 필터링하고 포인트 합산
	    List<GoodsCartViewVO> selectedCartList = allCartItems.stream()
	            .filter(item -> goodsNos.contains(String.valueOf(item.getCartGoods())))
	            .toList();

	    if (selectedCartList.isEmpty()) {
	        throw new RuntimeException("선택된 상품이 장바구니에 없습니다.");
	    }
	    
	    for (GoodsCartViewVO item : selectedCartList) {
	        // 장바구니에 있는 수량 확인 (VO에 이미 quantity와 total이 있으므로 활용)
	        int quantityToBuy = item.getCartQuantity(); 
	        
	        // 상품 정보 (재고 확인용)
	        GoodsDto goodsDto = goodsDao.selectOne(item.getCartGoods());
	        if (goodsDto == null) {
	             throw new TargetNotFoundException("존재하지 않는 상품입니다.");
	        }
	        
	        // 재고 확인
	        if (goodsDto.getGoodsQuantity() < quantityToBuy) {
	            throw new OperationFailedException("상품 [" + item.getGoodsName() + "]의 재고가 부족합니다. (재고: " + goodsDto.getGoodsQuantity() + ")");
	        }
	        
	        // 총 필요 포인트 합산
	        totalRequiredPoints += item.getGoodsPoint() * quantityToBuy;
	    }
	    
	    // 2. 회원 포인트 잔액 검사 ⭐️ (핵심 추가 로직)
	    int currentPoints = memberDao.getMemberPoints(loginMemberId); // memberDao에 이 메서드가 있다고 가정
	    if (currentPoints < totalRequiredPoints) {
	        throw new OperationFailedException("포인트 잔액이 부족하여 상품을 구매할 수 없습니다.");
	    }

	    // 3. 주문 처리 및 포인트 로그 기록
	    for (GoodsCartViewVO item : selectedCartList) {
	        
	        int goodsNo = item.getCartGoods();
	        int quantityToBuy = item.getCartQuantity();
	        int itemRequiredPoints = item.getGoodsPoint() * quantityToBuy;
	        
	        // (A) 재고 감소
	        GoodsDto goodsDto = goodsDao.selectOne(goodsNo); // 최신 재고 정보를 가져옴
	        goodsDao.updateQuantity(goodsNo, goodsDto.getGoodsQuantity() - quantityToBuy);

	        // (B) goodsorder에 구매 기록 추가
	        GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
	        goodsOrderDto.setOrderGoods(goodsNo);
	        goodsOrderDto.setOrderMember(loginMemberId);
	        goodsOrderDto.setOrderQuantity(quantityToBuy);
	        goodsOrderDto.setOrderPoint(itemRequiredPoints);
	        int orderNo = goodsOrderDao.insert(goodsOrderDto); // 👈 주문 번호 획득

	        // (C) 포인트 로그 기록 ⭐️ (핵심 추가 로직)
	        String reason = "상품 구매 (상품 번호: " + goodsNo + ", 주문 번호: " + orderNo + ")";
	        memberPointLogDao.insertByPurchase(loginMemberId, -itemRequiredPoints, reason, orderNo); // 음수 값으로 기록

	        // (D) 구매완료 후 장바구니에서 해당 상품 삭제
	        goodsCartDao.delete(loginMemberId, goodsNo);
	    }
	    
	    // 4. 회원 포인트 최종 차감 ⭐️ (핵심 추가 로직)
	    memberDao.addPoint(-totalRequiredPoints, loginMemberId); 

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
