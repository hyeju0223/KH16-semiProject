<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <jsp:include page="/WEB-INF/views/template/header.jsp"></jsp:include>

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
    <style>
        .roulette-area-fixed {
            width: 100%;
        }

        .roulette-area {
        position: relative;
        background-color: #ffeaa7;
        }

        .container {
           
            width: 1400px;;
        }

        .pointer {
            width: 320px;
            position: absolute;
            left: 50%;
            top: -7%;
            transform: translateX(-50%);
        }

        .btn.start-btn {
            width: 300px;
            height: 300px;
            border-radius: 50%;
            background-color: rgb(207, 81, 49);
            color: white;
            border: 20px solid rgb(150, 61, 61);
            transform: translate(-50%, -50%);
            position: absolute;
            left: 700px;
            top: 500px;
            font-size: 60px;
            padding: 20px;
        }

        .roulette-bottom{
            height: 200px;
            color: #43494b;
            font-size: 32px;
            font-weight: 500;
            padding: 50px;
            background-color: #dfe6e9;
        }
        
        .top {
                    background-color: #ffeaa7;
                    }
    </style>
    <script type="text/javascript">

        $(function () {
            //             const roulette = document.querySelector('.roulette'); // 룰렛 바퀴
            // const startButton = document.querySelector('.start-btn'); // 시작 버튼

            // // 2. 룰렛 설정을 위한 변수들
            // const numSections = 8; // 룰렛 섹션 개수
            // const sectionAngles = 360 / numSections; // 섹션당 각도

            // // 3. 룰렛을 회전시키는 함수
            // function spinRoulette() {
            //     // 1. 무작위로 당첨 섹션 결정
            //     const winningSection = Math.floor(Math.random() * numSections); // $0$부터 $numSections-1$까지의 무작위 정수

            //     // 2. 룰렛이 멈출 최종 각도 계산
            //     const finalAngle = -(winningSection * sectionAngles + Math.random() * sectionAngles);

            //     // 3. CSS로 룰렛 회전 애니메이션 적용
            //     roulette.style.transition = 'transform 5s ease-in-out'; // 5초 동안 애니메이션
            //     roulette.style.transform = `rotate(${finalAngle}deg)`; // 최종 각도로 회전

            //     // 4. 애니메이션이 끝난 후 애니메이션 속성을 제거
            //     setTimeout(() => {
            //         roulette.style.transition = 'none';
            //         // TODO: 당첨 결과 처리 로직 추가
            //         alert(`당첨된 섹션: ${winningSection + 1}`);
            //     }, 5000); // 5초 후 실행
            // }

            // // 4. 시작 버튼 클릭 시 룰렛 회전
            // startButton.addEventListener('click', spinRoulette);

            const NUM = 8;                 // 섹터 개수
            const BASE = 360 / NUM;        // 각도
            const ADJUST = -BASE;      // 경계선이 12시라서 22.5도 보정
            const POINT = [100, 500, 300, 70, 30, 30, 10, 300]; // 12시부터 시계방향

            let acc = 0; // 누적 각도

            // 섹터 i(0~7)의 중앙 각도
            const center = i => ( ADJUST + i * BASE + BASE / 2) % 360;

            // 현재 각도 기준으로 섹터 i로 회전
            function spinTo(i) {
                const wheel = document.querySelector('.roulette');
                const now = ((acc % 360) + 360) % 360;
                const target = center(i);
                const delta = ((target - now) + 360) % 360;
                const final = acc + 360 * 4 + delta; // 4바퀴 돌고 멈춤

                wheel.style.transition = 'transform 2.6s cubic-bezier(.17,.67,.3,1.02)';
                wheel.style.transform = `rotate(\${final}deg)`;
                wheel.addEventListener('transitionend', () => {
                    acc = final; wheel.style.transition = 'none';
                    // alert(`섹터 ${i + 1} / +${POINT[i]}P`);
                    // }, { once: true });
                    var p = POINT[i];

                    $.ajax({
                        url: "/rest/roulette/insert",
                        method: "post",
                        data: {
                            memberId: "${sessionScope.loginMemberId}",
                            point: p
                        },
                        success: function (response) {
                            if (response == "success") {
                                alert("룰렛 이벤트 결과[" + p + "p 당첨!]!");

                            } if (response == "already") {
                                alert("오늘은 이미 참여하였습니다. 내일 다시 방문해주세요!")
                            }
                        },
                    })
                }, { once: true }
                )
            };

            // 랜덤 스핀 예시
            document.querySelector('.start-btn').addEventListener('click', () => {
                const i = Math.floor(Math.random() * NUM); // 0~7
                $.ajax({
                    url: "/rest/roulette/check",
                    method: "post",
                    data: {
                        memberId: "${sessionScope.loginMemberId}"
                    },
                    success: function (response) {
                        if (response == "already") {
                            alert("오늘은 이미 참여하였습니다. 내일 다시 방문해주세요!")
                        } else {
                            spinTo(i);
                            }
                        },
                    })
            });

        });



    </script>
</head>

<body>
    <div>
        <div class="center top ">
            <img src="/image/roulette/roulette-img.png" class="img" style="width:70%;">
        </div>
        <div class="white-area"></div>
    
    <div>
        <div class="roulette-area center">
            <div class="roulete">
                <img src="/image/roulette/roulette.png" id="roulette" class="roulette">
                <img src="/image/roulette/roulette-point.png" class="pointer">
                <button class="btn start-btn">START</button>
            </div>
        </div>
        <div class="roulette-bottom">
            <div class="cell">
                <span><유의사항></span><br>
                <span>- 룰렛 이벤트는 하루에 1회만 참여 가능합니다</span>
            </div>
        </div>
    </div>
    </div>


</body>

</html>

 <jsp:include page="/WEB-INF/views/template/footer.jsp"></jsp:include>
