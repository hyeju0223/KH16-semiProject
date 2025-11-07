<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/commons.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/cart.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<style>

/* 크롬에서 수량버튼의 스피너 안보이게 */
    input[type="number"]::-webkit-inner-spin-button {
        -webkit-appearance: none;
        margin: 0;
    }

.table>tbody>tr>td {
	padding-top: 20px;
	padding-bottom: 20px;
	vertical-align: middle;
}

.table {
	table-layout: fixed;
	width: 100%;
}
/* 🟢 [추가] 정렬 필터 내부 요소들을 수직 중앙 정렬 */
.sort-filter {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    align-items: center; /* ⭐ 파이프 문자 정렬 핵심 */
}
</style>
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<div class="container w-1000">
	<!-- <a href="../">메인으로</a> -->
	<!-- <div class="cell right">
		<a href="cart/list"> <i class="fa-solid fa-cart-shopping"></i>
		</a>
	</div> -->
	<div class="cell category-filter mt-30">
		<a href="list"
			class="btn-category ${empty param.goodsCategory ? 'active' : ''}">전체</a>

		<a href="list?goodsCategory=의류"
			class="btn-category ${param.goodsCategory == '의류' ? 'active' : ''}">의류</a>
		<a href="list?goodsCategory=굿즈"
			class="btn-category ${param.goodsCategory == '굿즈' ? 'active' : ''}">굿즈</a>
		<a href="list?goodsCategory=음반"
			class="btn-category ${param.goodsCategory == '음반' ? 'active' : ''}">음반</a>
		<a href="list?goodsCategory=포토카드"
			class="btn-category ${param.goodsCategory == '포토카드' ? 'active' : ''}">포토카드</a>
		<a href="list?goodsCategory=문구류"
			class="btn-category ${param.goodsCategory == '문구류' ? 'active' : ''}">문구류</a>
		<a href="list?goodsCategory=전자기기"
			class="btn-category ${param.goodsCategory == '전자기기' ? 'active' : ''}">전자기기</a>
		<a href="list?goodsCategory=액세서리"
			class="btn-category ${param.goodsCategory == '액세서리' ? 'active' : ''}">액세서리</a>
		<a href="list?goodsCategory=생활용품"
			class="btn-category ${param.goodsCategory == '생활용품' ? 'active' : ''}">생활용품</a>
		<a href="list?goodsCategory=기타"
			class="btn-category ${param.goodsCategory == '기타' ? 'active' : ''}">기타</a>
	</div>
	
	
	<div class="cell center" style="margin-top: 50px; margin-bottom: 20px;">
		<form action="list" method="get" style="display: inline-flex; gap: 5px;">
			
			<input type="hidden" name="column" value="goods_name">
			
			<input type="text" name="keyword" placeholder="상품명 입력" value="${param.keyword}"
				style="padding: 8px; border: 1px solid #ccc; border-radius: 4px; width: 200px;">
			
			<button type="submit" class="btn btn-positive">
				<i class="fa-solid fa-magnifying-glass"></i> 검색
			</button>
			
		</form>
	</div>
	
	<div class="cell right" style="margin-bottom: 10px;">
        <div class="sort-filter">
            
            <%-- 정렬 기준: sort=price_asc (낮은가격순) --%>
            <a href="list?goodsCategory=${param.goodsCategory}&column=${param.column}&keyword=${param.keyword}&size=${pageVO.size}&sort=price_asc"
                class="btn-sort ${param.sort == 'price_asc' ? 'active' : ''}">낮은가격순</a>
            <span> | </span>
            
            <%-- 정렬 기준: sort=price_desc (높은가격순) --%>
            <a href="list?goodsCategory=${param.goodsCategory}&column=${param.column}&keyword=${param.keyword}&size=${pageVO.size}&sort=price_desc"
                class="btn-sort ${param.sort == 'price_desc' ? 'active' : ''}">높은가격순</a>
            <span> | </span>
            
            <%-- 정렬 기준: sort=regdate_desc (최신등록순 - 기본값) --%>
            <a href="list?goodsCategory=${param.goodsCategory}&column=${param.column}&keyword=${param.keyword}&size=${pageVO.size}&sort=regdate_desc"
                class="btn-sort ${empty param.sort || param.sort == 'regdate_desc' ? 'active' : ''}">최신등록순</a>
        </div>
    </div>
	
	<div class="cell">
	
	<c:if test="${goodsList == null || goodsList.size() == 0}">
            <div class="center" style="padding: 50px 0;">
                <p style="font-size: 1.2em; color: #777;">검색 결과가 없습니다.</p>
            </div>
        </c:if>
        
		<table class="table w-100">
			<thead>
				<tr>
					<th style="width: 200px;"></th>
					<th></th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="goods" items="${goodsList}">
					<tr>
						<td style="padding: 40px"><img src="image?goodsNo=${goods.goodsNo}"
							style="width: 100px; height: 100px; object-fit: cover;"></td>
						<td class="left"><a href="detail?goodsNo=${goods.goodsNo}"
							style="font-size: 1.25em">${goods.goodsName}</a><br><br><fmt:formatNumber value="${goods.goodsPoint}" pattern="#,##0" />point<br></td>
						<td class="left">
						
							<c:if test="${goods.goodsQuantity > 0}">
							<form class="cartAddForm" style="display: flex; justify-content: flex-start; margin-left: 50px;">
								<div style="display: flex; align-items: center; gap: 10px;">
									<div class="quantity-selector">

										<button type="button" class="btn-qty btn-qty-down">-</button>

										<input type="number" name="goodsQuantity" class="input-qty"
											value="1" min="1" max="${goods.goodsQuantity}">

										<button type="button" class="btn-qty btn-qty-up">+</button>
									</div>
									<div>
										<button type="submit" class="btn btn-positive"
											formaction="buy?goodsNo=${goods.goodsNo}" formmethod="post"
											onclick="return confirm('구매하시겠습니까?');">바로구매</button>

										<button type="button" class="cartAddBtn btn btn-neutral">
											장바구니 담기</button>
									</div>
									<input type="hidden" name="goodsNo" value="${goods.goodsNo}">
								</div>
							</form>
							</c:if>
							<c:if test="${goods.goodsQuantity == 0}">
							    <span style="color: red; font-weight: bold; margin-left: 50px;">품절</span>
							</c:if>
							
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
	<c:if test="${pageVO != null and pageVO.getTotalPage() > 1}">
		<div class="cell center">
			<div class="pagination">
			
				<%-- URL 파라미터 설정을 위한 baseLink 생성 --%>
				<c:url var="baseLink" value="list">
					<%-- 1. 카테고리 필터 정보 유지 --%>
					<c:param name="goodsCategory" value="${goodsCategory}"/>
					<%-- 2. 검색 정보 유지 (Controller에서 model.addAttribute로 받은 값 사용) --%>
					<c:param name="column" value="${column}"/>
					<c:param name="keyword" value="${keyword}"/>
					<%-- 3. 페이지당 게시물 수 유지 --%>
					<c:param name="size" value="${pageVO.size}"/>
				</c:url>
				
				<c:if test="${pageVO.getStrPage() > 1}">
					<a href="${baseLink}&page=${pageVO.getStrPage() - 1}">&lt;&lt;</a>
				</c:if>

				<%-- getTotalPage()와 getEndPage()를 비교하여 블럭의 끝을 결정 --%>
				<c:forEach var="p" begin="${pageVO.getStrPage()}" 
						   end="${pageVO.getTotalPage() < pageVO.getEndPage() ? pageVO.getTotalPage() : pageVO.getEndPage()}" step="1">
					<c:choose>
						<c:when test="${pageVO.page == p}">
							<%-- 현재 페이지 --%>
							<a class="active">${p}</a>
						</c:when>
						<c:otherwise>
							<%-- 다른 페이지 --%>
							<a href="${baseLink}&page=${p}">${p}</a>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:if test="${pageVO.getEndPage() < pageVO.getTotalPage()}">
					<a href="${baseLink}&page=${pageVO.getEndPage() + 1}">&gt;&gt;</a>
				</c:if>
				
			</div>
		</div>
	</c:if>
	</div>
<div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/js/cart-quantity.js"></script>

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
					var result = confirm("장바구니로 이동하시겠습니까?");
					if (result) {
						location.href = "/store/cart/list";
					}
				}
			});
		});
	});
</script>
