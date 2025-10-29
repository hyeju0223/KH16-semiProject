package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.BlacklistDto;

@Component
public class BlacklistMapper implements RowMapper<BlacklistDto> {

    @Override
    public BlacklistDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        BlacklistDto blacklistDto = new BlacklistDto();

        // NUMBER PK (NOT NULL 가정) → getInt()
        blacklistDto.setBlacklistNo(rs.getInt("blacklist_no"));

        blacklistDto.setBlacklistMember(rs.getString("blacklist_member"));
        blacklistDto.setBlacklistReason(rs.getString("blacklist_reason"));
        blacklistDto.setBlacklistStatus(rs.getString("blacklist_status"));
        blacklistDto.setBlacklistRegistrationTime(rs.getTimestamp("blacklist_registration_time"));
        blacklistDto.setBlacklistReleaseTime(rs.getTimestamp("blacklist_release_time"));

        return blacklistDto;
    }
}
