package com.muzic.vo;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsOrderViewVO {
	private int orderNo;
	private String orderMember;
	private int orderGoods;
	private int orderQuantity;
	private int orderPoint;
	private Timestamp orderTime;

	private String goodsName;

}
