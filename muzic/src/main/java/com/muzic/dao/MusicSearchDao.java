package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.condition.SearchCondition;
import com.muzic.mapper.MusicUserVOMapper;
import com.muzic.vo.MusicUserVO;

@Repository
public class MusicSearchDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MusicUserVOMapper musicUserVOMapper;
	
	// 검색 시 정확도 순
	public List<MusicUserVO> searchByAccuracy(SearchCondition searchCondition) {
	    String sql = "select * from ("
	               + "select rownum rn, tmp.* from ("
	               + "select *, "
	               + "case "
	               + "when lower(!@) = lower(?) then 100 "
	               + "when instr(lower(!@), lower(?)) = 1 then 80 "
	               + "when instr(lower(!@), lower(?)) > 0 then 50 "
	               + "else 0 end as score "
	               + "from music_user_view "
	               + "where instr(lower(!@), lower(?)) > 0 "
	               + "order by score desc, music_like desc "
	               + ") tmp"
	               + ") where rn between ? and ?";
	    sql = sql.replace("!@", searchCondition.getColumn());
	    Object[] params = {
	    	searchCondition.getKeyword(), searchCondition.getKeyword(), 
	    	searchCondition.getKeyword(), searchCondition.getKeyword(),
	    	searchCondition.getStart(), searchCondition.getEnd()
	    };
	    return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
	
	// 검색 시 좋아요 순
	public List<MusicUserVO> searchByLike(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "select rownum rn, TMP.* from ("
				+ "select * from music_user_view "
				+ "where instr(lower(!@), lower(?)) > 0 "
				+ "order by music_like desc, music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
		sql = sql.replace("!@", searchCondition.getColumn());
		Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
		return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
	
	// 검색 시 조회 순
	public List<MusicUserVO> searchByPlay(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "select rownum rn, TMP.* from ("
				+ "select * from music_user_view "
				+ "where instr(lower(!@), lower(?)) > 0 "
				+ "order by music_play desc, music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
		sql = sql.replace("!@", searchCondition.getColumn());
		Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
		return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
	
	// 검색 시 최신 순
	public List<MusicUserVO> searchByLatest(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "select rownum rn, TMP.* from ("
				+ "select * from music_user_view "
				+ "where instr(lower(!@), lower(?)) > 0 "
				+ "order by music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
		sql = sql.replace("!@", searchCondition.getColumn());
		Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
		return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
}
