<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


 <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>

<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/commons.css">
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
            background-color: white ;
        }
        
        /* 추가 */
        
                .btn.btn-positive {
            background-color: #413008;
            border: #413008;
        }

        a:hover {
            color: #2d3436;
            filter: brightness(0.9);

        }

        div {
            box-shadow: 0 0 0px gray;
        }

        .week {
            background-color: rgb(255, 255, 255);
            height: 50px;
            font-size: 24px;
            font-weight: 500;

        }

        .day {
            height: 40px;
            font-size: 20px;
            cursor: pointer;
        }

        .day-text {
            width: 40px;
            height: 40px;

            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;

        }

        .week,
        .day {
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .schedule {
            height: 120px;
            position: relative;
            background-color: rgb(255, 255, 255);
        }

        .progressbar {
            width: 800px;
            height: 30px;
            border: 2.5px solid #413008;
            border-radius: 10px;
            overflow: hidden;
        }

        .gauge {
            height: 100%;
            border-radius: 10px 0 0 10px;
            background-color: #fdcb6e;
            transition: width 0.4s ease-out;
        }

        .check-img {
            width: 90px;
            position: absolute;
            /*이미지 겹치게*/
            transform: translate(-55%, 15%);
            opacity: 30%;
            pointer-events: none;
        }

        .title {
            margin: 5px;
            display: block;
            text-align: left;
            padding-left: 5px;
            font-weight: 500;
            color: #2d3436;
            border-radius: 10px;

        }

        .area-center {
            max-width: 1200px;
            margin: 0% auto;
        }

        .calendar-area {
            padding-bottom: 100px;
        }

        .calendar {
            background-color: rgb(254, 255, 234);
            box-shadow: 0 4px 18px #a3a3a3;
        }

        #calendar-date {
            font-size: 50px;
        }

        .calendar-bottom {
            background-color: #00b894;
            padding: 50px;
        }

        .giftday-area {
            max-width: 800px;
            margin: 0% auto;

        }

        .gift-box {
            width: 20%;
            height: 200px;
            border: 1px solid #413008;
            border-radius: 10px;
            background-color: #fffff9ef;
            padding: 1%;
        }

        .text-point {
            color: #0984e3;
            font-size: 28px;
            font-weight: 500;
            padding: 2px;
        }

        .gift-btn {
            margin-top: 8px;
            width: 90%;
            border-radius: 20px;
            background-color: #0984e3;
            border: #0984e3;
            color: aliceblue;
        }

        .text-pointbox {
            background-color: #413008;
            color: aliceblue;
            border: 1px solid #413008;
            border-radius: 50px;
            width: 50px;
            height: 35px;
            margin: 0% auto;
        }

        .day-text.c {
            background: #6d483d;
            color: #fff;
            padding: 10px;
            border-radius: 50%;
            width: 30px;
            height: 30px;
        }

        
    </style>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script type="text/javascript">
    
    $(function () {

        var right = $("#cal-right");

        // //jajx에서 가지고 온 데이터
        var calendarData = [];
        console.log(calendarData);
        var memberId = "${sessionScope.loginMemberId}";

        var date = new Date(); //현재 날짜 가져오기
        var year = date.getFullYear(); //현재 년도
        var month = date.getMonth() + 1;  //현재 월 (10월인데 js입장에선 9월, 그래서 +1 해야함)
        var day = date.getDate();

        showCalendar();
        render();

        function showCalendar() {

            $(".schedule").empty().removeClass("addCalendar");
            $(".day").empty().removeClass("currentMonthDay addCalendar");

            //요일 구하는 방법
            var firstDay = new Date(year, month - 1, 1); //이번 달 1일 (month는 10, 근데 js입장에서는 11로 생각해서 -1)
            var lastDay = new Date(year, month, 0).getDate(); //다음 달의 0번째 날 = 이번 달 마지막 날 (month는 10, 근데 is입장에서는 11로 생각)
            var week = firstDay.getDay(); //요일 번호 - 일(0), 월(1), 화(2), 수(3), 목(4), 금(5), 토(6), 일(7)


            var lastMonthDay = new Date(year, month - 1, 0).getDate(); //지난 달 마지막 날 (10월, js입장에서는 11월. -1해야함)
            //1일이 시작되는 칸 전까지 이전 달 날짜 찍기
            for (var i = 1; i <= week; i++) { //0
                var index = i - 1;
                $(".day").eq(index).html("<div class=day-text>" + (lastMonthDay - week + i) + "</div>");
                dateToChar(year, month - 1, lastMonthDay - week + i, index);
            }

            //1일부터 날짜 찍기
            for (var i = 1; i <= lastDay; i++) {
                var index = i + week - 1;
                $(".day").eq(index).html("<div class=day-text>" + i + "</div>").addClass("currentMonthDay");

                dateToChar(year, month, i, index);

            }

            //마지막 날 이후 빈 칸 찍기
            var filled = week + lastDay;

            for (var i = 1; i <= 42 - filled; i++) {
                var index = filled + i - 1
                $(".day").eq(index).html("<div class=day-text>" + i + "</div>");
                dateToChar(year, month + 1, i, index);
            }

            //상단에 날짜 추가
            $("#calendar-date").text(year + "년" + (month) + "월");

        }

        // //'YYYY-MM-DD' 형식의 날짜 문자열 만들기
        function dateToChar(year, month, i, index) {
            var y = String(year);
            var m = String(month).padStart(2, '0'); //문자열 길이를 맞춤(전체 길이, 비어있으면 '0' 추가)
            var d = String(i).padStart(2, '0');
            var charDate = y + "-" + m + "-" + d;

            $(".day").eq(index).attr("data-date", charDate);
            $(".schedule").eq(index).attr("data-date", charDate);
            // console.log(charDate);
        };
        function dateToChar2(year, month, day) {
            var y = String(year);
            var m = String(month).padStart(2, '0'); //문자열 길이를 맞춤(전체 길이, 비어있으면 '0' 추가)
            var d = String(day).padStart(2, '0'); //문자열 길이를 맞춤(전체 길이, 비어있으면 '0' 추가)
            return String(y + "-" + m + "-" + d);
        };

        //이전&다음 버튼 기능 구현
        $("#prevMonth-btn").on("click", function () {

            date.setMonth(date.getMonth() - 1); //이전달로 설정

            year = date.getFullYear();
            month = date.getMonth() + 1;

            // console.log(year, month);
            // if(prevDate.set) month에 따라 year 다르게 구현하기

            showCalendar();
            render();
        });

        $("#nextMonth-btn").on("click", function () {
            date.setMonth(date.getMonth() + 1); //다음달로 설정

            year = date.getFullYear();
            month = date.getMonth() + 1;
            // console.log(year, month);
            // if(prevDate.set) month에 따라 year 다르게 구현하기

            showCalendar();
            render();
        });

        //ajax로 일정 및 출석 여부 가지고 오기


        //1. 일정 구현

        function render() {
            $.ajax({
                url: "/rest/mypage/calendar/home",
                method: "post",
                data: { memberId },
                success: function (response) { //calendarDay, title, content, attendane 가지고 오기
                    calendarData = response;
                    renderData();
                    renderData2();
                }
            });
        }

        function renderData() {
            calendarData.forEach(function (calendar) { //each => jQuery 반복명령
                //DB에 있는 날짜 문자열 가져오기
                var calendarDay = calendar.calendarDay;

                //출석 여부 확인하기
                if (calendar.calendarAttendance == 'Y') {
                    //출석 체크 대상이 되는 날짜 선택하기
                    var targetDay = $(".schedule[data-date=" + calendarDay + "]"); //여러 데이터 읽기
                    targetDay.addClass("addCalendar").append('<img src="${pageContext.request.contextPath}/image/calendar/calendar-check.png" class="check-img">');
                }

                var maxSchedule = 3;

                if (calendar.calendarScheduleTitle != "출석체크") {
                    var targetSchedule = $(".schedule[data-date=" + calendarDay + "]");

                }
            });

        }



        function renderData2() {
            calendarData.forEach(function (calendar) { //each => jQuery 반복명령
                //DB에 있는 날짜 문자열 가져오기
                var calendarDay = calendar.calendarDay;

                if (calendar.calendarScheduleTitle != "출석체크") {
                    var targetSchedule = $(".day[data-date=" + calendarDay + "]"); //일정이 있는 날짜 데이터들 가져오기

                    targetSchedule.find(".day-text").addClass("c"); //클래스 추가하기

                }
            });

        }


    });
    </script>
</head>

<body>
    <div class="container w-1200 ">

            <div class="cell area flex-box mt-50">
                <!-- 왼쪽 프로필 영역-->
                <div class="cell  profile-area">
                    <!-- 프로필 이미지 영역-->
                    <div class="cell  profile-img-area ms-20">
                        <img src="${pageContext.request.contextPath}/mypage/image?memberId=${memberDto.memberId}" class="profile-img">
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
                <div class="cell">
                <div class="cell title1 mt-30">
                <a href="calendar/"><i class="fa-solid fa-calendar orange"></i>나의 일정<i class="fa-solid fa-chevron-right"></i></a>
                </div>
                <!-- 달력구간 -->
                <div>
                <div class="flex-box flex-center" style="gap: 50px;">
            <div class="cell">
                <div class="calendar-area w-600">
                    <div class="center area-center">
                        <div class="cell flex-box flex-center">
                        </div>
                        <div class="cell calendar-title-area">
                            <div style="padding: 15px;">
                                <div style="font-size: 22px; font-weight: 600;" class="left"><i
                                        class="fa-solid fa-stamp red" style="font-size: 22px"></i>총 출석일수 : 1Day</div>
                            </div>

                        </div>
                        <!-- 달력 -->
                        <div class=" calendar cell">
                            <div class="flex-box flex-center">
                                <div class="w-100 week" style="color: #d63031;">일</div>
                                <div class="w-100 week">월</div>
                                <div class="w-100 week">화</div>
                                <div class="w-100 week">수</div>
                                <div class="w-100 week">목</div>
                                <div class="w-100 week">금</div>
                                <div class="w-100 week" style="color: #0984e3;">토</div>
                            </div>
                            <div class="flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                            <div class="flex-box flex-center">
                                <div class="w-100 schedule">일정</div>
                                <div class="w-100 schedule">일정</div>
                                <div class="w-100 schedule">일정</div>
                                <div class="w-100 schedule">일정</div>
                                <div class="w-100 schedule">일정</div>
                                <div class="w-100 schedule">일정</div>
                                <div class="w-100 schedule">일정</div>
                            </div>


                        </div>
                    </div>
                </div>
            </div>
            <div class="cell">
                <div class="calendar-area w-600">
                    <div class="center area-center">
                        <div class="cell flex-box flex-center">
                            <div class="left" id="calendar-date" style="font-size: 22px; font-weight: 600;">2025년 10월
                            </div>
                            <div class="flex-fill"></div>
                            <div class="left btn btn-positive"><a href="${pageContext.request.contextPath}/mypage/calendar/add">일정 등록하기</a></div>
                            <!-- <button class="btn btn-neutral" id="prevMonth-btn">이전</button>
                            <button class="btn btn-neutral" id="nextMonth-btn">다음</button> -->
                        </div>
                        <!-- 달력 -->
                        <div class=" calendar cell" id="cal-right">
                            <div class="flex-box flex-center">
                                <div class="w-100 week" style="color: #d63031;">일</div>
                                <div class="w-100 week">월</div>
                                <div class="w-100 week">화</div>
                                <div class="w-100 week">수</div>
                                <div class="w-100 week">목</div>
                                <div class="w-100 week">금</div>
                                <div class="w-100 week" style="color: #0984e3;">토</div>
                            </div>
                            <div class="flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                            <div class=" flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                            <div class=" flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                            <div class=" flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                            <div class=" flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                            <div class=" flex-box flex-center">
                                <div class="w-100 day">1</div>
                                <div class="w-100 day">2</div>
                                <div class="w-100 day">3</div>
                                <div class="w-100 day">4</div>
                                <div class="w-100 day">5</div>
                                <div class="w-100 day">6</div>
                                <div class="w-100 day">7</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </div>



    </div>

                  <!-- 달력구간 -->

            <div class="cell flex-box">
                <div class="calendar"></div>
            </div>
            <div class="cell flex-box">
                <div class="calendar"></div>
            </div>

            <div class="cell">
                <div class="cell title1">
                <i class="fa-solid fa-music green" style="padding-top: 6px;"></i>음원 목록
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

<div class="cell">
  <div class="cell title1 mt-30">
  <i class="fa-solid fa-gift blue" style="padding-top: 6px;"></i>주문 목록
</div>
<table  class="table table-border table-striped w-100 mt-30 center">
	<thead >
		<th>상품이미지</th>
		<th>상품명</th>
		<th>구매수량</th>
		<th>구매포인트</th>
		<th>주문시간</th>
	</thead>
	<tbody>
	<c:forEach var="goodsOrderDto" items="${orderList}">
	<tr>
		<td><img src="${pageContext.request.contextPath}/store/image?goodsNo=${goodsOrderDto.orderGoods}" style="width: 100px; height: 100px; object-fit: cover;"></td>
		<td><a href="${pageContext.request.contextPath}/store/detail?goodsNo=${goodsOrderDto.orderGoods }">${goodsOrderDto.goodsName}</a></td>
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
                <a href="withDraw" class="red" style="margin-bottom: 50px">탈퇴하기<i class="fa-solid fa-chevron-right"></i></a>
            </div>

    </div>


</body>

</html>

 <jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>