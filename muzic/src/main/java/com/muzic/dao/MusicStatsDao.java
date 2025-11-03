package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.condition.SearchCondition;
import com.muzic.mapper.MusicUserVOMapper;
import com.muzic.vo.MusicUserVO;

@Repository
public class MusicStatsDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MusicUserVOMapper musicUserVOMapper;
	
    public List<MusicUserVO> findTopByMbti(String mbti, SearchCondition searchCondition) {
        String sql = "select *" 
        		+ " from("
        		+ " select rownum rn, tmp.*"
        		+ " from("
        		+ " select m.music_no, m.music_title, m.music_artist, m.music_album,"
        		+ " m.music_play, m.music_like, m.music_utime,"
        		+ " m.uploader_nickname, m.cover_attachment_no, m.music_file_attachment_no"
        		+ " from play_log p"
        		+ " join music_user_view m on p.play_log_music = m.music_no"
        		+ " where p.play_log_mbti = ?"
        		+ " group by m.music_no, m.music_title, m.music_artist, m.music_album,"
        		+ " m.music_play, m.music_like, m.music_utime,"
        		+ " m.uploader_nickname, m.cover_attachment_no, m.music_file_attachment_no"
        		+ " order by count(*) desc, m.music_play desc, m.music_like desc"
        		+ " )tmp"
        		+ " )"
        		+ " where rn between ? and ?";
        Object[] params = { mbti, searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicUserVOMapper, params);
    }

}
