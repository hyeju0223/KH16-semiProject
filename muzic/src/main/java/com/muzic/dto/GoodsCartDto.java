package com.muzic.dto;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoodsCartDto {
	private int cartNo;
	private String cartMember;
	private int cartGoods;
	private int cartQuantity;
	private int cartTotal;
	private Timestamp cartTime;
}
