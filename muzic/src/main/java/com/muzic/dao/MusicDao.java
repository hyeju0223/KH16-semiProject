package com.muzic.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MusicDto;
import com.muzic.mapper.MusicMapper;

@Repository
public class MusicDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MusicMapper musicMapper;
	
	public int sequence() {
		String sql = "select music_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public boolean insert(MusicDto musicDto) {
		String sql = "insert into music("
				+ "music_no, music_title, music_title_chosung, music_artist, music_artist_chosung, "
				+ "music_title_search, music_artist_search, music_album, music_uploader, music_status) "
				+ "values("
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Object[] params = { 
				musicDto.getMusicNo(), musicDto.getMusicTitle(), musicDto.getMusicTitleChosung(), 
				musicDto.getMusicArtist(), musicDto.getMusicArtistChosung(), musicDto.getMusicTitleSearch(), 
				musicDto.getMusicArtistSearch(), musicDto.getMusicAlbum(), musicDto.getMusicUploader(), musicDto.getMusicStatus()
				};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean delete(int musicNo) {
		String sql = "delete from music where music_no = ?";
		Object[] params = { musicNo };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean update(MusicDto musicDto) {
		String sql = "update music set music_title = ?, music_title_chosung = ?, music_artist = ?, "
		        	+ "music_artist_chosung = ?,  music_title_search = ?, music_artist_search = ?, "
		            + "music_album = ?, music_status = ?, music_etime = systimestamp "
		            + "where music_no = ?";
	    Object[] params = {
	        musicDto.getMusicTitle(), musicDto.getMusicTitleChosung(), musicDto.getMusicArtist(),  
	        musicDto.getMusicArtistChosung(), musicDto.getMusicTitleSearch(),  musicDto.getMusicArtistSearch(),
	        musicDto.getMusicAlbum(),  musicDto.getMusicStatus(), musicDto.getMusicNo()
	    };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public int insertMusicGenres(int musicNo, List<String> musicGenres) { // 중간에 실행이 안될수 있으니 int로 몇번 업데이트가 작동했는지 확인
        if (musicGenres == null || musicGenres.isEmpty()) {
            return 0;
        }
        String sql = "insert into map_music_genre(map_music_no, map_genre_name) values (?, ?)";
        // BatchPreparedStatementSetter를 사용하여 리스트의 크기만큼 반복적으로 INSERT 쿼리를 실행. 성능 최적화
        int[] results = jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            
            // 쿼리 실행 횟수 (리스트의 크기)
            @Override
            public int getBatchSize() {
                return musicGenres.size();
            }
            // 매 실행마다 PreparedStatement에 값을 설정.
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                // 1. music_no 설정 
                ps.setInt(1, musicNo); // 1은 첫번째 홀더
                // 2. i번째 장르 이름 설정
                ps.setString(2, musicGenres.get(i)); // 2는 두번째 홀더
            }
        });
        
        // 성공한 모든 행의 개수를 합산하여 반환
        int totalUpdatedRows = 0;
        for (int result : results) {
            totalUpdatedRows += result;
        }
        
        return totalUpdatedRows;
    }
	
	public boolean deleteMusicGenres(int musicNo) {
		String sql = "delete from map_music_genre where map_music_no = ?";
		Object[] params = { musicNo };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean updateMusicStatus(int musicNo, String musicStatus) {
		String sql = "update music set music_status = ? where music_no = ?";
		Object[] params = { musicStatus, musicNo };
		return jdbcTemplate.update(sql,params) > 0;
	}
	
	public List<MusicDto> selectByMemberId(String memberId) {
		String sql = "select * from music where memberId = ?";
		Object[] params = {memberId};
		return jdbcTemplate.query(sql, musicMapper, params);
	}
	
	public MusicDto selectOne(int musicNo) {
		String sql = "select * from music where music_no = ?";
		Object[] params = {musicNo};
		List<MusicDto> list = jdbcTemplate.query(sql, musicMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public boolean updateLikeCount(int musicNo) {
		String sql = "update music set music_like = ("
				+ "select count (*) from map_music_like where map_music_no = ? and like_status = 'Y') "
				+ "where music_no = ?";
		Object[] params = { musicNo, musicNo };
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	public boolean updatePlayCount(int musicNo) {
		String sql = "update music set music_play = music_play + 1 where music_no = ?";
		Object[] params = { musicNo };
		return jdbcTemplate.update(sql, params) > 0;
	}
}
	

