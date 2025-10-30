<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css" href="/css/cart.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    
 <style>
 /* 💡 테이블 내부 스타일 조정 */
.table>tbody>tr>td {
	padding-top: 15px; /* 패딩 조정 */
	padding-bottom: 15px; /* 패딩 조정 */
	vertical-align: middle;
	/* text-align은 개별 td에서 조정 */
}

.table {
	table-layout: fixed;
	width: 100%;
    margin: 0 auto; /* 테이블 중앙 정렬 */
    border-collapse: collapse; /* 테이블 테두리 겹침 방지 */
}

/* 💡 테이블 헤더 (<th>) 스타일 */
.table thead th {
    background-color: #f7f7f7;
    border-bottom: 2px solid #ddd;
    padding: 10px 0;
    text-align: center;
}

/* 💡 상품 정보 셀 왼쪽 정렬 */
.left-align {
    text-align: left !important;
    padding-left: 20px !important;
}

.sort-filter {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
    align-items: center; 
}
 </style>
    
<div class="container w-1000">
	<div class="cell center">
		<h1>관리자 - 상품목록</h1>
	</div>
	<a href="/">메인으로</a>
    
    <div class="cell right" style="margin-bottom: 15px; margin-top: 15px;">
        <a href="add" class="btn btn-positive">상품 등록</a>
    </div>

	<div class="cell category-filter">
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
        
		<table class="table">
			<thead>
				<tr>
					<th style="width: 150px;">이미지</th> 
					<th>상품 정보</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="goodsDto" items="${goodsList}">
					<tr>
						<td style="text-align: center;"> 
                            <img src="/store/image?goodsNo=${goodsDto.goodsNo}" style="width: 100px; height: 100px; object-fit: cover; border-radius: 4px;">
                        </td>
						<td class="left-align">
                            <a href="detail?goodsNo=${goodsDto.goodsNo}" style="font-size: 1.2em; font-weight: bold; color: #333;">${goodsDto.goodsName}</a>
                            <br><br>
                            <span style="font-size: 1.1em; color: #007bff;">
                                <fmt:formatNumber value="${goodsDto.goodsPoint}" pattern="#,##0" /> point
                            </span>
                             <div style="margin-top: 10px;">
                                <a href="edit?goodsNo=${goodsDto.goodsNo}" class="btn btn-neutral">수정</a>
                                <a href="delete?goodsNo=${goodsDto.goodsNo}" class="btn btn-negative" onclick="return confirm('상품을 삭제하시겠습니까?');">삭제</a>
                            </div>
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
					<c:param name="column" value="${param.column}"/>
					<c:param name="keyword" value="${param.keyword}"/>
					<%-- 3. 페이지당 게시물 수 유지 --%>
					<c:param name="size" value="${pageVO.size}"/>
                    <%-- 4. 정렬 정보 유지 --%>
                    <c:param name="sort" value="${sort}"/>
				</c:url>
				
				<c:if test="${pageVO.getStrPage() > 1}">
					<a href="${baseLink}&page=${pageVO.getStrPage() - 1}">&lt;&lt;</a>
				</c:if>

				<c:forEach var="p" begin="${pageVO.getStrPage()}" 
						   end="${pageVO.getTotalPage() < pageVO.getEndPage() ? pageVO.getTotalPage() : pageVO.getEndPage()}" step="1">
					<c:choose>
						<c:when test="${pageVO.page == p}">
							<a class="active">${p}</a>
						</c:when>
						<c:otherwise>
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