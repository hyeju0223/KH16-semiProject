<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css" href="/css/cart.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<style>

/* 크롬에서 수량버튼의 스피너 안보이게 */
    input[type="number"]::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

.table>tbody>tr>td {
	padding-top: 20px;
	padding-bottom: 20px;
	vertical-align: middle;
}

.table {
	table-layout: fixed;
	width: 100%;
}
</style>
<div class="container w-1000">
	<div class="cell center">
		<h1>STORE</h1>
	</div>
	<div class="cell right">
		<a href="cart/list"> <i class="fa-solid fa-cart-shopping"></i>
		</a>
	</div>
	<div class="cell category-filter">
		<a href="list"
			class="btn-category ${empty param.goodsCategory ? 'active' : ''}">전체</a>

		<a href="list?goodsCategory=의류"
			class="btn-category ${param.goodsCategory == '의류' ? 'active' : ''}">의류</a>
		<a href="list?goodsCategory=굿즈"
			class="btn-category ${param.goodsCategory == '굿즈' ? 'active' : ''}">굿즈</a>
		<a href="list?goodsCategory=음반"
			class="btn-category ${param.goodsCategory == '음반' ? 'active' : ''}">음반</a>
		<a href="list?goodsCategory=포토카드"
			class="btn-category ${param.goodsCategory == '포토카드' ? 'active' : ''}">포토카드</a>
		<a href="list?goodsCategory=문구류"
			class="btn-category ${param.goodsCategory == '문구류' ? 'active' : ''}">문구류</a>
		<a href="list?goodsCategory=전자기기"
			class="btn-category ${param.goodsCategory == '전자기기' ? 'active' : ''}">전자기기</a>
		<a href="list?goodsCategory=액세서리"
			class="btn-category ${param.goodsCategory == '액세서리' ? 'active' : ''}">액세서리</a>
		<a href="list?goodsCategory=생활용품"
			class="btn-category ${param.goodsCategory == '생활용품' ? 'active' : ''}">생활용품</a>
		<a href="list?goodsCategory=기타"
			class="btn-category ${param.goodsCategory == '기타' ? 'active' : ''}">기타</a>
	</div>
	<div class="cell center" style="margin-top: 20px; margin-bottom: 20px;">
		<form action="list" method="get" style="display: inline-flex; gap: 5px;">
			
			<select name="column" style="padding: 8px; border: 1px solid #ccc; border-radius: 4px;">
				<option value="goods_name" ${param.column == 'goods_name' ? 'selected' : ''}>상품명</option>
				<option value="goods_description" ${param.column == 'goods_description' ? 'selected' : ''}>상품 설명</option>
			</select>
			
			<input type="text" name="keyword" placeholder="검색어 입력" value="${param.keyword}"
				style="padding: 8px; border: 1px solid #ccc; border-radius: 4px; width: 200px;">
			
			<button type="submit" class="btn btn-positive">
				<i class="fa-solid fa-magnifying-glass"></i> 검색
			</button>
			
		</form>
	</div>
	<div class="cell">
		<table class="table w-100">
			<thead>
				<tr>
					<th style="width: 200px;"></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="goods" items="${goodsList}">
					<tr>
						<td><img src="image?goodsNo=${goods.goodsNo}"
							style="width: 100px; height: 100px; object-fit: cover;"></td>
						<td class="left"><a href="detail?goodsNo=${goods.goodsNo}"
							style="font-size: 1.25em">${goods.goodsName}</a><br>${goods.goodsPoint} point<br></td>
						<td class="left">
						
							<c:if test="${goods.goodsQuantity > 0}">
							<form class="cartAddForm" style="display: flex; justify-content: flex-start;">
								<div style="display: flex; align-items: center; gap: 10px;">
									<div class="quantity-selector">

										<button type="button" class="btn-qty btn-qty-down">-</button>

										<input type="number" name="goodsQuantity" class="input-qty"
											value="1" min="1" max="${goods.goodsQuantity}">

										<button type="button" class="btn-qty btn-qty-up">+</button>
									</div>
									<div>
										<button type="submit" class="btn btn-positive"
											formaction="buy?goodsNo=${goods.goodsNo}" formmethod="post"
											onclick="return confirm('구매하시겠습니까?');">바로구매</button>

										<button type="button" class="cartAddBtn btn btn-neutral">
											장바구니 담기</button>
									</div>
									<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
								</div>
							</form>
							</c:if>
							<c:if test="${goods.goodsQuantity == 0}">
							    <span style="color: red; font-weight: bold;">품절</span>
							</c:if>
							
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="/js/cart-quantity.js"></script>

<script>
	$(function() {
		$(".cartAddBtn").click(function() {

			// 1. 클릭된 버튼이 속한 폼(.cartAddForm)을 찾고 데이터를 가져옵니다.
			var $form = $(this).closest('.cartAddForm');

			$.ajax({
				url : "/rest/store/cart/add",
				type : "post",
				data : {
					"goodsNo" : $form.find('[name=goodsNo]').val(),
					"goodsQuantity" : $form.find('[name=goodsQuantity]').val()
				},
				success : function() {
					var result = confirm("장바구니에 상품을 담았습니다. 장바구니로 이동하시겠습니까?");
					if (result) {
						location.href = "/store/cart/list";
					}
				}
			});
		});
	});
</script>
