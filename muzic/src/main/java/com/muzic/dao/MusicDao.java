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
	
	public boolean insert(MusicDto musicDto) {
		String sql = "insert into music("
				+ "music_no, music_title, music_title_chosung, music_artist, music_artist_chosung, "
				+ "music_title_search, music_artist_search, music_album, music_uploader) "
				+ "values("
				+ "music_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { musicDto.getMusicTitle(), musicDto.getMusicTitleChosung(), musicDto.getMusicArtist(), 
				musicDto.getMusicArtistChosung(), musicDto.getMusicTitleSearch(), musicDto.getMusicArtistSearch(),
				musicDto.getMusicAlbum(), musicDto.getMusicUploader()
		};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean delete(int musicNo) {
		String sql = "delete from music where music_no = ?";
		Object[] params = { musicNo };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public List<MusicDto> selectBymemberId(String musicUploader) {
		String sql = "select * from music where music_uploader=?";
		Object[] params = {musicUploader};
		return jdbcTemplate.query(sql,musicMapper, params);
	}
	
	
	
}
