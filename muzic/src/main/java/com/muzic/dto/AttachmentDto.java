package com.muzic.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttachmentDto {

    private int attachmentNo;             // PK
    private String attachmentType;        // img, mp4 등
    private String attachmentCategory;    // 'music', 'profile', 'goods', 'post' 등
    private int attachmentParent;         // 연결될 테이블의 PK (music_no, member_id 등)
    private String attachmentOriginalName;// 실제 이름
    private String attachmentStoredName;  // 저장된 이름
    private long attachmentSize;          // 파일 크기
    private Timestamp attachmentTime;     // 저장된 시간 (기본값 systimestamp)
}
