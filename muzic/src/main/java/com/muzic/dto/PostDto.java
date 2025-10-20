package com.muzic.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class PostDto {
	private int postNo;
	private String postTitle;
	private String postWriter;
	private String postMbti;
	private String postContent;
	private int postMusic;
	private Timestamp postWtime;
	private Timestamp postEtime;
	private int postLike;
	private int postread;
	private String postNotice;
}
