package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.muzic.dto.MemberPointLogDto;

@Configuration
public class MemberPointLogMapper implements RowMapper<MemberPointLogDto>{

	@Override
	public MemberPointLogDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return MemberPointLogDto.builder()
				.pointLogNo(rs.getInt("point_log_no"))
				.pointLogMember(rs.getString("point_log_member"))
				.pointLogChange(rs.getInt("point_log_change"))
				.pointLogReason(rs.getString("point_log_reason"))
				.pointLogRelatedPost(rs.getInt("point_log_related_post"))
				.pointLogRelatedOrder(rs.getInt("point_log_related_order"))
				.pointLogTime(rs.getTimestamp("point_log_time"))
				.build();
	}

	
}
