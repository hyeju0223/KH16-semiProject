package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.CommentsDto;

@Component
public class CommentsMapper implements RowMapper<CommentsDto>{

	@Override
	public CommentsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		CommentsDto commentsDto = new CommentsDto();
		
		commentsDto.setCommentNo(rs.getInt("comment_no"));
		commentsDto.setCommentPost(rs.getInt("comment_post"));
		commentsDto.setCommentWriter(rs.getString("comment_writer"));
		commentsDto.setCommentContent(rs.getString("comment_content"));
		commentsDto.setCommentWtime(rs.getTimestamp("comment_wtime"));
		commentsDto.setCommentEtime(rs.getTimestamp("comment_etime"));
		commentsDto.setCommentLike(rs.getInt("comment_like"));
		commentsDto.setCommentOrigin(rs.getInt("comment_origin"));
		commentsDto.setCommentDepth(rs.getInt("comment_depth"));
		
		return commentsDto;
	}

}
