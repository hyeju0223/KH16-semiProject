package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MusicDto;
import com.muzic.mapper.MusicMapper;

@Repository
public class MusicDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MusicMapper musicMapper;
	
	public List<MusicDto> selectBymemberId(String musicUploader) {
		String sql = "select * from music where music_uploader=?";
		Object[] params = {musicUploader};
		return jdbcTemplate.query(sql,musicMapper, params);

	}
	
	
	
}
