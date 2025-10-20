package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MemberDto;
import com.muzic.mapper.MemberMapper;

@Repository
public class MemberDao {

	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private MemberMapper memberMapper;
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
	
}
