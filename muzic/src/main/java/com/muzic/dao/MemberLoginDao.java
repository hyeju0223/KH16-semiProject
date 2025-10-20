package com.muzic.dao;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MemberLoginDto;
import com.muzic.mapper.MemberLoginMapper;

@Repository
public class MemberLoginDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberLoginMapper memberLoginMapper;

    /**
     * ✅ 로그인 상태 조회 (로그인 시 자동 해제 검사 포함)
     * - 로그인 시도 시 자동 호출됨
     * - 잠긴 상태일 경우, 5분 경과 시 자동 해제
     */
    public MemberLoginDto selectOne(String memberId) {
        String sql = "SELECT * FROM member_login WHERE login_member = ?";
        List<MemberLoginDto> list = jdbcTemplate.query(sql, memberLoginMapper, memberId);

        if (list.isEmpty()) return null;

        MemberLoginDto dto = list.get(0);

        // 🔒 잠긴 계정이면 5분 경과 여부 확인
        if ("Y".equals(dto.getLoginLocked()) && dto.getLoginLockTime() != null) {
            LocalDateTime lockedAt = dto.getLoginLockTime().toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();

            // ✅ 잠긴 시각 + 5분 이후면 자동 해제
            if (now.isAfter(lockedAt.plusMinutes(5))) {
                unlockAccount(memberId);
                dto.setLoginLocked("N");
                dto.setLoginFailCount(0);
                dto.setLoginLockTime(null);
            }
        }

        return dto;
    }

    /**
     * ✅ 최초 로그인 기록 생성 (회원가입 시)
     */
    public void insert(String memberId) {
        String sql = "INSERT INTO member_login(login_no, login_member, login_try_time) "
                   + "VALUES(member_login_seq.NEXTVAL, ?, SYSTIMESTAMP)";
        jdbcTemplate.update(sql, memberId);
    }

    /**
     * ✅ 로그인 시도 실패 시 실패 횟수 +1
     */
    public void increaseFailCount(String memberId) {
        String sql = "UPDATE member_login "
                   + "SET login_fail_count = login_fail_count + 1, "
                   + "login_try_time = SYSTIMESTAMP "
                   + "WHERE login_member = ?";
        jdbcTemplate.update(sql, memberId);
    }

    /**
     * ✅ 로그인 성공 시 실패 기록 초기화
     */
    public void resetFailCount(String memberId) {
        String sql = "UPDATE member_login "
                   + "SET login_fail_count = 0, "
                   + "login_locked = 'N', "
                   + "login_lock_time = NULL, "
                   + "login_try_time = SYSTIMESTAMP "
                   + "WHERE login_member = ?";
        jdbcTemplate.update(sql, memberId);
    }

    /**
     * ✅ 5회 이상 실패 시 계정 잠금 처리
     */
    public void lockAccount(String memberId) {
        String sql = "UPDATE member_login "
                   + "SET login_locked = 'Y', login_lock_time = SYSTIMESTAMP "
                   + "WHERE login_member = ?";
        jdbcTemplate.update(sql, memberId);
    }

    /**
     * ✅ 잠금 해제 (자동 또는 관리자 해제 시 공용)
     */
    public boolean unlockAccount(String memberId) {
        String sql = "UPDATE member_login "
                   + "SET login_locked = 'N', login_fail_count = 0, login_lock_time = NULL "
                   + "WHERE login_member = ?";
        return jdbcTemplate.update(sql, memberId) > 0;
    }
}
