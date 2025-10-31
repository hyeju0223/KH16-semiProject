// src/main/java/com/muzic/vo/AdminMemberListVO.java
package com.muzic.vo;

import lombok.Data;

@Data
public class AdminMemberListVO {
    private String memberId;
    private String memberPw;
    private String memberNickname;
    private String memberName;
    private String memberEmail;
    private String memberMbti;
    private String memberBirth;
    private String memberContact;
    private String memberRole;
    private int    memberPoint;
    private String blacklistYn; // 'Y'/'N'
}
