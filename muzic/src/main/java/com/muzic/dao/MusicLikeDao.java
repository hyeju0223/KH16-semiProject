package com.muzic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MusicLikeDao {
	
	@Autowired
	private JdbcTemplate  jdbcTemplate;
	
	public boolean insert(int musicNo, String memberId, String likeStatus)  {
		String sql = "insert into map_music_like(map_music_no, map_member_id, map_like_status"
				+ ") values (?, ?, ?)";
		Object[] params = { musicNo, memberId, likeStatus };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	
	
}
