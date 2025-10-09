<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
            gap: 20px;
        }

        .container{
            padding-left: 140px;
            padding : 100px;
            position: relative;
        }


        .profile-area {
            /* flex: 1; */
            border-radius: 10px;
            border: 1px solid #dfe6e9;
            padding: 20px;

        }

        .edit-area {
            gap: 20px;

        }

        .img {
            position: absolute;
            left: 21px;
            top: 116px;
            z-index: 2;
            width: 19%;
        }
        
        .img1 {
            position: absolute;
            left: 19px;
            top: 112px;
            z-index: 1;
            background-color : #9396D5;
            width: 106px;
            height : 118px;
            border-radius : 8px 

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

        .title {
            margin-top: 15px;
            display: flex;
            margin-left: 5px;
        }

        .text {

            font-size: 20px;
            font-weight: 500;
            padding: 6px;

        }

        .box {
            outline: none;
            /* 기본 강조효과 제거*/
            font-size: 16px;
            padding: 0.5em 0.75em;
            border: 1px solid #b2bec3;
            border-radius: 0.3em;
            margin: 5px;
            width: 98%;
            /* background-color: #edeeff             */
        }

        


        .box:focus {
            border-color: #9396D5;
        }

        textarea.box {
            resize: vertical;
        }

        .btn.btn-neutral {
            margin-left: 5px;
        }

        .red {
            color: red !important;
        }

        .flex-box.right {
            margin-left: 61%;
            gap: 10px;
        }

        ;
    </style>

 <!-- moment cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.30.1/locale/ko.min.js"></script>

    <!-- lightpicker cdn-->
    <link href="https://cdn.jsdelivr.net/npm/lightpick@1.6.2/css/lightpick.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/lightpick@1.6.2/lightpick.min.js"></script>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <script type="text/javascript">


    </script>
</head>

<body>
    <div class="container">
        <div>
            <img src="/image/calendar/calendar_img4.png" class="img">
            <div class="img1"></div>
        </div>
        <div class="mt-50 ms-50 text" style="font-size: 30px;">일정 상세보기</div>
          <form action="add" method="post">
            <div class="area flex-box ">
                <div class="w-500">
                    <hr style=" border: 1px solid #9396D5;">
				<div class="title mt-30" style="font-size: 24px">${calendarDto.calendarScheduleTitle}</div>
                <div class="title">${calendarDto.calendarDay}</div>
                <div class="title gray" style="font-size: 16px;">
                            <pre>작은 일도 일정으로 남겨두세요.
내일의 준비는 오늘의 기록에서 시작됩니다.</pre>
                        </div>
<div class="title">
                    <fmt:formatDate value="${calendarDto.calendarWtime}" pattern="yyyy년 MM dd일 E HH:MM" />
                </div>
                
                <c:if test="${calendarDto.calendarEtime != null}">
                    <div class="title">
                        <fmt:formatDate value="${calendarDto.calendarEtime}" pattern="yyyy년 MM dd일 E HH:MM" />(수정됨)
                    </div>
                </c:if>

                <div class="cell">
                    <label class="ms-10"></label><br>
                    <textarea type="text" name="calendarScheduleContent" class="box" style="height: 300px;" readonly="readonly">
${calendarDto.calendarScheduleContent}
                            </textarea>
                </div>
                <div class="flex-box right">

                    <div class="right mt-20">
                        <a href="/mypage/calendar/" class="btn btn-neutral">취소</a>
                    </div>
                      <div class="right mt-20">
                        <a href="edit?calendarNo=${calendarDto.calendarNo}" class="btn btn-positive">수정</a>
                    </div>
                    <div class="right mt-20">
                        <a href="delete?calendarNo=${calendarDto.calendarNo}" class="btn btn-negative">삭제</a>
                    </div>
                </div>
    </div>





</body>

</html>