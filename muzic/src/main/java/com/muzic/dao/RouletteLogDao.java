// src/main/java/com/muzic/dao/RouletteLogDao.java
package com.muzic.dao;

import com.muzic.dto.RouletteLogDto;
import com.muzic.mapper.RouletteLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RouletteLogDao {
    private final JdbcTemplate jdbc;
    private final RouletteLogMapper mapper;

    // type: "logNo" | "member" | null(전체)
    public int countLogs(int rouletteNo, String type, String keyword) {
        StringBuilder sql = new StringBuilder(
                "select count(*) from roulette_log where roulette_log_roulette = ?"
        );
        if ("logNo".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            sql.append(" and roulette_log_no = ?");
            return jdbc.queryForObject(sql.toString(), Integer.class, rouletteNo, Integer.parseInt(keyword));
        } else if ("member".equalsIgnoreCase(type) && keyword != null && !keyword.isBlank()) {
            sql.append(" and roulette_log_member like '%'||?||'%'");
            return jdbc.queryForObject(sql.toString(), Integer.class, rouletteNo, keyword);
        }
        return jdbc.queryForObject(sql.toString(), Integer.class, rouletteNo);
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

        return jdbc.query(sql, mapper, tailArgs);
    }
}
