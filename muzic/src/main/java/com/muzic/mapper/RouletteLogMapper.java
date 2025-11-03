// src/main/java/com/muzic/mapper/RouletteLogMapper.java
package com.muzic.mapper;

import com.muzic.dto.RouletteLogDto;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

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
