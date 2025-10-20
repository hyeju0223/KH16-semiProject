package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CalendarDto {
	
	private int calendarNo;
	private Date calendarDay;
	private String calendarMember;
	private String calendarScheduleTitle;
	private String calendarScheduleContent;
	private String calendarAttendance;
	private Timestamp calendarWtime;
	private Timestamp calendarEtime;
	
}
