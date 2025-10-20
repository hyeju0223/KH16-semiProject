package com.muzic.dto;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PlayLogDto {

	private int playLogNo;
	private int playLogMusic;
	private String playLogGenre;
	private String playLogMember;
	private String playLogMbti;
	private Timestamp playLogTime;
	
}
