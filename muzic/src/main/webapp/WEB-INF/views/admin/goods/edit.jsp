<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!-- <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> -->
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
<style>
/* 테이블 스타일은 add.jsp와 유사하게 구성 */
.table { width: 100%; border-collapse: collapse; }
.table th, .table td { padding: 10px; border: 1px solid #ddd; text-align: left; }
.table th { width: 150px; background-color: #f7f7f7; }
</style>
<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
	<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
<link rel="stylesheet" type="text/css" href="/summernote/custom-summernote.css">  
<script src="/summernote/custom-summernote.js"></script>
<div class="container w-1000">
	<div class="cell center">
		<h2>관리자 - 상품 수정</h2>
	</div>
	<div class="cell right">
		<a href="detail?goodsNo=${goodsDto.goodsNo}" class="btn btn-neutral">상세 보기로 돌아가기</a>
	</div>
	
	<form action="edit" method="post" enctype="multipart/form-data" class="mt-20">
		<input type="hidden" name="goodsNo" value="${goodsDto.goodsNo}">
		
		<table class="table">
			<tr>
			<th>상품 번호</th>
			<td>${goodsDto.goodsNo}</td>
			</tr>
			<tr>
			<th>상품명</th>
			<td>
			<input type="text" name="goodsName" required value="${goodsDto.goodsName}" style="width: 100%;">
			</td>
			</tr>
			<tr>
			<th>카테고리</th>
                <td>
                    <select name="goodsCategory" required style="padding: 5px;">
                        <option value="의류" ${goodsDto.goodsCategory == '의류' ? 'selected' : ''}>의류</option>
                        <option value="굿즈" ${goodsDto.goodsCategory == '굿즈' ? 'selected' : ''}>굿즈</option>
                        <option value="음반" ${goodsDto.goodsCategory == '음반' ? 'selected' : ''}>음반</option>
                        <option value="포토카드" ${goodsDto.goodsCategory == '포토카드' ? 'selected' : ''}>포토카드</option>
                        <option value="문구류" ${goodsDto.goodsCategory == '문구류' ? 'selected' : ''}>문구류</option>
                        <option value="전자기기" ${goodsDto.goodsCategory == '전자기기' ? 'selected' : ''}>전자기기</option>
                        <option value="액세서리" ${goodsDto.goodsCategory == '액세서리' ? 'selected' : ''}>액세서리</option>
                        <option value="생활용품" ${goodsDto.goodsCategory == '생활용품' ? 'selected' : ''}>생활용품</option>
                        <option value="기타" ${goodsDto.goodsCategory == '기타' ? 'selected' : ''}>기타</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th>포인트 (가격)</th>
                <td>
                    <input type="number" name="goodsPoint" required min="0" value="${goodsDto.goodsPoint}" style="width: 100%;">
                </td>
            </tr>
            <tr>
                <th>재고 수량</th>
                <td>
                    <input type="number" name="goodsQuantity" required min="0" value="${goodsDto.goodsQuantity}" style="width: 100%;">
                </td>
            </tr>
             <tr>
                <th>설명</th>
                <td>
                    <textarea name="goodsDescription" class="summernote-editor">${goodsDto.goodsDescription}</textarea>
                </td>
            </tr>
            <tr>
                <th>유효기간</th>
                <td>
                    <input type="datetime-local" name="goodsExpiration" 
                           value="${goodsDto.formattedGoodsExpiration}" style="width: 100%;">
                           
                    <span style="font-size: 0.8em; color: gray;">(선택 사항)</span>
                </td>
            </tr>
            <tr>
                <th>대표 이미지</th>
                <td>
                    <img src="/store/image?goodsNo=${goodsDto.goodsNo}" style="width: 100px; height: 100px; object-fit: cover; margin-bottom: 10px; border: 1px solid #eee;">
                    <br>
                    <input type="file" name="attach" accept="image/*"><br>
                    <span style="font-size: 0.8em; color: gray;">(새 파일을 첨부하면 기존 이미지는 교체됩니다.)</span>
                </td>
            </tr>
        </table>
        
        <div class="cell right" style="margin-top: 20px;">
            <a href="detail?goodsNo=${goodsDto.goodsNo}" class="btn btn-neutral">취소</a>
            <button type="submit" class="btn btn-positive">수정 완료</button>
        </div>
	</form>

</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>