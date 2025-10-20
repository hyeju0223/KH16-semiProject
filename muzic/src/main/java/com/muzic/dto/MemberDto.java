package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberDto {
    private String memberId;              // 회원 아이디 (PK)
    private String memberPw;              // 비밀번호 (BCrypt 암호화 예정) bean추가해야될거같아요
    private String memberNickname;        // 닉네임 (unique)
    private String memberName;            // 실제 이름
    private String memberEmail;           // 이메일 (unique)
    private String memberMbti;            // MBTI (4글자, IE/SN/FT/PJ)
    private Date memberBirth;             // 생년월일 (date)
    private String memberContact;         // 연락처 (010-xxxx-xxxx)
    private String memberPostcode;        // 우편번호
    private String memberAddress1;        // 기본주소
    private String memberAddress2;        // 상세주소
    private String memberRole;            // 회원등급 (일반회원/정지회원/관리자)
    private int memberPoint;              // 포인트
    private Timestamp memberJoin;         // 가입일
    private Timestamp memberEtime;        // 수정일
    private Timestamp memberRecentLogin;  // 최근 로그인
}
