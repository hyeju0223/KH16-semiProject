<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">

<head>
  <meta charset="UTF-8">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Document</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/commons.css">
  <link rel="stylesheet" type="text/css"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

  <link rel="stylesheet" type="text/css"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
  <!-- 폰트 cdn-->
  <link href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/stylesheet.css" rel="stylesheet">
  <!-- 이미지 슬라이스 cdn-->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />

  <style>
    .swiper {
      width: 100%;
    }

    .logo {
      height: 50px;
    }
    .profile-area-1 {
      gap: 10px;
    }


    .profile-img-area-1 {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      overflow: hidden;
    }

    .profile-img-1 {
      width: 100%;
      height: 100%;
      object-fit: cover;
      object-position: center;
    }

    .profile-img:hover {
      filter: brightness(85%);
      transition: filter 0.3s ease-out;
    }
  </style>
  <link rel="icon" href="${pageContext.request.contextPath}/favicon.ico">
  <script src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>
  <script type="text/javascript">

    

  </script>
</head>

<body>
   <div class="container">
  <div>

  </div>
    <!-- 로고 / 검색창 / 회원상태 영역-->
    <!-- 로고 -->
    <div class="top-bar cell center flex-box flex-center">
      <div class="cell">
        <a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/image/home/logo.png" class="logo"></a>
      </div>
      <!-- 검색창 -->
      <!-- <div class="cell w-100 shearch-area">
        <input type="text" class="shearch-bar">
        <button class="btn btn-positive shearch-btn">
          <i class="fa-solid fa-magnifying-glass"></i>
        </button>
      </div> -->
      <div class="flex-fill "></div>
      
      <c:choose>
       <c:when test="${sessionScope.loginMemberId != null}">
       
        <c:if test="${sessionScope.loginMemberRole == '관리자'}">
      <div class="cell w-120 profile-text">
        <a href="<c:url value='/admin/member/list'/>" class="teg">
          <i class="fa-solid fa-screwdriver-wrench"></i>관리자
        </a>
      </div>
      </c:if>
      
      <div class="cell w-120 profile-text">
        <a href="#"  class="teg"><i class="fa-solid fa-headset"></i>고객센터</a>
      </div>
      <div class="cell w-120 profile-text">
        <a href="${pageContext.request.contextPath}/mypage/profile"><i class="fa-solid fa-user"></i>마이페이지</a>
      </div>
      <div class="cell w-120 profile-text">
        <a href="${pageContext.request.contextPath}/store/cart/list" class="teg"><i class="fa-solid fa-cart-shopping"></i>장바구니</a>
      </div>
       <div class="cell w-120 profile-text">
        <a href="${pageContext.request.contextPath}/music/add" class="teg"><i class="fa-solid fa-music"></i>음원등록</a>
      </div>
      <!-- 회원상태 -->
      <div class="cell w-150 profile-area-1 flex-box flex-center">
        <div class="cell profile-img-area-1">
          <img src="${pageContext.request.contextPath}/mypage/image?memberId=${sessionScope.loginMemberId}" class="profile-img-1">
        </div>
        <a href="${pageContext.request.contextPath}/member/logout" class="profile-state teg">로그아웃</a>
      </div>
       </c:when>
       
       <c:otherwise>
             <div class="cell w-120 profile-text">
        <a href="#"  class="teg"><i class="fa-solid fa-headset"></i>고객센터</a>
      </div>
      <!-- 회원상태 -->
      <div class="cell w-150 profile-area-1 flex-box flex-center">
        <a href="${pageContext.request.contextPath}/member/login" class="profile-state teg">로그인</a>
      </div>
       </c:otherwise>
      </c:choose>
      
    </div>
   

    <!-- 메뉴 영역-->
    <div class="cell center menu-bar flex-box flex-center mt-30">
      <div class="menu-text"><a href="${pageContext.request.contextPath}/post/free/list" class="teg">커뮤니티</a></div>
      <div class="menu-text" style="font-weight: 500; font-size: 37px;"><a href="${pageContext.request.contextPath}/post/mbti/list" class="teg">MBTI</a></div>
      <div class="menu-text"><a href="${pageContext.request.contextPath}/music/list" class="teg">음원 목록</a></div>
      <div class="menu-text"><a href="${pageContext.request.contextPath}/store/list"" class="teg">스토어</a></div>
      <div class="menu-block w-300"></div>
      <div class="menu-text"><a href="${pageContext.request.contextPath}/event/roulette" class="teg">이벤트</a></div>
    </div>
    <hr class="line">
