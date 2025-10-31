package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.condition.SearchCondition;
import com.muzic.mapper.MusicSearchVOMapper;
import com.muzic.vo.MusicSearchVO;

@Repository
public class MusicSearchDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MusicSearchVOMapper musicSearchVOMapper;
	
	 // 정확도순 (초성)
    public List<MusicSearchVO> searchByChosungAccuracy(SearchCondition searchCondition) {
        String sql = "select * from (" 
            + " select rownum rn, tmp.* from (" 
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime, "
            + " case "
            + " when replace(lower(m.!@), ' ', '') = replace(lower(?), ' ', '') then 100 " 
            + " when instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) = 1 then 80 "
            + " when instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 then 50 "
            + " else 0 end as score "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 "
            + " order by score desc, m.music_like desc, m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = {
            searchCondition.getKeyword(), searchCondition.getKeyword(),
            searchCondition.getKeyword(), searchCondition.getKeyword(),
            searchCondition.getStart(), searchCondition.getEnd()
        };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }

    // 좋아요순 (초성)
    public List<MusicSearchVO> searchByChosungLike(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
            + " order by m.music_like desc, m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }

    // 조회순 (초성)
    public List<MusicSearchVO> searchByChosungPlay(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
            + " order by m.music_play desc, m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }

    // 최신순 (초성)
    public List<MusicSearchVO> searchByChosungLatest(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
            + " order by m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }
    
    // 정확도순 원본 컬럼
    public List<MusicSearchVO> searchByAccuracy(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime, "
            + " case "
            + " when replace(lower(m.!@), ' ', '') = replace(lower(?), ' ', '') then 100 "
            + " when instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) = 1 then 80 "
            + " when instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 then 50 "
            + " else 0 end as score "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
            + " order by score desc, m.music_like desc, m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = {
            searchCondition.getKeyword(), searchCondition.getKeyword(),
            searchCondition.getKeyword(), searchCondition.getKeyword(),
            searchCondition.getStart(), searchCondition.getEnd()
        };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }

    // 좋아요순 원본 컬럼
    public List<MusicSearchVO> searchByLike(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
            + " order by m.music_like desc, m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }
    
    // 재생순 원본 컬럼
    public List<MusicSearchVO> searchByPlay(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 "
            + " order by m.music_play desc, m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }
    
    // 최신순 원본 컬럼
    public List<MusicSearchVO> searchByLatest(SearchCondition searchCondition) {
        String sql = "select * from ( select rownum rn, tmp.* from ("
            + " select m.music_no, m.music_title, m.music_artist, m.music_album, "
            + " u.member_nickname as uploader_nickname, m.music_play, m.music_like, m.music_utime "
            + " from music m left join member u on m.music_uploader = u.member_id "
            + " where m.music_status = '승인' "
            + " and instr(replace(lower(m.!@), ' ', ''), replace(lower(?), ' ', '')) > 0 " 
            + " order by m.music_no desc "
            + " ) tmp ) where rn between ? and ?";

        sql = sql.replace("!@", searchCondition.getColumn());
        Object[] params = { searchCondition.getKeyword(), searchCondition.getStart(), searchCondition.getEnd() };
        return jdbcTemplate.query(sql, musicSearchVOMapper, params);
    }
}