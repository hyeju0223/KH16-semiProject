package com.muzic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.RouletteLogDto;



@Repository
public class RouletteLogDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//CRUD
	
	public int sequence() {
		String sql = "select roulette_log_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public void insert(RouletteLogDto rouletteLogDto) {
		String sql = "insert into roulette_log("
				+ "roulette_log_no, roulette_log_roulette,"
				+ "roulette_log_member, roulette_log_point"
				+ ") values (?,?,?,?)";
		Object[] params= {
				rouletteLogDto.getRouletteLogNo(), rouletteLogDto.getRouletteLogRoulette(),
				rouletteLogDto.getRouletteLogMember(), rouletteLogDto.getRouletteLogPoint()};
		jdbcTemplate.update(sql, params);
	}

	//1일 참여 여부 확인
	public int selectRouletteCkeck(String memberId) {
		String sql = "select count(*) from roulette_log where TRUNC(roulette_log_time) = TRUNC(SYSDATE) and roulette_log_member=?";
		Object[] params={memberId};
		return jdbcTemplate.queryForObject(sql, Integer.class,params);
	}
}
