package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.GoodsCartDto;

@Component
public class GoodsCartWithNameMapper implements RowMapper<GoodsCartDto> {

	@Override
	public GoodsCartDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		GoodsCartDto goodsCartDto = new GoodsCartDto();
		goodsCartDto.setCartNo(rs.getInt("cart_no"));
		goodsCartDto.setCartMember(rs.getString("cart_member"));
		goodsCartDto.setCartGoods(rs.getInt("cart_goods"));
		goodsCartDto.setCartQuantity(rs.getInt("cart_quantity"));
		goodsCartDto.setCartTotal(rs.getInt("cart_total"));
		goodsCartDto.setCartTime(rs.getTimestamp("cart_time"));
		// 조인한 컬럼 - 상품 이름, 상품 포인트
		goodsCartDto.setGoodsName(rs.getString("goods_name"));
		goodsCartDto.setGoodsPoint(rs.getInt("goods_point"));
		return goodsCartDto;
	}

}
