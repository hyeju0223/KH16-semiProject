// src/main/java/com/muzic/mapper/AdminMemberListMapper.java
package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.vo.AdminMemberListVO;

@Component
public class AdminMemberListMapper implements RowMapper<AdminMemberListVO> {
    @Override
    public AdminMemberListVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        AdminMemberListVO v = new AdminMemberListVO();
        v.setMemberId(rs.getString("member_id"));
        v.setMemberPw(rs.getString("member_pw"));
        v.setMemberNickname(rs.getString("member_nickname"));
        v.setMemberName(rs.getString("member_name"));
        v.setMemberEmail(rs.getString("member_email"));
        v.setMemberMbti(rs.getString("member_mbti"));
        v.setMemberBirth(rs.getString("member_birth"));
        v.setMemberContact(rs.getString("member_contact"));
        v.setMemberRole(rs.getString("member_role"));
        v.setMemberPoint(rs.getInt("member_point"));
        v.setBlacklistYn(rs.getString("blacklist_yn"));
        return v;
    }
}
