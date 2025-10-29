<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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
      transition-duration: 0.3s;

    }
    

    a:hover {
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

    .profile-area {
      gap: 10px;
    }


    .profile-img-area {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      overflow: hidden;
    }

    .profile-img {
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
      margin: 10px;
      padding: 10px;
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

    .music-rank .ranking-text {
      font-size: 16px;
      padding: 0.5em;
      margin-left: 20px;
    }

    .music-rank-1 .ranking-text {
      font-size: 18px;
      padding: 0.5em;
      margin-left: 5px;

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
      padding: 20px;
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
      margin: 20px;
      gap: 20px;
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
  <script src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>
  <script type="text/javascript">

    $(function () {

      //앨범 스와이퍼
      var albumSwiper = new Swiper('#album-swiper', {
        // Optional parameters
        direction: 'horizontal', //가로 방향
        loop: true, //무한 반복
        loopAdditionalSlides: 0, //양옆으로 복제
        slidesPerView: 5, //한 화면에 보여질 슬라이드 개수
        centeredSlides: true, //활성화된 슬라이드를 중앙에 위치


        // If we need pagination
        pagination: {
          el: '#album-swiper .album-box .swiper-pagination',
          type: 'bullets',
          clickable: true, //점 클릭 가능
        },

        autoplay: {
          delay: 3000, // 3초마다 자동 이동
          disableOnInteraction: false, // 마우스 올렸다가 빼면 다시 재생
          pauseOnMouseEnter: true, // 올려두는 동안만 일시정지
        },

        // Navigation arrows
        navigation: {
          nextEl: '#album-swiper .swiper-button-next',
          prevEl: '#album-swiper .swiper-button-prev',
        },
        effect: 'coverflow',
        coverflowEffect: {
          rotate: 0,
          stretch: 0,
          depth: 200,
          modifier: 1,
          slideShadows: false,
        },

        on: { /*클래스 직접 추가*/
          slideChange: function () {
            var slides = $("#album-swiper .swiper-slide");
            var realndex = this.activeIndex; //현재 활성 슬라이드
            var total = slides.length; //전체 슬라이드

            slides.removeClass("swiper-slide-prev2 swiper-slide-next2");

            // 두 칸 뒤 (next2)
            slides.eq((realndex + 2) % total).addClass("swiper-slide-next2");

            // 두 칸 앞 (prev2)
            slides.eq((realndex - 2 + total) % total).addClass("swiper-slide-prev2");
          }

        },

      });

      //배너 스와이퍼
      var bannerSwiper = new Swiper('#banner-swiper', {
        // Optional parameters
        direction: 'horizontal', //가로 방향
        loop: true, //무한 반복


        // If we need pagination
        pagination: {
          el: '#banner-swiper .swiper-pagination',
          type: 'bullets',
          clickable: true, //점 클릭 가능
        },

        autoplay: {
          delay: 3000, // 3초마다 자동 이동
          disableOnInteraction: false, // 마우스 올렸다가 빼면 다시 재생
          pauseOnMouseEnter: true, // 올려두는 동안만 일시정지
        },

        effect: 'slide', // 'fade', 'cube', 'coverflow' 등 변경 가능
      });


      //음원 차트 색깔넣기

      $(".music-rank:even").css("background-color", "#f5f6fa");

      //호버 기능을 위한 클래스 추가
      $(document).on("mouseenter", ".music-rank", function(){
        $(this).removeClass("music-rank music-rank-1").addClass("music-rank-1");
      });

        $(document).on("mouseleave", ".music-rank, .music-rank-1", function(){
        $(this).removeClass("music-rank music-rank-1").addClass("music-rank");
      });
        
        //문자열 자르기
      /*   $(".post-comment-title").each(function(){
        	var text = $(this).text();
        	var change = text.substring(0, 5);
        	$(this).text(change);
        	} */
       /*  }); */

    });

  </script>
</head>

<body>
  <div class="container">
  <div>
  loginMemberId</td><td>${sessionScope.loginMemberId} 
loginMemberMbti</td><td>${sessionScope.loginMemberMbti}
loginMemberRole</td><td>${sessionScope.loginMemberRole}
loginMemberNickname</td><td>${sessionScope.loginMemberNickname}

<c:if test="${empty sessionScope.loginMemberId}">
  <p style="color:red;">❌ 현재 로그인된 세션이 없습니다.</p>
</c:if>
<c:if test="${not empty sessionScope.loginMemberId}">
  <p style="color:green;">✅ 세션이 정상적으로 유지되고 있습니다.</p>
</c:if>
  </div>
    <!-- 로고 / 검색창 / 회원상태 영역-->
    <!-- 로고 -->
    <div class="top-bar cell center flex-box flex-center">
      <div class="cell">
        <a href=""><img src="/image/home/logo.png" class="logo"></a>
      </div>
      <!-- 검색창 -->
      <!-- <div class="cell w-100 shearch-area">
        <input type="text" class="shearch-bar">
        <button class="btn btn-positive shearch-btn">
          <i class="fa-solid fa-magnifying-glass"></i>
        </button>
      </div> -->
      <div class="flex-fill "></div>
      <div class="cell w-120 profile-text">
        <a href="#"><i class="fa-solid fa-headset"></i>고객센터</a>
      </div>
      <div class="cell w-120 profile-text">
        <a href="/mypage/profile"><i class="fa-solid fa-user"></i>마이페이지</a>
      </div>
      <div class="cell w-120 profile-text">
        <a href="#"><i class="fa-solid fa-cart-shopping"></i>장바구니</a>
      </div>
      <!-- 회원상태 -->
      <div class="cell w-150 profile-area flex-box flex-center">
        <div class="cell profile-img-area">
          <img src="/image/home/profile-img.jpg" class="profile-img">
        </div>
        <a href="#" class="profile-state">로그아웃</a>
        </span>
      </div>
    </div>

    <!-- 앨범 영역-->
    <div class="cell center album-area flex-box flex-center swiper mt-30" id="album-swiper">
      <div class="swiper-wrapper">
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img3.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img2.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div>
        <!-- <div class="cell swiper-slide">
          <div class="album-box">
            <img class="cell album-img" src="/image/home/album-img1.jpg">
            <div>노래명 - 가수명</div>
          </div>
        </div> -->
      </div>
      <div class="swiper-button-prev"></div>
      <div class="swiper-button-next"></div>
    </div>

    <!-- 메뉴 영역-->
    <div class="cell center menu-bar flex-box flex-center mt-30">
      <div class="menu-text"><a href="/post/free/list">커뮤니티</a></div>
      <div class="menu-text" style="font-weight: 500; font-size: 37px;"><a href="/post/mbti/list">MBTI</a></div>
      <div class="menu-text"><a href="music/add">음원등록</a></div>
      <div class="menu-text"><a href="/store/list"">스토어</a></div>
      <div class="menu-block w-300"></div>
      <div class="menu-text"><a href="#">이벤트</a></div>
    </div>
    <hr class="line">
    <div class="advertising-text">
    </div>

    <!-- 게시물 영역 -->

    <div class="cell mt-30 center-area area">

      <div class="cell flex-box">


        <!-- 등록된 음원 -->
        <div class="cell start-area area ">
          <label class="post-title">등록된 음원</label>
          <div class="cell record-area flex-box flex-center center">
            <div class="record w-100">
              <img src="/image/home/record-img.png" class="record-img">
              <span class="text">앨범명</span>
            </div>
            <div class="record w-100">
              <img src="/image/home/record-img.png" class="record-img">
              <span class="text">앨범명</span>
            </div>
            <div class="record w-100">
              <img src="/image/home/record-img.png" class="record-img">
              <span class="text">앨범명</span>
            </div>
            <div class="record w-100">
              <img src="/image/home/record-img.png" class="record-img">
              <span class="text">앨범명</span>
            </div>
          </div>

          <!-- 배너 영역 -->
          <div class="cell banner-area area center mt-30">
            <div class="banner swiper" id="banner-swiper">
              <div class="swiper-wrapper">
                <img src="/image/home/banner-1-1100x200.png" class="banner-img swiper-slide">
                <img src="/image/home/banner-2-1100x200.png" class="banner-img swiper-slide">
              </div>
              <div class="swiper-pagination"></div>
            </div>
          </div>



          <div class="cell post-area area flex-box w-100 mt-30">
            <div class="post free-post-area w-100">
              <span class="post-title">자유 게시판</span>

		 	<c:forEach var="freePostDto" items="${freePostList}">
				<div class="free-post flex-box ms-20">
                  <div style="color: red;">HOT</div>
                  <div class="post-comment-title">${freePostDto.postTitle}</div>
                  <div class="post-comment-content">${freePostDto.postContent}</div>
                  <div class="reply">${freePostDto.commentNo}</div>
                  <div class="like">
                    <i class="fa-solid fa-heart red"></i>${freePostDto.postLike}
                  </div>
                </div>
			</c:forEach> 
    
            </div>
	
            <div class="post mbti-post w-100 ms-20">
              <div class="post free-post-area">
                <span class="post-title">MBTI 게시판</span>

 			<c:forEach var="mbtiPostDto" items="${mbtiPostList}">
				<div class="free-post flex-box ms-20">
                  <div style="color: red;">HOT</div>
                  <div class="post-comment-title">${mbtiPostDto.postTitle}</div>
                  <div class="post-comment-content">${mbtiPostDto.postContent}</div>
                  <div class="reply">${mbtiPostDto.commentNo}</div>
                  <div class="like">
                    <i class="fa-solid fa-heart red"></i>${mbtiPostDto.postLike}
                  </div>
                </div>
			</c:forEach> 
               


              </div>


            </div>
          </div>
        </div>


        <div class="end-area">
          <!-- 로그인 영역-->
          <div class="cell login-area">
            <div class="cell login-box center">
              <a href="/member/login">
              <div class="cell login-bar">
                <span> 로그인 </span>
              </div>
              </a>
              <div class="cell">
                <a href="member/findMemberId">아이디</a>
                <span>/</span>
                <a href="member/findMemberPw">비밀번호 찾기</a>
                <a href="member/join">회원가입</a>
              </div>
            </div>
          </div>

          <!-- 광고2 영역 -->

          <div class="cell ad-banner">
            <img src="/image/home/banner-img1.png" class="ad-banner-img">
          </div>

          <!--음원 순위 영역-->
          <div class="cell mt-30 music-area border-area">
            <div class="flex-box rank-title">
              <div>
                <span class="music-post">muzic 차트</span>
              </div>
              <div class="flex-fill"></div>
              <div>
                <span class="music-post">2025년 10월 26일 기준</span>
              </div>
            </div>
            <div class="mt-10 rank-area">
              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">1</span>
                <span class="ranking-update">- 0</span>
                <img class="album-img" src="/image/home/album-img3.jpg">
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">2</span>
                <span class="ranking-update">- 0</span>
                <img class="album-img" src="/image/home/album-img3.jpg">
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">3</span>
                <span class="ranking-update">- 0</span>
                <img class="album-img" src="/image/home/album-img3.jpg">
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">4</span>
                <span class="ranking-update">- 0</span>
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">5</span>
                <span class="ranking-update">- 0</span>
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">6</span>
                <span class="ranking-update">- 0</span>
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">7</span>
                <span class="ranking-update">- 0</span>
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">8</span>
                <span class="ranking-update">- 0</span>
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">9</span>
                <span class="ranking-update">- 0</span>
                <div class="flex-fill">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>

              <div class="flex-box flex-center music-rank">
                <span class="ranking-text">10</span>
                <span class="ranking-update" style="margin-left: -7px;">- 0</span>
                <div class="flex-fill" style="margin-left: -2px;">
                  <div><span class="album-title">앨범명</span></div>
                  <div><span class="album-artist">아티스트명</span></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <hr style="margin: 30px;">

    <div class="footer">
      <div class="flex-box flex-center">
        <div>
          <img src="/image/home/logo.png" class="logo">
          <pre>  (주)무직 대표이사 : 이민수 사업자등록번호 : 107-81-20541
  서울특별시 강남구 테헤란로14길 6 남도빌딩 4층, 무직 강남 KH정보교육원 강남지점 1관(역삼동) 사업자정보확인 >
  통신판매업 신고 : 제 2025-서울강남-1004호
  개인정보보호 책임자 : 한상혁 이메일 : muzic@kh.co.kr
  COPYRIGHT (주)무직 All Rights Reserved.</pre>
        </div>
        <div class="flex-fill"></div>
        <div>
          이미지넣을까
        </div>
      </div>
    </div>
</body>

</html>

