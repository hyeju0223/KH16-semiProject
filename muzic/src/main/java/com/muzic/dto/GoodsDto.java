package com.muzic.dto;

import java.sql.Timestamp;

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
	private Timestamp goodsExpiration;
	private Timestamp goodsRegistrationTime;
	private Timestamp goodsEditTime;
}
