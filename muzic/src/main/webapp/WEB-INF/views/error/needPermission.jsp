<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/commons.css">
<link rel="stylesheet" type="text/css"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
	<jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
<div class="container w-1000">
	</div>
		<div class="cell center">
		<img src="${pageContext.request.contextPath}/images/error/warn.png" width="100">
	</div>
	<div class="cell center">
		<h1>${title}</h1>
	<div class="cell center mt-40" style="display: flex; gap: 10px; justify-content: center;">
		<a href="#" class="btn btn-neutral" onclick="goBack()"style="flex-grow: 1; max-width: 150px; text-align: center;">이전 페이지</a> 
		<a href="${pageContext.request.contextPath}/member/login" class="btn btn-positive" style="flex-grow: 1; max-width: 150px; text-align: center;"><i class="fa-solid fa-right-to-bracket"></i><span style="margin-left: 0.4em">로그인</span></a>
	</div>
</div>
<jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	// 브라우저 히스토리 기능을 사용하여 이전 페이지로 이동하는 함수
	function goBack() {
		history.back();
	}
</script>