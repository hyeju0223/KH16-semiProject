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

/* 🟢 [추가] 수량 버튼 디자인 (input-qty는 cart.css에 있다고 가정) */
.quantity-selector {
	display: flex;
	align-items: center;
	border: 1px solid #dee2e6;
	border-radius: 4px;
	overflow: hidden;
	height: 36px; /* 높이 설정 */
}

.btn-qty {
	background: none; /* 배경 제거 */
	border: none;
	padding: 0 10px;
	height: 100%;
	font-size: 1.2em;
	cursor: pointer;
}

.btn-qty-down, .btn-qty-up {
	/* 수량 버튼 배경색은 이미지에 따라 없애거나 약하게 */
	background: #f8f9fa;
}
/* 크롬에서 수량버튼의 스피너 안보이게 */
input[type="number"]::-webkit-inner-spin-button {
	-webkit-appearance: none;
	margin: 0;
}

.input-qty {
	width: 40px;
	text-align: center;
	border: none;
	height: 100%;
	font-size: 1.1em;
	padding: 0;
}

/* 🟢 [추가] 버튼 스타일 (이미지처럼 배경색 조정) */
.btn-positive { /* 바로구매 버튼 */
	background-color: #7b68ee; /* 보라색 계열 */
	color: white;
	border: none;
	padding: 10px 20px;
	font-size: 1em;
}

.btn-neutral { /* 장바구니 버튼 */
	background-color: #cccccc; /* 회색 계열 */
	color: #333;
	border: none;
	padding: 10px 20px;
	font-size: 1em;
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

			<form method="post"
				style="display: flex; flex-direction: column; gap: 20px;">

				<div style="display: flex; align-items: center; justify-content: flex-start; gap: 20px;">
					<div style="font-size: 1em; color: ${goodsDto.goodsQuantity == 0 ? 'black' : 'green'};"> 재고: ${goodsDto.goodsQuantity}개</div>
				</div>

			

				<input type="hidden" name="goodsNo" value="${goodsDto.goodsNo}">
			</form>
		</div>
	</div>
	<div class="cell product-description-section"
		style="padding-top: 30px; border-top: 1px solid #ddd; margin-top: 30px;">
		<h2 style="font-size: 1.5em; margin-bottom: 20px;">상품 정보</h2>
		<p style="line-height: 1.8;">${goodsDto.goodsDescription}</p>
	</div>

	<div class="cell right" style="margin-top: 30px;">
		<a href="list"><button type="button" class="btn btn-neutral">목록</button></a>
	<a href="edit"><button type="button" class="btn btn-positive">수정하기</button></a>
	</div>
</div>
