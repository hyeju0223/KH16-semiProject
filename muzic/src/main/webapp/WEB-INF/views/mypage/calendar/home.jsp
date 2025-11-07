<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
     <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
    

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/commons.css">
    <link rel="stylesheet" type="text/css"
        href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
     <style>
        * {
            color: #413008;
            font-size: 18px;
            gap: 5px;
        }

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
            background-color: rgb(254, 255, 234);
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
            width: 120px;
            position: absolute;
            /*이미지 겹치게*/
            transform: translate(-50%, 0%);
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
            background-color: #fdcb6e;
            border-radius: 10px;

        }

        .area-center {
            max-width: 1200px;
            margin: 0% auto;
        }

        .calendar-area {
            background-color: #ffeaa7;
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

        .prevDay{
            border-top-left-radius: 10px;
            border-top-right-radius: 10px;
            filter: brightness(0.95);
        }

        .prevSchedule {
            border-bottom-left-radius: 10px;
            border-bottom-right-radius: 10px;
            filter: brightness(0.95);
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
    </style>
    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script type="text/javascript">

        $(function () {

            // //jajx에서 가지고 온 데이터
            var calendarData = [];
            var memberId = "${memberDto.memberId}";
            var date = new Date(); //현재 날짜 가져오기
            var year = date.getFullYear(); //현재 년도
            var month = date.getMonth() + 1;  //현재 월 (10월인데 js입장에선 9월, 그래서 +1 해야함)
            var day = date.getDate();

            showCalendar();
            checkAttendance();
            render();

            function showCalendar() {

            	 $(".schedule").empty().removeClass("addCalendar prevSchedule");
                 $(".day").empty().removeClass("currentMonthDay addCalendar prevDay");

                //요일 구하는 방법
                var firstDay = new Date(year, month - 1, 1); //이번 달 1일 (month는 10, 근데 js입장에서는 11로 생각해서 -1)
                var lastDay = new Date(year, month, 0).getDate(); //다음 달의 0번째 날 = 이번 달 마지막 날 (month는 10, 근데 is입장에서는 11로 생각)
                var week = firstDay.getDay(); //요일 번호 - 일(0), 월(1), 화(2), 수(3), 목(4), 금(5), 토(6), 일(7)


                var lastMonthDay = new Date(year, month - 1, 0).getDate(); //지난 달 마지막 날 (10월, js입장에서는 11월. -1해야함)
                //1일이 시작되는 칸 전까지 이전 달 날짜 찍기
                for (var i = 1; i <= week; i++) { //0
                    var index = i - 1;
                    $(".day").eq(index).html("<div class=day-text>" + (lastMonthDay - week + i) + "</div>").addClass("prevDay");
                    $(".schedule").eq(index).addClass("prevSchedule");
                    dateToChar(year, month - 1, lastMonthDay - week + i, index);
                }

                //1일부터 날짜 찍기
                for (var i = 1; i <= lastDay; i++) {
                    var index = i + week - 1;
                    $(".day").eq(index).html("<div class=day-text>" + i + "</div>").addClass("currentMonthDay");


                    dateToChar(year, month, i, index);
                    // //'YYYY-MM-DD' 형식의 날짜 문자열 만들기
                    // var y = String(year);
                    // var m = String(month).padStart(2, '0'); //문자열 길이를 맞춤(전체 길이, 비어있으면 '0' 추가)
                    // var d = String(i).padStart(2, '0');
                    // var charDate = y + "-" + m + "-" + d;
                    // console.log(charDate);


                    // //.day와 schedule 칸에 data-date 속성 부여
                    // $(".day").eq(index).attr("data-date", charDate);
                    // $(".schedule").eq(index).attr("data-date", charDate);
                }

                //마지막 날 이후 빈 칸 찍기
                var filled = week + lastDay;

                for (var i = 1; i <= 42 - filled; i++) {
                    var index = filled + i - 1
                    $(".day").eq(index).html("<div class=day-text>" + i + "</div>").addClass("prevDay");
                    $(".schedule").eq(index).addClass("prevSchedule");
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
                        rendarGaugebar();
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

                        if (targetSchedule.find("div").length < maxSchedule) { //div가 최대 타이틀 개수 미만일 경우 일정을 추가하는 코드 추가
                            var title = String(calendar.calendarScheduleTitle).trim();
                            if (title.length > 8) {
                                title = title.substring(0, 7) + "…";
                            }
                            targetSchedule.append('<div><a href="detail?calendarNo=' + calendar.calendarNo+'" class="title">' + title + '</a></div>'); //일정 누르면 detail로 넘어가도록 구현
                        }
                    }
                });

                rendarGaugebar();
            }

            //게이지바 구현
            function rendarGaugebar() {
                //출석 현황 ( 출석 일수 / 총 일수 )
                var totalDay = $(".currentMonthDay").length;
                var checkDay = $(".schedule.addCalendar").not(".prevSchedule").length; //이전 일수는 빼고 계산
                var parcent = parseInt(checkDay / totalDay * 100);


                $(".progressbar > .gauge").css("width", parcent + "%");

                console.log(totalDay + "," + checkDay + "," + parcent);
            };

            //2. 출석체크 (day-text 누르면 출석이 'Y' 로 바뀜)
            function checkAttendance() {
                $(".calendar").on("click", ".day", function () {
                    var dayText = $(this).closest(".day").attr("data-date");
                    var targetDay = $(".schedule[data-date =" + dayText + "]");
                    var currentDay = dateToChar2(year, month, day);

                    if (currentDay !== dayText) {
                        alert("오늘 날짜가 아닙니다!");
                        return;
                    }

                    $.ajax({
                        url: "/rest/mypage/calendar/check",
                        method: "post",
                        data: {
                            calendarMember: memberId,
                            calendarDay: dayText
                        },
                        success: function (response) {
                            //오늘 날짜와 day의 data-date 날짜가 다르면 도장 못찍게 구현
                            if (response == "already") {
                                alert("이미 출석 완료되었습니다. 내일 다시 방문해주세요!");
                            }
                            if (response == "success") {
                                alert("출석 완료되었습니다!");
                                //같은 data-tate인 스케쥴에 출첵 이미지를 추가해야함
                                if (targetDay.find(".check-img").length === 0) { //출첵이미지가 없다면
                                    targetDay.addClass("addCalendar").append("<img src='/image/calendar/calendar-check.png' class='check-img'>");
                                    rendarGaugebar(); //게이지바 갱신
                                }
                            }
                        }

                    })
                });

            };

            $(".gift-btn").on("click", function(){
            	//var checkDay = $(".schedule.addCalendar").not(".prevSchedule").length;
                $.ajax({
                    url : "/rest/mypage/calendar/gift",
                    method : "post",
                    //data : {checkDay},
                    success : function(response){
                    	if(response == "success"){
                            alert("포인트 지급이 완료되었습니다!");
                    	}
                    	if(response == "already"){
                    		alert("포인트 지급은 한 달에 한 번만 가능합니다!");
                    	}
                    	if(response == "fail"){
                    		alert("총 출석일수가 부족합니다!");
                    	}

                    }
                })
            });


        });


    </script>
</head>

<body>
    <div class="container">
        <div class="calendar-area">
            <div class="cell center area-center">
                <div style="padding-top: 100px; color: #413008; font-size: 28px;">
                    매일매일 할수록 혜택이 쌓이는
                </div>
                <img src="${pageContext.request.contextPath}/image/calendar/calendar-img2.png" style="width: 50%; font-weight:bold;">
            </div>
            <div class="center area-center">
                <div class="cell flex-box flex-center">
                    <div class="left" id="calendar-date">2025년 10월</div>
                    <div class="flex-fill"></div>
                    <div class="left btn btn-positive"><a href="add">일정 등록하기</a></div>
                    <button class="btn btn-neutral" id="prevMonth-btn">이전</button>
                    <button class="btn btn-neutral" id="nextMonth-btn">다음</button>
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

                    <div class=" flex-box flex-center">
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
                    <div class=" flex-box flex-center">
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
                    <div class=" flex-box flex-center">
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
                    <div class=" flex-box flex-center">
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
                    <div class=" flex-box flex-center">
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
        <div class="calendar-bottom">
            <div class="cell area-center">
                <div class="center" style="font-size: 50px;">
                    <img src="${pageContext.request.contextPath}/image/calendar/calendar-img3.png" style="width: 50%; margin-top: 40px;">
                </div>
                <div class="cell center">
                    <span style="font-size: 24px;">게이지바</span>
                    <div class="cell flex-box flex-center">
                        <div class="cell center progressbar">
                            <div class="gauge"></div>
                        </div>
                    </div>
                </div>

                <div class="cell flex-box flex-center giftday-area">
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                    <div class="cell center w-100">
                        <div>|</div>
                    </div>
                </div>
                <div class="cell flex-box flex-center giftday-area">
                    <div class="cell center w-100 ">
                        <div>0일</div>
                    </div>
                    <div class="cell center w-100 ">
                        <div class="text-pointbox">5일</div>
                    </div>
                    <div class="cell center w-100 ">
                        <div>10일</div>
                    </div>
                    <div class="cell center w-100 ">
                        <div>15일</div>
                    </div>
                    <div class="cell center w-100 ">
                        <div class="text-pointbox">20일</div>
                    </div>
                    <div class="cell center w-100 ">
                        <div>25일</div>
                    </div>
                    <div class="cell center w-100 ">
                        <div class="text-pointbox">30일</div>
                    </div>
                </div>

                <!-- 5일 달성 시 : 상품1 / 15일 달성 시 : 상품2 / 30일 달성 시 : 상품3-->
                <div class="cell center flex-box flex-center">
                    <div class="cell gift-box" style="margin-left: 120px;">
                        <div style="font-weight: bolder;">총 5일 달성</div>
                        <hr style="width: 80%;">
                        <div class="text-point orange">100point</div>
                        <div class="text-point orange" style="font-size: 18px;">[전체 회원]</div>
                        <div class="btn gift-btn" style="background-color: #e17055;">포인트 받기</div>
                    </div>
                    <div class="cell gift-box" style="margin-left: 85px;">
                        <div style="font-weight: bolder;">총 20일 달성</div>
                        <hr style="width: 80%;">
                        <div class="text-point green">300point</div>
                        <div class="text-point green" style="font-size: 18px;">[전체 회원]</div>
                        <div class="btn gift-btn" style="background-color: #00b894;">포인트 받기</div>
                    </div>
                    <div class="cell gift-box">
                        <div style="font-weight: bolder;">총 30일 달성</div>
                        <hr style="width: 80%;">
                        <div class="text-point">1000point</div>
                        <div class="text-point" style="font-size: 18px;">[전체 회원]</div>
                        <div class="btn gift-btn ">포인트 받기</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    </div>


</body>

</html>

 <jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
