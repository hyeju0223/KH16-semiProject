// src/main/java/com/muzic/dao/RouletteDao.java
package com.muzic.dao;

import com.muzic.dto.RouletteDto;
import com.muzic.mapper.RouletteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RouletteDao {
    private final JdbcTemplate jdbc;
    private final RouletteMapper mapper;

    // 이름 키워드 카운트 (null/빈문자면 전체)
    public int countByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return jdbc.queryForObject("select count(*) from roulette", Integer.class);
        }
        String sql = "select count(*) from roulette where roulette_name like '%'||?||'%'";
        return jdbc.queryForObject(sql, Integer.class, keyword);
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

        return jdbc.query(sql, mapper, args);
    }

    public RouletteDto selectOne(int rouletteNo) {
        String sql = "select * from roulette where roulette_no = ?";
        List<RouletteDto> list = jdbc.query(sql, mapper, rouletteNo);
        return list.isEmpty() ? null : list.get(0);
    }

    public boolean updateName(int rouletteNo, String name) {
        String sql = "update roulette set roulette_name=? where roulette_no=?";
        return jdbc.update(sql, name, rouletteNo) > 0;
    }
}
