<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
<style>

/* [중요] 아이콘 버튼 스타일 */
.btn-delete-icon {
	background: none; /* 1. 배경을 없앰 */
	border: none; /* 2. 테두리를 없앰 */
	cursor: pointer; /* 3. 마우스 커서를 손가락 모양으로 */
	padding: 5px;
	font-size: 1.2em; /* 아이콘 크기 (조절 가능) */
	color: #777; /* 아이콘 기본 색상 */
}
/* 마우스 올리면 색 변경 */
.btn-delete-icon:hover {
	color: #d9534f; /* 붉은색 */
}
</style>
<h1>장바구니</h1>
<c:choose>
	<c:when test="${cartList == null || cartList.size() == 0}">
		<h2>장바구니에 담긴 상품이 없습니다.</h2>
	</c:when>
	<c:otherwise>
		<div>
			선택 상품 총액: <span id="totalPrice">0</span>원
		</div>

		<label> <input type="checkbox" class="check-all"> 전체
			선택
		</label>
		<table border="1">
			<thead>
				<tr>
					<th></th>
					<th>상품번호</th>
					<th>상품이름</th>
					<th>상품포인트</th>
					<th>수량</th>
					<th>총액</th>
					<th>삭제</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="goodsCart" items="${cartList}">
					<tr>
						<td><input type="checkbox" class="check-item"
							value="${goodsCart.cartGoods}"
							data-price="${goodsCart.cartTotal }"></td>
						<td>${goodsCart.cartGoods}</td>
						<td>${goodsCart.goodsName}</td>
						<td>${goodsCart.goodsPoint}</td>
						<td><input type="number" class="input-quantity"
							value="${goodsCart.cartQuantity}" min="1"
							max="${goodsCart.goodsQuantity}"
							style="width: 60px; text-align: center;"
							data-goods-no="${goodsCart.cartGoods}"></td>
						<td>${goodsCart.cartTotal}</td>
						<td>
							<form action="delete" method="post">
								<input type="hidden" name="goodsNo"
									value="${goodsCart.cartGoods}"> <input type="hidden"
									name="memberId" value="${memberId}">
								<button type="submit" class="btn-delete-icon" title="삭제"
									onclick="return confirm('이 상품을 삭제하시겠습니까?');">
									<i class="fa-solid fa-xmark"></i>
								</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<button id="buySelected">선택 구매</button>
		<button id="deleteSelected">선택 삭제</button>
	</c:otherwise>
</c:choose>
<br>

<a href="../list"><button type="button">계속 쇼핑</button></a>

<!-- jQuery-->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>


<script>
	$(function() {

		//수량변경 
		$(".input-quantity").change(function() {

			var quantityInput = $(this);

			//input 수량 가져옴
			var quantity = quantityInput.val();
			var goodsNo = quantityInput.data("goods-no");

			//입력수량이 1미만일때
			if (quantity < 1) {
				alert("수량은 1 이상이어야 합니다");
				quantityInput.val(1);
				return;
			}

			//재고수량보다 많을때
			var maxStock = parseInt(quantityInput.attr("max"));
			if (quantity > maxStock) {
				alert("재고 수량(" + maxStock + "개)보다 많이 담을 수 없습니다.");
				quantityInput.val(maxStock); // 재고 수량으로 복구
				return;
			}

			$.ajax({
				url : "update",
				type : "post",
				data : {
					memberId : "testuser2",
					goodsNo : goodsNo,
					quantity : quantity
				},
				success : function() {
					location.reload();
				}
			});

		});

		//장바구니 총액 계산 함수
		function calculateTotal() {
			var total = 0;
			$(".check-item:checked").each(function() {
				//data-price 값을 읽어서 숫자로 변환 -> total에 +
				total += parseInt($(this).data("price"));
			});
			//총액 태그에 계산된 total 넣기
			$("#totalPrice").text(total);
		}

		//전체 체크박스 클릭 이벤트
		$(".check-all").click(function() {
			// "전체" 체크박스의 현재 체크 상태
			var isChecked = $(this).prop("checked");

			// 모든 "개별" 체크박스들의 상태를 "전체" 체크박스와 동일하게 변경
			$(".check-item").prop("checked", isChecked);

			// 총액 다시 계산
			calculateTotal();
		});

		//개별 체크박스 클릭 이벤트
		$(".check-item").click(function() {
			// 총액 다시 계산
			calculateTotal();

			// "개별" 체크박스들의 총 개수
			var totalItems = $(".check-item").length;
			// "개별" 체크박스 중 체크된 것들의 개수
			var checkedItems = $(".check-item:checked").length;

			// 두 개수가 같으면 "전체" 체크박스도 체크
			if (totalItems == checkedItems) {
				$(".check-all").prop("checked", true);
			}
			// 하나라도 다르면 "전체" 체크박스는 해제
			else {
				$(".check-all").prop("checked", false);
			}
		});

		//선택 구매
		$("#buySelected").click(function() {
			var selected = [];
			$(".check-item:checked").each(function() {
				selected.push($(this).val());
			});
			if (selected.length == 0) {
				alert("구매할 상품을 선택하세요");
				return;
			}
			if (!confirm("선택한 상품을 구매하시겠습니까?"))
				return;

			$.ajax({
				url : "buyMultiple",
				type : "post",
				data : {
					goodsNos : selected,
					memberId : "testuser2"
				},
				traditional : true,//배열 전송 시 필요
				success : function() {
					alert("구매 완료!");
					location.reload();
				}
			});
		});
		// 선택 삭제
		$("#deleteSelected").click(function() {
			var selected = [];
			$(".check-item:checked").each(function() {
				selected.push($(this).val());
			});

			if (selected.length === 0) {
				alert("삭제할 상품을 선택하세요.");
				return;
			}

			if (!confirm("선택한 상품을 삭제하시겠습니까?"))
				return;

			$.ajax({
				url : "deleteMultiple",
				type : "POST",
				data : {
					goodsNos : selected,
					memberId : "testuser2"
				}, // 일반 POST 파라미터 전송, 세션 추후 구현
				traditional : true,
				success : function() {
					alert("삭제 완료!");
					location.reload();
				}
			});
		});
	});
</script>
