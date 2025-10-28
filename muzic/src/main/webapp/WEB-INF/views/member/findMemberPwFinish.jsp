<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/js/all.min.js"></script>

<div class="container login-container center" style="margin-top:120px;">

  <!-- 상단 헤더 -->
  <div class="cell left" style="width:650px; margin:auto; margin-bottom:60px;">
    <span class="blue" style="font-size:24px; margin-right:10px;">
      <i class="fa-solid fa-circle"></i>
    </span>

    <!-- MUZIC 로고 (메인으로 이동) -->
    <a href="${pageContext.request.contextPath}/" 
       class="muzic-logo" 
       style="font-size:28px; letter-spacing:3px; color:#111; text-decoration:none;">
       MUZIC
    </a>

    <span style="font-size:22px; color:#555; font-weight:500; margin-left:6px;">비밀번호 변경완료</span>
    <hr style="border:0; border-top:1px solid #ddd; margin-top:20px;">
  </div>

  <!-- 중앙 안내 -->
  <div class="cell center" style="margin-top:40px;">

    <!-- ✅ 체크 아이콘 (연한 민트그린) -->
    <div style="width:80px; height:80px; border-radius:50%; background-color:#1DD1A1;
                color:white; display:flex; align-items:center; justify-content:center; margin:auto;">
      <i class="fa-solid fa-check" style="font-size:36px;"></i>
    </div>

    <h1 style="color:#111; font-weight:800; font-size:26px; margin-top:40px; line-height:1.6;">
      비밀번호 변경 완료
    </h1>
    <p style="color:#666; margin-top:12px; font-size:16px; letter-spacing:-0.3px;">
      비밀번호 변경이 완료되었습니다.<br>
      새로운 비밀번호로 로그인해주세요.
    </p>
  </div>

  <!-- 로그인 버튼 -->
  <div class="cell center" style="margin-top:45px;">
    <a href="${pageContext.request.contextPath}/member/login" 
       class="btn-positive" 
       style="display:block; width:420px; height:54px; 
              line-height:54px; font-weight:bold; 
              font-size:16px; border-radius:10px;
              background-color:#9A9CE3; color:white;
              margin:auto; text-decoration:none;">
      로그인 화면으로
    </a>
  </div>

</div>
