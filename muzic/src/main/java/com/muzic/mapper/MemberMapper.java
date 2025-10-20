package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.MemberDto;

@Component
public class MemberMapper implements RowMapper<MemberDto> {

    @Override
    public MemberDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        MemberDto memberDto = new MemberDto();

        memberDto.setMemberId(rs.getString("member_id"));
        memberDto.setMemberPw(rs.getString("member_pw"));
        memberDto.setMemberNickname(rs.getString("member_nickname"));
        memberDto.setMemberName(rs.getString("member_name"));
        memberDto.setMemberEmail(rs.getString("member_email"));
        memberDto.setMemberMbti(rs.getString("member_mbti"));
        memberDto.setMemberBirth(rs.getDate("member_birth"));
        memberDto.setMemberContact(rs.getString("member_contact"));
        memberDto.setMemberPostcode(rs.getString("member_postcode"));
        memberDto.setMemberAddress1(rs.getString("member_address1"));
        memberDto.setMemberAddress2(rs.getString("member_address2"));
        memberDto.setMemberRole(rs.getString("member_role"));
        memberDto.setMemberPoint(rs.getInt("member_point"));
        memberDto.setMemberJoin(rs.getTimestamp("member_join"));
        memberDto.setMemberEtime(rs.getTimestamp("member_etime"));
        memberDto.setMemberRecentLogin(rs.getTimestamp("member_recent_login"));

        return memberDto;
    }
}
