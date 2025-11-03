
package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.RouletteDto;
import com.muzic.mapper.RouletteMapper;


@Repository
public class RouletteDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private RouletteMapper mapper;
	
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


    // 이름 키워드 카운트 (null/빈문자면 전체)
    public int countByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return jdbcTemplate.queryForObject("select count(*) from roulette", Integer.class);
        }
        String sql = "select count(*) from roulette where roulette_name like '%'||?||'%'";
        return jdbcTemplate.queryForObject(sql, Integer.class, keyword);
    }

    // Oracle 페이징 (ROWNUM) + 이름 검색
    public List<RouletteDto> selectList(int begin, int end, String keyword) {
        StringBuilder inner = new StringBuilder(
                "select * from roulette "
        );
        Object[] args;
        if (keyword != null && !keyword.isBlank()) {
            inner.append("where roulette_name like '%'||?||'%' ");
            inner.append("order by roulette_no desc");
            args = new Object[]{keyword, end, begin};
        } else {
            inner.append("order by roulette_no desc");
            args = new Object[]{end, begin};
        }

        String sql =
                "select * from (" +
                "  select rownum rn, a.* from (" + inner + ") a where rownum <= ?" +
                ") where rn >= ?";

        return jdbcTemplate.query(sql, mapper, args);
    }

    public RouletteDto selectOne(int rouletteNo) {
        String sql = "select * from roulette where roulette_no = ?";
        List<RouletteDto> list = jdbcTemplate.query(sql, mapper, rouletteNo);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean updateName(int rouletteNo, String name) {
        String sql = "update roulette set roulette_name=? where roulette_no=?";
        return jdbcTemplate.update(sql, name, rouletteNo) > 0;
    }

}
