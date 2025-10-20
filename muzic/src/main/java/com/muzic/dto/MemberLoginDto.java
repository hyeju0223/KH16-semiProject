package com.muzic.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class MemberLoginDto {

    private int loginNo;                // 로그인 기록 번호 (PK)
    private String loginMember;         // 회원 아이디 (FK)
    private int loginFailCount;         // 로그인 실패 횟수
    private String loginLocked;         // 잠금 여부 ('Y'/'N')
    private Timestamp loginLockTime;    // 잠긴 시각	
    private Timestamp loginTryTime;     // 로그인 시도 시각
}
