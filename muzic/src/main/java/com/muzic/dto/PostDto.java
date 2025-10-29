package com.muzic.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PostDto {
	private int postNo;
	private String postTitle;
	private String postWriter;
	private String postMbti;
	private String postContent;
	private Integer postMusic;
	private Timestamp postWtime;
	private Timestamp postEtime;
	private int postLike;
	private int postRead;
	private String postNotice = "N";
	
//	private List<Integer> attachmentTempNos;
//	
//	public List<Integer> getAttachmentTempNos() {
//        return attachmentTempNos;
//    }
//
//    public void setAttachmentTempNos(List<Integer> attachmentTempNos) {
//        this.attachmentTempNos = attachmentTempNos;
//    }
}
