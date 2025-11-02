package com.muzic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.PlayLogDto;

@Repository
public class PlayLogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean insert(PlayLogDto playLogDto) {
        String sql = "insert into play_log(play_log_no, play_log_music, play_log_member, play_log_mbti"
        		+ ") values(play_log_seq.nextval, ?, ?, ?)";
        Object[] params = {
        		playLogDto.getPlayLogMusic(),
        		playLogDto.getPlayLogMember(),
        		playLogDto.getPlayLogMbti()
        };
        return jdbcTemplate.update(sql, params) > 0;
    }
    
}
