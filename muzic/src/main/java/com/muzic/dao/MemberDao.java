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
    private BCryptPasswordEncoder passwordEncoder; //  비밀번호 비교용

	//CRUD
	
	//조회 - 회원 아이디
	public MemberDto selectByMemberId(String memberId) {
		String sql = "select * from member where member_id = ?";
		Object[] params = {memberId};
		 List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	//조회 - 전체
	public List<MemberDto> selectList() {
		String sql = "select * from member order by member_join desc";
		return jdbcTemplate.query(sql, memberMapper);
	}
	
//	//등록
//	public void insert(MemberDto memberDto) {
//		String sql = "insert into member() values ()";
//		Object[] params = {};
//		jdbcTemplate.update(sql, params);
//	}
	
	//수정
	
	//삭제 (=탈퇴)
	public boolean delete(String memberId) {
		String sql = "delete from member where member_id=?";
		Object[] params = {memberId};
		return jdbcTemplate.update(sql, params) > 0;
	}
	


    //  회원가입 (비밀번호 암호화 저장)
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

    //   단일 회원 조회 (로그인/검증용)
    public MemberDto selectOne(String memberId) {
        String sql = "SELECT * FROM member WHERE member_id = ?";
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, memberId);
        return list.isEmpty() ? null : list.get(0);
    }


    //  아이디 찾기 (닉네임 + 이메일)
    public MemberDto findIdByNicknameAndEmail(String nickname, String email) {
        String sql = "SELECT * FROM member WHERE member_nickname = ? AND member_email = ?";
        Object[] params = { nickname, email };
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, params);
        return list.isEmpty() ? null : list.get(0);
    }
 


    //  비밀번호 찾기 (아이디 + 닉네임 + 이메일)
    public MemberDto findPwByIdNicknameEmail(String memberId, String nickname, String email) {
        String sql = "SELECT * FROM member WHERE member_id = ? AND member_nickname = ? AND member_email = ?";
        Object[] params = { memberId, nickname, email };
        List<MemberDto> list = jdbcTemplate.query(sql, memberMapper, params);
        return list.isEmpty() ? null : list.get(0);
    }
    // 아이디 중복 검사
    public boolean existsById(String memberId) {
        String sql = "SELECT COUNT(*) FROM member WHERE member_id = ?";
        int count = jdbcTemplate.queryForObject(sql, int.class, memberId);
        return count > 0;
    }

    //  닉네임 중복 검사
    public boolean existsByNickname(String nickname) {
        String sql = "SELECT COUNT(*) FROM member WHERE member_nickname = ?";
        int count = jdbcTemplate.queryForObject(sql, int.class, nickname);
        return count > 0;
    }
    //비번변경
 // updateMemberPw 메서드 (수정된 코드)
    public boolean updateMemberPw(MemberDto memberDto) {
        String sql = "UPDATE member SET member_pw = ? WHERE member_id = ?";

        // 비밀번호 암호화 로직 추가
        String encPw = passwordEncoder.encode(memberDto.getMemberPw());

        Object[] params = { encPw, memberDto.getMemberId() };
        return jdbcTemplate.update(sql, params) > 0;
    }
    
    //회원 정보 수정
	public boolean update(MemberDto memberDto) {
		String sql = "update member set "
				+ "member_nickname=?, member_name=?,"
				+ "member_email=?, member_mbti=?,"
				+ "member_birth=?, member_contact=?, "
				+ "member_postcode=?, member_address1=?,"
				+ "member_address2=?, member_etime=systimestamp "
				+ "where member_id=?";
		Object[] params = {
				memberDto.getMemberNickname(), memberDto.getMemberName(),
				memberDto.getMemberEmail(), memberDto.getMemberMbti(),
				memberDto.getMemberBirth(), memberDto.getMemberContact(),
				memberDto.getMemberPostcode(), memberDto.getMemberAddress1(),
				memberDto.getMemberAddress2(), memberDto.getMemberId()};
		return jdbcTemplate.update(sql,params) > 0;
	}
}
