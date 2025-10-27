package com.muzic.dao;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;

@Repository
public class MusicGenreDao {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Set을 사용하여 장르 이름을 저장하고 검색 속도를 높임, 서버 시작 시 데이터가 로드.(저장된 장르 자체는 변하는 일이 거의 없기때문)
    public Set<String> cachedGenres;
    
    public void cacheAllGenres() {
    	String sql = "select genre_name from music_genre order by genre_name asc";
    	 List<String> genreList = jdbcTemplate.queryForList(sql, String.class);
    	 // HashSet으로 데이터 조회 속도 향상
    	// Collections.unmodifiableSet로 감싸서 외부에서 Set을 변경하지 못하도록 보호.
    	 cachedGenres = Collections.unmodifiableSet(new HashSet<>(genreList));
    }
    
    // 애플리케이션 시작 시 (Bean 생성 및 의존성 주입 완료 후) 
    // DB에서 모든 장르 목록을 로드하여 메모리에 저장. (@PostConstruct로 자동 실행)
    @PostConstruct
    public void init() {
    	cacheAllGenres();
    }
    
    // DB에서 최신 장르 목록을 다시 로드하여 메모리 캐시를 갱신.
    // 관리자 기능 등으로 장르 추가/삭제 발생 시 이 메서드를 수동으로 호출해야 
    // 최신 장르 목록이 캐시에 반영.
    
    public boolean addGenre(String genreName) {
        String sql = "insert into music_genre(genre_name) values(?)";
        Object[] params = { genreName };
        int result = jdbcTemplate.update(sql,params);
        // DB 변경 성공 시 캐시 강제 갱신
        if (result > 0) {
            // 장르가 추가되었으므로, 캐시를 재로딩
            cacheAllGenres();
        }
        
        return result > 0;
    }
    
    //읽기 전용 Getter
    public Set<String> getCachedGenres() {
        return cachedGenres;
    }
    
    // 음원선택시 선택한 장르가 캐시에 저장한 장르와 모두 일치하는지 확인하는 메소드
    public boolean areAllGenresValid(Set<String> musicGenres) {
        return cachedGenres.containsAll(musicGenres);
    }
    
    // 필요없음(areAllGenresValid로 전부 검사하는게 중요)
//    public boolean isExistGenre(String genreName) {
//        // 해당 장르가 장르목록에 존재하는지
//    	String sql = "select count(*) from music_genre where genre_name = ?";
//    	Object[] params = { genreName };
//        return jdbcTemplate.queryForObject(sql, int.class, params) > 0;
//    }
    
    
}
