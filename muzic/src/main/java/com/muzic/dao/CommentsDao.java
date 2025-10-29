package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.CommentsDto;
import com.muzic.mapper.CommentsMapper;

@Repository
public class CommentsDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CommentsMapper commentsMapper;
	
	public int sequence() {
		String sql = "select comments_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public List<CommentsDto> selectList(String commentWriter) {
		
		String sql = "select * from comments where comment_writer = ? "
				+ "order by comment_no desc";
		Object[] params = {commentWriter};
		return jdbcTemplate.query(sql, commentsMapper, params);
	}
	
	public List<CommentsDto> selectList(int commentPost) {
		
		String sql = "select * from comments where comment_post = ? "
				+ "order by comment_no desc";
		Object[] params = {commentPost};
		return jdbcTemplate.query(sql, commentsMapper, params);
	}
	
	public boolean delete(int commentNo) {
		String sql = "delete from comments where comment_no = ?";
		Object[] params = {commentNo};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public void insert(CommentsDto commentsDto) {
		String sql = "insert into comments(comment_no, comment_post,"
				+ "comment_writer, comment_content, comment_wtime) "
				+ " values (?, ?, ?, ?, systimestamp)";
		Object[] params = {
				commentsDto.getCommentNo(), commentsDto.getCommentPost(),
				commentsDto.getCommentWriter(), commentsDto.getCommentContent()};
		jdbcTemplate.update(sql, params);
	}
	
	public boolean update(CommentsDto commentsDto) {
		String sql = "update comments set "
				+ "comment_content = ?, comment_etime = systimestamp"
				+ " where comment_no = ?";
		Object[] params = {commentsDto.getCommentContent(), commentsDto.getCommentNo()};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public CommentsDto selectOne(int commentNo) {
		String sql = "select * from comments where comment_no = ?";
		Object[] params = {commentNo};
		List<CommentsDto> commentsList = jdbcTemplate.query(sql, commentsMapper, params);
		return commentsList.isEmpty() ? null : commentsList.get(0);
	}
}
