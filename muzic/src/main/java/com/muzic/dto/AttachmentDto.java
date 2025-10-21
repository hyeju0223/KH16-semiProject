package com.muzic.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AttachmentDto {
	
		private int attachmentNo;           // attachment_no
	    private String attachmentType;       // attachment_type (img, mp4 등)
	    private String attachmentPath; 		// attachment_path(저장된 경로) 
	    private String attachmentCategory;   // attachment_category ('music', 'profile', 'goods', 'post')
	    private String attachmentParent;       // attachment_parent (연결된 테이블 PK)
	    private String attachmentOriginalName; // attachment_original_name
	    private String attachmentStoredName; // attachment_stored_name
	    private long attachmentSize;         // attachment_size
	    private Timestamp attachmentTime;    // attachment_time
	    
}
