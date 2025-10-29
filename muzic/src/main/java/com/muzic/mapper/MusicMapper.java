package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.MusicDto;

@Component
public class MusicMapper implements RowMapper<MusicDto> {

	@Override
	public MusicDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		  return MusicDto.builder()
			        .musicNo(rs.getInt("music_no"))
			        .musicTitle(rs.getString("music_title"))
			        .musicTitleChosung(rs.getString("music_title_chosung"))
			        .musicArtist(rs.getString("music_artist"))
			        .musicArtistChosung(rs.getString("music_artist_chosung"))
			        .musicTitleSearch(rs.getString("music_title_search"))
			        .musicArtistSearch(rs.getString("music_artist_search"))
			        .musicAlbum(rs.getString("music_album"))
			        .musicUploader(rs.getString("music_uploader"))
			        .musicPlay(rs.getInt("music_play"))
			        .musicLike(rs.getInt("music_like"))
			        .musicUtime(rs.getTimestamp("music_utime"))
			        .musicEtime(rs.getTimestamp("music_etime"))
			        .musicStatus(rs.getString("music_status"))
			        .musicAlbumChosung(rs.getString("music_album_chosung"))
			        .musicAlbumSearch(rs.getString("music_album_search"))
			        .build();
	}
}
