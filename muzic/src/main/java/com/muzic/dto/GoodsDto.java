package com.muzic.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")//상품 등록, 상품 수정시에 필요
	private LocalDateTime goodsExpiration; // Timestamp 대신 LocalDateTime 사용
	private Timestamp goodsRegistrationTime;
	private Timestamp goodsEditTime;

	//관리자-상품 edit페이지에 필요
	public String getFormattedGoodsExpiration() {
		if (this.goodsExpiration == null) {
			return "";
		}
		// HTML <input type="datetime-local"> 형식에 맞는 패턴
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
		return this.goodsExpiration.format(formatter);
	}
}
