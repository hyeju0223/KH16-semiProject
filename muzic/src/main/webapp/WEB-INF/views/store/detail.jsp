<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css" href="/css/cart.css">

<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<div class="container w-1000">
	<c:choose>
		<c:when test="${goodsDto==null}">
			<div class="cell center">
				<h3>존재하지 않는 상품</h3>
			</div>
		</c:when>
		<c:otherwise>
			<div class="cell">
				<h1>${goodsDto.goodsName}</h1>
			</div>
			<div class="cell right">
				<a href="cart/list"> <i class="fa-solid fa-cart-shopping"></i>
				</a>
			</div>
			<div class="cell right">
				<table class="table table-border w-100">
					<tr>
						<th>이름</th>
						<td>${goodsDto.goodsName}</td>
					</tr>
					<tr>
						<th>설명</th>
						<td>${goodsDto.goodsDescription}</td>
					</tr>
					<tr>
						<th>카테고리</th>
						<td>${goodsDto.goodsCategory}</td>
					</tr>
					<tr>
						<th>포인트</th>
						<td>${goodsDto.goodsPoint}point</td>
					</tr>
					<tr>
						<th>재고</th>
						<td><c:choose>
								<c:when test="${goodsDto.goodsQuantity ==0 }">품절</c:when>
								<c:otherwise>${goodsDto.goodsQuantity}개</c:otherwise>
							</c:choose></td>
					</tr>
					<tr>
						<th>유효기간</th>
						<td><c:choose>
								<c:when test="${not empty goodsDto.goodsExpiration}">
									<fmt:formatDate value="${goodsDto.goodsExpiration}"
										pattern="yyyy-MM-dd" />
								</c:when>
								<c:otherwise>
							-
						</c:otherwise>
							</c:choose></td>
					</tr>
				</table>
			</div>
			<div class="cell right">
				<form method="post">
					<div style="display: flex; align-items: center; gap: 10px;">

						<div class="quantity-selector">
							<label>수량</label>
							<%-- 상세페이지에는 수량 라벨 유지 --%>

							<button type="button" class="btn-qty btn-qty-down"
								${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>-</button>

							<input type="number" name="goodsQuantity" class="input-qty"
								value="1" min="1" max="${goodsDto.goodsQuantity}"
								${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>

							<button type="button" class="btn-qty btn-qty-up"
								${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>+</button>
						</div>

						<div>
							<c:if test="${goodsDto.goodsQuantity > 0}">
								<button type="submit" class="btn btn-positive" formaction="buy" onclick="return confirm('구매하시겠습니까?');">
									바로구매</button>
							</c:if>

							<button type="button" id="cartAddBtn" class="btn btn-neutral"
								${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>
								${goodsDto.goodsQuantity == 0 ? "품절" : "장바구니에 추가"}</button>
						</div>
					</div>

					<input type="hidden" name="goodsNo" value="${goodsDto.goodsNo}">
				</form>
			</div>
			<div class="cell">
				<a href="list"><button type="button">목록</button></a>
			</div>
		</c:otherwise>
	</c:choose>
</div>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="/js/cart-quantity.js"></script>

<script>
	$(function() {
		$("#cartAddBtn").click(function() {

			var goodsNo = $("[name=goodsNo]").val();
			var goodsQuantity = $("[name=goodsQuantity]").val();

			$.ajax({
				url : "/rest/store/cart/add",
				type : "post",
				data : {
					"goodsNo" : goodsNo, // @ModelAttribute GoodsDto의 goodsNo 필드에 바인딩
					"goodsQuantity" : goodsQuantity
				},
				success : function() {
					var result = confirm("장바구니에 상품을 담았습니다. 장바구니로 이동하시겠습니까?");

					if (result) {
						// 예 (장바구니 목록으로 이동)
						location.href = "/store/cart/list";
					}
				}

			});
		});

	});
</script>
