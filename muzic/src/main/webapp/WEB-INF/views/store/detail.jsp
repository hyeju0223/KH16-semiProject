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
		<form action="buy?goodsNo=${goodsDto.goodsNo}" method="post">
			<input type="hidden" name="goodsNo" value="${goodsDto.goodsNo}">
			<label>수량</label><input type="number" name="goodsQuantity" value="1"
				min="1" max="${goodsDto.goodsQuantity}">
			<button type="submit" onclick="return confirm('구매하시겠습니까?');">바로구매</button>
		</form>
		<button type="button">장바구니에 추가</button>
		<br>
		<a href="list"><button type="button">목록</button></a>
	</c:otherwise>
</c:choose>