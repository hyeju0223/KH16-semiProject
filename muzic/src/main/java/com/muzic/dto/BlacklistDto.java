// src/main/java/com/muzic/dto/BlacklistDto.java
package com.muzic.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class BlacklistDto {
    private int blacklistNo;
    private String blacklistMember;
    private String blacklistReason;
    private String blacklistStatus; // 'Y' or 'N'
    private Timestamp blacklistRegistrationTime;
    private Timestamp blacklistReleaseTime;
}
