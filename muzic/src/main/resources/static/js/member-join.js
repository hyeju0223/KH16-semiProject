$(function(){
  const ctx = window.contextPath || "";

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
  // [1] 아이디 중복검사
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
      url: `${ctx}/rest/member/checkMemberId`,
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
  // [2] 비밀번호 검사
  //------------------------------------------------------
  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,16}$/;
  $("[name=memberPw], #password-check").on("blur", function(){
    const pw = $("[name=memberPw]").val();
    const pw2 = $("#password-check").val();
    const valid = pwRegex.test(pw);
    $("[name=memberPw]").removeClass("success fail").addClass(valid ? "success" : "fail");
    state.memberPwValid = valid;
    const match = pw.length > 0 && pw === pw2;
    $("#password-check").removeClass("success fail").addClass(match ? "success" : "fail");
    state.memberPwCheckValid = match;
  });

  //------------------------------------------------------
  // [3] 닉네임 중복검사
  //------------------------------------------------------
  
  $("[name=memberNickname]").on("blur", function(){
    const input = $(this);
    const value = input.val().trim();
    const regex = /^[가-힣0-9]{2,10}$/;

    // ✅ 아무것도 안 쳤을 때: 상태/클래스 모두 초기화
    if (value.length === 0) {
      input.removeClass("success fail fail2");
      input.next(".feedback").text(""); // 피드백 문구도 초기화
      state.memberNicknameValid = false;
      return;
    }

    if (!regex.test(value)) {
      input.removeClass("success fail2").addClass("fail");
      state.memberNicknameValid = false;
      return;
    }

    $.ajax({
      url: `${ctx}/rest/member/checkMemberNickname`,
      data: { memberNickname: value },
      success(resp) {
        if (resp) {
          input.removeClass("success fail").addClass("fail2");
          state.memberNicknameValid = false;
        } else {
          input.removeClass("fail fail2").addClass("success");
          state.memberNicknameValid = true;
        }
      },
    });
  });

  //------------------------------------------------------
  // [4] 이름 검사
  //------------------------------------------------------
  $("[name=memberName]").on("blur", function(){
    const val = $(this).val().trim();
    const regex = /^[가-힣]{2,6}$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberNameValid = regex.test(val);
  });

  //------------------------------------------------------
  // [5] 이메일 인증
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
      data: {certEmail: email},
      success: function(){
        $(".cell-cert-input").show();
        startTimer();
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
      url: `${ctx}/rest/member/certCheck`,
      method: "post",
      data: {certEmail: email, certNumber: code},
      success: function(resp){
        if(resp){
          clearInterval(certTimer);
          $(".cert-timer").text("인증 완료 ✅");
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

  //------------------------------------------------------
  // [6] MBTI 검사
  //------------------------------------------------------
  $("[name=memberMbti]").on("blur", function(){
    const val = $(this).val().trim().toUpperCase();
    const regex = /^[IE][SN][FT][PJ]$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberMbtiValid = regex.test(val);
  });

  //------------------------------------------------------
  // [7] 연락처 검사
  //------------------------------------------------------
  $("[name=memberContact]").on("blur", function(){
    const val = $(this).val().trim();
    const regex = /^010-[1-9][0-9]{3}-[0-9]{4}$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberContactValid = regex.test(val);
  });

  //------------------------------------------------------
  // [8] 주소 검색
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

  //------------------------------------------------------
  // [9] 최종 제출
  //------------------------------------------------------
  $(".check-form").on("submit", function(){
    if(!state.ok()){
      alert("❌ 필수 정보를 정확히 입력해야 가입이 가능합니다.");
      return false;
    }
    return true;
  });
});