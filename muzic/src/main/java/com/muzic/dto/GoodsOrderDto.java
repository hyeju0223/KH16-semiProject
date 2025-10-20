package com.muzic.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsOrderDto {
	private int orderNo;
	private String orderMember;
	private int orderGoods;
	private int orderQuantity;
	private int orderPoint;
	private Timestamp orderTime;
}
