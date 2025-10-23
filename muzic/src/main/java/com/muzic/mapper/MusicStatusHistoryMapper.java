package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.MusicStatusHistoryDto;

@Component
public class MusicStatusHistoryMapper implements RowMapper<MusicStatusHistoryDto>{

	@Override
    public MusicStatusHistoryDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        
		return MusicStatusHistoryDto
        		.builder()
        		.mshNo(rs.getInt("msh_no"))
                .mshMusicNo(rs.getInt("msh_music_no"))
                .mshMusicStatus(rs.getString("msh_music_status"))
                .mshComment(rs.getString("msh_comment"))
                .mshAdminId(rs.getString("msh_admin_id"))
                .mshChangedTime(rs.getTimestamp("msh_changed_time"))
                .build();
    }
}
