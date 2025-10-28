package com.muzic.dao;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.muzic.dto.CalendarDto;
import com.muzic.mapper.CalendarMapper;

@Repository
public class CalendarDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private CalendarMapper calendarMapper;
	
	//CRUD 구현
	
	//등록
	
	public int sequence() {
		String sql = "select calendar_seq.nextval from dual";
		return jdbcTemplate.queryForObject(sql, int.class);
	}
	
	public void insert(CalendarDto calendarDto) {
		String sql = "insert into calendar("
				+ "calendar_no, calendar_day, calendar_member,"
				+ "calendar_schedule_title, calendar_schedule_content)"
				+ " values (?,?,?,?,?)";
		Object[] params = {
				calendarDto.getCalendarNo(), calendarDto.getCalendarDay(),calendarDto.getCalendarMember(),
				calendarDto.getCalendarScheduleTitle(), calendarDto.getCalendarScheduleContent()
				};
		jdbcTemplate.update(sql, params);
	}

	//조회
	public List<CalendarDto> selectByMemberId(String member) {
		String sql = "select * from calendar where calendar_member = ?";
		Object[] params = {member};

		return jdbcTemplate.query(sql, calendarMapper, params);
	}
	
	//단일조회
	public CalendarDto selectOne(int calendarNo) {
		String sql = "select * from calendar where calendar_no=?";
		Object[] params = {calendarNo};
		List<CalendarDto> list = jdbcTemplate.query(sql, calendarMapper, params);
		return list.isEmpty() ? null : list.get(0);
	}
	
	//출석체크 조회
	public String selectAttendance(CalendarDto calendarDto) {
		String sql = "select calendar_attendance from calendar where calendar_member=? and calendar_day=?";
		Object[] params = {calendarDto.getCalendarMember(), calendarDto.getCalendarDay()};
		try {
			return jdbcTemplate.queryForObject(sql,String.class, params);			
		} catch (Exception e) { //일정이 없을 경우 null로 예외처리
			return null;
		}
		
	}
	
	//출석체크 개수조회
	public int selectTotalAttendance(String memberId) {
	
		String sql = "select count(calendar_attendance) from calendar where calendar_member=? "
				+ "and calendar_attendance = 'Y' "
				+ "and calendar_day between trunc(sysdate, 'MM')"
				+ "and last_day(sysdate)";
		Object[] params = {memberId};
		return jdbcTemplate.queryForObject(sql,Integer.class, params);			
	}


	//일정 삭제
	public boolean delete(int calendarNo) {
		String sql = "delete from calendar where calendar_no=?";
		Object[] params = {calendarNo};
		return jdbcTemplate.update(sql,params) > 0;
	}
	
	//수정
	public boolean update(CalendarDto calendarDto) {
		String sql = "update calendar set "
				+ "calendar_day=?, calendar_schedule_title=?,"
				+ "calendar_schedule_content=?,"
				+ "calendar_etime=systimestamp "
				+ "where calendar_no=?";
		Object[] params = {
				calendarDto.getCalendarDay(), calendarDto.getCalendarScheduleTitle(),
				calendarDto.getCalendarScheduleContent(), 
				calendarDto.getCalendarNo()
		};
		return jdbcTemplate.update(sql,params) > 0;
		
	}
	
	//출석체크 (업데이트)
	public boolean updateAttendance(CalendarDto calendarDto) {
		String sql = "update calendar set calendar_attendance='Y' where calendar_member=? "
				+ "and calendar_day =?";
		Object[] params = {calendarDto.getCalendarMember(), calendarDto.getCalendarDay()};
		return jdbcTemplate.update(sql, params) > 0;
	}
	
	//출석체크 (추가)
	public void checkAttendance(CalendarDto calendarDto) {
		String sql = "insert into calendar("
				+ "calendar_no, calendar_day, calendar_member,"
				+ "calendar_schedule_title, calendar_schedule_content, calendar_attendance)"
				+ " values (?,?,?,'출석체크','출석체크','Y')";
		Object[] params = {
				calendarDto.getCalendarNo(), calendarDto.getCalendarDay(),calendarDto.getCalendarMember()
				};
		jdbcTemplate.update(sql, params);
	}
	

}
