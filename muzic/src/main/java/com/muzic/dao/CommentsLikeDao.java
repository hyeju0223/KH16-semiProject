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
	
	//댓글에 닿아요 설정
	public void insert(String memberId, int commentNo) {
		String sql = "insert into map_comments_like(map_member_id, map_comment_no) "
				+ "values (?, ?)";
		Object[] params = {memberId, commentNo};
		jdbcTemplate.update(sql, params);
	}
	
	//댓글 좋아요 삭제
	public boolean delete(String memberId, int commentNo) {
		String sql = "delete from map_comments_like where map_member_id=? "
				+ "and map_comment_no=?";
		Object[] params = {memberId, commentNo};
		
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	//좋아요 체크
	public boolean check(String memberId, int commentNo) {
		if(memberId == null) {
			throw new NeedPermissionException("로그인 후 이용이 가능합니다.");
		}
		
		String sql = "select count(*) from map_comments_like where map_member_id=?"
				+ " and map_comment_no=?";
		Object[] params = {memberId, commentNo};
		
		return jdbcTemplate.queryForObject(sql, int.class, params) > 0;
	}
	
	//댓글의 총 좋아요 개수 조회
	public int countCommentNo(int commentNo) {
		String sql = "select count(*) from map_comments_like where map_comment_no = ?";
		Object[] params = {commentNo};
		
		return jdbcTemplate.queryForObject(sql,  int.class, params);
	}
	
	//회원이 누른 댓글 좋아요 목록
	public List<Integer> commentLikeListByMember(String memberId) {
		String sql = "select map_comment_no from map_comments_like where map_member_id = ?";
		Object[] params = {memberId};
		
		return jdbcTemplate.queryForList(sql, int.class, params);
	}
}
