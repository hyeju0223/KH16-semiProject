package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.condition.SearchCondition;
import com.muzic.mapper.MusicUploaderVOMapper;
import com.muzic.mapper.MusicUserVOMapper;
import com.muzic.vo.MusicUploaderVO;
import com.muzic.vo.MusicUserVO;

@Repository
public class MusicViewDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MusicUserVOMapper musicUserVOMapper;
	
	@Autowired
	private MusicUploaderVOMapper musicUploaderVOMapper;
	
	public MusicUserVO selectOneMusicUserVO(int musicNo) {
		String sql = "select * from music_user_view where music_no = ?";
		Object[] params = {musicNo};
		List<MusicUserVO> list = jdbcTemplate.query(sql, musicUserVOMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public MusicUploaderVO selectOneMusicUploaderVO(String nickname) {
		String sql = "select * from music_uploader_view where uploader_nickname = ?";
		Object[] params = {nickname};
		List<MusicUploaderVO> list = jdbcTemplate.query(sql, musicUploaderVOMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	// 가나다순 조회
	public List<MusicUserVO> selectPagingByTitle(SearchCondition searchCondition) {
		String sql = "select * from ("
	               + " select rownum rn, tmp.* from ("
	               + " select * from music_user_view "
	               + " order by lower(music_title) asc"
	               + " ) tmp "
	               + " ) where rn between ? and ?";
	    Object[] params = { searchCondition.getStart(), searchCondition.getEnd() };
	    return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
	
	// 최신순 조회
	public List<MusicUserVO> selectPagingByLatest(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "select rownum rn, TMP.* from ("
				+ "select * from music_user_view "
				+ "order by music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
	    Object[] params = { searchCondition.getStart(), searchCondition.getEnd() };
	    return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
	
	// 좋아요순 조회
	public List<MusicUserVO> selectPagingByLike(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "	select rownum rn, TMP.* from ("
				+ "select * from music_user_view "
				+ "order by music_like desc, music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
		Object[] params = { searchCondition.getStart(), searchCondition.getEnd() };
		return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}
		
	// 조회순 조회
	public List<MusicUserVO> selectPagingByPlay(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "select rownum rn, TMP.* from ("
				+ "select * from music_user_view "
				+ "order by music_play desc, music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
		Object[] params = { searchCondition.getStart(), searchCondition.getEnd() };
		return jdbcTemplate.query(sql, musicUserVOMapper, params);
	}		
	
	// 마이페이지 업로드 조회
	public List<MusicUploaderVO> selectUploaderPaging(SearchCondition searchCondition) {
		String sql = "select * from ("
				+ "select rownum rn, TMP.* from ("
				+ "select * from music_uploader_view "
				+ "where instr(uploader_nickname, ?) > 0 "
				+ "order by music_no desc"
				+ ") TMP "
				+ ") where rn between ? and ?";
		Object[] params = { 
				searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() 
				};
		return jdbcTemplate.query(sql, musicUploaderVOMapper, params);
	}

}