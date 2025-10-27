package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MusicLikeDao {
	
	@Autowired
	private JdbcTemplate  jdbcTemplate;
	
	public boolean insert(int musicNo, String memberId, String likeStatus)  {
		String sql = "insert into map_music_like(map_music_no, map_member_id, like_status"
				+ ") values (?, ?, ?)";
		Object[] params = { musicNo, memberId, likeStatus };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean delete(String memberId, int musicNo) {
		String sql = "delete from map_music_like "
				+ "where map_member_id = ? and map_music_no = ?";
		Object[] params = { memberId, musicNo };
		return jdbcTemplate.update(sql ,params) > 0;
	}
	
	public boolean isLiked(String memberId, int musicNo) {
		if(memberId == null) return false;
		String sql = "select count(*) from map_music_like "
				+ "where map_member_id = ? and map_music_no= ? and like_status = 'Y'";
		Object[] params = { memberId, musicNo };
		int count = jdbcTemplate.queryForObject(sql, int.class, params);
		return count > 0; 
	}
	
//	public int countByMusicNo(int musicNo) {
//		String sql = "select count(*) from map_music_like where map_music_no = ? and like_status = 'Y'";
//		Object[] params = { musicNo };
//		return jdbcTemplate.queryForObject(sql, int.class, params);
//	}
	
	public List<Integer> findLikedListByMemberId(String memberId){
		String sql = "select music_no from map_music_like where map_member_id = ? and like_status = 'Y'";
		Object[] params = { memberId };
		return jdbcTemplate.queryForList(sql, int.class, params);
	}
	
}
