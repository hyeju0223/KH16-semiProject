<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>아이디 찾기 | MUZIC</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/idPwFind.css">
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>window.ctx='${pageContext.request.contextPath}';</script>
</head>
<body>
  <div class="tab-bar">
    <div class="tab active">아이디 찾기</div>
    <div class="tab" id="pwTab">비밀번호 찾기</div>
  </div>

  <form action="${pageContext.request.contextPath}/member/findMemberId" method="post" class="find-form">
    <input type="text" name="memberName"  class="field" placeholder="이름 입력">
    <input type="date" name="memberBirth" class="field" placeholder="생년월일">

    <div class="email-line">
      <input type="email" name="memberEmail" class="field" placeholder="이메일 입력 (예: example@muzic.com)">
      <button type="button" class="btn-outline btn-cert-send">인증번호 전송</button>
    </div>

    <div class="cert-area" style="display:none;">
      <div class="cert-line">
        <input type="text" class="field cert-input" placeholder="인증번호 입력">
        <button type="button" class="btn-outline btn-cert-check">확인</button>
      </div>
    </div>

    <input type="hidden" name="certNumber" class="hidden-cert">
    <button type="submit" class="btn-main">아이디 찾기</button>
    <div class="message"></div>
  </form>

<script>
$(function(){
  $("#pwTab").click(()=> location.href = ctx + "/member/findMemberPw");

  $(".btn-cert-send").click(function(){
    const n=$("input[name=memberName]").val().trim();
    const b=$("input[name=memberBirth]").val().trim();
    const e=$("input[name=memberEmail]").val().trim();
    if(!n||!b||!e) return $(".message").css("color","red").text("이름/생년월일/이메일을 모두 입력하세요.");

    $.post(ctx+"/rest/member/findIdCertSend",{memberName:n,memberBirth:b,memberEmail:e})
     .done(ok=>{
       if(ok){ $(".cert-area").show(); $(".message").css("color","#9A9CE3").text("인증번호를 전송했습니다."); }
       else  { $(".message").css("color","red").text("일치하는 회원 정보를 찾을 수 없습니다."); }
     })
     .fail(()=>$(".message").css("color","red").text("이메일 전송 중 오류가 발생했습니다."));
  });

  $(".btn-cert-check").click(function(){
    const e=$("input[name=memberEmail]").val().trim();
    const c=$(".cert-input").val().trim();
    $.post(ctx+"/rest/member/certCheck",{certEmail:e,certNumber:c})
     .done(valid=>{
       if(valid){ $(".hidden-cert").val(c); $(".message").css("color","#9A9CE3").text("인증 완료! 아이디를 확인합니다."); $(".find-form").submit(); }
       else     { $(".message").css("color","red").text("인증번호가 올바르지 않거나 만료되었습니다."); }
     })
     .fail(()=>$(".message").css("color","red").text("서버 오류가 발생했습니다."));
  });
});
</script>
</body>
</html>
