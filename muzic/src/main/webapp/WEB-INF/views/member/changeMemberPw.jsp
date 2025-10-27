<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>비밀번호 변경 | MUZIC</title>

  <!-- CSS (기존 보라 테마) -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/idPwFind.css">

  <!-- jQuery / FontAwesome -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/js/all.min.js"></script>

  <script>
    window.ctx = '${pageContext.request.contextPath}';
  </script>

  <style>
    /* 페이지 전용 약간의 보완 */
    .form-title { 
      width: 500px; margin: 60px auto 16px; 
      font-size: 20px; font-weight: 700; color:#6c67d9; text-align:center;
    }
    .hint { font-size: 13px; color:#777; margin-top:-6px; margin-bottom:12px; text-align:left; }
    .feedback { font-size:13px; margin: 6px 0 0 0; text-align:left; display:none; }
    .feedback.ok { color:#2aa198; }
    .feedback.bad { color:#d63031; }
    .pw-visibility { position:absolute; right:12px; top:10px; cursor:pointer; color:#9A9CE3; }
    .field-wrap { position:relative; }
  </style>

  <script>
  $(function(){
    const $pw  = $("#pw");
    const $pw2 = $("#pw2");
    const $btn = $(".btn-main");

    // 비밀번호 규칙: 영문+숫자+특수문자 8~16자
    const pwRule = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[^\w\s]).{8,16}$/;

    function validate() {
      const v1 = $pw.val().trim();
      const v2 = $pw2.val().trim();
      let ok = true;

      if(!pwRule.test(v1)){
        $("#pwRule").show().removeClass("ok").addClass("bad")
          .text("영문 + 숫자 + 특수문자 조합 8~16자로 입력하세요.");
        ok = false;
      } else {
        $("#pwRule").show().removeClass("bad").addClass("ok")
          .text("사용 가능한 형식입니다.");
      }

      if(v2.length > 0){
        if(v1 !== v2){
          $("#pwMatch").show().removeClass("ok").addClass("bad")
            .text("비밀번호가 일치하지 않습니다.");
          ok = false;
        } else {
          $("#pwMatch").show().removeClass("bad").addClass("ok")
            .text("비밀번호가 일치합니다.");
        }
      } else {
        $("#pwMatch").hide();
        ok = false;
      }

      $btn.prop("disabled", !ok);
      return ok;
    }

    $pw.on("input", validate);
    $pw2.on("input", validate);

    // 눈 아이콘 토글
    $(".toggle1").on("click", function(){
      const type = $pw.attr("type") === "password" ? "text" : "password";
      $pw.attr("type", type);
      $(this).toggleClass("fa-eye fa-eye-slash");
    });
    $(".toggle2").on("click", function(){
      const type = $pw2.attr("type") === "password" ? "text" : "password";
      $pw2.attr("type", type);
      $(this).toggleClass("fa-eye fa-eye-slash");
    });

    // 제출 전 최종 검증
    $(".change-form").on("submit", function(e){
      if(!validate()){
        e.preventDefault();
        $(".message").css("color","red").text("입력값을 다시 확인해주세요.");
      }
    });
  });
  </script>
</head>
<body>

  <div class="form-title">비밀번호 변경</div>

  <form action="${pageContext.request.contextPath}/member/changeMemberPw"
        method="post" class="find-form change-form" autocomplete="off">

    <!-- 서버에서 전달된 파라미터(필수) -->
    <input type="hidden" name="memberId"   value="${memberId}">
    <input type="hidden" name="certNumber" value="${certNumber}">

    <!-- 새 비밀번호 -->
    <div class="field-wrap">
      <input type="password" name="memberPw" id="pw" class="field"
             placeholder="새 비밀번호 (영문+숫자+특수문자 8~16자)">
      <i class="fa-solid fa-eye pw-visibility toggle1"></i>
    </div>
    <div class="hint">영문/숫자/특수문자 각각 1자 이상 포함, 8~16자</div>
    <div class="feedback" id="pwRule"></div>

    <!-- 새 비밀번호 확인 -->
    <div class="field-wrap">
      <input type="password" id="pw2" class="field" placeholder="새 비밀번호 확인">
      <i class="fa-solid fa-eye pw-visibility toggle2"></i>
    </div>
    <div class="feedback" id="pwMatch"></div>

    <button type="submit" class="btn-main" disabled>비밀번호 변경 완료</button>
    <div class="message" style="margin-top:12px;"></div>
  </form>

</body>
</html>
