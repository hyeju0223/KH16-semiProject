package com.muzic.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsDto {
	private int goodsNo;
	private String goodsName;
	private String goodsDescription;
	private int goodsPoint;
	private int goodsQuantity;
	private String goodsCategory;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime goodsExpiration; // 🚨 Timestamp 대신 LocalDateTime 사용
	private Timestamp goodsRegistrationTime;
	private Timestamp goodsEditTime;
}
