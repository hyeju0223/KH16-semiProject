$(function () {

    // //jajx에서 가지고 온 데이터
    var calendarData = [];
    var memberId = "testuser2";

    var date = new Date(); //현재 날짜 가져오기
    var year = date.getFullYear(); //현재 년도
    var month = date.getMonth() + 1;  //현재 월 (10월인데 js입장에선 9월, 그래서 +1 해야함)
    var day = date.getDate();

    showCalendar();
    checkAttendance();
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
            url: "http://localhost:8080/mypage/calendar/home",
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
                targetDay.addClass("addCalendar").append('<img src="./image/calendar-check.png" class="check-img">');
            }

            var maxSchedule = 3;

            if (calendar.calendarScheduleTitle != "출석체크") {
                var targetSchedule = $(".schedule[data-date=" + calendarDay + "]");

                if (targetSchedule.find("div").length < maxSchedule) { //div가 최대 타이틀 개수 미만일 경우 일정을 추가하는 코드 추가
                    var title = String(calendar.calendarScheduleTitle).trim();
                    if (title.length > 8) {
                        title = title.substring(0, 7) + "…";
                    }
                    targetSchedule.append('<div><a href="#" class="title">' + title + '</a></div>'); //일정 누르면 detail로 넘어가도록 구현
                }
            }
        });

        rendarGaugebar();
    }

    //게이지바 구현
    function rendarGaugebar() {
        //출석 현황 ( 출석 일수 / 총 일수 )
        var totalDay = $(".currentMonthDay").length;
        var checkDay = $(".addCalendar").length;
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
                url: "http://localhost:8080/mypage/calendar/check",
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
                            targetDay.addClass("addCalendar").append("<img src='./image/calendar-check.png' class='check-img'>");
                            rendarGaugebar(); //게이지바 갱신
                        }
                    }
                }

            })
        });

    };


});