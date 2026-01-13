<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

  
<link href="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/summernote@0.9.0/dist/summernote-lite.min.js"></script>
<link rel="stylesheet" type="text/css" href="/summernote/custom-summernote.css">  
<script src="/summernote/custom-summernote.js"></script>

<!--  <h1>게시글 작성</h1> -->
<!--  <p>인격 어쩌구.... 말 이쁘게 쓰기🎅🎅<p> -->

<form autocomplete="off" action="write" method="post">
<!-- 	답글일 경우(boardOrigin이 있을 경우) 이것을 전달하는 코드를 작성 -->
<%-- <c:if test="${param.boardOrigin != null}"> --%>
<%-- 	<input type="hidden" name="boardOrigin" value="${param.boardOrigin}"> --%>
<%-- </c:if> --%>

<div class="container w-800">
    <div class="cell">
        <h1>게시글 작성</h1>
    </div>            
    <div class="cell">
        <p>인격 어쩌구.... 말 이쁘게 쓰기🎅🎅<p>
    </div>
    
    <c:if test="${sessionScope.loginRole == '관리자'}">
    	<div class="cell right">
        	<input type="checkbox" name="postNotice" value="Y">
        	<span>공지사항으로 등록</span>
    	</div>
    	<div class="cell right">
        	<input type="checkbox" name="postNotice" value="Y">
        	<span>공지사항으로 등록</span>
    	</div>
    </c:if>
    
    <div class="cell">
        <label>제목 <span class="red">*</span></label>
        <input type="text" name="boardTitle" required class="field w-100">
    </div>
    <div class="cell">
        <label>내용 <span class="red">*</span></label>
        <textarea name="boardContent" class="summernote-editor"></textarea>
    </div>
    <div class="cell right">
        <a href="list" class="btn btn-neutral">목록으로</a>
        <button class="btn btn-positive">등록하기</button>
    </div>
</div>
</form>