// src/main/java/com/muzic/mapper/RouletteMapper.java
package com.muzic.mapper;

import com.muzic.dto.RouletteDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RouletteMapper implements RowMapper<RouletteDto> {
    @Override
    public RouletteDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return RouletteDto.builder()
                .rouletteNo(rs.getInt("roulette_no"))
                .rouletteName(rs.getString("roulette_name"))
                .rouletteDailyCount(rs.getInt("roulette_daily_count"))
                .rouletteMaxPoint(rs.getInt("roulette_max_point"))
                .rouletteMinPoint(rs.getInt("roulette_min_point"))
                .rouletteDate(rs.getDate("roulette_date"))
                .build();
    }
}
