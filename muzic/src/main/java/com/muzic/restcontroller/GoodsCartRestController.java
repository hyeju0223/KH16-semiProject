package com.muzic.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dto.GoodsCartDto;
import com.muzic.dto.GoodsDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.OperationFailedException;
import com.muzic.service.GoodsCartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@Tag(name = "상품 관리 컨트롤러")

@CrossOrigin
@RestController
@RequestMapping("/rest/store/cart")
public class GoodsCartRestController {
	@Autowired
	private GoodsCartService goodsCartService;
	
	//-SpringDoc 설정을 추가하여 문서에 정보를 정확하게 기재한다
	
	@Operation(
			deprecated = false //비추천 여부(향후 사용 중지 예정이라면 true를 작성)
			, description = "상품 관리를 위한 등록 기능" //기능에 대한 설명
			, responses = {//예상되는 응답 코드
					@ApiResponse(responseCode = "200"),
					@ApiResponse(responseCode = "400"),
					@ApiResponse(responseCode = "500")
			}
			)

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

	// ⭐️ OperationFailedException 처리 핸들러 추가
	@ExceptionHandler(OperationFailedException.class)
	public ResponseEntity<String> handleOperationFailedException(OperationFailedException e) {
		// HTTP 400 Bad Request 상태 코드와 예외 메시지를 반환
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
