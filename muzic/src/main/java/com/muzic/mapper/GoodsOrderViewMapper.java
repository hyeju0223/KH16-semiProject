package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.vo.GoodsOrderViewVO;

@Component
public class GoodsOrderViewMapper implements RowMapper<GoodsOrderViewVO> {

	@Override
	public GoodsOrderViewVO mapRow(ResultSet rs, int rowNum) throws SQLException {

		GoodsOrderViewVO orderViewVO = new GoodsOrderViewVO();

		orderViewVO.setOrderNo(rs.getInt("order_no"));
		orderViewVO.setOrderMember(rs.getNString("order_member"));
		orderViewVO.setOrderGoods(rs.getInt("order_goods"));
		orderViewVO.setOrderQuantity(rs.getInt("order_quantity"));
		orderViewVO.setOrderPoint(rs.getInt("order_point"));
		orderViewVO.setOrderTime(rs.getTimestamp("order_time"));

		orderViewVO.setGoodsName(rs.getString("goods_name"));
		return orderViewVO;

	}

}
