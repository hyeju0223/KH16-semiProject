package com.muzic.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsCartViewVO {
	private int cartNo;
	private String cartMember;
	private int cartGoods;
	private int cartQuantity;
	private int cartTotal;

	private String goodsName;
	private int goodsPoint;
	private int goodsQuantity;

}
