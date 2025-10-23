package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.vo.GoodsCartViewVO;

@Component
public class GoodsCartViewMapper implements RowMapper<GoodsCartViewVO> {

	@Override
	public GoodsCartViewVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		GoodsCartViewVO cartViewVO = new GoodsCartViewVO();

		// 1. 장바구니 정보 매핑 (goods_cart 테이블의 컬럼)
		cartViewVO.setCartNo(rs.getInt("cart_no"));
		cartViewVO.setCartMember(rs.getString("cart_member"));
		cartViewVO.setCartGoods(rs.getInt("cart_goods"));
		cartViewVO.setCartQuantity(rs.getInt("cart_quantity"));
		cartViewVO.setCartTotal(rs.getInt("cart_total"));

		// 2. 상품 상세 정보 매핑 (goods 테이블의 컬럼, View를 통해 가져옴)
		cartViewVO.setGoodsName(rs.getString("goods_name"));
		cartViewVO.setGoodsPoint(rs.getInt("goods_point"));
		cartViewVO.setGoodsQuantity(rs.getInt("goods_quantity")); // 재고 수량

		return cartViewVO;
	}

}
