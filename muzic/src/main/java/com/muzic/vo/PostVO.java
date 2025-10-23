package com.muzic.vo;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PostVO {
	private int postNo;
	private String postTitle;
	private String postWriter;
	private String postMbti;
	private String postContent;
	private int postMusic;
	private Timestamp postWtime;
	private Timestamp postEtime;
	private int postLike;
	private String postNotice = "N";
	
	private String memberId;
	private String memberNickname;
	private String memberMbti;
	private String memberRole;
	
//	private String postWriteTime() {
//		LocalDateTime wtime = postWtime.toLocalDateTime();
//		LocalDate today = LocalDate.now();
//		LocalDate wday = wtime.toLocalDate();
//		
//		if(wday.isBefore(today)) {
//			return wtime.toLocalDate().toString();
//		}
//		else {
//			DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
//			return wtime.toLocalTime().format(fmt);
//		}
//	}
	
	public String getPostWriteTime() {
		LocalDateTime wtime = postWtime.toLocalDateTime();
		LocalDateTime now = LocalDateTime.now();
		//Duration = 시간의 길이를 나타내는 클래스
		Duration duration = Duration.between(wtime, now);
		
		long seconds = duration.getSeconds();
	    
		//기존 방식 = 오늘 작성한 글 : HH:mm 형식의 시간 반환, 오늘이 지나면 날짜 표시
		//현재 방식 = 초단위로 시간 표현 
	    if (seconds < 60) { //1분 이내
	        return "방금 전";
	    }
	    else if (seconds < 3600) { // 1시간 이내
	        return duration.toMinutes() + "분 전";
	    }
	    else if (seconds < 86400) { // 24시간 이내
	        return duration.toHours() + "시간 전";
	    }
	    else { //그 이후 시간
	        return wtime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); 
	    }
	}
	
}
