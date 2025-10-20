package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.MusicDto;

@Component
public class MusicMapper implements RowMapper<MusicDto>{

	@Override
	public MusicDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		MusicDto musicDto = new MusicDto();
	
		musicDto.setMusicNo(rs.getInt("music_no"));
        musicDto.setMusicTitle(rs.getString("music_title"));
        musicDto.setMusicTitleChosung(rs.getString("music_title_chosung"));
        musicDto.setMusicArtist(rs.getString("music_artist"));
        musicDto.setMusicArtistChosung(rs.getString("music_artist_chosung"));
        musicDto.setMusicAlbum(rs.getString("music_album"));
        musicDto.setMusicGenre(rs.getString("music_genre"));
        musicDto.setMusicUploader(rs.getString("music_uploader"));
        musicDto.setMusicPlay(rs.getInt("music_play"));
        musicDto.setMusicLike(rs.getInt("music_like"));
        musicDto.setMusicUtime(rs.getTimestamp("music_utime"));
		
		return musicDto;
	}
	
	
}
