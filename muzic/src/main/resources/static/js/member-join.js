$(function(){
  const ctx = window.contextPath || "";

  const state = {
    memberIdValid: false,
    memberPwValid: false,
    memberPwCheckValid: false,
    memberNicknameValid: false,
    memberNameValid: false,
    memberBirthValid: false,
    memberMbtiValid: false,
    memberContactValid: false,
    memberEmailValid: false,
    memberAddressValid: true,
    ok: function(){
      return this.memberIdValid && this.memberPwValid && this.memberPwCheckValid &&
             this.memberNicknameValid && this.memberNameValid && this.memberBirthValid &&
             this.memberMbtiValid && this.memberContactValid && this.memberEmailValid &&
             this.memberAddressValid;
    }
  };

  //------------------------------------------------------
  // [1] 아이디 중복 검사
  //------------------------------------------------------
  $("[name=memberId]").on("blur", function(){
    const id = $(this).val().trim();
    const regex = /^[a-z][a-z0-9]{4,19}$/;
    const input = $(this);

    if (!id) { // ✅ 아무것도 입력 안 했을 때 리셋
      input.removeClass("success fail fail2");
      state.memberIdValid = false;
      return;
    }

    if (!regex.test(id)) {
      input.removeClass("success fail2").addClass("fail");
      state.memberIdValid = false;
      return;
    }

    $.ajax({
      url: `${ctx}/rest/member/checkMemberId`,
      method: "get",
      data: { memberId: id },
      success: function(res){
        if (res) {
          input.removeClass("success fail").addClass("fail2");
          state.memberIdValid = false;
        } else {
          input.removeClass("fail fail2").addClass("success");
          state.memberIdValid = true;
        }
      }
    });
  });

  //------------------------------------------------------
  // [2] 비밀번호 형식 및 확인
  //------------------------------------------------------
  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,16}$/;

  $("[name=memberPw], #password-check").on("blur", function(){
    const pw = $("[name=memberPw]").val().trim();
    const pw2 = $("#password-check").val().trim();

    if (!pw && !pw2) { // ✅ 아무것도 안 썼을 때 리셋
      $("[name=memberPw], #password-check").removeClass("success fail");
      state.memberPwValid = false;
      state.memberPwCheckValid = false;
      return;
    }

    const pwValid = pwRegex.test(pw);
    const match = pw.length > 0 && pw === pw2;

    $("[name=memberPw]").removeClass("success fail").addClass(pwValid ? "success" : "fail");
    $("#password-check").removeClass("success fail").addClass(match ? "success" : "fail");

    state.memberPwValid = pwValid;
    state.memberPwCheckValid = match;
  });

  //------------------------------------------------------
  // [3] 닉네임 중복 검사
  //------------------------------------------------------
  $("[name=memberNickname]").on("blur", function(){
    const nick = $(this).val().trim();
    const regex = /^[가-힣0-9]{2,10}$/;
    const input = $(this);

    if (!nick) { // ✅ 아무것도 안 썼으면 리셋
      input.removeClass("success fail fail2");
      state.memberNicknameValid = false;
      return;
    }

    if (!regex.test(nick)) {
      input.removeClass("success fail2").addClass("fail");
      state.memberNicknameValid = false;
      return;
    }

    $.ajax({
      url: `${ctx}/rest/member/checkMemberNickname`,
      method: "get",
      data: { memberNickname: nick },
      success: function(res){
        if (res) {
          input.removeClass("success fail").addClass("fail2");
          state.memberNicknameValid = false;
        } else {
          input.removeClass("fail fail2").addClass("success");
          state.memberNicknameValid = true;
        }
      }
    });
  });

  //------------------------------------------------------
  // [4] 이름 검사
  //------------------------------------------------------
  $("[name=memberName]").on("blur", function(){
    const val = $(this).val().trim();
    if (!val) { // ✅ 빈칸이면 리셋
      $(this).removeClass("success fail");
      state.memberNameValid = false;
      return;
    }
    const regex = /^[가-힣]{2,6}$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberNameValid = regex.test(val);
  });

  //------------------------------------------------------
  // [5] 생년월일 검사
  //------------------------------------------------------
  $("[name=memberBirth]").on("blur", function(){
    const val = $(this).val().trim();
    state.memberBirthValid = val.length > 0;
  });

  //------------------------------------------------------
  // [6] MBTI 검사
  //------------------------------------------------------
  $("[name=memberMbti]").on("blur change", function(){
    const val = $(this).val().trim().toUpperCase();
    if (!val) {
      $(this).removeClass("success fail");
      state.memberMbtiValid = false;
      return;
    }
    const regex = /^[IE][SN][FT][PJ]$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberMbtiValid = regex.test(val);
  });

  //------------------------------------------------------
  // [7] 연락처 검사
  //------------------------------------------------------
  $("[name=memberContact]").on("blur", function(){
    const val = $(this).val().trim();
    if (!val) {
      $(this).removeClass("success fail");
      state.memberContactValid = false;
      return;
    }
    const regex = /^010-[1-9][0-9]{3}-[0-9]{4}$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberContactValid = regex.test(val);
  });

  //------------------------------------------------------
  // [8] 이메일 인증
  //------------------------------------------------------
  let certTimer = null;
  let remain = 300;

  $(".btn-cert-send").on("click", function(){
    const email = $("[name=memberEmail]").val().trim();
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+$/;
    if(!regex.test(email)){
      $("[name=memberEmail]").removeClass("success fail fail2").addClass("fail");
      state.memberEmailValid = false;
      return;
    }
    $.ajax({
      url: `${ctx}/rest/member/certSend`,
      method: "post",
      data: { certEmail: email },
      success: function(){
        $(".cell-cert-input").show();
        startTimer();
        alert("인증번호가 발송되었습니다!");
      },
      error: function(){
        alert("메일 발송 중 오류 발생");
      }
    });
  });

  $(".btn-cert-check").on("click", function(){
    const certEmail = $("[name=memberEmail]").val();
    const certNumber = $(".cert-input").val();
    $.ajax({
      url: `${ctx}/rest/member/certCheck`,
      method: "post",
      data: { certEmail: certEmail, certNumber: certNumber },
      success: function(res){
        if(res){
          alert("이메일 인증 성공!");
          $("[name=memberEmail]").prop("readonly", true).addClass("success");
          state.memberEmailValid = true;
          clearInterval(certTimer);
        } else {
          alert("인증번호가 일치하지 않습니다.");
          state.memberEmailValid = false;
        }
      }
    });
  });

  function startTimer(){
    remain = 300;
    clearInterval(certTimer);
    certTimer = setInterval(function(){
      remain--;
      if(remain <= 0){
        clearInterval(certTimer);
        state.memberEmailValid = false;
      }
    },1000);
  }

  //------------------------------------------------------
  // [9] 주소 검사
  //------------------------------------------------------
  $(".btn-address-search").on("click", function(){
    new daum.Postcode({
      oncomplete: function(data){
  $("[name=memberPostcode]").val(data.zonecode);
        $("[name=memberAddress1]").val(data.address);
        $("[name=memberAddress2]").focus();
      }
    }).open();
  });

  //------------------------------------------------------
  // [10] 최종 제출
  //------------------------------------------------------
  $(".check-form").on("submit", function(){
    if(!state.ok()){
      alert("❌ 필수 정보를 정확히 입력해야 가입이 가능합니다.");
      return false;
    }
    return true;
  });
});
