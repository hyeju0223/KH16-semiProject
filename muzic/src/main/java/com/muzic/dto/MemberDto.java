package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MemberDto {

	private String memberId;
	private String memberPw;
	private String memberNickname;
	private String memberName;
	private String memberEmail;
	private String memberMbti;
	private Date memberBirth;
	private String memberContact;
	private String memberPostcode;
	private String memberAddress1;
	private String memberAddress2;
	private String memberRole;
	private Integer memberPoint;
	private Timestamp memberJoin;
	private Timestamp memberEtime;
	private Timestamp memberRecentLogin;
}
