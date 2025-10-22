package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MusicGenreDao {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String SELECT_ALL_GENRES_SQL = 
        "select genre_name from music_genre order by genre_name asc";
        
    private static final String COUNT_GENRE_BY_NAME_SQL = 
        "select count(*) from music_genre where genre_name = ?";
    
    public List<String> selectAllGenres() {
        // DB에서 모든 장르 목록을 가져옵니다. (시작 시점에 호출됨)
        return jdbcTemplate.queryForList(SELECT_ALL_GENRES_SQL, String.class);
    }

    public boolean isExistGenre(String genreName) {
        // 해당 장르가 장르목록에 존재하는지
        return jdbcTemplate.queryForObject(COUNT_GENRE_BY_NAME_SQL, int.class, genreName) > 0;
    }
}
