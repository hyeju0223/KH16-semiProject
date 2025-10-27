package com.muzic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class MusicLikeVO {
	
	private boolean like;
	private int likeCount;
	private String likeStatus;
	
}
