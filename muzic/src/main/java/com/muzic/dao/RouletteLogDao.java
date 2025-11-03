
package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.RouletteLogDto;
import com.muzic.mapper.RouletteLogMapper;



@Repository
public class RouletteLogDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
    private RouletteLogMapper mapper;
	
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
	
    // type: "logNo" | "member" | null(전체)
    public int countLogs(int rouletteNo, String type, String keyword) {
        StringBuilder sql = new StringBuilder(
                "select count(*) from roulette_log where roulette_log_roulette = ?"
        );
        if ("logNo".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            sql.append(" and roulette_log_no = ?");
            return jdbcTemplate.queryForObject(sql.toString(), Integer.class, rouletteNo, Integer.parseInt(keyword));
        } else if ("member".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            sql.append(" and roulette_log_member like '%'||?||'%'");
            return jdbcTemplate.queryForObject(sql.toString(), Integer.class, rouletteNo, keyword);
        }
        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, rouletteNo);
    }

    public List<RouletteLogDto> selectLogs(int rouletteNo, int begin, int end, String type, String keyword) {
        StringBuilder where = new StringBuilder(
                "from roulette_log where roulette_log_roulette = ? "
        );
        Object[] tailArgs;
        if ("logNo".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            where.append("and roulette_log_no = ? ");
            tailArgs = new Object[]{rouletteNo, Integer.parseInt(keyword), end, begin};
        } else if ("member".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            where.append("and roulette_log_member like '%'||?||'%' ");
            tailArgs = new Object[]{rouletteNo, keyword, end, begin};
        } else {
            tailArgs = new Object[]{rouletteNo, end, begin};
        }

        String inner = "select * " + where + "order by roulette_log_no desc";
        String sql =
                "select * from (" +
                "  select rownum rn, a.* from (" + inner + ") a where rownum <= ?" +
                ") where rn >= ?";

        return jdbcTemplate.query(sql, mapper, tailArgs);
    }

}
