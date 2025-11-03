package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.RouletteLogDto;



@Component
public class RouletteLogMapper implements RowMapper<RouletteLogDto> {

	@Override
	public RouletteLogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
 		return RouletteLogDto.builder()
 				.rouletteLogNo(rs.getInt("roulette_log_no"))
 				.rouletteLogRoulette(rs.getInt("roulette_log_roulette"))
 				.rouletteLogMember(rs.getString("roulette_log_member"))
 				.rouletteLogPoint(rs.getInt("roulette_log_point"))
 				.rouletteLogTime(rs.getTimestamp("roulette_log_time"))
 				.build();
	}
	

}
