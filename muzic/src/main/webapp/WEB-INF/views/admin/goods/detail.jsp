<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css" href="/css/cart.css">

<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

<style>
/* 🟢 [추가] 카테고리 태그 스타일 (이미지 상단의 '음반' 태그) */
.category-tag {
	display: inline-block;
	padding: 5px 10px;
	background-color: #f0f0f0;
	color: #555;
	border-radius: 4px;
	font-size: 0.9em;
	font-weight: 500;
	margin-bottom: 10px;
}



.btn-qty {
	background: none; /* 배경 제거 */
	border: none;
	padding: 0 10px;
	height: 100%;
	font-size: 1.2em;
	cursor: pointer;
}

.input-qty {
	width: 40px;
	text-align: center;
	border: none;
	height: 100%;
	font-size: 1.1em;
	padding: 0;
}


</style>

<div class="container w-1000">

	<div class="cell right">
		<a href="cart/list"> <i class="fa-solid fa-cart-shopping"></i>
		</a>
	</div>

	<div class="cell product-detail-main"
		style="display: flex; gap: 60px; margin-bottom: 50px;">

		<div style="flex-shrink: 0; width: 35%;">
			<div class="category-tag">${goodsDto.goodsCategory}</div>
			<img src="/store/image?goodsNo=${goodsDto.goodsNo}"
				style="width: 100%; height: auto; object-fit: cover;">
		</div>

		<div style="flex-grow: 1; width: 60%; padding-top: 10px;">

			<h2 style="font-size: 1.8em; margin-bottom: 5px;">${goodsDto.goodsName}</h2>
			<p
				style="font-size: 1.5em; color: #333; font-weight: bold; padding-bottom: 20px; border-bottom: 1px solid #eee; margin-bottom: 20px;">
				<fmt:formatNumber value="${goodsDto.goodsPoint}" pattern="#,###" />
				point
			</p>

			<div style="display: flex; gap: 20px;">

				<div style="display: flex; align-items: center; justify-content: flex-start; gap: 20px;">
					<div style="font-size: 1em; color: ${goodsDto.goodsQuantity == 0 ? 'black' : 'green'};"> 재고: ${goodsDto.goodsQuantity}개</div>
					<c:if test="${goodsDto.goodsExpiration != null}">
        <div style="font-size: 1em; color: #555;"> 
            유효 기간: 
            ${goodsDto.formattedGoodsExpiration}
        </div>
    </c:if>
				</div>

			

				<input type="hidden" name="goodsNo" value="${goodsDto.goodsNo}">
			</div>
		</div>
	</div>
	<div class="cell product-description-section"
		style="padding-top: 30px; border-top: 1px solid #ddd; margin-top: 30px;">
		<h2 style="font-size: 1.5em; margin-bottom: 20px;">상품 정보</h2>
		<p style="line-height: 1.8;">${goodsDto.goodsDescription}</p>
	</div>

	<div class="cell right" style="margin-top: 30px;">
	<a href="delete?goodsNo=${goodsDto.goodsNo}" class="btn btn-negative" onclick="return confirm('상품을 삭제하시겠습니까?');">삭제</a>
		<a href="list"><button type="button" class="btn btn-neutral">목록</button></a>
	<a href="edit?goodsNo=${goodsDto.goodsNo}"><button type="button" class="btn btn-positive">수정하기</button></a>
	</div>
</div>
