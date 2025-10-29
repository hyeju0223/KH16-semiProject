<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/css/commons.css">
<div class="container w-1000">
	<div class="cell center">
		<h1>${title}</h1>
	</div>
	<div class="center mt-40" style="display: flex; gap: 10px; justify-content: center;">
		<a href="/" class="btn btn-positive" style="flex-grow: 1; max-width: 200px; text-align: center;">홈으로</a> 
		<a href="#" class="btn btn-neutral" onclick="goBack()" style="flex-grow: 1; max-width: 200px; text-align: center;">이전 페이지</a>
	</div>
</div>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
	// 브라우저 히스토리 기능을 사용하여 이전 페이지로 이동하는 함수
	function goBack() {
		history.back();
	}
</script>