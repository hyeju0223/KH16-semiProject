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
	
	// 초성 검색 시 최신 순
	public List<MusicUserVO> searchByChosungAccuracy(SearchCondition searchCondition) {
	    String sql = "select * from (" 
	        + " select rownum rn, tmp.* "
	        + " from ("
	        + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
	        + " u.member_nickname as uploader_nickname, " 
	        + " m.music_play, m.music_like, m.music_utime, "
	        + " case " 
	        + " when replace(lower(m.!@), ' ', '') = replace(lower(?), ' ', '') then 100 " 
	        + " when instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) = 1 then 80 "
	        + " when instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 then 50 " 
	        + " else 0 " 
	        + " end as score " 
	        + " from music m " 
	        + " left join member u on m.music_uploader = u.member_id " 
	        + " where m.music_status = '승인' " 
	        + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
	        + " order by score desc, m.music_like desc, m.music_no desc " 
	        + " ) tmp " 
	        + " ) where rn between ? and ?";

	    sql = sql.replace("!@", searchCondition.getColumn());

	    Object[] params = {
	        searchCondition.getKeyword(),
	        searchCondition.getKeyword(),
	        searchCondition.getKeyword(),
	        searchCondition.getKeyword(),
	        searchCondition.getStart(),
	        searchCondition.getEnd()
	    };

	    return jdbcTemplate.query(sql,(rs, rowNum) -> MusicUserVO.builder()
	            .musicNo(rs.getInt("music_no"))
	            .musicTitle(rs.getString("music_title"))
	            .musicArtist(rs.getString("music_artist"))
	            .musicAlbum(rs.getString("music_album"))
	            .uploaderNickname(rs.getString("uploader_nickname"))
	            .musicPlay(rs.getInt("music_play"))
	            .musicLike(rs.getInt("music_like"))
	            .musicUtime(rs.getTimestamp("music_utime"))
	            .build(), params
	    );
	}
	
	// 초성 검색 시 좋아요 순
	public List<MusicUserVO> searchByChosungLike(SearchCondition searchCondition) {
	    String sql = "select * from ("
	            + " select rownum rn, tmp.* "
	            + " from ("
	            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
	            + " u.member_nickname as uploader_nickname, "
	            + " m.music_play, m.music_like, m.music_utime "
	            + " from music m "
	            + " left join member u on m.music_uploader = u.member_id "
	            + " where m.music_status = '승인' "
	            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 "
	            + " order by m.music_like desc, m.music_no desc "
	            + " ) tmp "
	            + ") where rn between ? and ?";

	    sql = sql.replace("!@", searchCondition.getColumn());

	    Object[] params = {
	        searchCondition.getKeyword(),
	        searchCondition.getStart(),
	        searchCondition.getEnd()
	    };

	    return jdbcTemplate.query(sql, (rs, rowNum) -> MusicUserVO.builder()
	            .musicNo(rs.getInt("music_no"))
	            .musicTitle(rs.getString("music_title"))
	            .musicArtist(rs.getString("music_artist"))
	            .musicAlbum(rs.getString("music_album"))
	            .uploaderNickname(rs.getString("uploader_nickname"))
	            .musicPlay(rs.getInt("music_play"))
	            .musicLike(rs.getInt("music_like"))
	            .musicUtime(rs.getTimestamp("music_utime"))
	            .build(), params
	    );
	}
	
	// 초성 검색 시 조회 순
	public List<MusicUserVO> searchByChosungPlay(SearchCondition searchCondition) {
	    String sql = "select * from ("
	            + " select rownum rn, tmp.* "
	            + " from ("
	            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
	            + " u.member_nickname as uploader_nickname, "
	            + " m.music_play, m.music_like, m.music_utime "
	            + " from music m "
	            + " left join member u on m.music_uploader = u.member_id "
	            + " where m.music_status = '승인' "
	            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 "
	            + " order by m.music_play desc, m.music_no desc "
	            + " ) tmp "
	            + ") where rn between ? and ?";

	    sql = sql.replace("!@", searchCondition.getColumn());

	    Object[] params = {
	        searchCondition.getKeyword(),
	        searchCondition.getStart(),
	        searchCondition.getEnd()
	    };

	    return jdbcTemplate.query(sql, (rs, rowNum) -> MusicUserVO.builder()
	            .musicNo(rs.getInt("music_no"))
	            .musicTitle(rs.getString("music_title"))
	            .musicArtist(rs.getString("music_artist"))
	            .musicAlbum(rs.getString("music_album"))
	            .uploaderNickname(rs.getString("uploader_nickname"))
	            .musicPlay(rs.getInt("music_play"))
	            .musicLike(rs.getInt("music_like"))
	            .musicUtime(rs.getTimestamp("music_utime"))
	            .build(), params
	    );
	}
	
	// 초성 검색 시 최신 순
	public List<MusicUserVO> searchByChosungLatest(SearchCondition searchCondition) {
	    String sql = "select * from ("
	            + " select rownum rn, tmp.* "
	            + " from ("
	            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
	            + " u.member_nickname as uploader_nickname, "
	            + " m.music_play, m.music_like, m.music_utime "
	            + " from music m "
	            + " left join member u on m.music_uploader = u.member_id "
	            + " where m.music_status = '승인' "
	            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 "
	            + " order by m.music_no desc "
	            + " ) tmp "
	            + ") where rn between ? and ?";

	    sql = sql.replace("!@", searchCondition.getColumn());

	    Object[] params = {
	        searchCondition.getKeyword(),
	        searchCondition.getStart(),
	        searchCondition.getEnd()
	    };

	    return jdbcTemplate.query(sql, (rs, rowNum) -> MusicUserVO.builder()
	            .musicNo(rs.getInt("music_no"))
	            .musicTitle(rs.getString("music_title"))
	            .musicArtist(rs.getString("music_artist"))
	            .musicAlbum(rs.getString("music_album"))
	            .uploaderNickname(rs.getString("uploader_nickname"))
	            .musicPlay(rs.getInt("music_play"))
	            .musicLike(rs.getInt("music_like"))
	            .musicUtime(rs.getTimestamp("music_utime"))
	            .build(), params
	    );
	}
	
}
