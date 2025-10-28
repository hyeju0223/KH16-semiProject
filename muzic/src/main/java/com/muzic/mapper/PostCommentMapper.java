package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import com.muzic.vo.PostCommentsVO;

@Configuration
public class PostCommentMapper implements RowMapper<PostCommentsVO> {

	@Override
	public PostCommentsVO mapRow(ResultSet rs, int rowNum) throws SQLException {
		PostCommentsVO postCommentsVO = new PostCommentsVO();
		postCommentsVO.setPostNo(rs.getInt("post_no"));
		postCommentsVO.setPostTitle(rs.getString("post_title"));
		postCommentsVO.setPostWriter(rs.getString("post_writer"));
		postCommentsVO.setPostMbti(rs.getString("post_mbti"));
		postCommentsVO.setPostContent(rs.getString("post_content"));
		postCommentsVO.setPostMusic(rs.getInt("post_music"));
		postCommentsVO.setPostWtime(rs.getTimestamp("post_wtime"));
		postCommentsVO.setPostEtime(rs.getTimestamp("post_etime"));
		postCommentsVO.setPostLike(rs.getInt("post_like"));
		postCommentsVO.setPostNotice(rs.getString("post_notice"));
		postCommentsVO.setCommentNo(rs.getInt("comment_no"));

		return postCommentsVO;

	}

}
