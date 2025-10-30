<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!-- 회원 탈퇴 전 유의사항 안내 (멀티페이지 1)-->



<form action="/mypage/withDraw" method="post">
<input type="checkbox" name="agree" > 안내 사항을 모두 확인하였으며, 이에 동의합니다.
<button type="button" id="#">확인</button>


<!-- 비밀번호 확인 후 탈퇴하기 (멀티페이지 2) -->
	<hr>
	<h1>회원 비밀번호 확인</h1>

	비밀번호를 한번 더 입력해주세요<br>
	비밀번호를 입력하시면 회원탈퇴가 완료됩니다<br>
	비밀번호를 모르시면 정보수정 페이지에서 새로 설정 후 진행하시면 됩니다<br>
	<a href="password">수정하러 가기</a><br>
	
	<h3>회원 아이디 : ${memberDto.memberId}</h3>
	<input type="password" name="memberPw">
	<button type="submit">탈퇴하기</button>
	</form>
	
	
	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

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
            margin-top: 15px;
            display: flex;
            margin-left: 5px;
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

        .btn.btn-neutral {
            margin-left: 5px;
        }

        .btn.btn-positive {
            margin: 20px;

        }

        .large {
            font-size: 22px;
            font-weight: 500;
            
        }
    </style>

    <!-- jquery cdn-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>


    <script type="text/javascript">

        $(function () {
  // 폼 전체에 대해 submit 검증
  $("form").on("submit", function (e) {
    if (!$("#agree").is(":checked")) {
      e.preventDefault(); // 새로고침/전송 막기
      $(".agree-text").addClass("red");
    }
  });

  // 체크하면 경고 해제
  $("#agree").on("change", function () {
    if ($(this).is(":checked")) {
      $(".agree-text").removeClass("red");
    }
  });
});

    </script>
</head>

<body>
    <div class="container ">
        <div class="mt-50 text" style="font-size: 30px;">비밀번호 변경</div>
        <form>
            <div class="area flex-box ">

                <div class="w-800">
                    <div class="edit-area ">
                        <div class="title large" style="font-size: 20px;">
                            <span class="large">회원탈퇴 유의사항</span>
                        </div>
                        <div class="title" style="font-size: 20px;">
                            회원탈퇴를 신청하기 전에 안내사항을 꼭 확인해주세요.
                        </div>
                        <div class="title" style="font-size: 20px;">
                             <span class="large">사용하고 계신 아이디()는 탈퇴할 경우 재사용 및 복구가 불가능합니다</span>
                        </div>
                        <div class="title" style="font-size: 20px;">
                            <span class="large">탈퇴 후 회원정보 및 개인형 서비스 이용기록은 모두 삭제됩니다.</span>
                        </div>
                        <div class="title gray" style="font-size: 16px;">
                            <pre>회원정보 및 메일, 블로그, 주소록 등 개인형 서비스 이용기록은 모두 삭제되며, 삭제된 데이터는 복구되지 않습니다.
삭제되는 내용을 확인하시고 필요한 데이터는 미리 백업을 해주세요.</pre>
                        </div>
                        <div class="title" style="font-size: 20px;">
                           <span class="large"> 탈퇴 후에도 게시판 및 앨범형 서비스에 등록한 게시물 및 음원은 그대로 남아있습니다</span>
                        </div>
                        <div class="title gray" style="font-size: 16px;">
                            <pre>뉴스, 카페, 지식iN 등에 올린 게시글 및 댓글은 탈퇴 시 자동 삭제되지 않고 그대로 남아 있습니다.
삭제를 원하는 게시글이 있다면 반드시 탈퇴 전 비공개 처리하거나 삭제하시기 바랍니다.
탈퇴 후에는 회원정보가 삭제되어 본인 여부를 확인할 수 있는 방법이 없어, 게시글을 임의로 삭제해드릴 수 없습니다.</pre>
                        </div>
                        <div class="title" style="font-size: 20px;">
                            <input type="checkbox" id="agree" ><span class="agree-text">안내 사항을 모두 확인하였으며, 이에 동의합니다.</span>
                        </div>
                    </div>
        <hr>
        <div class="pagination-2">
            <div class="title mt-30 large" style="font-size: 20px;">
                회원 비밀번호 확인
            </div>
            <div class="title gray" style="font-size: 16px;">
                <pre>비밀번호를 한번 더 입력해주세요
비밀번호를 입력하시면 회원탈퇴가 완료됩니다
비밀번호를 모르시면 정보수정 페이지에서 새로 설정 후 진행하시면 됩니다</pre>
            </div>
            <a class="btn btn-neutral">수정하러 가기</a>
            <div class="title" style="font-size: 20px;">
                <span class="title large">회원 아이디 :</span>
            </div>
            <!-- 닉네임 영역-->
            <div class="cell">
                <div class="title large mt-40">현재 비밀번호<i class="fa-solid fa-star-of-life red"></i></div>
                <input type="text" name="memberNickname" class="box box-long">
            </div>
            <div class="center right mt-20">
                <button type="submit" class="btn btn-positive ">수정</button>
            </div>

        </div>
        </form>
    </div>
    </div>

</body>

</html>
