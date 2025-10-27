<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/js/all.min.js"></script>

<div class="container login-container center" style="margin-top:100px;">

  <!-- 상단 헤더 -->
  <div class="cell left" style="width:650px; margin:auto; margin-bottom:50px;">
    <span class="blue" style="font-size:24px; margin-right:10px;">
      <i class="fa-solid fa-circle"></i>
    </span>

    <!-- MUZIC 로고 (hover 시 파란색으로 변경) -->
    <a href="${pageContext.request.contextPath}/" 
       class="muzic-logo" 
       style="font-size:28px; letter-spacing:3px; text-decoration:none;">
       MUZIC
    </a>

    <span style="font-size:22px; color:#555; font-weight:500; margin-left:6px;">가입완료</span>
    <hr style="border:0; border-top:1px solid #ddd; margin-top:20px;">
  </div>

  <!-- 중앙 안내 -->
  <div class="cell center" style="margin-top:60px;">
    <h1 style="color:#111; font-weight:800; font-size:26px; line-height:1.8;">
      환영합니다.<br>
      회원가입이 <span class="success">완료</span>되었습니다.
    </h1>
    <p style="color:#666; margin-top:16px; font-size:16px; letter-spacing:-0.3px;">
      MUZIC에서 제공되는 모든 서비스를 정상적으로 이용하실 수 있습니다.
    </p>
  </div>

  <!-- 로그인 버튼 (hover 시 진한 보라색으로 변경) -->
  <div class="cell center" style="margin-top:45px;">
    <a href="${pageContext.request.contextPath}/member/login" 
       class="btn-positive" 
       style="display:block; width:420px; height:54px; 
              line-height:54px; font-weight:bold; 
              font-size:16px; border-radius:10px;
              color:white; margin:auto; text-decoration:none;">
      로그인
    </a>
  </div>

</div>
