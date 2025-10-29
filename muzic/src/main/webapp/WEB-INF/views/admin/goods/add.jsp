<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css" href="/css/cart.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">


<div class="container w-100">
	<div class="cell">
		<h2>관리자 - 상품 등록</h2>
	</div>
	<div class="cell">
		<form action="add" method="post" enctype="multipart/form-data">
			<div class="cell">
				<table class="table">
					<tbody>
						<tr>
							<th style="width: 150px;">카테고리</th>
							<td><select name="goodsCategory" class="form-input" required>
									<option value="">-- 카테고리를 선택하세요 --</option>
									<option value="의류">의류</option>
									<option value="굿즈">굿즈</option>
									<option value="음반">음반</option>
									<option value="포토카드">포토카드</option>
									<option value="문구류">문구류</option>
									<option value="전자기기">전자기기</option>
									<option value="액세서리">액세서리</option>
									<option value="생활용품">생활용품</option>
									<option value="기타">기타</option>
							</select></td>
						</tr>
						<tr>
							<th>상품명</th>
							<td><input type="text" name="goodsName" class="form-input"
								style="width: 100%;"></td>
						</tr>
						<tr>
							<th>상품 설명</th>
							<td><textarea name="goodsDescription" class="form-input"
									rows="5" style="width: 100%;"></textarea></td>
						</tr>
						<tr>
							<th>포인트(가격)</th>
							<td><input type="number" name="goodsPoint"
								class="form-input" placeholder="0"></td>
						</tr>
						<tr>
							<th>재고 수량</th>
							<td><input type="number" name="goodsQuantity"
								class="form-input" placeholder="0"></td>
						</tr>
						<!-- <tr>
							<th>유효 기간</th>
							<td><input type="date" name="goodsExpiration"
								class="form-input"></td>
						</tr> -->
						<tr>
							<th>이미지</th>
							<td><input type="file" name="attach"></td>
						</tr>
					</tbody>
				</table>
			</div>

			<div>
				<a href="/store/list" class="btn">취소</a>
				<button type="submit" class="btn btn-positive">상품 등록하기</button>
			</div>

		</form>
	</div>

</div>
