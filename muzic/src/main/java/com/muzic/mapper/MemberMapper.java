package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.MemberDto;

@Component
public class MemberMapper implements RowMapper<MemberDto>{

	@Override
	public MemberDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return MemberDto.builder()
				.memberId(rs.getString("member_id"))
				.memberPw(rs.getString("member_pw"))
				.memberNickname(rs.getString("member_nickname"))
				.memberName(rs.getString("member_name"))
				.memberEmail(rs.getString("member_email"))
				.memberMbti(rs.getString("member_mbti"))
				.memberBirth(rs.getDate("member_birth"))
				.memberContact(rs.getString("member_contact"))
				.memberPostcode(rs.getString("member_postcode"))
				.memberAddress1(rs.getString("member_address1"))
				.memberAddress2(rs.getString("member_address2"))
				.memberRole(rs.getString("member_role"))
				.memberPoint(rs.getInt("member_point"))
				.memberJoin(rs.getTimestamp("member_join"))
				.memberEtime(rs.getTimestamp("member_etime"))
				.memberRecentLogin(rs.getTimestamp("member_recent_login"))
				.build();
	}
}
	
	