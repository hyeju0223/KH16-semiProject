// src/main/java/com/muzic/dao/AdminMemberDao.java
package com.muzic.dao;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MemberDto;
import com.muzic.mapper.MemberMapper;            // ✅ 기존 공용 매퍼 재사용
import com.muzic.mapper.AdminMemberListMapper;  // 리스트(VO) 전용
import com.muzic.vo.AdminMemberListVO;

@Repository
public class AdminMemberDao {

    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private AdminMemberListMapper listMapper; // 목록 전용(VO: blacklist_yn 포함)
    @Autowired private MemberMapper memberMapper;        // ✅ 단건/수정용 DTO 매퍼 재사용

    // 검색 컬럼 화이트리스트
    private static final Map<String, String> COL = Map.of(
        "id", "m.member_id",
        "email", "m.member_email",
        "name", "m.member_name",
        "nickname", "m.member_nickname",
        "all", ""
    );

    // [list] count
    public int count(String type, String keyword) {
        StringBuilder sql = new StringBuilder("select count(*) from member m where 1=1 ");
        List<Object> p = new ArrayList<>();
        applySearch(sql, p, type, keyword);
        return jdbcTemplate.queryForObject(sql.toString(), Integer.class, p.toArray());
    }

    // [list] 최신가입순 + Oracle rownum 페이징
    public List<AdminMemberListVO> selectList(String type, String keyword, int begin, int end) {
        StringBuilder inner = new StringBuilder();
        List<Object> p = new ArrayList<>();

        inner.append("""
            select
              m.member_id, m.member_pw, m.member_nickname, m.member_name,
              m.member_email, m.member_mbti, m.member_birth, m.member_contact,
              m.member_role, m.member_point, m.member_login,
              case when exists (
                select 1 from blacklist b
                where b.blacklist_member = m.member_id
                  and b.blacklist_status = 'Y'
              ) then 'Y' else 'N' end as blacklist_yn
            from member m
            where 1=1
            """);

        applySearch(inner, p, type, keyword);
        inner.append(" order by m.member_join desc, m.member_id desc ");

        String paging = """
            select * from (
              select rownum rn, tmp.* from ( %s ) tmp
            ) where rn between ? and ?
            """.formatted(inner.toString());

        p.add(begin);
        p.add(end);

        return jdbcTemplate.query(paging, listMapper, p.toArray());
    }

    // [detail/edit] 단건 조회 — ✅ MemberMapper 재사용
    public MemberDto selectOne(String memberId) {
        String sql = "select * from member where member_id = ?";
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, memberId);
        return list.isEmpty() ? null : list.get(0);
    }

    // [edit] 관리자 수정(등급/포인트 포함)
    public boolean updateByAdmin(MemberDto m) {
        String sql = """
            update member set
              member_nickname=?, member_name=?, member_email=?, member_mbti=?,
              member_birth=?, member_contact=?,
              member_role=?, member_point=?, member_etime = systimestamp
            where member_id=?
            """;
        Object[] p = {
            m.getMemberNickname(), m.getMemberName(), m.getMemberEmail(), m.getMemberMbti(),
            m.getMemberBirth(), m.getMemberContact(),
            m.getMemberRole(), m.getMemberPoint(),
            m.getMemberId()
        };
        return jdbcTemplate.update(sql, p) > 0;
    }

    // [drop] 하드 삭제
    public boolean drop(String memberId) {
        String sql = "delete from member where member_id=?";
        return jdbcTemplate.update(sql, memberId) > 0;
    }

    // 공통 검색절
    private void applySearch(StringBuilder sql, List<Object> p, String type, String keyword) {
        String t = (type == null) ? "all" : type.toLowerCase();
        String k = (keyword == null) ? "" : keyword.trim();

        if ("all".equals(t)) {
            if (!k.isEmpty()) {
                sql.append("""
                    and (
                      instr(m.member_id, ?) > 0 or
                      instr(m.member_email, ?) > 0 or
                      instr(m.member_name, ?) > 0 or
                      instr(m.member_nickname, ?) > 0
                    )
                    """);
                p.add(k); p.add(k); p.add(k); p.add(k);
            }
        } else {
            String col = COL.getOrDefault(t, "m.member_id");
            if (!k.isEmpty()) {
                sql.append(" and instr(").append(col).append(", ?) > 0 ");
                p.add(k);
            }
        }
    }
}
