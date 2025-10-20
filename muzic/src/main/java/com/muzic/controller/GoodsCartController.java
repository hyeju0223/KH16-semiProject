package com.muzic.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.muzic.dao.GoodsCartDao;
import com.muzic.dao.GoodsDao;
import com.muzic.dto.GoodsCartDto;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/store/cart")
public class GoodsCartController {
	@Autowired
	private GoodsCartDao goodsCartDao;
	@Autowired
	private GoodsDao goodsDao;
	
	//장바구니 목록
	public String list(HttpSession session, Model model) {
		String loginId = (String) session.getAttribute("loginId");
		List<GoodsCartDto> cartList = goodsCartDao.selectByMember(loginId);
		model.addAttribute("cartList",cartList);
		return "/WEB-INF/views/store/cart.jsp";
	}

}
