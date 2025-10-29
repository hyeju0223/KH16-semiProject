package com.muzic.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
//화면 표시 로직을 위한 분리
public class CommentsVO {

	private int commentNo; //번호
	private int commentPost; //어느 게시글 댓글인지
	private String commentWriter; //작성자 id
	private String commentContent; //댓글 내용
	private Timestamp commentWtime; //작성 시간
	private Timestamp commentEtime; //수정 시간
	private int commentLike; //좋아요 수
	private int commentOrigin; //대댓글인지 확인
	private int commentDepth; //차수
	
	private boolean owner; //댓글 작성자가 현재 사용자인지 여부
	private boolean writer; //댓글 작성자가 게시글 작성자인지 여부
}
