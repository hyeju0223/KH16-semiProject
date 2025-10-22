<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:choose>
	<c:when test="${goodsDto==null}">
		<h3>존재하지 않는 상품</h3>
	</c:when>
	<c:otherwise>
		<h1>${goodsDto.goodsName}</h1>

		<table>
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
						<c:when test="${goodsDto.goodsQuantity ==0 }">
				 품절
				</c:when>
						<c:otherwise>
				${goodsDto.goodsQuantity}개
				</c:otherwise>
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
		<form method="post">
			<%-- 1. 공통 파라미터 --%>
			<input type="hidden" name="goodsNo" value="${goodsDto.goodsNo}">
			<input type="hidden" name="memberId" value="testuser2">
			<%-- [추가] 장바구니(add)에서 필요 (세션 구현 전 임시) --%>

			<%-- 2. 수량 --%>
			<label>수량</label> <input type="number" name="goodsQuantity" value="1"
				min="1" max="${goodsDto.goodsQuantity}"
				${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>

			<%-- 3. 바로구매 버튼 --%>
			<%-- 'buy'는 현재 /store/detail 기준 상대경로 "buy"로 보냅니다. --%>
			<button type="submit" formaction="buy"
				onclick="return confirm('구매하시겠습니까?');"
				${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>바로구매</button>

			<%-- 4. 장바구니 버튼 --%>
			<%-- [수정] '장바구니'는 /store/cart/add로 보내야 하므로 상대경로 "../cart/add"를 사용합니다. --%>
			<button type="button" id="cartAddBtn"
				${goodsDto.goodsQuantity == 0 ? 'disabled' : ''}>장바구니에 추가</button>
		</form>
		<br>
		<a href="list"><button type="button">목록</button></a>
	</c:otherwise>
</c:choose>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<script>
	$(function() {
		$("#cartAddBtn").click(function() {
			var goodsNo = $("[name=goodsNo]").val();
			var memberId = $("[name=memberId]").val();
			var goodsQuantity = $("[name=goodsQuantity]").val();

			$.ajax({
				url : "/rest/store/cart/add",
				type : "post",
				data : {
					"memberId" : memberId, // @RequestParam String memberId에 바인딩
					"goodsNo" : goodsNo, // @ModelAttribute GoodsDto의 goodsNo 필드에 바인딩
					"goodsQuantity" : goodsQuantity
				},
				success : function() {
					var result = confirm("장바구니에 상품을 담았습니다. 장바구니로 이동하시겠습니까?");

					if (result) {
						// 예 (장바구니 목록으로 이동)
						location.href = "/store/cart/list?memberId=testuser2";
					}
				}

			});
		});

	});
</script>
