package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.vo.PostVO;

@Component
public class PostListMapper implements RowMapper<PostVO>{

	@Override
	public PostVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PostVO postVO = new PostVO();
		
		postVO.setPostNo(rs.getInt("post_no"));
		postVO.setPostTitle(rs.getString("post_title"));
		postVO.setPostWriter(rs.getString("post_writer"));
		postVO.setPostMbti(rs.getString("post_mbti"));
		postVO.setPostContent(rs.getString("post_content"));
		postVO.setPostMusic(rs.getInt("post_music"));
		postVO.setPostWtime(rs.getTimestamp("post_wtime"));
		postVO.setPostEtime(rs.getTimestamp("post_etime"));
		postVO.setPostLike(rs.getInt("post_like"));
		postVO.setPostNotice(rs.getString("post_notice"));
		
		postVO.setMemberId(rs.getString("member_id"));
		postVO.setMemberNickname(rs.getString("member_nickname"));
		postVO.setMemberMbti(rs.getString("member_mbti"));
		postVO.setMemberRole(rs.getNString("member_role"));
		
		return postVO;
	}

}
