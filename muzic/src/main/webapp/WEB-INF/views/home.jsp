<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>세션 확인 페이지</h2>
<hr>

<table border="1" cellpadding="8" cellspacing="0">
  <tr><th>세션 속성명</th><th>값</th></tr>
  <tr><td>loginId</td><td>${sessionScope.loginId}</td></tr>
  <tr><td>loginName</td><td>${sessionScope.loginName}</td></tr>
  <tr><td>loginEmail</td><td>${sessionScope.loginEmail}</td></tr>
  <tr><td>loginMbti</td><td>${sessionScope.loginMbti}</td></tr>
  <tr><td>loginBirth</td><td>${sessionScope.loginBirth}</td></tr>
  <tr><td>loginPoint</td><td>${sessionScope.loginPoint}</td></tr>
  <tr><td>loginRole</td><td>${sessionScope.loginRole}</td></tr>
</table>

<br>

<c:if test="${empty sessionScope.loginId}">
  <p style="color:red;">❌ 현재 로그인된 세션이 없습니다.</p>
</c:if>
<c:if test="${not empty sessionScope.loginId}">
  <p style="color:green;">✅ 세션이 정상적으로 유지되고 있습니다.</p>
</c:if>
