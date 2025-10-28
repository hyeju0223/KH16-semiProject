package com.muzic.dao;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.GoodsDto;
import com.muzic.mapper.GoodsMapper;
import com.muzic.vo.PageVO;

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
		return jdbcTemplate.queryForObject(sql, Integer.class,params);
	}

	public int countGoods(PageVO pageVO, String goodsCategory) {
	    // 텍스트 검색 컬럼 허용 목록 검증 (SQL Injection 방지)
	    Set<String> allowList = Set.of("goods_name", "goods_description");
	    
	    // 1. 카테고리 필터가 있는 경우
	    if (goodsCategory != null && !goodsCategory.isEmpty()) {
	        if (pageVO.search() && allowList.contains(pageVO.getColumn())) {
	            // 1-1. 카테고리 + 검색
	            String sql = "SELECT COUNT(*) FROM GOODS WHERE goods_category = ? AND INSTR(#1, ?) > 0";
	            sql = sql.replace("#1", pageVO.getColumn());
	            Object[] params = { goodsCategory, pageVO.getKeyword() };
	            return jdbcTemplate.queryForObject(sql, int.class, params);
	        } else {
	            // 1-2. 카테고리만
	            String sql = "SELECT COUNT(*) FROM GOODS WHERE goods_category = ?";
	            Object[] params = { goodsCategory };
	            return jdbcTemplate.queryForObject(sql, int.class, params);
	        }
	    } 
	    // 2. 카테고리 필터가 없는 경우
	    else {
	        if (pageVO.search() && allowList.contains(pageVO.getColumn())) {
	            // 2-1. 검색만
	            String sql = "SELECT COUNT(*) FROM GOODS WHERE INSTR(#1, ?) > 0";
	            sql = sql.replace("#1", pageVO.getColumn());
	            Object[] params = { pageVO.getKeyword() };
	            return jdbcTemplate.queryForObject(sql, int.class, params);
	        } else {
	            // 2-2. 전체 목록
	            String sql = "SELECT COUNT(*) FROM GOODS";
	            return jdbcTemplate.queryForObject(sql, int.class);
	        }
	    }
	}
	private String getOrderByClause(String sort) {
        if ("price_asc".equals(sort)) {
            return "goods_point ASC, goods_no DESC"; // 낮은가격순
        } else if ("price_desc".equals(sort)) {
            return "goods_point DESC, goods_no DESC"; // 높은가격순
        } else {
            // 기본값: 최신등록순 (regdate_desc)
            return "goods_registration_time DESC, goods_no DESC";
        }
    }
	
	public List<GoodsDto> selectGoodsList(PageVO pageVO, String goodsCategory, String sort) {
	    Set<String> allowList = Set.of("goods_name", "goods_description");
	    String orderByClause = getOrderByClause(sort);
	    
	    // 1. 카테고리 필터가 있는 경우
	    if (goodsCategory != null && !goodsCategory.isEmpty()) {
	        
	        if (pageVO.search() && allowList.contains(pageVO.getColumn())) {
	            // 1-1. 카테고리 + 검색
	            String sql = "SELECT * FROM ( "
	                       + "    SELECT ROWNUM AS RNUM, TMP.* FROM ( "
	                       + "        SELECT goods_no, goods_name, goods_description, goods_point, goods_quantity, goods_category, goods_expiration, goods_registration_time, goods_edit_time "
	                       + "        FROM GOODS "
	                       + "        WHERE goods_category = ? AND INSTR(#1, ?) > 0 " 
	                       + "        ORDER BY " + orderByClause
	                       + "    ) TMP"
	                       + ") WHERE RNUM BETWEEN ? AND ?";
	            
	            sql = sql.replace("#1", pageVO.getColumn());
	            Object[] params = { goodsCategory, pageVO.getKeyword(), pageVO.getStr(), pageVO.getEnd() };
	            return jdbcTemplate.query(sql, goodsMapper, params);
	            
	        } else {
	            // 1-2. 카테고리만
	            String sql = "SELECT * FROM ( "
	                       + "    SELECT ROWNUM AS RNUM, TMP.* FROM ( "
	                       + "        SELECT goods_no, goods_name, goods_description, goods_point, goods_quantity, goods_category, goods_expiration, goods_registration_time, goods_edit_time "
	                       + "        FROM GOODS "
	                       + "        WHERE goods_category = ? " 
	                       + "        ORDER BY " + orderByClause
	                       + "    ) TMP"
	                       + ") WHERE RNUM BETWEEN ? AND ?";
	            
	            Object[] params = { goodsCategory, pageVO.getStr(), pageVO.getEnd() };
	            return jdbcTemplate.query(sql, goodsMapper, params);
	        }
	        
	    } 
	    // 2. 카테고리 필터가 없는 경우
	    else {
	        
	        if (pageVO.search() && allowList.contains(pageVO.getColumn())) {
	            // 2-1. 검색만
	            String sql = "SELECT * FROM ( "
	                       + "    SELECT ROWNUM AS RNUM, TMP.* FROM ( "
	                       + "        SELECT goods_no, goods_name, goods_description, goods_point, goods_quantity, goods_category, goods_expiration, goods_registration_time, goods_edit_time "
	                       + "        FROM GOODS "
	                       + "        WHERE INSTR(#1, ?) > 0 " 
	                       + "        ORDER BY " + orderByClause
	                       + "    ) TMP"
	                       + ") WHERE RNUM BETWEEN ? AND ?";
	            
	            sql = sql.replace("#1", pageVO.getColumn());
	            Object[] params = { pageVO.getKeyword(), pageVO.getStr(), pageVO.getEnd() };
	            return jdbcTemplate.query(sql, goodsMapper, params);
	            
	        } else {
	            // 2-2. 전체 목록
	            String sql = "SELECT * FROM ( "
	                       + "    SELECT ROWNUM AS RNUM, TMP.* FROM ( "
	                       + "        SELECT goods_no, goods_name, goods_description, goods_point, goods_quantity, goods_category, goods_expiration, goods_registration_time, goods_edit_time "
	                       + "        FROM GOODS "
	                       + "        ORDER BY " + orderByClause
	                       + "    ) TMP"
	                       + ") WHERE RNUM BETWEEN ? AND ?";
	            
	            Object[] params = { pageVO.getStr(), pageVO.getEnd() };
	            return jdbcTemplate.query(sql, goodsMapper, params);
	        }
	    }
	}
	

}
