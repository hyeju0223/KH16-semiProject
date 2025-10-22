package com.muzic.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsCartDto {
	private int cartNo;
	private String cartMember;
	private int cartGoods;
	private int cartQuantity;
	private int cartTotal;
	private Timestamp cartTime;
//	조인
	private String goodsName;
	private int goodsPoint;
	private int goodsQuantity;
}
