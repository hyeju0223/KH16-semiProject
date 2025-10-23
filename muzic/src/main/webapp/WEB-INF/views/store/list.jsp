<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css" href="/css/cart.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<style>
.table>tbody>tr>td {
	padding-top: 20px;
	padding-bottom: 20px;
	vertical-align: middle;
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
	<div class="cell right">
		<form method="get" action="list">
			<select name="goodsCategory" onchange="this.form.submit()" class="field">
				<option value="">전체</option>
				<option value="의류" ${goodsCategory == '의류' ? 'selected' : ''}>의류</option>
				<option value="굿즈" ${goodsCategory == '굿즈' ? 'selected' : ''}>굿즈</option>
				<option value="음반" ${goodsCategory == '음반' ? 'selected' : ''}>음반</option>
				<option value="포토카드" ${goodsCategory == '포토카드' ? 'selected' : ''}>포토카드</option>
				<option value="문구류" ${goodsCategory == '문구류' ? 'selected' : ''}>문구류</option>
				<option value="전자기기" ${goodsCategory == '전자기기' ? 'selected' : ''}>전자기기</option>
				<option value="액세서리" ${goodsCategory == '액세서리' ? 'selected' : ''}>액세서리</option>
				<option value="생활용품" ${goodsCategory == '생활용품' ? 'selected' : ''}>생활용품</option>
				<option value="기타" ${goodsCategory == '기타' ? 'selected' : ''}>기타</option>
			</select>
		</form>
	</div>
	<div class="cell">
		<table class="table w-100">
			<thead>
				<tr>
					<th></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="goods" items="${goodsList}">
					<tr>
						<td><img
							src="https://dummyimage.com/100x100/cccccc/000000.png"></td>
						<td class="left"><a href="detail?goodsNo=${goods.goodsNo}">${goods.goodsName}</a><br>
							포인트 : ${goods.goodsPoint}<br></td>
						<td class="left">
							<form class="cartAddForm">
								<div style="display: flex; align-items: center; gap: 10px;">
									<!-- 수량 입력창 -->
									<div class="quantity-selector">

										<button type="button" class="btn-qty btn-qty-down"
											${goods.goodsQuantity == 0 ? "disabled" : ""}>-</button>

										<input type="number" name="goodsQuantity" class="input-qty"
											value="1" min="1" max="${goods.goodsQuantity}"
											${goods.goodsQuantity == 0 ? "disabled" : ""}>

										<button type="button" class="btn-qty btn-qty-up"
											${goods.goodsQuantity == 0 ? "disabled" : ""}>+</button>
									</div>
									<div>
										<!-- 바로구매 -->
										<c:if test="${goods.goodsQuantity > 0}">
											<button type="submit" class="btn btn-positive"
												formaction="buy?goodsNo=${goods.goodsNo}" formmethod="post"
												onclick="return confirm('구매하시겠습니까?');">바로구매</button>
										</c:if>

										<!-- 장바구니 담기 -->
										<button type="button" class="cartAddBtn btn btn-neutral"
											${goods.goodsQuantity == 0 ? "disabled" : ""}>

											${goods.goodsQuantity == 0 ? "품절" : "장바구니 담기"}</button>
									</div>
									<!-- 장바구니 담기용 hidden -->
									<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
							</form>
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
