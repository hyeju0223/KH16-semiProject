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
		String sql = "select comments_sql.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public List<CommentsDto> selectList(String commentsWriter) {
		
		String sql = "select * from where comments_writer = ? "
				+ "order by comments_no desc";
		Object[] params = {commentsWriter};
		return jdbcTemplate.query(sql, commentsMapper, params);
	}
	
	public List<CommentsDto> selectList(int commentsPost) {
		
		String sql = "select * from where comments_post = ? "
				+ "ordder by comments_no desc";
		Object[] params = {commentsPost};
		return jdbcTemplate.query(sql, commentsMapper, params);
	}
	
	public boolean delete(int commentsNo) {
		String sql = "delete comments where comments_no = ?";
		Object[] params = {commentsNo};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public void insert(CommentsDto commentsDto) {
		String sql = "insert into comments(comments_no, comments_post,"
				+ "comments_writer, comments_content) "
				+ " values (?, ?, ?, ?)";
		Object[] params = {commentsDto.getCommentsNo(), commentsDto.getCommentsPost(),
				commentsDto.getCommentsWriter(), commentsDto.getCommentsContent()};
		jdbcTemplate.update(sql, params);
	}
	
	public boolean update(CommentsDto commentsDto) {
		String sql = "update comments set "
				+ "comments_content = ?, comments_etime = systimestamp"
				+ " where comments_no = ?";
		Object[] params = {commentsDto.getCommentsContent(), commentsDto.getCommentsNo()};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public CommentsDto selectOne(int commentsNo) {
		String sql = "select * from where comments_no = ?";
		int[] params = {commentsNo};
		List<CommentsDto> commentsList = jdbcTemplate.query(sql, commentsMapper, params);
		return commentsList.isEmpty() ? null : commentsList.get(0);
	}
}
