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
		
		commentsDto.setCommentsNo(rs.getInt("comments_no"));
		commentsDto.setCommentsPost(rs.getInt("comments_post"));
		commentsDto.setCommentsWriter(rs.getString("comments_writer"));
		commentsDto.setCommentsContent(rs.getString("comments_content"));
		commentsDto.setCommentsWtime(rs.getTimestamp("comments_wtime"));
		commentsDto.setCommentsEtime(rs.getTimestamp("comments_etime"));
		commentsDto.setCommentsLike(rs.getInt("comments_like"));
		commentsDto.setCommentsOrigin(rs.getInt("comments_origin"));
		commentsDto.setCommentsDepth(rs.getInt("comments_depth"));
		
		return commentsDto;
	}

}
