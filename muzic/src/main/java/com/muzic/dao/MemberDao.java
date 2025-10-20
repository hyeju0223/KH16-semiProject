package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MemberDto;
import com.muzic.mapper.MemberMapper;

@Repository
public class MemberDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 🔒 비밀번호 비교용

    // ✅ 회원가입 (비밀번호 암호화 저장)
    public void insert(MemberDto memberDto) {
        String sql = """
            INSERT INTO member(
                member_id, member_pw, member_nickname, member_name,
                member_email, member_mbti, member_birth, member_contact,
                member_postcode, member_address1, member_address2
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        // 비밀번호 암호화
        String encPw = passwordEncoder.encode(memberDto.getMemberPw());

        Object[] params = {
            memberDto.getMemberId(), encPw,
            memberDto.getMemberNickname(), memberDto.getMemberName(),
            memberDto.getMemberEmail(), memberDto.getMemberMbti(),
            memberDto.getMemberBirth(), memberDto.getMemberContact(),
            memberDto.getMemberPostcode(), memberDto.getMemberAddress1(),
            memberDto.getMemberAddress2()
        };
        jdbcTemplate.update(sql, params);
    }

    // ✅ 단일 회원 조회 (로그인/검증용)
    public MemberDto selectOne(String memberId) {
        String sql = "SELECT * FROM member WHERE member_id = ?";
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, memberId);
        return list.isEmpty() ? null : list.get(0);
    }

    // ✅ 로그인 (BCrypt 비교)
    public boolean login(String memberId, String inputPw) {
        MemberDto findDto = selectOne(memberId);
        if (findDto == null) return false;
        return passwordEncoder.matches(inputPw, findDto.getMemberPw());
    }

    // ✅ 아이디 찾기 (닉네임 + 이메일)
    public MemberDto findIdByNicknameAndEmail(String nickname, String email) {
        String sql = "SELECT * FROM member WHERE member_nickname = ? AND member_email = ?";
        Object[] params = { nickname, email };
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, params);
        return list.isEmpty() ? null : list.get(0);
    }

    // ✅ 비밀번호 찾기 (아이디 + 닉네임 + 이메일)
    public MemberDto findPwByIdNicknameEmail(String memberId, String nickname, String email) {
        String sql = "SELECT * FROM member WHERE member_id = ? AND member_nickname = ? AND member_email = ?";
        Object[] params = { memberId, nickname, email };
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, params);
        return list.isEmpty() ? null : list.get(0);
    }
}
