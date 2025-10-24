package com.muzic.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.vo.MusicUploaderVO;

@Component
public class MusicUploaderVOMapper implements RowMapper<MusicUploaderVO> {

    @Override
    public MusicUploaderVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MusicUploaderVO.builder()
                .musicNo(rs.getInt("music_no"))
                .musicTitle(rs.getString("music_title"))
                .musicArtist(rs.getString("music_artist"))
                .musicAlbum(rs.getString("music_album"))
                .uploaderNickname(rs.getString("uploader_nickname"))
                .musicPlay(rs.getInt("music_play"))
                .musicLike(rs.getInt("music_like"))
                .musicUtime(rs.getTimestamp("music_utime"))
                .musicEtime(rs.getTimestamp("music_etime"))
                .musicStatus(rs.getString("music_status"))
                .adminComment(rs.getString("admin_comment"))
                // musicGenres는 Dao에서 따로 Set으로 주입 예정
                .build();
    }
}