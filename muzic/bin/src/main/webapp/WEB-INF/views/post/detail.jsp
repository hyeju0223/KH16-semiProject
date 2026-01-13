<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
	<a href="write">글쓰기</a> 
<%-- 	<a href="write?boardOrigin=${boardDto.boardNo}">답글쓰기</a>  --%>
	<%-- 내 글일 경우 수정 삭제를 모두 표시 --%>
	<c:if test="${sessionScope.loginId != null}">
	<c:choose>
		<c:when test="${sessionScope.loginId == postDto.postWriter}">
			<a href="edit?boardNo=${postDto.postNo}">수정</a> 
			<a href="delete?boardNo=${postDto.postNo}">삭제</a>
		</c:when>
		<c:when test="${sessionScope.loginLevel == '관리자'}">
			<a href="delete?boardNo=${postDto.postNo}">삭제</a>
		</c:when>
	</c:choose>
	</c:if>
	<a href="list">목록</a>
</div>