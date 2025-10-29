package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.error.NeedPermissionException;

@Repository
public class PostLikeDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public void insert(String memberId, int postNo) {
		String sql = "insert into map_post_like(map_member_id, map_post_no) "
				+ "values (?, ?)";
		Object[] params = {memberId, postNo};
		
		jdbcTemplate.update(sql, params);
	}
	
	public boolean delete(String memberId, int postNo) {
		String sql = "delete from map_post_like where map_member_id=? and map_post_no=?";
		Object[] params = {memberId, postNo};
		return jdbcTemplate.update(sql, params) > 0;
	}

	public boolean check(String loginId, int postNo) {
		if(loginId == null) {
			throw new NeedPermissionException("로그인 후 이용이 가능합니다");
		}
		
		String sql = "select count(*) from map_post_like where map_member_id=? and map_post_no=?";
		Object[] params = {loginId, postNo};
		
		return jdbcTemplate.queryForObject(sql, int.class, params) > 0;
	}

	public int countPostNo(int postNo) {
		String sql = "select count(*) from map_post_like where map_post_no = ?";
		Object[] params = {postNo};
		
		return jdbcTemplate.queryForObject(sql, int.class, params);
	}
	
	public List<Integer> likeListByMember (String memberId) {
		String sql = "select map_post_no from map_post_like where map_member_id = ?";
		Object[] params = {memberId};
		
		return jdbcTemplate.queryForList(sql, int.class, params);
	}
}
