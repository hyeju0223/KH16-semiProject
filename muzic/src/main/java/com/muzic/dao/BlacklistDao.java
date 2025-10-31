package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.muzic.dto.BlacklistDto;
import com.muzic.mapper.BlacklistMapper;

@Repository
public class BlacklistDao {
    @Autowired private JdbcTemplate jdbc;

    // 활성(Y) 존재 여부
    public boolean existsActive(String memberId) {
        String sql = "select count(*) from blacklist where blacklist_member=? and blacklist_status='Y'";
        Integer cnt = jdbc.queryForObject(sql, Integer.class, memberId);
        return cnt != null && cnt > 0;
    }

    // 활성(Y) 목록 (상세용)
    public List<BlacklistDto> selectActiveByMember(String memberId) {
        String sql = """
            select * from blacklist
             where blacklist_member=? and blacklist_status='Y'
             order by blacklist_registration_time desc
        """;
        return jdbc.query(sql, new BlacklistMapper(), memberId);
    }

    // 등록: 기존 활성 모두 해제 후 새로 1건 생성 (중복 활성 방지)
    @Transactional
    public void insert(String memberId, String reason) {
        // 1) 혹시 남아있던 활성건 일괄 해제
        String off = """
            update blacklist set
              blacklist_status='N',
              blacklist_release_time = systimestamp
            where blacklist_member=? and blacklist_status='Y'
        """;
        jdbc.update(off, memberId);

        // 2) 신규 활성건 등록
        String on = """
            insert into blacklist(
              blacklist_no, blacklist_member, blacklist_reason,
              blacklist_status, blacklist_registration_time, blacklist_release_time
            ) values (
              blacklist_seq.nextval, ?, ?, 'Y', systimestamp, null
            )
        """;
        jdbc.update(on, memberId, reason);
    }

    // 해제: 활성(Y)만 일괄 해제
    public int releaseActive(String memberId) {
        String sql = """
            update blacklist set
              blacklist_status='N',
              blacklist_release_time = systimestamp
            where blacklist_member=? and blacklist_status='Y'
        """;
        return jdbc.update(sql, memberId);
    }
}
