<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<h1>장바구니</h1>
<div>
	선택 상품 총액: <span id="totalPrice">0</span>원
</div>

<!-- 선택 구매/삭제 버튼 -->
<button id="buySelected">선택 구매</button>
<button id="deleteSelected">선택 삭제</button>
<table>
	<thead>
		<tr>
			<th><input type="checkbox" class="check-all"></th>
			<th>상품번호</th>
			<th>상품이름</th>
			<th>상품포인트</th>
			<th>수량</th>
			<th>총액</th>
			<th>옵션</th>
			<!-- <th>삭제</th> -->
		</tr>
	</thead>
	<tbody>
		<c:forEach var="goodsCart" items="${cartList}">
			<tr>
				<td><input type="checkbox" class="check-item"
					value="${goodsCart.cartGoods}"></td>
				<td>${goodsCart.cartGoods}</td>
				<td>${goodsCart.goodsName}</td>
				<td>${goodsCart.goodsPoint}
				<td>${goodsCart.cartQuantity}</td>
				<td>${goodsCart.cartTotal}</td>
				<td><button>수량변경</button></td>
				<%-- <td>
					<form action="delete" method="post">
						<input type="hidden" name="goodsNo" value="${goodsCart.cartGoods}">
						<input type="hidden" name="memberId" value="${memberId}">
						<button type="submit">삭제</button>
					</form>
				</td> --%>
			</tr>
		</c:forEach>
	</tbody>
</table>
<a href="../list"><button type="button">계속 쇼핑</button></a>

<!-- jQuery 먼저 불러오기 -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- checkbox.js 불러오기 -->
<script src="/js/checkbox.js"></script>

<script>
	$(function(){
		//선택 구매
		$("#buySelected").click(function(){
			var selected = [];
			$(".check-item:checked").each(function(){
				selected.push($(this).val());
			});
			if(selected.length==0){
				alert("구매할 상품을 선택하세요");
				return;
			}
			if(!confirm("선택한 상품을 구매하시겠습니까?")) return;
			
			$.ajax({
				url:"buyMultiple",
				type:"post",
				data:{goodsNos:selected},
				traditional:true,//배열 전송 시 필요
				success:function(){
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

	        if(selected.length === 0) {
	            alert("삭제할 상품을 선택하세요.");
	            return;
	        }

	        if(!confirm("선택한 상품을 삭제하시겠습니까?")) return;

	        $.ajax({
	            url: "deleteMultiple", // 서버 선택 삭제 처리 URL
	            type: "POST",
	            data: { goodsNos: selected }, // 일반 POST 파라미터 전송
	            traditional: true,
	            success: function() {
	                alert("삭제 완료!");
	                location.reload();
	            }
	        });
	    });
	});

</script>
