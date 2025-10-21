package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.AttachmentDto;

@Component
public class AttachmentMapper implements RowMapper<AttachmentDto>{

	@Override
	public AttachmentDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return AttachmentDto.builder()
                .attachmentNo(rs.getInt("attachment_no"))
                .attachmentType(rs.getString("attachment_type"))
                .attachmentPath(rs.getString("attachment_path"))
                .attachmentCategory(rs.getString("attachment_category"))
                .attachmentParent(rs.getString("attachment_parent"))
                .attachmentOriginalName(rs.getString("attachment_original_name"))
                .attachmentStoredName(rs.getString("attachment_stored_name"))
                .attachmentSize(rs.getLong("attachment_size"))
                .attachmentTime(rs.getTimestamp("attachment_time"))
                .build();
	}
	
}
