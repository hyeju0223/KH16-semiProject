package com.muzic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.RouletteDto;


@Repository
public class RouletteDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//CRUD
	
	//등록
	public int sequence() {
		String sql = "select roulette_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public void insert(RouletteDto rouletteDto) {
		String sql = "insert into roulette("
				+ "roulette_no, roulette_name, "
				+ "roulette_daily_count, roulette_max_point,"
				+ "roulette_min_point, roulette_date"
				+ ") values (?,?,?,?,?,?)";
		Object[] params = {
				rouletteDto.getRouletteNo(),rouletteDto.getRouletteName(),
				rouletteDto.getRouletteDailyCount(), rouletteDto.getRouletteMaxPoint(),
				rouletteDto.getRouletteMinPoint(), rouletteDto.getRouletteDate()};
		jdbcTemplate.update(sql, params);
	}
	

}
