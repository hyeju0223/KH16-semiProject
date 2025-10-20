package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.GoodsOrderDto;
@Component
public class GoodsOrderMapper implements RowMapper<GoodsOrderDto>{

	@Override
	public GoodsOrderDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		GoodsOrderDto goodsOrderDto = new GoodsOrderDto();
		goodsOrderDto.setOrderNo(rs.getInt("order_no"));
		goodsOrderDto.setOrderMember(rs.getString("order_member"));
		goodsOrderDto.setOrderGoods(rs.getInt("order_goods"));
		goodsOrderDto.setOrderQuantity(rs.getInt("order_quantity"));
		goodsOrderDto.setOrderPoint(rs.getInt("order_point"));
		goodsOrderDto.setOrderTime(rs.getTimestamp("order_time"));
		return goodsOrderDto;
	}

}
