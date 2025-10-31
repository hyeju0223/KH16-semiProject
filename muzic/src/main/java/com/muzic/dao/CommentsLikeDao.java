package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.error.NeedPermissionException;

@Repository
public class CommentsLikeDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void insert(String memberId, int commentNo) {
		String sql = "insert into map_comments_like(map_member_id, map_comment_no) "
				+ "values (?, ?)";
		Object[] params = {memberId, commentNo};
		jdbcTemplate.update(sql, params);
	}
	
	public boolean delete(String memberId, int commentNo) {
		String sql = "delete from map_comments_like where map_member_id=? "
				+ "and map_comment_no=?";
		Object[] params = {memberId, commentNo};
		
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean check(String memberId, int commentNo) {
		if(memberId == null) {
			throw new NeedPermissionException("로그인 후 이용이 가능합니다.");
		}
		
		String sql = "select count(*) from map_comments_like where map_member_id=?"
				+ " and map_comment_no=?";
		Object[] params = {memberId, commentNo};
		
		return jdbcTemplate.queryForObject(sql, int.class, params) > 0;
	}
	
	public int countCommentNo(int commentNo) {
		String sql = "select count(*) from map_comments_like where map_comment_no = ?";
		Object[] params = {commentNo};
		
		return jdbcTemplate.queryForObject(sql,  int.class, params);
	}
	
	public List<Integer> commentLikeListByMember(String memberId) {
		String sql = "select map_comment_no from map_comments_like where map_member_id = ?";
		Object[] params = {memberId};
		
		return jdbcTemplate.queryForList(sql, int.class, params);
	}
}
