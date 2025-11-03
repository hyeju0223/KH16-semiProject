package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.PlayLogDto;

@Component
public class PlayLogMapper implements RowMapper<PlayLogDto>{

	@Override
	public PlayLogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return PlayLogDto.builder()
				.playLogNo(rs.getInt("play_log_no"))
				.playLogMusic(rs.getInt("play_log_music"))
				.playLogMember(rs.getString("play_log_member"))
				.playLogMbti(rs.getNString("play_log_mbti"))
				.playLogTime(rs.getTimestamp("play_log_time"))
				.build();
	}
	
}
