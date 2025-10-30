package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.GoodsDto;

@Component
public class GoodsMapper implements RowMapper<GoodsDto> {

	@Override
	public GoodsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		GoodsDto goodsDto = new GoodsDto();
		goodsDto.setGoodsNo(rs.getInt("goods_no"));
		goodsDto.setGoodsName(rs.getString("goods_name"));
		goodsDto.setGoodsDescription(rs.getString("goods_description"));
		goodsDto.setGoodsPoint(rs.getInt("goods_point"));
		goodsDto.setGoodsQuantity(rs.getInt("goods_quantity"));
		goodsDto.setGoodsCategory(rs.getString("goods_category"));

		// 🚨 수정 부분: getTimestamp()로 가져온 후 toLocalDateTime()으로 변환
		if (rs.getTimestamp("goods_expiration") != null) {
			goodsDto.setGoodsExpiration(rs.getTimestamp("goods_expiration").toLocalDateTime());
		} else {
			goodsDto.setGoodsExpiration(null);
		}

		goodsDto.setGoodsRegistrationTime(rs.getTimestamp("goods_registration_time"));
		goodsDto.setGoodsEditTime(rs.getTimestamp("goods_edit_time"));
		return goodsDto;
	}

}
