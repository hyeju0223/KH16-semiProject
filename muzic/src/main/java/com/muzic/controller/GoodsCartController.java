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

import com.muzic.dao.AttachmentDao;
import com.muzic.dao.GoodsDao;
import com.muzic.dao.GoodsOrderDao;
import com.muzic.dao.MemberDao;
import com.muzic.dao.MemberPointLogDao;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.GoodsOrderDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.OperationFailedException;
import com.muzic.service.GoodsCartService;
import com.muzic.vo.GoodsCartViewVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store/cart")
public class GoodsCartController {
	@Autowired
	private GoodsCartService goodsCartService;
	@Autowired
	private AttachmentDao attachmentDao;
	@Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsOrderDao goodsOrderDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private MemberPointLogDao memberPointLogDao;

	// 장바구니 목록
	@GetMapping("/list")
	public String list(Model model, HttpSession session) {
		String loginMemberId = (String) session.getAttribute("loginMemberId");
		if (loginMemberId == null) {
			throw new NeedPermissionException("로그인이 필요합니다");
		}

		List<GoodsCartViewVO> cartList = goodsCartService.getCartListWithDetails(loginMemberId);
		model.addAttribute("cartList", cartList);

		return "/WEB-INF/views/store/cart/list.jsp";
	}

	@GetMapping("/image")
	public String image(@RequestParam int goodsNo) {
		try {
			String category = "goods";
			int attachmentNo = attachmentDao .findAttachmentNoByParent(goodsNo, category);
			return "redirect:/attachment/download?attachmentNo=" + attachmentNo;
		} catch (Exception e) {
			return "redirect:/images/error/no-image.png";
		}
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

		// 서비스로 삭제
		boolean success = goodsCartService.delete(loginMemberId, goodsNo);

		if (success == false) {
			return "/store/cart/error.jsp";
		}
		return "redirect:/store/cart/list";
	}
	
	// 장바구니 구매 처리
    @PostMapping("/checkout")
    @Transactional // 모든 DB 작업의 일관성 보장
    public String checkout(HttpSession session, Model model) {
        String loginMemberId = (String) session.getAttribute("loginMemberId");
        if (loginMemberId == null) {
            throw new NeedPermissionException("로그인이 필요합니다");
        }

        // 1. 장바구니 상품 목록 조회
        List<GoodsCartViewVO> cartList = goodsCartService.getCartListWithDetails(loginMemberId);
        if (cartList.isEmpty()) {
            return "redirect:/store/cart/list?error=emptyCart";
        }

        // 2. 총 필요 포인트 계산 및 재고 확인 (미리 한 번 확인)
        int totalRequiredPoints = 0;
        for (GoodsCartViewVO item : cartList) {
            
            // ⭐️ 수정됨: getGoodsNo() 대신 getCartGoods() 사용
            GoodsDto goodsDto = goodsDao.selectOne(item.getCartGoods()); 
            
            if (goodsDto == null || goodsDto.getGoodsQuantity() < item.getCartQuantity()) {
                throw new RuntimeException("상품 [" + item.getGoodsName() + "]의 재고가 부족하거나 존재하지 않습니다.");
            }
            totalRequiredPoints += goodsDto.getGoodsPoint() * item.getCartQuantity();
        }

        // 3. 회원 포인트 잔액 검사
        int currentPoints = memberDao.getMemberPoints(loginMemberId);
        if (currentPoints < totalRequiredPoints) {
            throw new OperationFailedException("포인트 잔액이 부족하여 장바구니 상품을 구매할 수 없습니다.");
        }

        // 4. 모든 상품에 대한 재고 감소, 주문 기록, 포인트 로그 기록
        for (GoodsCartViewVO item : cartList) {
            // 상품 정보 (VO에 있는 goodsPoint와 goodsQuantity는 View 시점의 정보이므로, 
            // 안전을 위해 다시 조회하는 것이 좋습니다. 여기서는 VO의 데이터를 사용합니다.)
            int itemRequiredPoints = item.getGoodsPoint() * item.getCartQuantity();
            
            // 재고 감소
            goodsDao.updateQuantity(item.getCartGoods(), item.getGoodsQuantity() - item.getCartQuantity());

            // 주문 기록 (GoodsOrderDto 생성 및 삽입)
            GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
            goodsOrderDto.setOrderGoods(item.getCartGoods()); // ⭐️ 수정됨
            goodsOrderDto.setOrderMember(loginMemberId);
            goodsOrderDto.setOrderQuantity(item.getCartQuantity());
            goodsOrderDto.setOrderPoint(itemRequiredPoints); 
            
            int orderNo = goodsOrderDao.insert(goodsOrderDto); // 👈 주문 등록 및 주문 번호 획득

            // 포인트 로그 기록
            String reason = "상품 구매 (상품 번호: " + item.getCartGoods() + ", 주문 번호: " + orderNo + ")";
            memberPointLogDao.insertByPurchase(loginMemberId, -itemRequiredPoints, reason, orderNo);
            
            // 장바구니에서 해당 상품 삭제 (구매 성공 시)
            goodsCartService.delete(loginMemberId, item.getCartGoods());
        }
        
        // 5. 총 포인트 차감 (각 주문 건마다 포인트 로그와 차감을 기록했으므로, 
        // 전체 포인트 차감은 마지막에 한 번만 실행합니다.)
        memberDao.addPoint(-totalRequiredPoints, loginMemberId); 
        
        return "redirect:/store/cart/buyFinish"; // 구매 완료 페이지로 리다이렉트
    }

    // 구매 완료 페이지 (필요하다면)
    @GetMapping("/buyFinish")
    public String buyFinish() {
        return "/WEB-INF/views/store/cart/buyFinish.jsp"; // 구매 완료 JSP
    }

}
