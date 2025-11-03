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
  <link rel="stylesheet" type="text/css" href="/css/commons.css">
  <link rel="stylesheet" type="text/css"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">

  <link rel="stylesheet" type="text/css"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
  <!-- 폰트 cdn-->
  <link href="https://cdn.jsdelivr.net/gh/sunn-us/SUIT/fonts/static/stylesheet.css" rel="stylesheet">
  <!-- 이미지 슬라이스 cdn-->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />

  <style>
    body {
      font-family: 'Franklin Gothic Medium', 'Arial Narrow', Arial, sans-serif;
      color: #2d3436;
    }
    
    a {
    color: inherit;
          text-decoration: none;
    }

    .teg {
      color: inherit;
      text-decoration: none;
      transition-duration: 0.3s;

    }
    

    .teg:hover {
      color: #9396D5;
    }
    

    .top-bar,
    .album-area,
    .menu-bar,
    .center-area {
      max-width: 1400px;
      margin: 0 auto;
    }


    .swiper {
      width: 100%;
    }

    .logo {
      height: 50px;
    }

    /* .shearch-area {
      height: 50px;
    }

    .shearch-bar {
      width: 92%;
      height: 100%;
      border-radius: 0.5em;
      padding-left: 1em;
    }

    .shearch-btn {
      height: 100%;
    } */

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

    .menu-bar {
      height: 40px;
    }

    .line {
      border: none;
      /*기존 선 효과 삭제*/
      background-color: #9396D5;
      height: 1.5px;
    }

    .album-area {
      width: 100%;
      height: 400px;
      padding: 30px;
    }

    .menu-text {
      width: 20%;
      font-size: 30px;
      margin-bottom: 30px;
      font-weight: 700;
    }

    .area {
      /* margin-block: 40px; */
    }


    .post-title {
      font-size: 24px;
      font-weight: bold;
      margin : 10px;

    }
    


    .music-rank {
      margin-top: 5px;
      gap: 10px;
      padding: 5px;
    }

      .music-rank-1 {
      /* margin-top: 5px; */
      gap: 10px;
      padding: 10px;
      font-size: 22px;
      font-weight: bold;
    }

    .music-rank .flex-fill,
    .music-rank-1 .flex-fill {
      text-align: left;
      padding-left: 5px;
    }


    .album-box {

      width: 300px;
      height: 100%;
      font-size: 18px;
      background-color: #ffffff;
      padding-bottom: 10px;
      padding-top: 10px;
      border: 1px solid #dcdce4;
      border-radius: 4px;
      box-shadow: 0 12px 18px #d3d3db;
    }

    #album-swiper {
      overflow: hidden;
      height: 85%;

    }

    #album-swiper .swiper-slide {
      opacity: 0;
      z-index: 1;
    }

    #album-swiper .swiper-slide.swiper-slide-next,
    #album-swiper .swiper-slide.swiper-slide-prev {
      transform: scale(0.6);
      opacity: 0.9;
      z-index: 3;
    }

    #album-swiper .swiper-slide.swiper-slide-next2,
    #album-swiper .swiper-slide.swiper-slide-prev2 {
      transform: scale(0.3);
      opacity: 0.4;
      z-index: 2;
    }


    #album-swiper .swiper-slide.swiper-slide-active {
      transform: scale(1);
      opacity: 1;
      z-index: 4;
    }

    .album-img {
      width: 280px;
      height: 280px;
      /* border-radius: 3px; */
    }

    .start-area {
      flex: 2;
      margin-right: 30px;
    }

    .end-area {
      flex: 1;
    }


    .login-area {
      width: 100%;
      height: 100px;
    }

    .login-box {
      padding: 20px;
      border: 1px solid #dfe6e9;
      border-radius: 10px;
    }

    .border-area {
      border: 1px solid #dfe6e9;
      border-radius: 10px;
    }

    .login-bar {
      color: whitesmoke;
      background-color: #9396D5;
      padding: 0.5em 0.75em;
      border-radius: 0.3em;
      font-weight: 600;
    }

    .login-bar:hover {
      filter: brightness(0.9);
    }

    .ad-banner-img {
      width: 100%;
      border-radius: 10px;
      object-fit: cover;
      object-position: center;
    }

    .ad-banner {
      margin-top: 30px;
      border: 1px solid #dfe6e9;
      border-radius: 10px;
    }

    .music-area {
      padding: 10px;
    }

    .rank-title {
      font-size: 18px;
      font-weight: 500;
      padding: 10px;
    }

    .music-rank>.album-img{
      width: 0%;
      height: 0%;
      margin-left: 5px;
    }

      .music-rank-1>.album-img{
      width: 25%;
      height: 25%;
      margin-left: 5px;
    }

    .record-area {
      width: 100%;

    }

    .record {
      position: relative;
      /*기준점*/
    }

    .record-img {
      width: 60%;
      height: 60%;
      animation: spin 4s linear infinite;
      /*애니메이션 이름, 한바퀴 도는 시간, 속도, 무한반복*/
    }

    .record .text {
      position: absolute;
      /* 부모를 기준으로*/
      font-size: 24px;
      font-weight: bold;
      color: #000000;
      top: 50%;
      left: 50%;
      opacity: 0;
      transform: translate(-50%, -50%);
      transition: opacity 0.3s;
    }

    .record:hover {
      transform: scale(1.1);
      filter: opacity(0.3);
    }

    .record:hover .text {
      opacity: 1;
    }

    @keyframes spin {
      to {
        transform: rotate(360deg);
      }
    }

    .post-area {
      height: 400px;
      gap: 10px;
    }

    .post .free-post,
    .post .mbti-post {
      gap: 20px;
    }
    
    .post-comment-content,
    .post-comment-title,
    .reply, .like {
    	padding : 8px;
    }
    
    .post-row:hover {
   		font-weight: 600;
    }
    

    .album-title {
      font-weight: bold;
    }

    .banner-area {
      width: 100%;
    }

    .banner {
      max-width: 980px;
      height: 200px;
      border-radius: 10px;


    }

    .banner-img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      object-position: center;
    }

    .footer {
      max-width: 1400px;
      margin: 0% auto;
    }


  </style>
  <link rel="icon" href="/favicon.ico">
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
        <a href="/"><img src="/image/home/logo.png" class="logo"></a>
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
        <a href="/mypage/profile"><i class="fa-solid fa-user"></i>마이페이지</a>
      </div>
      <div class="cell w-120 profile-text">
        <a href="/store/cart/list" class="teg"><i class="fa-solid fa-cart-shopping"></i>장바구니</a>
      </div>
       <div class="cell w-120 profile-text">
        <a href="/music/add" class="teg"><i class="fa-solid fa-music"></i>음원등록</a>
      </div>
      <!-- 회원상태 -->
      <div class="cell w-150 profile-area-1 flex-box flex-center">
        <div class="cell profile-img-area-1">
          <img src="/mypage/image?memberId=${sessionScope.loginMemberId}" class="profile-img-1">
        </div>
        <a href="/member/logout" class="profile-state teg">로그아웃</a>
      </div>
       </c:when>
       
       <c:otherwise>
             <div class="cell w-120 profile-text">
        <a href="#"  class="teg"><i class="fa-solid fa-headset"></i>고객센터</a>
      </div>
      <!-- 회원상태 -->
      <div class="cell w-150 profile-area-1 flex-box flex-center">
        <a href="/member/login" class="profile-state teg">로그인</a>
      </div>
       </c:otherwise>
      </c:choose>
      
    </div>
   

    <!-- 메뉴 영역-->
    <div class="cell center menu-bar flex-box flex-center mt-30">
      <div class="menu-text"><a href="/post/free/list" class="teg">커뮤니티</a></div>
      <div class="menu-text" style="font-weight: 500; font-size: 37px;"><a href="/post/mbti/list" class="teg">MBTI</a></div>
      <div class="menu-text"><a href="music/list" class="teg">음원 목록</a></div>
      <div class="menu-text"><a href="/store/list"" class="teg">스토어</a></div>
      <div class="menu-block w-300"></div>
      <div class="menu-text"><a href="/event/roulette" class="teg">이벤트</a></div>
    </div>
    <hr class="line">

