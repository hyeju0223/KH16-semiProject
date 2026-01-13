<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>세션 확인 페이지</h2>
<hr>

<table border="1" cellpadding="8" cellspacing="0">
  <tr><th>세션 속성명</th><th>값</th></tr>
  <tr><td>loginMemberId</td><td>${sessionScope.loginMemberId}</td></tr>
  <tr><td>loginMemberMbti</td><td>${sessionScope.loginMemberMbti}</td></tr>
  <tr><td>loginMemberRole</td><td>${sessionScope.loginMemberRole}</td></tr>
    <tr><td>loginMemberNickname</td><td>${sessionScope.loginMemberNickname}</td></tr>
  
</table>

<br>

<c:if test="${empty sessionScope.loginMemberId}">
  <p style="color:red;">❌ 현재 로그인된 세션이 없습니다.</p>
</c:if>
<c:if test="${not empty sessionScope.loginMemberId}">
  <p style="color:green;">✅ 세션이 정상적으로 유지되고 있습니다.</p>
</c:if>

<a href="/mypage/profile">마이페이지</a>
<br>
<a href="/store/list">STORE</a>
<a href="/post/free/list">자유게시판</a>
<a href="/post/mbti/list">mbti게시판</a>
