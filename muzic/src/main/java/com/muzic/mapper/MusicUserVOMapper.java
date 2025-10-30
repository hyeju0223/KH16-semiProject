package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.vo.MusicUserVO;

@Component
public class MusicUserVOMapper implements RowMapper<MusicUserVO> {

    @Override
    public MusicUserVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MusicUserVO.builder()
                .musicNo(rs.getInt("music_no"))
                .musicTitle(rs.getString("music_title"))
                .musicArtist(rs.getString("music_artist"))
                .musicAlbum(rs.getString("music_album"))
                .uploaderNickname(rs.getString("uploader_nickname"))
                .musicPlay(rs.getInt("music_play"))
                .musicLike(rs.getInt("music_like"))
                .musicUtime(rs.getTimestamp("music_utime"))
                .coverAttachmentNo(rs.getInt("cover_attachment_no"))
                .musicFileAttachmentNo(rs.getInt("music_file_attachment_No"))
                // musicGenres는 따로 DAO에서 setGenreSet()으로 채워줘야 함.
                .build();
    }
}