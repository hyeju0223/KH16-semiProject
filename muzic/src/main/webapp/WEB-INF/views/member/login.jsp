<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">


<form action="login" method="post">
  <div class="container login-container center">
    <div class="cell center mb-30">
      <h1 class="login-title">로그인</h1>
    </div>

    <div class="cell center">
      <input type="text" name="memberId" placeholder="아이디 또는 이메일" required class="field">
    </div>

    <div class="cell center">
      <input type="password" name="memberPw" placeholder="비밀번호" required class="field">
    </div>

    <div class="cell center mt-30">
      <button type="submit" class="btn btn-positive">로그인</button>
    </div>

    <div class="cell center mt-20 link-group">
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
  </div>
</form>
