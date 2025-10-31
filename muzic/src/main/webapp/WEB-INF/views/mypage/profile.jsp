<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" type="text/css" href="/css/commons.css">
    <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
    <style>
        body {
            color: #2d3436;
            font-size: 18px;
            display: flex;
            justify-content: center;
            /* 가로 중앙 */
            align-items: center;
            /* 세로 중앙 */
            margin: 0;
        }

        .area {
            gap: 10px;
            padding: 10px;
            border: 1px solid #dfe6e9;
            border-radius: 10px;
        }

        .edit-area {
            gap: 20px;

        }

        .profile-img-area {

            margin: 0% auto;
            width: 200px;
            height: 200px;
            border-radius: 50%;
            overflow: visible;
            position: relative;
        }


        .profile-img {
            width: 100%;
            height: 100%;
            border-radius: 50%;
            object-fit: cover;
            object-position: center;
            position: relative;
            z-index: 0;
        }


        .profile-change {
            width: 50px;
            height: 50px;
            background-color: #636e72;
            border-radius: 50%;
            position: absolute;
            left: 140px;
            top: 140px;
            z-index: 10;
            border: 1px solid #ffffff;
        }


        .profile-change>i {
            font-size: 28px;

        }

        .change-text {
            font-size: 30px;
            position: absolute;
            left: 45px;
            top: 80px;
            opacity: 0;
        }

        .banner-area {
            width: 300px;
        }

        .banner {
            max-width: 100%;
            height: 200px;
            border-radius: 10px;


        }

        .banner-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            object-position: center;
        }
        .text {
            color: #636e72;
            font-size: 20px;
            margin-top: 10px;
        }

        .title {
            display: flex;
            font-size: 30px;
            color: #2d3436;
                        font-weight: 500;
        }
        .title1 {
            display: flex;
            font-size: 22px;
            color: #2d3436;
                        font-weight: 500;
        }


        .title .fa-star-of-life {
            font-size: 10px;
        }

        .fa-star-of-life.hide {
            display: none;
        }

        .box {
            outline: none;
            /* 기본 강조효과 제거*/
            padding: 0.5em 0.75em;
            border: 1px solid #b2bec3;
            /* background-color: #edeeff             */
        }

        .box.top {
            border-top-left-radius: 0.3em;
            border-top-right-radius: 0.3em;
            margin-top: -1px;
            background-color: #dfe6e9;
            color: #636e72;
        }
        .box.bottom {
            border-bottom-left-radius: 0.3em;
            border-bottom-right-radius: 0.3em;
            margin-top: -1px;
            background-color: #dfe6e9; color: #636e72;
        }

        .box.box-long {
            width: 97%;
        }

        .box:focus {
            border-color: #9396D5;
        }

        textarea.box {
            resize: vertical;
            
        }

        a:hover {
            color: rgb(58, 58, 58);
        }
        .box.bottom:hover{
            filter: brightness(0.9);
        }
        
         .table {
           border: 1px solid white;
            border-collapse: collapse;
            
        }
        .table > thead > tr > th,
        .table > thead > tr > td,
        .table > tbody > tr > th,
        .table > tbody > tr > td,
        .table > tfoot > tr > th,
        .table > tfoot > tr > td {
            /* border: 1px solid rgb(173, 173, 173); */
            font-size: inherit; /* 가장 가까운 외부영역의 규칙을 따라라! (지금은 table) */
            font-weight: inherit; /*가장 가까운 외부영역의 규칙을 따라라! */
            text-align: center;
            padding: 0.25em;
        }

        /* 확장 스타일 1번 - 테두리가 있는 테이블 */
        .table.table-border,
        .table.table-border > thead > tr > td,
        .table.table-border > thead > tr > th,
        .table.table-border > tbody > tr > th,
        .table.table-border > tbody > tr > td,
        .table.table-border > tfoot > tr > th,
        .table.table-border > tfoot > tr > td
         {
             border: 1px solid rgb(112, 112, 112);
        }

        /* 확장 스타일 2번 - 마우스에 반응하는 테이블 */
        .table.table-hover > tbody > tr:hover 
         {
            background-color: #fab1a0 !important;
            color: white;
        }

        /*
         확장 스타일 3번 - 줄무늬 테이블 
         - nth-child는 순서와 패턴을 지정할 때 사용
         - n이라는 글자를 쓰면 n=1,2,3,4,5,... 로 변하며 적용
         */
         .table.table-striped > thead > tr{
                        background-color: #dfe6e9;
            color: #636e72;
         }
        .table.table-striped > tbody > tr:nth-child(2n) {
            background-color: #9396D5;
        }

        
    </style>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script type="text/javascript">
    </script>
</head>

<body>
    <div class="container w-800 ">

            <div class="cell area flex-box mt-50">
                <!-- 왼쪽 프로필 영역-->
                <div class="cell  profile-area">
                    <!-- 프로필 이미지 영역-->
                    <div class="cell  profile-img-area ms-20">
                        <img src="/image/home/profile-img.jpg" class="profile-img">
                    </div>
                </div>
                <!-- 오른쪽 회원정보 -->
                <div class="w-600 ms-10" style="padding: 20px;">
                    <div class="title">${memberDto.memberNickname} (${memberDto.memberMbti})</div>
                    <hr>
                    <div class="text">이름 | ${memberDto.memberName}</div>
                    <div class="text">이메일 | ${memberDto.memberEmail}</div>
                    <div class="text">전화번호 | ${memberDto.memberContact}</div>
                </div>
            </div>
            <div class="mt-50">

                <a href="edit"><div class="box top">
                    <span>프로필 수정</span>
                </div>
                </a>
                <a href="password"><div class="box bottom">
                    <span>비밀번호 변경</span>
                </div>
                </a>
            </div>
              <a href="calendar/"><div class="box bottom">
                    <span>나의 일정</span>
                </div>
                </a>
            <div class="cell flex-box">
                <div class="calendar"></div>
            </div>
            <div class="cell flex-box">
                <div class="calendar"></div>
            </div>

            <div class="cell">
                <div class="cell title1 mt-30">
                <i class="fa-solid fa-music green" style="padding-top: 6px;"></i>음원 리스트
                </div>
            </div>
            <div>
            <table class="table table-border table-striped w-100 mt-30 center">
	<thead>
	<tr>
		<th>음원 제목</th>
		<th>가수명</th>
		<th>앨범</th>
		<th>재생수</th>
		<th>좋아요</th>
		<th>등록시간</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach var="musicDto" items="${musicList}">
	<tr>
		<td>${musicDto.musicTitle}</td>
		<td>${musicDto.musicArtist}</td>
		<td>${musicDto.musicAlbum}</td>
		<td>${musicDto.musicPlay}</td>
		<td>${musicDto.musicLike}</td>
		<td>
		<fmt:formatDate value="${musicDto.musicUtime}" pattern="yyyy-MM-dd (E) hh:ss"/>
		</td>
	</tr>
	</c:forEach>
	</tbody>
</table>

<h4>주문 리스트</h4>
<table border="1">
	<thead>
		<th>상품이미지</th>
		<th>상품명</th>
		<th>구매수량</th>
		<th>구매포인트</th>
		<th>주문시간</th>
	</thead>
	<tbody>
	<c:forEach var="goodsOrderDto" items="${orderList}">
	<tr>
		<td><img src="/store/image?goodsNo=${goodsOrderDto.orderGoods}" style="width: 100px; height: 100px; object-fit: cover;"></td>
		<td><a href="/store/detail?goodsNo=${goodsOrderDto.orderGoods }">${goodsOrderDto.goodsName}</a></td>
		<td>${goodsOrderDto.orderQuantity}</td>
		<td>${goodsOrderDto.orderPoint}</td>
		<td>${goodsOrderDto.orderTime}</td>
		</tr>
		</c:forEach>
	</tbody>

</table>

            </div>
            <div class="cell">
                <div class="cell title1 mt-30">
                <i class="fa-solid fa-coins yellow" style="padding-top: 6px; "></i>포인트 내역
                </div>
            </div>
            <div>
            	<table class="table table-border table-striped w-100 mt-30 center">
            	<thead>
            	<tr>
            	<th style="width: 40%">일자</th>
            	<th>포인트</th>
            	<th>내역</th>
            	</tr>
            	</thead>
            	<tbody>
            	<c:forEach var="pointLogDto" items="${pointLogList}">
            	<tr>
            	<td>
            	<fmt:formatDate value="${pointLogDto.pointLogTime}" pattern="yyyy-MM-dd (E) hh:ss"/>
            	</td>
            	<td>${pointLogDto.pointLogChange}</td>
            	<td >${pointLogDto.pointLogReason}</td>
            	</tr>
            	</c:forEach>
            	</tbody>
            	</table>
            </div>

            <div class="cell mt-30 right">
                <a href="withDraw" class="red">탈퇴하기<i class="fa-solid fa-chevron-right"></i></a>
            </div>

    </div>


</body>

</html>