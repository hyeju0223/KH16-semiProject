package com.muzic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.GoodsOrderDto;

@Repository
public class GoodsOrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	//상품 구매 기록을 삽입 goods_order 테이블에 삽입
	public void insert(GoodsOrderDto goodsOrderDto) {
		String sql = "insert into goods_order(order_no, order_member, order_goods, order_quantity, order_point, order_time) "
				+ "values(goods_order_seq.nextval, ?, ?, ?, ?, systimestamp)";
		Object[] params = { goodsOrderDto.getOrderMember(), goodsOrderDto.getOrderGoods(),
				goodsOrderDto.getOrderQuantity(), goodsOrderDto.getOrderPoint() };
		jdbcTemplate.update(sql, params);

	}
}
