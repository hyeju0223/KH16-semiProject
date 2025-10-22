<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">

<div class="container login-container center">
  <div class="cell center mb-30">
    <h1 class="login-title">회원가입</h1>
  </div>

  <form action="join" method="post">
    <!-- 아이디 -->
    <div class="cell center">
      <input type="text" name="memberId" placeholder="아이디" required class="field">
    </div>

    <!-- 비밀번호 -->
    <div class="cell center">
      <input type="password" name="memberPw" placeholder="비밀번호" required class="field">
    </div>

    <!-- 닉네임 -->
    <div class="cell center">
      <input type="text" name="memberNickname" placeholder="닉네임" required class="field">
    </div>

    <!-- 이름 -->
    <div class="cell center">
      <input type="text" name="memberName" placeholder="이름" required class="field">
    </div>

    <!-- 이메일 -->
    <div class="cell center">
      <input type="email" name="memberEmail" placeholder="이메일" required class="field">
    </div>

    <!-- MBTI -->
    <div class="cell center">
      <input type="text" name="memberMbti" placeholder="MBTI (예: INFP)" maxlength="4" class="field">
    </div>

    <!-- 생년월일 -->
    <div class="cell center">
      <input type="text" name="memberBirth" placeholder="생년월일 (예: 2000-01-01)" class="field">
    </div>

    <!-- 연락처 -->
    <div class="cell center">
      <input type="text" name="memberContact" placeholder="연락처 (예: 010-0000-0000)" class="field">
    </div>

    <!-- 주소 -->
    <div class="cell center">
      <input type="text" name="memberPostcode" placeholder="우편번호 (선택)" class="field">
    </div>

    <div class="cell center">
      <input type="text" name="memberAddress1" placeholder="기본주소 (선택)" class="field">
    </div>

    <div class="cell center">
      <input type="text" name="memberAddress2" placeholder="상세주소 (선택)" class="field">
    </div>

    <div class="cell center mt-30">
      <button type="submit" class="btn btn-positive">가입하기</button>
    </div>
  </form>
</div>
