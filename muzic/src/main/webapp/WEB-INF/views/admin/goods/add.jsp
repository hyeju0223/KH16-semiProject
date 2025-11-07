<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/commons.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cart.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<style>
/* .container {
	border: 1px solid gray;
	border-radius: 20px;
	padding: 10px;
} */
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { padding: 10px; border: 1px solid #ddd; text-align: left; }
.table th { width: 150px; background-color: #f7f7f7; }

</style>
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/summernote/custom-summernote.css">  
<script src="${pageContext.request.contextPath}/summernote/custom-summernote.js"></script>
<div class="container w-1000">
	<div class="cell center">
		<h2>관리자 - 상품 등록</h2>
	</div>
	<div class="cell center">
		<form action="add" method="post" enctype="multipart/form-data">
			<div class="cell center">
				<table class="table" style="margin: 0 auto;">
					<tbody>
						<tr>
							<th style="width: 150px;">카테고리</th>
							<td><select name="goodsCategory" required>
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
							<td><input type="text" name="goodsName" style="width: 100%;"></td>
						</tr>
						<tr>
							<th>상품 설명</th>
							<td><textarea name="goodsDescription" class="summernote-editor"></textarea></td>
						</tr>
						<tr>
							<th>포인트(가격)</th>
							<td><input type="number" name="goodsPoint" placeholder="0"></td>
						</tr>
						<tr>
							<th>재고 수량</th>
							<td><input type="number" name="goodsQuantity"
								placeholder="0"></td>
						</tr>
						<tr>
							<th>유효 기간</th>
							<td><input type="datetime-local" name="goodsExpiration"></td>
						</tr>
						<tr>
							<th>이미지</th>
							<td><input type="file" name="attach"></td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="cell right mt-20">
				<a href="${pageContext.request.contextPath}/admin/goods/list" class="btn btn-neutral">취소</a>
				<button type="submit" class="btn btn-positive">상품 등록하기</button>
			</div>

		</form>
	</div>

</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
