// 회원가입 전체 페이지 통합 스크립트
$(function(){

  // ------------------------------
  // 🔹 현재 페이지 인덱스 추적
  // ------------------------------
  let currentPage = 0;
  const pages = $(".page");
  pages.hide().eq(0).show(); // 첫 페이지 보이기

  // 🔹 다음 버튼 클릭 시
  $(".btn-next").on("click", function(){
      if(currentPage < pages.length - 1){
          pages.eq(currentPage).hide();
          currentPage++;
          pages.eq(currentPage).fadeIn(200);
      }
  });

  // 🔹 이전 버튼 클릭 시
  $(".btn-prev").on("click", function(){
      if(currentPage > 0){
          pages.eq(currentPage).hide();
          currentPage--;
          pages.eq(currentPage).fadeIn(200);
      }
  });

  // ------------------------------
  // 🔹 상태 객체 정의
  // ------------------------------
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
             this.memberNicknameValid && this.memberNameValid && this.memberEmailValid &&
             this.memberMbtiValid && this.memberContactValid && this.memberAddressValid;
    }
  };

  // [1] 아이디 검사 — DB: ^[a-z][a-z0-9]{4,19}$
  $("[name=memberId]").on("blur", function(){
    const regex = /^[a-z][a-z0-9]{4,19}$/;
    const val = $(this).val().trim();
    if(!regex.test(val)){
      $(this).removeClass("success fail fail2").addClass("fail");
      state.memberIdValid = false;
      return;
    }
    $.ajax({
      url: "/rest/member/checkMemberId",
      data: {memberId: val},
      success: function(resp){
        if(resp){ // 존재함
          $("[name=memberId]").removeClass("success fail fail2").addClass("fail2");
          state.memberIdValid = false;
        } else {
          $("[name=memberId]").removeClass("success fail fail2").addClass("success");
          state.memberIdValid = true;
        }
      }
    });
  });

  // [2] 비밀번호 검사 (보안상 강화)
  const pwRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$])[A-Za-z0-9!@#$]{8,16}$/;
  $("[name=memberPw], #password-check").on("blur input", function(){
    const pw = $("[name=memberPw]").val();
    const pw2 = $("#password-check").val();
    const pwValid = pwRegex.test(pw);
    $("[name=memberPw]").removeClass("success fail").addClass(pwValid ? "success" : "fail");
    state.memberPwValid = pwValid;

    const match = pw.length > 0 && pw === pw2;
    $("#password-check").removeClass("success fail").addClass(match ? "success" : "fail");
    state.memberPwCheckValid = match;
  });

  // [3] 닉네임 검사 — DB: ^[가-힣0-9]{2,10}$
  $("[name=memberNickname]").on("blur", function(){
    const regex = /^[가-힣0-9]{2,10}$/;
    const val = $(this).val().trim();
    if(!regex.test(val)){
      $(this).removeClass("success fail fail2").addClass("fail");
      state.memberNicknameValid = false;
      return;
    }
    $.ajax({
      url: "/rest/member/checkMemberNickname",
      data: {memberNickname: val},
      success: function(resp){
        if(resp){
          $("[name=memberNickname]").removeClass("success fail fail2").addClass("fail2");
          state.memberNicknameValid = false;
        } else {
          $("[name=memberNickname]").removeClass("success fail fail2").addClass("success");
          state.memberNicknameValid = true;
        }
      }
    });
  });

  // [4] 이름 검사 — DB: ^[가-힣]{2,6}$
  $("[name=memberName]").on("blur", function(){
    const regex = /^[가-힣]{2,6}$/;
    const val = $(this).val().trim();
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberNameValid = regex.test(val);
  });

  // [5] 이메일 인증
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
    const certNum = $(".cert-input").val().trim();
    const regex = /^[0-9]{6}$/;
    if(!regex.test(certNum)){
      $(".cert-input").removeClass("success fail fail2").addClass("fail");
      return;
    }
    const certEmail = $("[name=memberEmail]").val().trim();
    $.ajax({
      url: "/rest/member/certCheck",
      method: "post",
      data: {certEmail, certNumber: certNum},
      success: function(resp){
        if(resp){
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

  // [6] MBTI 검사
  $("[name=memberMbti]").on("change blur", function(){
    const val = $(this).val().toUpperCase();
    const regex = /^[IE][SN][FT][PJ]$/;
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberMbtiValid = regex.test(val);
  });

  // [7] 연락처 검사
  $("[name=memberContact]").on("blur", function(){
    const regex = /^010-[1-9][0-9]{3}-[0-9]{4}$/;
    const val = $(this).val().trim();
    $(this).removeClass("success fail").addClass(regex.test(val) ? "success" : "fail");
    state.memberContactValid = regex.test(val);
  });

  // [8] 주소 검사
  $("[name=memberAddress2]").on("blur", function(){
    const post = $("[name=memberPostcode]").val().trim();
    const addr1 = $("[name=memberAddress1]").val().trim();
    const addr2 = $("[name=memberAddress2]").val().trim();
    const allFill = post && addr1 && addr2;
    const allEmpty = !post && !addr1 && !addr2;
    const valid = allFill || allEmpty;
    $("[name=memberPostcode],[name=memberAddress1],[name=memberAddress2]")
      .removeClass("success fail").addClass(valid ? "success" : "fail");
    state.memberAddressValid = valid;
  });

    $(".btn-address-search, [name=memberPostcode], [name=memberAddress1]").on("click", findAddress);

  
    function findAddress() {
      new daum.Postcode({
        oncomplete: function(data) {
          $("[name=memberPostcode]").val(data.zonecode);
          $("[name=memberAddress1]").val(data.address);
          $("[name=memberAddress2]").focus();
        }
      }).open();
    }


  // [9] 프로필 이미지
  $("[name=attach]").on("input", function(){
    const url = $(".img-preview").prop("src");
    if(url && url.startsWith("blob:")) URL.revokeObjectURL(url);
    if(this.files.length == 0){
      $(".img-preview").prop("src", "./images/no-image.png");
    } else {
      const blob = URL.createObjectURL(this.files[0]);
      $(".img-preview").prop("src", blob);
    }
  });

  // [10] 최종 제출
  $(".join-form").on("submit", function(){
    if(!state.ok()){
      alert("필수 정보를 모두 정확히 입력해주세요.");
      return false;
    }
    return true;
  });
});
