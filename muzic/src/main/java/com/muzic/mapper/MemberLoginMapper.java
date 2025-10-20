package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.muzic.dto.MemberLoginDto;

public class MemberLoginMapper implements RowMapper<MemberLoginDto> {

    @Override
    public MemberLoginDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberLoginDto dto = new MemberLoginDto();
        dto.setLoginNo(rs.getInt("login_no"));
        dto.setLoginMember(rs.getString("login_member"));
        dto.setLoginFailCount(rs.getInt("login_fail_count"));
        dto.setLoginLocked(rs.getString("login_locked"));
        dto.setLoginLockTime(rs.getTimestamp("login_lock_time"));
        dto.setLoginTryTime(rs.getTimestamp("login_try_time"));
        return dto;
    }
}
