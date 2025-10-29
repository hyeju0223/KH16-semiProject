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
        AdminMemberListVO adminMemberVo = new AdminMemberListVO();
        adminMemberVo.setMemberId(rs.getString("member_id"));
        adminMemberVo.setMemberPw(rs.getString("member_pw"));
        adminMemberVo.setMemberNickname(rs.getString("member_nickname"));
        adminMemberVo.setMemberName(rs.getString("member_name"));
        adminMemberVo.setMemberEmail(rs.getString("member_email"));
        adminMemberVo.setMemberMbti(rs.getString("member_mbti"));
        adminMemberVo.setMemberBirth(rs.getString("member_birth"));
        adminMemberVo.setMemberContact(rs.getString("member_contact"));
        adminMemberVo.setMemberRole(rs.getString("member_role"));
        adminMemberVo.setMemberPoint(rs.getInt("member_point"));
        adminMemberVo.setMemberLogin(rs.getTimestamp("member_login"));
        adminMemberVo.setBlacklistYn(rs.getString("blacklist_yn"));
        return adminMemberVo;
    }
}
