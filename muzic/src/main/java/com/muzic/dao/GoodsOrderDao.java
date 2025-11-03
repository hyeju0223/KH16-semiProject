package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.GoodsOrderDto;
import com.muzic.mapper.GoodsOrderMapper;

@Repository
public class GoodsOrderDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GoodsOrderMapper goodsOrderMapper;

	// 주문 번호 시퀀스를 먼저 생성하여 반환하는 메서드 (추가)
    public int getOrderSequence() {
        String sql = "select goods_order_seq.nextval from dual";
        return jdbcTemplate.queryForObject(sql, int.class);
    }

	// 상품 구매 기록을 삽입하고, 생성된 주문 번호(order_no)를 반환하도록 수정
	public int insert(GoodsOrderDto goodsOrderDto) {
        
        // 1. 주문 번호 시퀀스를 미리 획득
        int orderNo = getOrderSequence(); // 👈 시퀀스 호출
        goodsOrderDto.setOrderNo(orderNo); // 👈 DTO에 주문 번호 설정 (컨트롤러에서 사용 예정)
        
		String sql = "insert into goods_order(order_no, order_member, order_goods, order_quantity, order_point, order_time) "
				+ "values(?, ?, ?, ?, ?, systimestamp)"; // 👈 nextval 대신 획득한 orderNo 사용
                
		Object[] params = { 
            goodsOrderDto.getOrderNo(), // 👈 획득한 orderNo 사용
            goodsOrderDto.getOrderMember(), 
            goodsOrderDto.getOrderGoods(),
            goodsOrderDto.getOrderQuantity(), 
            goodsOrderDto.getOrderPoint() 
        };
        
		jdbcTemplate.update(sql, params);
        
        return orderNo; // 👈 생성된 주문 번호 반환
	}
	
	//회원별 상품 구매 기록
	public List<GoodsOrderDto> selectListByMemberId(String memberId) {
		String sql = "select * from goods_order where order_member=?";
		Object[] params= {memberId};
		return jdbcTemplate.query(sql,goodsOrderMapper,params);
	}
}
