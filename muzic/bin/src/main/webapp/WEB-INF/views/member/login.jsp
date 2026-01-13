<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- 외부 CSS 연결 -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">

<form action="login" method="post">
  <div class="container login-container center">

    <!-- MUZIC 로고 -->
    <div class="cell center">
      <a href="${pageContext.request.contextPath}/" class="muzic-logo">MUZIC</a>
    </div>

    <!-- 아이디 -->
    <div class="cell center">
      <input type="text" name="memberId" placeholder="아이디" required class="field">
    </div>

    <!-- 비밀번호 -->
    <div class="cell center">
      <input type="password" name="memberPw" placeholder="비밀번호" required class="field">
    </div>

    <!-- 로그인 버튼 -->
    <div class="cell center">
      <button type="submit" class="btn-positive">로그인</button>
    </div>

    <!-- 하단 링크 -->
    <div class="cell center link-group">
      <a href="join">회원가입</a>
      <span class="divider">|</span>
      <a href="findMemberId">아이디 찾기</a>
      <span class="divider">|</span>
      <a href="findMemberPw">비밀번호 찾기</a>
    </div>

    <c:if test="${param.error != null}">
      <div class="cell center mt-20">
        <h2 class="red">입력하신 정보가 일치하지 않습니다.</h2>
      </div>
    </c:if>

    <hr class="bottom-line">
  </div>
</form>
