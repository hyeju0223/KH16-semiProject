package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@NoArgsConstructor
@ToString(exclude = {"memberPw"}) // 비밀번호는 toString() 출력 제외
public class MemberDto {
	private String memberId;             // 회원 아이디 (PK)
	private String memberPw;             // 비밀번호 (암호화 저장) //해쉬비밀번호 해야됨
	private String memberNickname;       // 닉네임 (unique)
	private String memberName;           // 실제 이름
	private String memberEmail;          // 이메일 (unique)
	private String memberMbti;           // MBTI (4글자)
	private Date memberBirth;            // 생년월일 (DATE)
	private String memberContact;        // 연락처 (010-xxxx-xxxx)
	private String memberPostcode;       // 우편번호
	private String memberAddress1;       // 기본주소
	private String memberAddress2;       // 상세주소
	private String memberRole;           // 회원등급 (일반회원/정지회원/관리자)
	private int memberPoint;             // 포인트
	private Timestamp memberJoin;        // 가입일
	private Timestamp memberEtime;       // 수정일
	private Timestamp memberRecentLogin; // 최근 로그인
}
