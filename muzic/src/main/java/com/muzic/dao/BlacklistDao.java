// src/main/java/com/muzic/dao/BlacklistDao.java
package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.BlacklistDto;
import com.muzic.mapper.BlacklistMapper;

@Repository
public class BlacklistDao {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private BlacklistMapper blacklistMapper;

    public void insert(String memberId, String reason) {
        String sql = """
            insert into blacklist(
              blacklist_no, blacklist_member, blacklist_reason,
              blacklist_status, blacklist_registration_time
            )
            values(blacklist_seq.nextval, ?, ?, 'Y', systimestamp)
            """;
        jdbcTemplate.update(sql, memberId, reason);
    }

    public boolean existsActive(String memberId) {
        String sql = "select count(*) from blacklist where blacklist_member=? and blacklist_status='Y'";
        Integer c = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
        return c != null && c > 0;
    }

    public List<BlacklistDto> selectActiveByMember(String memberId) {
        String sql = """
            select * from blacklist
            where blacklist_member=? and blacklist_status='Y'
            order by blacklist_registration_time desc, blacklist_no desc
            """;
        return jdbcTemplate.query(sql, blacklistMapper, memberId);
    }

    public int releaseActive(String memberId) {
        String sql = """
            update blacklist
            set blacklist_status='N', blacklist_release_time=systimestamp
            where blacklist_member=? and blacklist_status='Y'
            """;
        return jdbcTemplate.update(sql, memberId);
    }
}
