package com.muzic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MemberPointLogDto;

@Repository
public class MemberPointLogDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//포인트 선물에 따른 로그 등록
	
	public int sequence() {
		String sql = "select point_log_no.nextval from dual";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public void insertByGiftpoint(MemberPointLogDto memberPointLogDto) {
		String sql = "insert into member_point_log("
				+ "point_log_no, point_log_member, point_log_change,"
				+ "point_log_reason"
				+ ") values (?,?,?,?)";
		Object[] params = {
				memberPointLogDto.getPointLogMember(), 
				memberPointLogDto.getPointLogChange(),
				memberPointLogDto.getPointLogReason()
				};
		jdbcTemplate.update(sql, params);
	}
	
	public String selectReasonByMonthly(String memberId) {
		String sql = "select point_log_reason from member_point_log where point_log_member=? "
				+ "and point_log_time between trunc(sysdate, 'MM') and last_day(sysdate)";
		Object[] params = {memberId};
		return jdbcTemplate.queryForObject(sql,String.class, params);
	}
}
