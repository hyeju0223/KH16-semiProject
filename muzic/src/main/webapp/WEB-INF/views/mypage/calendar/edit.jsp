<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
     <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
    

 <!-- moment cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.30.1/locale/ko.min.js"></script>

    <!-- lightpicker cdn-->
    <link href="https://cdn.jsdelivr.net/npm/lightpick@1.6.2/css/lightpick.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/lightpick@1.6.2/lightpick.min.js"></script>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script type="text/javascript">

        $(function () {

            var picker = new Lightpick({
                field: document.querySelector(".calendar"),
                format : 'YYYY-MM-DD',
                onSelect: function (date) {
                    document.getElementById('result-1').innerHTML = date.format('Do MMMM YYYY');
                }
            });
        });

    </script>
</head>

<body>
    <div class="container w-800">
        <div class="cell center">
            <h1>일정 수정하기</h1>
        </div>
        <form action="edit" method="post">
        	<input type="hidden" name="calendarNo" value="${calendarDto.calendarNo}" >
            일정<input type="text" class="calendar" name="calendarDay" value="${calendarDto.calendarDay}"/><br>
            <input type="hidden" name="calendarMember" value="${sessionScope.loginMemberId}">
            제목<input type="text" name="calendarScheduleTitle" value="${calendarDto.calendarScheduleTitle}"><br>
            내용<input type="text" name="calendarScheduleContent" value="${calendarDto.calendarScheduleContent}"> <br>
            <button type="submit">등록</button>
        </form>
    </div>
</body>

</html>

 <jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
