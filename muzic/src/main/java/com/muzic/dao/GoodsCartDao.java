package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.GoodsCartDto;
import com.muzic.mapper.GoodsCartMapper;

@Repository
public class GoodsCartDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private GoodsCartMapper goodsCartMapper;

	// 회원별 장바구니 조회
	public List<GoodsCartDto> selectByMember(String cartMember) {
		String sql = "select cart_no, cart_member, cart_goods, cart_quantity, cart_total, cart_time "
				+ "from goods_cart " + "where cart_member = ?";
		Object[] params = { cartMember };
		return jdbcTemplate.query(sql, goodsCartMapper, params);
	}

//		public List<GoodsCartDto> selectByMember(String cartMember) {
//			String sql = "SELECT gc.cart_no, gc.cart_member, gc.cart_goods, gc.cart_quantity, gc.cart_total, gc.cart_time, "
//					+ "g.goods_name, g.goods_point, g.goods_quantity " // [추가] g.goods_quantity
//					+ "FROM goods_cart gc " + "JOIN goods g ON gc.cart_goods = g.goods_no "
//					+ "WHERE gc.cart_member = ?";
//			Object[] params = { cartMember };
//			return jdbcTemplate.query(sql, goodsCartWithNameMapper, params);
//		}

	// 이미 담긴 상품 조회
	public GoodsCartDto selectOne(String loginId, int goodsNo) {
		String sql = "select * from goods_cart where cart_member = ? and cart_goods = ?";
		Object[] params = { loginId, goodsNo };
		List<GoodsCartDto> list = jdbcTemplate.query(sql, goodsCartMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}

	// 시퀀스 조회
	public int sequence() {
		String sql = "select goods_cart_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}

	// 장바구니에 새로 담기 (이미 존재하면 수량, 토탈만 업데이트)
	public void insert(GoodsCartDto goodsCartDto) {
		// 이미 담은 상품인지 아닌지 확인
		GoodsCartDto existing = selectOne(goodsCartDto.getCartMember(), goodsCartDto.getCartGoods());
		// 이미 있다면 수량, 토탈 업데이트
		if (existing != null) {
			String sql = "update goods_cart set cart_quantity = cart_quantity + ?, cart_total=cart_total + ? where cart_member = ? and cart_goods = ?";
			Object[] params = { goodsCartDto.getCartQuantity(), goodsCartDto.getCartTotal(),
					goodsCartDto.getCartMember(), goodsCartDto.getCartGoods() };
			jdbcTemplate.update(sql, params);
		} else {
			String sql = "insert into goods_cart (cart_no, cart_member, cart_goods, cart_quantity, cart_total, cart_time) "
					+ "values (goods_cart_seq.nextval,?, ?, ?, ?, systimestamp)";
			Object[] params = { goodsCartDto.getCartMember(), goodsCartDto.getCartGoods(),
					goodsCartDto.getCartQuantity(), goodsCartDto.getCartTotal() };
			jdbcTemplate.update(sql, params);
		}
	}

	// 장바구니 상품 삭제
	public boolean delete(String cartMember, int cartGoods) {
		String sql = "delete from goods_cart where cart_member=? and cart_goods=?";
		Object[] params = { cartMember, cartGoods };
		return jdbcTemplate.update(sql, params) > 0;
	}

	// 장바구니 수량변경 (+해당 상품 총액도 같이)
	public boolean updateQuantity(GoodsCartDto goodsCartDto) {
		String sql = "update goods_cart set cart_quantity = ?, cart_total = ? where cart_member = ? and cart_goods=?";
		Object[] params = { goodsCartDto.getCartQuantity(), goodsCartDto.getCartTotal(), goodsCartDto.getCartMember(),
				goodsCartDto.getCartGoods() };

		return jdbcTemplate.update(sql, params) > 0;
	}

}
