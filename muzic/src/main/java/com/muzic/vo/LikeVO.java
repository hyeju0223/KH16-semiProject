package com.muzic.vo;

import lombok.Data;

@Data
public class LikeVO {
	private boolean like; //좋아요 true / false
	private int count; //좋아요 개수
}
