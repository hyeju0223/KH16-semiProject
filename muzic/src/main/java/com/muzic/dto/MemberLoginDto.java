package com.muzic.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberLoginDto {

	private int loginNo;
	private String loginMember;
	private Integer loginFailCount;
	private String loginLocked;
	private Timestamp loginLockTime;
	private Timestamp loginTryTime;
	
}
