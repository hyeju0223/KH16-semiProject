package com.muzic.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MusicStatusHistoryDto {
	
    private int mshNo;           
    private int mshMusicNo;         
    private String mshMusicStatus;    
    private String mshComment;        
    private String mshAdminId;       
    private Timestamp mshChangedTime; 

}