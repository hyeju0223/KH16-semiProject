package com.muzic.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.MemberPointLogDto;
import com.muzic.mapper.MemberPointLogMapper;

@Repository
public class MemberPointLogDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	//포인트 선물에 따른 로그 등록
	@Autowired
	private MemberPointLogMapper memberPointLogMapper;
	
	public int sequence() {
		String sql = "select point_log.nextval from dual";
		
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	//등록
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
	
	//출석체크 보상 제한을 위한 조회(1달에 한번만 지급 가능)
	public boolean selectReasonByMonthly(String memberId) {
		String sql = "select count(*) from member_point_log "
				+ "where point_log_member = ? and point_log_reason = '출석체크' "
				+ "and point_log_time >= trunc(sysdate, 'MM')";
		Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, memberId);
		return cnt != null && cnt > 0;
	}
	
	//마이페이지용 전체조회
	public List<MemberPointLogDto> selectByMemberId(String memberId) {
		String sql = "select * from member_point_log where point_log_member=? order by point_log_time desc";
		Object[] params = {memberId};
		return jdbcTemplate.query(sql, memberPointLogMapper, params);
	}
}
