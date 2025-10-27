$(function () {
  const ctx = window.contextPath || "";

  //------------------------------------------------------
  // 상태 관리
  //------------------------------------------------------
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
    ok() {
      return (
        this.memberIdValid &&
        this.memberPwValid &&
        this.memberPwCheckValid &&
        this.memberNicknameValid &&
        this.memberNameValid &&
        this.memberBirthValid &&
        this.memberMbtiValid &&
        this.memberContactValid &&
        this.memberEmailValid &&
        this.memberAddressValid
      );
    },
  };

  //------------------------------------------------------
  // [1] 아이디 중복 검사
  //------------------------------------------------------
  $("[name=memberId]").off("blur").on("blur", function () {
    const id = $(this).val().trim();
    const regex = /^[a-z][a-z0-9]{4,19}$/;
    const input = $(this);

    if (!id) {
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
      success(res) {
        if (res) {
          input.removeClass("success fail").addClass("fail2");
          state.memberIdValid = false;
        } else {
          input.removeClass("fail fail2").addClass("success");
          state.memberIdValid = true;
        }
      },
    });
  });

  //------------------------------------------------------
  // [2] 비밀번호 및 확인
  //------------------------------------------------------
  const pwRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,16}$/;

  $("[name=memberPw], #password-check")
    .off("blur")
    .on("blur", function () {
      const pw = $("[name=memberPw]").val().trim();
      const pw2 = $("#password-check").val().trim();

      if (!pw && !pw2) {
        $("[name=memberPw], #password-check").removeClass("success fail");
        state.memberPwValid = false;
        state.memberPwCheckValid = false;
        return;
      }

      const pwValid = pwRegex.test(pw);
      const match = pw.length > 0 && pw === pw2;

      $("[name=memberPw]")
        .removeClass("success fail")
        .addClass(pwValid ? "success" : "fail");
      $("#password-check")
        .removeClass("success fail")
        .addClass(match ? "success" : "fail");

      state.memberPwValid = pwValid;
      state.memberPwCheckValid = match;
    });

  //------------------------------------------------------
  // [3] 닉네임 중복 검사 (아이디 blur와 완전 분리)
  //------------------------------------------------------
  $("[name=memberNickname]").off("blur").on("blur", function () {
    const nick = $(this).val().trim();
    const regex = /^[가-힣0-9]{2,10}$/;
    const input = $(this);

    if (!nick) {
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
      success(res) {
        if (res) {
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
  // [4~9] 나머지는 동일
  //------------------------------------------------------

  // 이름 검사
  $("[name=memberName]").off("blur").on("blur", function () {
    const val = $(this).val().trim();
    if (!val) {
      $(this).removeClass("success fail");
      state.memberNameValid = false;
      return;
    }
    const regex = /^[가-힣]{2,6}$/;
    $(this)
      .removeClass("success fail")
      .addClass(regex.test(val) ? "success" : "fail");
    state.memberNameValid = regex.test(val);
  });

  // 생년월일 검사
  $("[name=memberBirth]").off("blur").on("blur", function () {
    const val = $(this).val().trim();
    state.memberBirthValid = val.length > 0;
  });

  // MBTI 검사
  $("[name=memberMbti]").off("blur change").on("blur change", function () {
    const val = $(this).val().trim().toUpperCase();
    if (!val) {
      $(this).removeClass("success fail");
      state.memberMbtiValid = false;
      return;
    }
    const regex = /^[IE][SN][FT][PJ]$/;
    $(this)
      .removeClass("success fail")
      .addClass(regex.test(val) ? "success" : "fail");
    state.memberMbtiValid = regex.test(val);
  });

  // 연락처 검사
  $("[name=memberContact]").off("blur").on("blur", function () {
    const val = $(this).val().trim();
    if (!val) {
      $(this).removeClass("success fail");
      state.memberContactValid = false;
      return;
    }
    const regex = /^010-[1-9][0-9]{3}-[0-9]{4}$/;
    $(this)
      .removeClass("success fail")
      .addClass(regex.test(val) ? "success" : "fail");
    state.memberContactValid = regex.test(val);
  });

  // 이메일 인증
  $(".btn-cert-send").off("click").on("click", function () {
    const email = $("[name=memberEmail]").val().trim();
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+$/;
    if (!regex.test(email)) {
      $("[name=memberEmail]").removeClass("success fail fail2").addClass("fail");
      state.memberEmailValid = false;
      return;
    }
    $.ajax({
      url: `${ctx}/rest/member/certSend`,
      method: "post",
      data: { certEmail: email },
      success() {
        $(".cell-cert-input").show();
        alert("인증번호가 발송되었습니다!");
      },
      error() {
        alert("메일 발송 중 오류 발생");
      },
    });
  });

  $(".btn-cert-check").off("click").on("click", function () {
    const certEmail = $("[name=memberEmail]").val();
    const certNumber = $(".cert-input").val();
    $.ajax({
      url: `${ctx}/rest/member/certCheck`,
      method: "post",
      data: { certEmail, certNumber },
      success(res) {
        if (res) {
          alert("이메일 인증 성공!");
          $("[name=memberEmail]").prop("readonly", true).addClass("success");
          state.memberEmailValid = true;
        } else {
          alert("인증번호가 일치하지 않습니다.");
          state.memberEmailValid = false;
        }
      },
    });
  });

  // 주소 검색
  $(".btn-address-search").off("click").on("click", function () {
    new daum.Postcode({
      oncomplete(data) {
        $("[name=memberPost]").val(data.zonecode);
        $("[name=memberAddress1]").val(data.address);
        $("[name=memberAddress2]").focus();
      },
    }).open();
  });

  //------------------------------------------------------
  // [10] 최종 제출
  //------------------------------------------------------
  $(".check-form").off("submit").on("submit", function () {
    if (!state.ok()) {
      alert("❌ 필수 정보를 정확히 입력해야 가입이 가능합니다.");
      return false;
    }
    return true;
  });
});