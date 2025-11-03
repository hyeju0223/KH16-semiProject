package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.muzic.dao.GoodsDao;
import com.muzic.dao.GoodsOrderDao;
import com.muzic.domain.AttachmentCategory;
import com.muzic.dto.GoodsDto;
import com.muzic.dto.GoodsOrderDto;
import com.muzic.error.NeedPermissionException;
import com.muzic.error.TargetNotFoundException;
import com.muzic.service.AttachmentService;
import com.muzic.vo.PageVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store")
public class GoodsController {
	@Autowired
	private GoodsOrderDao goodsOrderDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private AttachmentService attachmentService;

	// 목록
	@RequestMapping("/list")
	public String list(Model model, @RequestParam(required = false) String goodsCategory, HttpSession session,
			@RequestParam(required = false) String column, @RequestParam(required = false) String keyword,
			@RequestParam(required = false, defaultValue = "regdate_desc") String sort, @ModelAttribute PageVO pageVO) {
		int totalCount = goodsDao.countGoods(pageVO, goodsCategory);
		pageVO.setAllData(totalCount); // PageVO에 총 데이터 수 설정
		List<GoodsDto> goodsList = goodsDao.selectGoodsList(pageVO, goodsCategory, sort);

		model.addAttribute("column", column);
		model.addAttribute("keyword", keyword);
		model.addAttribute("sort", sort);
		model.addAttribute("goodsList", goodsList);
		model.addAttribute("goodsCategory", goodsCategory);
		model.addAttribute("pageVO", pageVO);

		return "/WEB-INF/views/store/list.jsp";
	}

	// 이미지
	@GetMapping("/image")
	public String image(@RequestParam int goodsNo) {
		AttachmentCategory category = AttachmentCategory.GOODS;
		String categoryValue = category.getCategoryName();
		int attachmentNo = attachmentService.getAttachmentNoByParent(goodsNo, categoryValue);
		if (attachmentNo > 0 ) {
			return "redirect:/attachment/download?attachmentNo=" + attachmentNo;
		} else {
			return "redirect:/images/error/no-image.png";
		}
	}

	// 상세 정보
	@GetMapping("/detail")
	public String detail(@RequestParam int goodsNo, Model model) {

		GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
		if (goodsDto == null) {
			throw new TargetNotFoundException("존재하지 않는 상품입니다.");
		}
		model.addAttribute("goodsDto", goodsDto);
		return "/WEB-INF/views/store/detail.jsp";
	}

	// 바로구매
	@PostMapping("/buy")
	@Transactional
	public String buy(@RequestParam int goodsNo, @RequestParam int goodsQuantity, HttpSession session) {

		// 세션 가져오기
		String loginMemberId = (String) session.getAttribute("loginMemberId");

		if (loginMemberId == null) {
			throw new NeedPermissionException("로그인이 필요합니다");
		}

		// 1.상품 정보 조회
		GoodsDto goodsDto = goodsDao.selectOne(goodsNo);
		if (goodsDto == null || goodsDto.getGoodsQuantity() < goodsQuantity) {
			return "/WEB-INF/views/store/error.jsp";
		}
		// 2.재고 감소
		int updatedQuantity = goodsDao.updateQuantity(goodsNo, goodsDto.getGoodsQuantity() - goodsQuantity);
		if (updatedQuantity == 0) {
			throw new RuntimeException("재고 감소 실패");
		}

		// 3.주문 기록 추가
		GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
		goodsOrderDto.setOrderGoods(goodsNo);
		goodsOrderDto.setOrderMember(loginMemberId);
		goodsOrderDto.setOrderQuantity(goodsQuantity);
		goodsOrderDto.setOrderPoint(goodsDto.getGoodsPoint() * goodsQuantity);
		goodsOrderDao.insert(goodsOrderDto);

		return "redirect:buyFinish";
	}

	@GetMapping("/buyFinish")
	public String buyFinish() {
		return "/WEB-INF/views/store/buyFinish.jsp";
	}

}
