package com.muzic.restcontroller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.muzic.dao.CalendarDao;
import com.muzic.dto.CalendarDto;

@CrossOrigin
@RestController
@RequestMapping("/mypage/calendar")
public class CalendarRestController {
	
	@Autowired
	private CalendarDao calendarDao;
	
	//일정 조회
	@PostMapping("/home")
	public List<CalendarDto> selectByMemberId(@RequestParam String memberId) {
		
		return calendarDao.selectByMemberId(memberId);
		
	}

}
