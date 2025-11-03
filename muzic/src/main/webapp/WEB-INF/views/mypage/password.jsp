<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
     <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>
    

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
            padding: 30px;

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

        .title {
            margin: 5px;
            display: flex;
        }

        .text {

            font-size: 20px;
            font-weight: 500;
            padding: 6px;

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
            font-size: 16px;
            padding: 0.5em 0.75em;
            border: 1px solid #b2bec3;
            border-radius: 0.3em;
            margin: 5px;
            /* background-color: #edeeff             */
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

        .btn.btn-positive {
            margin-right: 4px;
            width: 98%;
        }

        .fail1-feedback,
        .fail2-feedback,
        .fail3-feedback,
        .fail4-feedback {
            display: none;
            margin-left: 5px;
        }

        .fail.fail1-feedback,
        .fail.fail2-feedback,
        .fail.fail3-feedback,
        .fail.fail4-feedback {
            display: block;
            color: red;
        }

        .red {
            color: red !important;
        }
    </style>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>


    <script type="text/javascript">

        $(function () {

            var state = {
                memberPwValid: true,
                ok: function () {
                    return this.memberPwValid;
                }
            };

            //입력 중 내용 지우면 피드백 문구 삭제
            $(".box").on("input", function () {
                $(this).nextAll(".fail1-feedback").removeClass("fail");
                $(this).nextAll(".fail2-feedback").removeClass("fail");
                $(this).nextAll(".fail3-feedback").removeClass("fail");

                if ($(this).val().length == 0) {
                    $(this).nextAll(".fail4-feedback").removeClass("fail");
                }
            });

            $(".box").on("blur", function () {

                //아무것도 적지 않을 경우 비밀번호 입력 요청 문구 추가
                if ($(this).val().length == "") {
                    $(this).nextAll(".fail4-feedback").addClass("fail");
                    state.memberPwValid = false;
                    return;
                } else {
                    $(this).nextAll(".fail4-feedback").removeClass("fail");
                }

            });
            
            var memberId = "${sessionScope.loginMemberId}";

            //과거 오류 제거
            $("[name=memberPw]").on("input", function () {
                $(".fail1-feedback").removeClass("fail");
            });

            // [2]입력한 현재 비밀번호가 현재 비밀번호와 맞는지?
            $("[name=memberPw]").on("blur", function () {
            	var currentPw = $(this).val();
                $.ajax({
                    url: "/rest/mypage/currentPwCheck",
                    method: "post",
                    data: {
                        memberId: memberId,
                        currentPw : currentPw,
                    },
                    success: function (response) {
                        if (response == "unauthorized") {
                            alert("잘못된 접근입니다.");
                            return;
                        }
                        if (response == "fail") { //현재 비밀번호와 불일치
                            $(".fail1-feedback").addClass("fail");
                            state.memberPwValid = false;
                        }
                        if(response == "success") {
                        	$(".fail1-feedback").removeClass("fail");
                        }
                    }
                })
            });

            //새 비밀번호와 현재 비밀번호가 같은지
            $("[name=memberChangePw1]").on("blur", function () {
                var memberPw = $("[name=memberPw]").val();
                var memberChangePw1 = $(this).val();
                if (memberPw && memberChangePw1 && memberPw === memberChangePw1) {
                    $(".fail2-feedback").addClass("fail");
                } else {
                    $(".fail2-feedback").removeClass("fail");
                }

            });

            //새 비밀번호와 새 비밀번호 확인이 맞는지
            $("[name=memberChangePw2]").on("blur", function () {
                var memberChangePw1 = $("[name=memberChangePw1]").val();
                var memberChangePw2 = $(this).val();

                if (memberChangePw1 && memberChangePw2 && memberChangePw1 !== memberChangePw2) {
                    $(".fail3-feedback").addClass("fail");
                } else {
                    $(".fail3-feedback").removeClass("fail"); 
                    }
            });
        });

    </script>
</head>

<body>
    <div class="container ">
        <div class="mt-50 ms-30 text" style="font-size: 30px;">비밀번호 변경</div>
        <form method="post" action="/mypage/password" autocomplete="off">
            <div class="cell area flex-box ">
                <!-- 왼쪽 프로필 영역-->
                <div class="cell  profile-area">
                    <!-- 프로필 이미지 영역-->
                    <div class="cell  profile-img-area mt-20">
                        <img src="/mypage/image?memberId=${sessionScope.loginMemberId}" class="profile-img">

                    </div>
                    <!-- 닉네임/메일주소-->
                    <div class="center mt-30">
                        <div class="text" style="font-weight: bolder; font-size: 28px">${sessionScope.loginMemberId}</div>
                        <div class="text">${sessionScope.loginMemberMbti}</div>
                    </div>
                    <!-- 배너 영역-->
                    <div class="banner-area mt-50">
                        <a href="/mypage/calendar/"><img src="/image/home/banner-img1.png" class="banner-img"></a>
                    </div>
                </div>
                <!-- 오른쪽 영역 -->
                <div>
                    <div class="cell edit-area w-400">
                        <div class="title mt-30" style="font-size: 20px;">
                            안전한 비밀번호로 내정보를 보호하세요.
                        </div>
                        <div class="title gray mt-50" style="font-size: 16px;">
                            <pre>다른 사이트에서 사용한 적 없거나
이전에 사용한 적 없는 비밀번호가 안전합니다.</pre>
                        </div>
                        <div>
                            <!-- 현재 비밀번호 영역-->
                            <div class="cell">
                                <div class="title">현재 비밀번호<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="password" name="memberPw" class="box box-long" placeholder="영문+숫자+특수문자 8~16자">
                                <div class="fail1-feedback">현재 비밀번호와 일치하지 않습니다</div>
                                <div class="fail4-feedback">비밀번호를 입력해주세요</div>
                                

                            </div>
                            <!-- 새 비밀번호 영역-->
                            <div class="cell">
                                <div class="title">새 비밀번호<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="password" name="memberChangePw1" class="box box-long" placeholder="영문+숫자+특수문자 8~16자">
                                <div class="fail2-feedback">현재 비밀번호와 동일합니다</div>
                                <div class="fail4-feedback">비밀번호를 입력해주세요</div>

                            </div>
                            <!-- 새 비밀번호 확인 영역-->
                            <div class="cell">
                                <div class="title">새 비밀번호 확인<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="password" name="memberChangePw2" class="box box-long" placeholder="영문+숫자+특수문자 8~16자">
                                <div class="fail3-feedback">새 비밀번호와 일치하지 않습니다</div>
                                <div class="fail4-feedback">비밀번호를 입력해주세요</div>
                            </div>

                        </div>

                    </div>
                    <div class="cell edit-area">
                        <div class="center right mt-20">
                            <button type="submit" class="btn btn-positive ">수정</button>
                        </div>
                    </div>
                </div>
            </div>
        </form>

    </div>


</body>

</html>

 <jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
