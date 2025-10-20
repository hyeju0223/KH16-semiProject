package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.MemberLoginDto;

@Component
public class MemberLoginMapper implements RowMapper<MemberLoginDto>{

	@Override
	public MemberLoginDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return MemberLoginDto.builder()
				.loginNo(rs.getInt("login_no"))
				.loginMember(rs.getString("login_member"))
				.loginFailCount(rs.getInt("login_fail_count"))
				.loginLocked(rs.getString("login_locked"))
				.loginLockTime(rs.getTimestamp("login_lock_time"))
				.loginTryTime(rs.getTimestamp("login_try_time"))
				.build();
	}

}
