<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>비밀번호 변경 | MUZIC</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/idPwFind.css">
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script>window.ctx='${pageContext.request.contextPath}';</script>
</head>
<body>
  <div class="tab-bar">
    <div class="tab" id="idTab">아이디 찾기</div>
    <div class="tab active">비밀번호 찾기</div>
  </div>

  <div class="find-form">
    <input type="text"  name="memberId"    class="field" placeholder="아이디 입력">
    <input type="text"  name="memberName"  class="field" placeholder="이름 입력">

    <!-- 브라우저별 호환을 위해 date + JS 검증 둘 다 사용 -->
    <input type="date"  name="memberBirth" class="field" placeholder="생년월일 (YYYY-MM-DD)">

    <div class="email-line">
      <input type="email" name="memberEmail" class="field" placeholder="이메일 입력 (예: example@muzic.com)">
      <button type="button" class="btn-outline btn-cert-send">인증번호 전송</button>
    </div>

    <div class="cert-area" style="display:none;">
      <div class="cert-line" style="display:flex; gap:10px;">
        <input type="text" class="field cert-input" placeholder="인증번호 입력">
        <button type="button" class="btn-outline btn-cert-check">확인</button>
      </div>
    </div>

    <div class="message"></div>
    <button type="button" class="btn-main btn-go-change" disabled>비밀번호 변경</button>
  </div>

  <script>
  // ===== 생년월일 유효성 검사 =====
  function isValidBirth(iso) {
    // 형식: YYYY-MM-DD
    const m = /^(\d{4})-(\d{2})-(\d{2})$/.exec(iso);
    if (!m) return false;

    const y = +m[1], mo = +m[2], d = +m[3];
    if (y < 1900) return false;              // 너무 옛날 제한
    if (mo < 1 || mo > 12) return false;
    if (d < 1 || d > 31) return false;

    // 존재하는 날짜인지 검사 (JS 날짜 롤오버 방지: 구성요소 재확인)
    const dt = new Date(Date.UTC(y, mo - 1, d));
    const ok = (dt.getUTCFullYear() === y &&
                (dt.getUTCMonth() + 1) === mo &&
                dt.getUTCDate() === d);
    if (!ok) return false;

    // 미래 날짜 금지 (오늘 23:59:59 기준)
    const today = new Date();
    const endToday = new Date(today.getFullYear(), today.getMonth(), today.getDate(), 23,59,59,999);
    const valueLocal = new Date(y, mo - 1, d); // 로컬 기준 비교
    if (valueLocal.getTime() > endToday.getTime()) return false;

    return true;
  }

  // ===== 오늘 날짜를 max로 설정 (미래 선택 방지) =====
  function setBirthMaxToday() {
    const $birth = $("input[name=memberBirth]");
    const t = new Date();
    const yyyy = t.getFullYear();
    const mm   = String(t.getMonth() + 1).padStart(2, "0");
    const dd   = String(t.getDate()).padStart(2, "0");
    $birth.attr("max", `${yyyy}-${mm}-${dd}`);
  }

  $(function(){
    setBirthMaxToday();

    // 탭 이동
    $("#idTab").click(()=> location.href = ctx + "/member/findMemberId");

    // 인증번호 전송
    $(".btn-cert-send").click(function(){
      const d={
        memberId:    $("input[name=memberId]").val().trim(),
        memberName:  $("input[name=memberName]").val().trim(),
        memberBirth: $("input[name=memberBirth]").val().trim(),
        memberEmail: $("input[name=memberEmail]").val().trim()
      };

      if(!d.memberId||!d.memberName||!d.memberBirth||!d.memberEmail){
        $(".message").css("color","red").text("아이디/이름/생년월일/이메일을 모두 입력하세요.");
        return;
      }

      // 생년월일 엄격 검증
      if(!isValidBirth(d.memberBirth)){
        $(".message").css("color","red").text("생년월일은 YYYY-MM-DD 형식의 유효한 과거 날짜여야 합니다.");
        return;
      }

      $.post(ctx+"/rest/member/findPwCertSend", d)
        .done(ok=>{
          if(ok){
            $(".cert-area").show();
            $(".message").css("color","#9A9CE3").text("인증번호를 전송했습니다.");
          }else{
            $(".message").css("color","red").text("일치하는 회원 정보를 찾을 수 없습니다.");
          }
        })
        .fail(()=>$(".message").css("color","red").text("이메일 전송 중 오류가 발생했습니다."));
    });

    // 인증번호 확인
    $(".btn-cert-check").click(function(){
      const email = $("input[name=memberEmail]").val().trim();
      const code  = $(".cert-input").val().trim();

      if(!code){
        $(".message").css("color","red").text("인증번호를 입력하세요.");
        return;
      }

      $.post(ctx+"/rest/member/certCheck", { certEmail: email, certNumber: code })
        .done(valid=>{
          if(valid){
            $(".message").css("color","#9A9CE3").text("인증이 완료되었습니다. 비밀번호 변경 페이지로 이동할 수 있습니다.");
            $(".btn-go-change").prop("disabled", false).data("code", code);
          }else{
            $(".message").css("color","red").text("인증번호가 일치하지 않습니다.");
          }
        })
        .fail(()=>$(".message").css("color","red").text("서버 오류가 발생했습니다."));
    });

    // 비밀번호 변경 페이지로 이동
    $(".btn-go-change").click(function(){
      const memberId   = $("input[name=memberId]").val().trim();
      const certNumber = $(this).data("code");
      if(!certNumber) return;

      location.href = ctx + "/member/changeMemberPw?memberId=" + encodeURIComponent(memberId)
                    + "&certNumber=" + encodeURIComponent(certNumber);
    });
  });
  </script>
</body>
</html>
