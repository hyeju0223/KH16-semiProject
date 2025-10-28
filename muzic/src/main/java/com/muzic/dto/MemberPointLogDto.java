package com.muzic.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MemberPointLogDto {
	
	private int pointLogNo;
	private String pointLogMember;
	private int pointLogChange;
	private String pointLogReason;
	private int pointLogRelatedPost;
	private int pointLogRelatedOrder;
	private Timestamp pointLogTime;

}
