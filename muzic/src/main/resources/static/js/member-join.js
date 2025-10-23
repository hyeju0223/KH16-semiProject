$(function(){
  //------------------------------------------------------
  // 🔹 상태 객체 정의
  //------------------------------------------------------
  const state = {
    memberIdValid: false,
    memberPwValid: false,
    memberPwCheckValid: false,
    memberNicknameValid: false,
    memberNameValid: false,
    memberEmailValid: false,
    memberMbtiValid: false,
    memberContactValid: false,
    memberAddressValid: true,
    ok: function(){
      return this.memberIdValid && this.memberPwValid && this.memberPwCheckValid &&
             this.memberNicknameValid && this.memberNameValid &&
             this.memberEmailValid && this.memberMbtiValid &&
             this.memberContactValid && this.memberAddressValid;
    }
  };

  //------------------------------------------------------
  // [1] 아이디 중복검사 (DB: ^[a-z][a-z0-9]{4,19}$)
  //------------------------------------------------------
  $("[name=memberId]").on("blur", function(){
    const val = $(this).val().trim();
    const regex = /^[a-z][a-z0-9]{4,19}$/;
    if(!regex.test(val)){
      $(this).removeClass("success fail fail2").addClass("fail");
      state.memberIdValid = false;
      return;
    }
    $.ajax({
      url: "/rest/member/checkMemberId",
      data: {memberId: val},
      success: function(resp){
        if(resp){
          $("[name=memberId]").removeClass("success fail fail2").addClass("fail2");
          state.memberIdValid = false;
        } else {
          $("[name=memberId]").removeClass("success fail fail2").addClass("success");
          state.memberIdValid = true;
        }
      }
    });
  });

  //------------------------------------------------------
  // [2] 비밀번호 검사 (보안강화, DB에는 제약 없음)
  //------------------------------------------------------
  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,16}$/;
  $("[name=memberPw], #password-check").on("blur input", function(){
    const pw = $("[name=memberPw]").val();
    const pw2 = $("#password-check").val();
    const valid = pwRegex.test(pw);
    $("[name=memberPw]").removeClass("success fail").addClass(valid ? "success" : "fail");
    state.memberPwValid = valid;
    const match = pw.length > 0 && pw === pw2;
    $("#password-check").removeClass("success fail").addClass(match ? "success" : "fail");
    state.memberPwCheckValid = match;
  });

  $("#password-show").on("click", function(){
    const inputs = $("[name=memberPw], #password-check");
    const isHidden = inputs.attr("type") === "password";
    inputs.attr("type", isHidden ? "text" : "password");
    $(this).toggleClass("fa-eye fa-eye-slash");
  });

  //------------------------------------------------------
  // [3] 닉네임 유효성 검사 + 중복검사 (빈값 처리 포함)
  //------------------------------------------------------
  $("[name=memberNickname]").on("input", function () {
    const regex = /^[가-힣0-9]{2,10}$/; // 2~10자 한글+숫자
    const value = $(this).val().trim();

    // 입력이 비어 있으면 => 초기 상태로 리셋
    if (value.length === 0) {
      $(this).removeClass("success fail fail2");
      state.memberNicknameValid = false;
      return;
    }

    // 정규식 불일치 → 형식 오류
    if (!regex.test(value)) {
      $(this).removeClass("success fail2").addClass("fail");
      state.memberNicknameValid = false;
      return;
    }

    // 정규식 통과 → AJAX 중복 확인
    const input = $(this);
    $.ajax({
      url: "/rest/member/checkMemberNickname",
      data: { memberNickname: value },
      success: function (resp) {
        if (resp) {
          input.removeClass("success fail").addClass("fail2"); // 이미 존재하는 닉네임
          state.memberNicknameValid = false;
        } else {
          input.removeClass("fail fail2").addClass("success"); // 사용 가능
          state.memberNicknameValid = true;
        }
      }
    });
  });


  //------------------------------------------------------
  // [4] 이름 검사 (DB: ^[가-힣]{2,6}$)
  //------------------------------------------------------
  $("[name=memberName]").on("blur", function(){
    const val = $(this).val().trim();
    const regex = /^[가-힣]{2,6}$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberNameValid = regex.test(val);
  });

  //------------------------------------------------------
  // [5] 이메일 인증 (DB: [A-Za-z0-9_-]+@[A-Za-z0-9_-]+)
  //------------------------------------------------------
  let certTimer = null;
  let remain = 300; // 5분

  $(".btn-cert-send").on("click", function(){
    const email = $("[name=memberEmail]").val().trim();
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+$/;
    if(!regex.test(email)){
      $("[name=memberEmail]").removeClass("success fail fail2").addClass("fail");
      state.memberEmailValid = false;
      return;
    }
    $.ajax({
      url: "/rest/member/certSend",
      method: "post",
      data: {certEmail: email},
      success: function(){
        $(".cell-cert-input").show();
        startTimer(); // 타이머 시작
      },
      beforeSend: function(){
        $(".btn-cert-send").prop("disabled", true)
          .find("i").removeClass("fa-paper-plane").addClass("fa-spinner fa-spin");
      },
      complete: function(){
        $(".btn-cert-send").prop("disabled", false)
          .find("i").removeClass("fa-spinner fa-spin").addClass("fa-paper-plane");
      }
    });
  });

  $(".btn-cert-check").on("click", function(){
    const code = $(".cert-input").val().trim();
    if(!/^[0-9]{6}$/.test(code)){
      $(".cert-input").removeClass("success fail fail2").addClass("fail");
      return;
    }
    const email = $("[name=memberEmail]").val();
    $.ajax({
      url: "/rest/member/certCheck",
      method: "post",
      data: {certEmail: email, certNumber: code},
      success: function(resp){
        if(resp){
          clearInterval(certTimer);
          $(".cert-timer").text("인증 완료 ✅");
          $(".cert-input").val("").removeClass("fail fail2");
          $(".cell-cert-input").hide();
          $("[name=memberEmail]").addClass("success").prop("readonly", true);
          state.memberEmailValid = true;
        } else {
          $(".cert-input").removeClass("success fail").addClass("fail2");
          state.memberEmailValid = false;
        }
      }
    });
  });

  function startTimer(){
    remain = 300;
    clearInterval(certTimer);
    $(".cert-timer").show().text("남은 시간: 05:00");
    certTimer = setInterval(function(){
      remain--;
      const m = String(Math.floor(remain/60)).padStart(2,"0");
      const s = String(remain%60).padStart(2,"0");
      $(".cert-timer").text(`남은 시간: ${m}:${s}`);
      if(remain <= 0){
        clearInterval(certTimer);
        $(".cert-timer").text("⏰ 인증시간이 만료되었습니다");
        state.memberEmailValid = false;
      }
    },1000);
  }

  //------------------------------------------------------
  // [6] MBTI 검사 (DB: ^[IE][SN][FT][PJ]$)
  //------------------------------------------------------
  $("[name=memberMbti]").on("change blur", function(){
    const val = $(this).val().trim().toUpperCase();
    const regex = /^[IE][SN][FT][PJ]$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberMbtiValid = regex.test(val);
  });

  //------------------------------------------------------
  // [7] 연락처 검사 (DB: ^010-[1-9][0-9]{3}-[0-9]{4}$)
  //------------------------------------------------------
  $("[name=memberContact]").on("blur", function(){
    const val = $(this).val().trim();
    const regex = /^010-[1-9][0-9]{3}-[0-9]{4}$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberContactValid = regex.test(val);
  });

  //------------------------------------------------------
  // [8] 주소 검사 (3개 모두 null 또는 모두 존재)
  //------------------------------------------------------
  $(".btn-address-search, [name=memberPost], [name=memberAddress1]").on("click", function(){
    new daum.Postcode({
      oncomplete: function(data){
        $("[name=memberPost]").val(data.zonecode);
        $("[name=memberAddress1]").val(data.address);
        $("[name=memberAddress2]").focus();
      }
    }).open();
  });

  $("[name=memberAddress2]").on("blur", function(){
    const filled = $("[name=memberPost]").val() && $("[name=memberAddress1]").val() && $("[name=memberAddress2]").val();
    const empty = !$("[name=memberPost]").val() && !$("[name=memberAddress1]").val() && !$("[name=memberAddress2]").val();
    const valid = filled || empty;
    $("[name=memberPost],[name=memberAddress1],[name=memberAddress2]")
      .removeClass("success fail").addClass(valid ? "success" : "fail");
    state.memberAddressValid = valid;
  });

  //------------------------------------------------------
  // [9] 프로필 이미지 미리보기
  //------------------------------------------------------
  $("[name=attach]").on("input", function(){
    const url = $(".img-preview").prop("src");
    if(url && url.startsWith("blob:")) URL.revokeObjectURL(url);
    if(this.files.length === 0){
      $(".img-preview").prop("src", "/images/error/no-image.png");
    } else {
      const blob = URL.createObjectURL(this.files[0]);
      $(".img-preview").prop("src", blob);
    }
  });

  //------------------------------------------------------
  // [10] 멀티페이지 제어 + 유효성 검사
  //------------------------------------------------------
  $(".btn-next").on("click", function(){
    const page = $(this).closest(".page");
    const step = $(".page").index(page)+1;

    let valid = true;
    page.find("input[required], select[required]").each(function(){
      if($(this).val().trim() === ""){
        $(this).addClass("fail");
        valid = false;
      }
    });

    if(step===1 && (!state.memberIdValid || !state.memberPwValid || !state.memberPwCheckValid || !state.memberNicknameValid)){
      alert("⚠️ 기본정보를 정확히 입력하세요.");
      return;
    }
    if(step===2 && (!state.memberNameValid || !state.memberMbtiValid || !state.memberContactValid)){
      alert("⚠️ 개인정보 항목을 모두 입력하세요.");
      return;
    }
    if(step===3 && !state.memberEmailValid){
      alert("⚠️ 이메일 인증을 완료하세요.");
      return;
    }

    if(!valid){
      alert("⚠️ 필수 항목을 모두 입력해야 합니다.");
      return;
    }

    page.hide().next(".page").fadeIn(200);
    refreshPage();
  });

  $(".btn-prev").on("click", function(){
    $(this).closest(".page").hide().prev(".page").fadeIn(200);
    refreshPage();
  });

  //------------------------------------------------------
  // [11] 최종 제출
  //------------------------------------------------------
  $(".check-form").on("submit", function(){
    if(!state.ok()){
      alert("❌ 필수 정보를 정확히 입력해야 가입이 가능합니다.");
      return false;
    }
    return true;
  });
});
