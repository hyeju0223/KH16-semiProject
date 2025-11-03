package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MusicStatusHistoryDto;
import com.muzic.mapper.MusicStatusHistoryMapper;

@Repository
public class MusicStatusHistoryDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MusicStatusHistoryMapper musicStatusHistoryMapper;
	
	public int sequence() {
		String sql = "select music_status_history_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public boolean insert(MusicStatusHistoryDto musicStatusHistoryDto) {
		String sql = "insert into music_status_history ("
						+ "msh_no, msh_music_no, msh_music_status, msh_comment, msh_admin_id)" 
						+ "values (music_status_history_seq.nextval, ?, ?, ?, ?)";
		Object[] params = { 
				musicStatusHistoryDto.getMshMusicNo(), musicStatusHistoryDto.getMshMusicStatus(), 
				musicStatusHistoryDto.getMshComment(), musicStatusHistoryDto.getMshAdminId()
		};
		
		return jdbcTemplate.update(sql,params) > 0;
	}
	
    // 5) 해당 음원의 히스토리 전체 조회
    public List<MusicStatusHistoryDto> selectHistory(int musicNo) {
        String sql = "select * from music_status_history "
                   + "where msh_music_no = ? order by msh_changed_time desc";
        return jdbcTemplate.query(sql, musicStatusHistoryMapper, musicNo);
    }
	
}
