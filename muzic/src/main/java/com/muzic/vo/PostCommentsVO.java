package com.muzic.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PostCommentsVO {

	private int postNo;
	private String postTitle;
	private String postWriter;
	private String postMbti;
	private String postContent;
	private Integer postMusic;
	private Timestamp postWtime;
	private Timestamp postEtime;
	private int postLike;
	private String postNotice = "N";
	private int commentNo;
	
}
