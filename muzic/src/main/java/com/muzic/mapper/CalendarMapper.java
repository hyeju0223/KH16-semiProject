package com.muzic.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.muzic.dto.CalendarDto;

@Component
public class CalendarMapper implements RowMapper<CalendarDto> {

	@Override
	public CalendarDto mapRow(ResultSet rs, int rowNum) throws SQLException {
		return CalendarDto.builder()
				.calendarNo(rs.getInt("calendar_no"))
				.calendarDay(rs.getDate("calendar_day"))
				.calendarMember(rs.getString("calendar_member"))
				.calendarScheduleTitle(rs.getString("calendar_schedule_title"))
				.calendarScheduleContent(rs.getString("calendar_schedule_content"))
				.calendarAttendance(rs.getString("calendar_attendance"))
				.calendarWtime(rs.getTimestamp("calendar_wtime"))
				.calendarEtime(rs.getTimestamp("calendar_etime"))
				.build();
	}
	
	

}
