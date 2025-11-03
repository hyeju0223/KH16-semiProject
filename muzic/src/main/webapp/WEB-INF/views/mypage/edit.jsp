<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/fmt" %>

 <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>


<!DOCTYPE html>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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
            width: 180px;
            height: 180px;
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
        
         .profile-img-area:hover {
         	filter : brightness(0.9);
         }

        .profile-img-area:hover,
        .profile-img-area > .change-text:hover {
            transition: filter 0.3s ease-out;
            cursor: pointer;
        }

        .profile-change {
            width: 50px;
            height: 50px;
            background-color: #636e72;
            border-radius: 50%;
            position: absolute;
            left: 130px;
            top: 130px;
            z-index: 10;
            border: 1px solid #ffffff;
        }


        .profile-change>i {
            font-size: 28px;

        }

        .change-text {
            font-size: 30px;
            position: absolute;
            left: 38px;
            top: 73px;
            opacity: 0;
            user-select: none;  /* 텍스트 드래그 금지 */
  			cursor: default;  
  			z-index: 0;
        }

        .profile-img-area:hover .change-text {
            opacity: 1;
            color: #fff;
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

            font-size: 24px;
            font-weight: 500;
            padding: 6px;

        }
        
        .text-small {

            font-size: 18px;
            padding: 3px;
            color : #636e72;

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
        }

        .box.box-long {
            width: 98%;
        }

        .box:focus {
            border-color: #9396D5;
        }

        textarea.box {
            resize: vertical;
        }

        .btn.btn-positive {
            margin-right: 4px;
        }
    </style>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <!--카카오맵 cdn-->
    <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <!-- momentjs CDN-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.22.2/moment.min.js"></script>
    <!-- <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.30.1/locale/ko.min.js"></script> -->

    <!-- lightpicker cdn-->
    <link href="https://cdn.jsdelivr.net/npm/lightpick@1.6.2/css/lightpick.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/lightpick@1.6.2/lightpick.min.js"></script>

    <script type="text/javascript">

        $(function () {

            // 주소(만일 세 칸 중 하나라도 값을 입력할 경우 사용자가 모드 입력하도록 안내멘트와 함께 * 숨기기)

            // 주소 입력에 따라 별 보여주기, 단 별이 있고 없고를 먼저 체크해야함
            function updateAddressStars() {
                // [1] 값이 처음에 담겨져 있을 경우 * 표시
                var postcode = $('[name=memberPostcode]').val().trim();
                var postaddress1 = $('[name=memberAddress1]').val().trim();
                var postaddress2 = $('[name=memberAddress2]').val().trim();

                if (postcode === '' && postaddress1 === '' && postaddress2 === '') {
                    $('.post-title .fa-star-of-life').addClass("hide");
                }
                else if (postcode.length > 0 && postaddress1.length > 0 && postaddress2.length > 0) { //값이 다 지워졌을때 숨김 추가
                    $('.post-title .fa-star-of-life').addClass("hide");
                }
                else {
                    $('.post-title .fa-star-of-life').removeClass("hide");
                }

            }

            updateAddressStars();

            $(".post-text").on("input change", function () {
                updateAddressStars();

            });



            //생년월일 입력
            var picker = new Lightpick({
                field: document.querySelector('[name="memberBirth"]'),
                format: 'YYYY-MM-DD',
                onSelect: function (date) {
                    document.getElementById('result-1').innerHTML = date.format('Do MMMM YYYY');
                }
            });

            function findAddress() {
                //daum에서 제공하는 샘플 코드
                new daum.Postcode({
                    oncomplete: function (data) {
                        // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                        // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                        // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                        var addr = ''; // 주소 변수

                        //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                        if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                            addr = data.roadAddress;
                        } else { // 사용자가 지번 주소를 선택했을 경우(J)
                            addr = data.jibunAddress;
                        }
                        $("[name=memberPostcode]").val(data.zonecode);
                        $("[name=memberAddress1]").val(addr);
                        $("[name=memberAddress2]").trigger("focus");
                        updateAddressStars();
                    }
                }).open();

            }
            $(".btn-address-search, [name=memberPost], [name=memberAddress1]").on("click", findAddress);

            
            
            //프로필 사진 변경
            
            $(".profile-img-area").on("click", function(){
            	$("#profile-input").click();
            	
            });
            	
            
//             이미지의 최초 주소 ==> 미리보기 사진이 바뀌는거고 수정 눌렀을때 진짜 사진이 바뀌는거 
            var origin = $(".profile-img").attr("src");
            var memberId = "${sessionScope.loginMemberId}"
            $("#profile-input").on("change", function () {
            	var list = $(this).prop("files"); //선택된 파일 구하기
                if(list.length == 0) return;

            	//파일 전송
            	var form = new FormData(); //<form>역할
            	form.append("attach", list[0]);
            	
            	$.ajax({
            		processData : false,
            		contentType : false,
                    url: "/rest/mypage/profile",
                    method: "post",
                    data:  form,
                    success: function (response) {
                    	var newOrigin = origin + "&t=" + new Date().getTime();
                    	$(".profile-img").attr("src", newOrigin);
                    	console.log("실행1")
                    	}
                })
            });

        });

    </script>
</head>

<body>
<form action="edit" method="post">
    <div class="container ">
        <div class="mt-50 ms-30 text" style="font-size: 30px;">회원정보 수정</div>
        
            <div class="cell area flex-box ">
                <!-- 왼쪽 프로필 영역-->
                <div class="cell  profile-area">
                    <!-- 프로필 이미지 영역-->
                    <div class="cell  profile-img-area mt-30">
                        <img src="/mypage/image?memberId=${memberDto.memberId}" class="profile-img" >
                        <span class="change-text">변경하기</span>
                        <div class="cell profile-change flex-box flex-center"><i class="fa-solid fa-camera-rotate"
                                style="color: aliceblue;"></i></div>
                    </div>
                        <input type="file" id="profile-input" accept="image/*" style="display: none;">
                    <!-- 닉네임/메일주소-->
                    <div class="center mt-30">
                        <div class="text">${memberDto.memberNickname}</div>
                        <div class="text-small">${memberDto.memberEmail}</div>
                    </div>
                    <!-- 배너 영역-->
                    <div class="banner-area mt-50">
                        <a href="/mypage/calendar/"><img src="/image/home/banner-img1.png" class="banner-img"></a>
                    </div>
                </div>
                <div>
                             
                    <div class="cell edit-area flex-box">
                        <div class="cell">
                            <!-- 닉네임 영역-->
                            <div class="cell">
                                <div class="title" >닉네임<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="text" name="memberNickname" value="${memberDto.memberNickname}" class="box">
                            </div>
                            <!-- 이름 영역-->
                            <div class="cell">
                                <div class="title">이름<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="text" name="memberName" value="${memberDto.memberName}" class="box">
                            </div>
                            <!-- 이메일 영역-->
                            <div class="cell">
                                <div class="title">이메일<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="text" name="memberEmail"  value="${memberDto.memberEmail}" class="box">
                            </div>

                        </div>
                        <div class="cell">
                            <!-- MBTI 영역-->
                            <div class="cell">
                                <div class="title">MBTI<i class="fa-solid fa-star-of-life red"></i></div>
                                <select name="memberMbti" class="box">
    	<option value="ESTJ" ${memberDto.memberMbti == 'ESTJ' ? 'selected' : ''}>ESTJ</option>
    	<option value="ESTP" ${memberDto.memberMbti == 'ESTP' ? 'selected' : ''}>ESTP</option>
    	<option value="ESFJ" ${memberDto.memberMbti == 'ESFJ' ? 'selected' : ''}>ESFJ</option>
    	<option value="ESFP" ${memberDto.memberMbti == 'ESFP' ? 'selected' : ''}>ESFP</option>
    	<option value="ENTJ" ${memberDto.memberMbti == 'ENTJ' ? 'selected' : ''}>ENTJ</option>
    	<option value="ENTP" ${memberDto.memberMbti == 'ESTP' ? 'selected' : ''}>ENTP</option>
    	<option value="ENFJ" ${memberDto.memberMbti == 'ENFJ' ? 'selected' : ''}>ENFJ</option>
    	<option value="ENFP" ${memberDto.memberMbti == 'ENFP' ? 'selected' : ''}>ENFP</option>
    	<option value="ISTJ" ${memberDto.memberMbti == 'ISTJ' ? 'selected' : ''}>ISTJ</option>
    	<option value="ISTP" ${memberDto.memberMbti == 'ISTP' ? 'selected' : ''}>ISTP</option>
    	<option value="ISFJ" ${memberDto.memberMbti == 'ISFJ' ? 'selected' : ''}>ISFJ</option>
    	<option value="ISFP" ${memberDto.memberMbti == 'ISFP' ? 'selected' : ''}>ISFP</option>
    	<option value="INTJ" ${memberDto.memberMbti == 'INTJ' ? 'selected' : ''}>INTJ</option>
    	<option value="INTP" ${memberDto.memberMbti == 'INTP' ? 'selected' : ''}>INTP</option>
    	<option value="INFJ" ${memberDto.memberMbti == 'INFJ' ? 'selected' : ''}>INFJ</option>
    	<option value="INFP" ${memberDto.memberMbti == 'INFP' ? 'selected' : ''}>INFP</option>
                                </select><br>
                            </div>
                            
                            <!-- 생년월일 영역-->
                            <div class="cell">
                                <div class="title">생년월일<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="text" name="memberBirth" value="${memberDto.memberBirth}" class="box calendar">
                            </div>
                            <!-- 전화번호 영역-->
                            <div class="cell">
                                <div class="title">전화번호<i class="fa-solid fa-star-of-life red"></i></div>
                                <input type="text" name="memberContact" value="${memberDto.memberContact}" class="box" placeholder="XXX-XXXX-XXXX">
                            </div>
                        </div>
                    </div>
                    <div class="cell edit-area">
                        <!-- 주소 영역-->
                        <div class="cell ">
                            <div class="title post-title">주소<i class="fa-solid fa-star-of-life red"></i></div>
                            <input type="text" name="memberPostcode" value="${memberDto.memberPostcode}" class="box post-text">


                            <button type="button" class="btn btn-neutral btn-address-search">검색</button>
                        </div>
                        <div class="cell">
                            <div class="title post-title">상세주소<i class="fa-solid fa-star-of-life red"></i></div>
                            <input type="text" name="memberAddress1" value="${memberDto.memberAddress1}" class="box box-long post-text">
                        </div>
                        <div class="cell">
                            <div class="title post-title">상세주소2<i class="fa-solid fa-star-of-life red"></i></div>
                            <input type="text" name="memberAddress2" value="${memberDto.memberAddress2}" class="box box-long post-text">
                        </div>
                        <div class="center right mt-20">
                            <button type="submit" class="btn btn-positive">수정</button>
                        </div>
                    </div>
                </div>
            </div>
        
    </div>
    </form>
</body>
</html>
 <jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
