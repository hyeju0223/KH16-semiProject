<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

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
/* 🟢 [추가] 구매 요약 박스 스타일링 */
.order-summary-box {
    border: 1px solid #e0e0e0;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}
.total-price-section {
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    margin-bottom: 20px;
    border-bottom: 2px solid #e0e0e0; /* 총액 구분선 */
}

#totalPrice{
	font-size: 1.3em;
	font-weight: bold;
}
/* 🟢 [추가] 장바구니 목록 항목별 구분선 */
.table tbody tr {
    border-bottom: 1px solid #e0e0e0; /* 얇은 회색 선 추가 */
}
.table tbody tr:last-child {
    border-bottom: none;
}
.table>tbody>tr>td {
	padding-top: 20px;
	padding-bottom: 20px;
	vertical-align: middle;
}

</style>

<div class="container w-1000">
	<c:choose>
		<c:when test="${cartList == null || cartList.size() == 0}">
			<h2>장바구니에 담긴 상품이 없습니다.</h2>
		</c:when>
		<c:otherwise>
		<div class="cell">
				<h1>장바구니</h1>
			</div>

			<div class="cell"
				style="display: flex; gap: 30px; align-items: flex-start;">
				<div style="width: 80%">
					<table class="table w-100">
						<thead>
							<tr>
								<td colspan="7" class="left" style="padding: 10px 0;">
									<div style="display: flex; align-items: center; justify-content: space-between;">
										<div class="flex-fill">
											<label style="font-weight: bold;"> 
												<input type="checkbox" class="check-all"> 전체 선택
											</label>
										</div>
										<button id="deleteSelected" class="btn btn-negative" style="padding: 5px 15px;">선택 삭제</button>
									</div>
									<br>
									<hr color="#e0e0e0">
								</td>
							</tr>
						</thead>
						<tbody>

							<c:forEach var="goodsCart" items="${cartList}">
								<tr>
									<td width="5%"><input type="checkbox" class="check-item"
										value="${goodsCart.cartGoods}"
										data-price="${goodsCart.cartTotal }"></td>
									<td><img src="image?goodsNo=${goodsCart.cartGoods}"
										style="width: 100px; height: 100px; object-fit: cover;"></td>
									<td class="left" width="40%"><a
										href="/store/detail?goodsNo=${goodsCart.cartGoods}">${goodsCart.goodsName}</a></td>
									<%-- <td>${goodsCart.goodsPoint} point</td> --%>
									<td><input type="number" class="input-quantity"
										value="${goodsCart.cartQuantity}" min="1"
										max="${goodsCart.goodsQuantity}"
										data-goods-no="${goodsCart.cartGoods}"></td>
									<td>
									<fmt:formatNumber value="${goodsCart.cartTotal}" pattern="#,##0" /> point
									</td>
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
				</div>
				<div style="width: 20%;">
					<div class="order-summary-box">
						
						<div class="total-price-section">
							<span class="label">결제 예정금액</span> <br>
							<div class="cell right">
							<span class="price right"> <span id="totalPrice">0</span><span> point</span> </span>
							</div>
						</div>
						
						<button id="buySelected" class="btn btn-positive w-100">구매하기</button>
						
					</div>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
	<div class="cell right">
		<a href="../list"><button type="button" class="btn btn-neutral">계속 쇼핑하기</button></a>
	</div>
</div>

<!-- jQuery-->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>


<script>
	$(function() {
		
		// ❗ [추가] 숫자에 쉼표를 찍어주는 포맷팅 함수 (100000 -> 100,000)
	    function formatNumber(number) {
	        // Intl.NumberFormat은 최신 JavaScript 표준 포맷팅 방식입니다.
	        return new Intl.NumberFormat('ko-KR').format(number);
	    }

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
				url : "/rest/store/cart/update",
				type : "post",
				data : {
					cartGoods : goodsNo,
					cartQuantity : quantity
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
			$("#totalPrice").text(formatNumber(total));
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
				url : "/rest/store/cart/buyMultiple",
				type : "post",
				data : {
					goodsNos : selected,
				},
				traditional : true,//배열 전송 시 필요
				success : function() {
					location.href = "/store/buyFinish"
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
				url : "/rest/store/cart/deleteMultiple",
				type : "POST",
				data : {
					goodsNos : selected,
				},
				traditional : true,
				success : function() {
					alert("삭제 완료!");
					location.reload();
				}
			});
		});
	});
</script>
