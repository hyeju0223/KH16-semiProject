<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<h1>STORE</h1>
<a href="cart/list"><button type="button">장바구니 보기</button></a>
<form method="get" action="list">
	<select name="goodsCategory" onchange="this.form.submit()">
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
<table>
	<thead>
		<tr>
			<th>이미지</th>
			<th>제목 및 설명</th>
			<th>구매</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="goods" items="${goodsList}">
			<tr>
				<td><img src="https://dummyimage.com/100x100/cccccc/000000.png"></td>
				<td><a href="detail?goodsNo=${goods.goodsNo}">${goods.goodsName}</a><br>
					포인트 : ${goods.goodsPoint}<br></td>
				<td>
					<form id="purchaseForm">
						<!-- 수량 입력창 -->
						<label>수량</label> <input type="number" name="goodsQuantity"
							value="1" min="1" max="${goods.goodsQuantity}"
							${goods.goodsQuantity == 0 ? "disabled" : ""}>

						<!-- 바로구매 -->
						<c:if test="${goods.goodsQuantity > 0}">
							<button type="submit" formaction="buy?goodsNo=${goods.goodsNo}"
								formmethod="post" onclick="return confirm('구매하시겠습니까?');">
								바로구매</button>
						</c:if>

						<!-- 장바구니 담기 -->
						<button type="submit" formaction="/store/cart/add"
							formmethod="post" ${goods.goodsQuantity == 0 ? "disabled" : ""}>
							${goods.goodsQuantity == 0 ? "품절" : "장바구니 담기"}</button>

						<!-- 장바구니 담기용 hidden -->
						<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
						<input type="hidden" name="memberId" value="testuser2">
					</form>
				</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
