package com.muzic.dao;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.GoodsDto;
import com.muzic.mapper.GoodsMapper;

@Repository
public class GoodsDao {
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 시퀀스
	public int sequence() {
		String sql = "select goods_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}

	// 상품 등록
	public void insert(GoodsDto goodsDto) {
		String sql = "insert into goods (goods_no, goods_name, goods_description, goods_point, goods_quantity, goods_category, goods_expiration, goods_registration_time) "
				+ "values (?, ?, ?, ?, ?, ?, ?, systimestamp)";
		Object[] params = { goodsDto.getGoodsNo(), goodsDto.getGoodsName(), goodsDto.getGoodsDescription(),
				goodsDto.getGoodsPoint(), goodsDto.getGoodsQuantity(), goodsDto.getGoodsCategory(),
				goodsDto.getGoodsExpiration() };
		jdbcTemplate.update(sql, params);
	}

	// 전체 상품 조회
	public List<GoodsDto> selectList() {
		String sql = "select * from goods order by goods_no desc";
		return jdbcTemplate.query(sql, goodsMapper);
	}
	
	//검색 목록 조회
	public List<GoodsDto> selectList(String column, String keyword) {
		// 1. 컬럼 허용 목록 검증 (SQL Injection 방지)
		// 실제 데이터베이스의 컬럼명에 맞게 조정하세요.
		Set<String> allowList = Set.of("goods_name", "goods_description", "goods_category");

		if (!allowList.contains(column)) {
			return List.of();
		}

		String sql = "select * from goods where INSTR(#1, ?) > 0 order by #1, goods_no desc";
		
		sql = sql.replace("#1", column);
		
		Object[] params = { keyword };
		
		return jdbcTemplate.query(sql, goodsMapper, params);
	}
	

	// 단일 상품 조회
	public GoodsDto selectOne(int goodsNo) {
		String sql = "select * from goods where goods_no = ?";
		Object[] params = { goodsNo };
		List<GoodsDto> list = jdbcTemplate.query(sql, goodsMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}

	// 카테고리별 상품 조회
	public List<GoodsDto> selectByCategory(String goodsCategory) {
		String sql = "select * from goods where goods_category=? order by goods_no asc";
		Object[] params = { goodsCategory };
		return jdbcTemplate.query(sql, goodsMapper, params);
	}

	// 상품 재고 수량 업데이트
	public int updateQuantity(int goodsNo, int newQuantity) {
		String sql = "UPDATE goods SET goods_quantity = ? WHERE goods_no = ?";
		Object[] params = { newQuantity, goodsNo };
		return jdbcTemplate.update(sql, params);
	}

	public int selectPrice(int goodsNo) {
		String sql = "select goods_point from goods where goods_no=?";
		Object[] params = { goodsNo };
		return jdbcTemplate.queryForObject(sql, params, Integer.class);
	}

}
