package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.PostDto;

@Component
public class PostMapper implements RowMapper<PostDto>{

	@Override
	public PostDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		PostDto muzicDto = new PostDto();
		
		muzicDto.setPostNo(rs.getInt("post_no")); //번호
		muzicDto.setPostTitle(rs.getString("post_title")); //제목
		muzicDto.setPostWriter(rs.getString("post_writer")); //작성자
		muzicDto.setPostMbti(rs.getString("post_mbti")); //게시판(값이 null이면 자유)
		muzicDto.setPostContent(rs.getString("post_content")); //내용
		muzicDto.setPostMusic(rs.getInt("post_music")); //음악 첨부
		muzicDto.setPostWtime(rs.getTimestamp("post_wtime")); //작성시간
		muzicDto.setPostEtime(rs.getTimestamp("post_etime")); //수정시간
		muzicDto.setPostLike(rs.getInt("post_like")); //좋아요 수
		muzicDto.setPostread(rs.getInt("post_read")); //조회수
		muzicDto.setPostNotice(rs.getString("post_notice")); //공지사항
		
		return muzicDto;
	}

}
