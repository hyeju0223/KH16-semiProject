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
		String sql = "select point_log.nextval from dual";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public void insertByGiftpoint(MemberPointLogDto memberPointLogDto) {
		String sql = "insert into member_point_log("
				+ "point_log_no, point_log_member, point_log_change,"
				+ "point_log_reason"
				+ ") values (?,?,?,?)";
		Object[] params = {
				memberPointLogDto.getPointLogNo(),
				memberPointLogDto.getPointLogMember(), 
				memberPointLogDto.getPointLogChange(),
				memberPointLogDto.getPointLogReason()
				};
		jdbcTemplate.update(sql, params);
	}
	
	public boolean selectReasonByMonthly(String memberId) {
		String sql = "select count(*) from member_point_log "
				+ "where point_log_member = ? and point_log_reason = '출석체크' "
				+ "and point_log_time >= trunc(sysdate, 'MM')";
		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
		return cnt != null && cnt > 0;

	}
}
