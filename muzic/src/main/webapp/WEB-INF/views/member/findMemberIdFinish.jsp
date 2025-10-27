<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/js/all.min.js"></script>

<div class="container login-container center" style="margin-top:120px;">
  
  <!-- 상단 헤더 -->
  <div class="cell left" style="width:650px; margin:auto; margin-bottom:60px;">
    <span class="blue" style="font-size:24px; margin-right:10px;">
      <i class="fa-solid fa-circle"></i>
    </span>

    <a href="${pageContext.request.contextPath}/" 
       class="muzic-logo" 
       style="font-size:28px; letter-spacing:3px; color:#111; text-decoration:none;">
       MUZIC
    </a>

    <span style="font-size:22px; color:#555; font-weight:500; margin-left:6px;">아이디 찾기 결과</span>
    <hr style="border:0; border-top:1px solid #ddd; margin-top:20px;">
  </div>

  <!-- 중앙 안내 영역 -->
  <div id="resultArea" class="cell center" style="margin-top:40px;"></div>

  <!-- 하단 버튼 -->
  <div class="cell center" style="margin-top:45px;">
    <a href="${pageContext.request.contextPath}/member/login" 
       class="btn-positive" 
       style="display:block; width:420px; height:54px; 
              line-height:54px; font-weight:bold; 
              font-size:16px; border-radius:10px;
              background-color:#9A9CE3; color:white;
              margin:auto; text-decoration:none;">
      로그인 화면으로
    </a>
  </div>
</div>

<script>
  $(function() {
    // ✅ Controller에서 전달된 아이디 가져오기 (없으면 빈 문자열)
    const memberId = "${memberId != null ? memberId : ''}";

    if (memberId) {
      // ✅ 일치하는 아이디 존재할 경우
      $("#resultArea").html(`
        <div style="width:80px; height:80px; border-radius:50%; background-color:#1DD1A1;
                    color:white; display:flex; align-items:center; justify-content:center; margin:auto;">
          <i class="fa-solid fa-check" style="font-size:36px;"></i>
        </div>

        <h1 style="color:#111; font-weight:800; font-size:26px; margin-top:40px;">
          아이디 찾기가 완료되었습니다.
        </h1>
        <p style="color:#666; margin-top:12px; font-size:16px;">
          회원님의 아이디는<br>
          <span class="blue" style="font-size:30px;">${memberId}</span> 입니다.
        </p>
      `);
    } 
    else {
      // ❌ 일치하는 아이디가 없을 경우
      $("#resultArea").html(`
        <div style="width:80px; height:80px; border-radius:50%; background-color:#E57373;
                    color:white; display:flex; align-items:center; justify-content:center; margin:auto;">
          <i class="fa-solid fa-xmark" style="font-size:36px;"></i>
        </div>

        <h1 style="color:#111; font-weight:800; font-size:26px; margin-top:40px;">
          고객님 명의로 등록된 아이디가 없습니다.
        </h1>
        <p style="color:#666; margin-top:12px; font-size:16px;">
          닉네임과 이메일을 다시 확인하시거나<br>
          회원가입을 진행해주세요.
        </p>
      `);
    }
  });
</script>
