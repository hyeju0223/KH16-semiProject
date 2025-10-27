// ==================== 중앙 모달 알림 (단일 정의 + alert 가로채기) ====================
(() => {
  let _modalOpen = false; // 중복 방지

  window.niceAlert = function niceAlert(message) {
    return new Promise((resolve) => {
      if (_modalOpen) return resolve(); // 이미 열려있으면 무시
      _modalOpen = true;

      const wrap = document.createElement("div");
      wrap.className = "na-backdrop";
      wrap.innerHTML = `
        <div class="na-dialog" role="dialog" aria-modal="true">
          <div class="na-title">${message ?? ""}</div>
          <div class="na-actions">
            <button type="button" class="na-btn">확인</button>
          </div>
        </div>
      `;
      document.body.appendChild(wrap);

      const close = () => {
        if (!wrap.parentNode) return;
        wrap.remove();
        _modalOpen = false;
        resolve();
      };
      wrap.querySelector(".na-btn").addEventListener("click", close);
      wrap.addEventListener("click", (e) => {
        if (e.target === e.currentTarget) close();
      });
      document.addEventListener("keydown", function onEsc(e){
        if (e.key === "Escape") { document.removeEventListener("keydown", onEsc); close(); }
      });
    });
  };

  // ✅ 다른 스크립트의 window.alert도 모두 모달로 강제 라우팅
  const __nativeAlert = window.alert.bind(window);
  window.alert = function (msg) {
    try { return window.niceAlert(String(msg)); }
    catch (e) { return __nativeAlert(msg); }
  };
})();

// ==================== 가입 폼 스크립트 ====================
$(function () {
  const ctx = window.ctx || "";

  // ---------- 상태 토글 (input + 그룹 상태) ----------
  function setState($input, state) {
    const $g = $input.closest(".field-group");
    $g.addClass("touched");

    $input.removeClass("success fail fail2");
    if (state) $input.addClass(state);

    $g.removeClass("g-success g-fail g-fail2");
    if (state === "success") $g.addClass("g-success");
    else if (state === "fail") $g.addClass("g-fail");
    else if (state === "fail2") $g.addClass("g-fail2");
  }

  // ---------- [1] 아이디 ----------
  const $id = $("[name=memberId]");
  $id.off("blur").on("blur", function () {
    const v = $id.val().trim();
    const re = /^[a-z][a-z0-9]{4,19}$/;
    if (!v) return setState($id, null);
    if (!re.test(v)) return setState($id, "fail");

    $.ajax({
      url: `${ctx}/rest/member/checkMemberId`,
      method: "get",
      data: { memberId: v },
      success(res) { setState($id, res ? "fail2" : "success"); }, // true=중복 → fail2
    });
  });

  // ---------- [2] 비밀번호/확인 ----------
  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,16}$/;
  const $pw  = $("[name=memberPw]");
  const $pw2 = $("#password-check");

  $pw.off("input blur").on("blur", function () {
    const v = $pw.val().trim();
    if (!v) return setState($pw, null);
    setState($pw, pwRegex.test(v) ? "success" : "fail");
  });

  $pw2.off("input keyup change blur").on("blur", function () {
    const v1 = $pw.val().trim();
    const v2 = $pw2.val().trim();
    if (!v2) return setState($pw2, null);
    if (!v1) return setState($pw2, "fail");
    setState($pw2, v1 === v2 ? "success" : "fail");
  });

  // 비번 타이핑 중엔 확인칸 상태 중립화
  $pw.on("input", function () {
    if ($pw2.val()) setState($pw2, null);
  });

  // ---------- [3] 닉네임 ----------
  const $nick = $("[name=memberNickname]");
  $nick.off("blur").on("blur", function () {
    const v = $nick.val().trim();
    const re = /^[가-힣0-9]{2,10}$/;
    if (!v) return setState($nick, null);
    if (!re.test(v)) return setState($nick, "fail");

    $.ajax({
      url: `${ctx}/rest/member/checkMemberNickname`,
      method: "get",
      data: { memberNickname: v },
      success(res) { setState($nick, res ? "fail2" : "success"); },
    });
  });

  // ---------- [4] 이름 ----------
  const $name = $("[name=memberName]");
  $name.off("blur").on("blur", function () {
    const v = $name.val().trim();
    if (!v) return setState($name, null);
    setState($name, /^[가-힣]{2,6}$/.test(v) ? "success" : "fail");
  });

  // ---------- [5] 생년월일 ----------
  const $birth = $("[name=memberBirth]");
  $birth.off("change blur input").on("change blur input", function () {
    const v = $birth.val();
    if (!v) return setState($birth, null);
    const today = new Date().toISOString().slice(0, 10);
    if (!/^\d{4}-\d{2}-\d{2}$/.test(v)) return setState($birth, "fail");
    if (v > today) return setState($birth, "fail2");
    setState($birth, "success");
  });

  // ---------- [6] MBTI ----------
  const $mbti = $("[name=memberMbti]");
  $mbti.off("change blur").on("change blur", function () {
    const v = ($mbti.val() || "").trim().toUpperCase();
    if (!v) return setState($mbti, null);
    setState($mbti, /^[IE][SN][FT][PJ]$/.test(v) ? "success" : "fail");
  });

  // ---------- [7] 연락처 ----------
  const $contact = $("[name=memberContact]");
  $contact.off("blur").on("blur", function () {
    const v = $contact.val().trim();
    if (!v) return setState($contact, null);
    setState($contact, /^010-[1-9][0-9]{3}-[0-9]{4}$/.test(v) ? "success" : "fail");
  });
  // ===================== [8] 이메일 인증 (보내기 + 확인) =====================
  (function(){
    const SEND_COOLDOWN_SEC = 60;

    function isEmail(s){ return /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(s || ""); }

    let sendTimer = null;
    function startCooldown($btn){
      let left = SEND_COOLDOWN_SEC;
      $btn.prop("disabled", true).text(`재발송(${left}s)`);
      clearInterval(sendTimer);
      sendTimer = setInterval(()=>{
        left--;
        if (left <= 0) {
          clearInterval(sendTimer);
          $btn.prop("disabled", false).text("인증번호 보내기");
        } else {
          $btn.text(`재발송(${left}s)`);
        }
      }, 1000);
    }

    // ✅ 하단 안내문 표시
    function showEmailHint(msg, ok){
      $("#emailHint")
        .text(msg)
        .removeClass("ok err")
        .addClass(ok ? "ok" : "err")
        .show();
    }

    // 8-1) 인증번호 보내기
    $(".btn-cert-send").off("click").on("click", function(){
      const $email = $("[name=memberEmail]");
      const email  = $email.val().trim();
      if (!isEmail(email)) { setState($email, "fail"); return; }

      setState($email, null);
      const $btn = $(this);

      $.ajax({
        url: `${ctx}/rest/member/certSend`,
        method: "post",
        data: { certEmail: email },
        success() {
          $(".cell-cert-input").show();
          startCooldown($btn);
          showEmailHint("인증번호를 발송했습니다. 메일함을 확인해 주세요.", true);
        },
        error() {
          niceAlert("메일 발송 중 오류가 발생했습니다.");
        }
      });
    });

    // 응답 OK 판정
    function parseOk(res){
      try{
        if (typeof res === "boolean") return res;
        if (typeof res === "number")  return res === 1;
        if (typeof res === "string"){
          const s = res.trim().toLowerCase();
          if (s === "true" || s === "1" || s === "ok" || s === "success") return true;
          const j = JSON.parse(res);
          return !!(j?.result ?? j?.ok ?? j?.success);
        }
        if (res && typeof res === "object"){
          return !!(res.result ?? res.ok ?? res.success);
        }
      }catch(e){}
      return false;
    }

    // 8-2) 인증번호 확인
    $(".btn-cert-check").off("click").on("click", function () {
      const $email     = $("[name=memberEmail]");
      const certEmail  = $email.val().trim();
      const certNumber = $(".cert-input").val().trim();

      if (!certEmail || !certNumber) {
        niceAlert("이메일과 인증번호를 입력해 주세요.");
        return;
      }

      $.ajax({
        url: `${ctx}/rest/member/certCheck`,
        method: "post",
        data: { certEmail, certNumber },
        success(res) {
          const ok = parseOk(res);
          if (ok) {
            $email.prop("readonly", true);
            $(".cert-input").prop("readonly", true);
            $(".btn-cert-check").prop("disabled", true);
            setState($email, "success");
            showEmailHint("인증번호 확인이 완료되었습니다.", true);
            // 필요 시 자동 이동:
            // $(".page[data-step='3'] .btn-next").trigger("click");
          } else {
            setState($email, "fail");
            showEmailHint("인증번호가 일치하지 않습니다.", false);
          }
        },
        error() {
          niceAlert("인증 확인 요청 중 오류가 발생했습니다.");
        }
      });
    });
  })();




  // ---------- [9] 주소 검색 ----------
  $(".btn-address-search").off("click").on("click", async function () {
    if (!window.daum || !daum.Postcode) { await niceAlert("주소 검색 스크립트를 불러오지 못했습니다."); return; }
    new daum.Postcode({
      oncomplete(data) {
        $("[name=memberPostcode]").val(data.zonecode);
        $("[name=memberAddress1]").val(data.address);
        $("[name=memberAddress2]").focus();
      },
    }).open();
  });

  // ---------- [9.5] 프로필 이미지 미리보기 ----------
  $(document).on("change", "input[name=attach]", function () {
    const f = this.files && this.files[0];
    const $img = $(".img-preview");
    if (!f) return;
    const reader = new FileReader();
    reader.onload = (e) => $img.attr("src", e.target.result);
    reader.readAsDataURL(f);
  });

  // ---------- [10] 폼 제출 전 최종 검사 ----------
  $(".check-form").off("submit").on("submit", async function () {
    $("[name=memberId],[name=memberPw],#password-check,[name=memberNickname],[name=memberName],[name=memberBirth],[name=memberMbti],[name=memberContact],[name=memberEmail]")
      .each(function(){ $(this).triggerHandler("blur"); });

    const mustBeSuccess = [
      $("[name=memberId]"), $("[name=memberPw]"), $("#password-check"),
      $("[name=memberNickname]"), $("[name=memberName]"), $("[name=memberBirth]"),
      $("[name=memberMbti]"), $("[name=memberContact]"), $("[name=memberEmail]"),
    ];
    const allOk = mustBeSuccess.every($el => $el.hasClass("success"));
    if (!allOk) { await niceAlert("필수 정보를 정확히 입력해야 가입이 가능합니다."); return false; }
    return true;
  });

  // ---------- [NEXT] 스텝별 선검사 후 모달 ----------
  function pageOk($page){
    const step = Number($page.data("step"));
    let req = [];
    if (step === 1) {
      req = [$("[name=memberId]"), $("[name=memberPw]"), $("#password-check"), $("[name=memberNickname]")];
    } else if (step === 2) {
      req = [$("[name=memberName]"), $("[name=memberBirth]"), $("[name=memberMbti]"), $("[name=memberContact]")];
    } else if (step === 3) {
      req = [$("[name=memberEmail]")]; // 인증 성공 시 .success가 붙음
    }
    req.forEach($el => $el.triggerHandler("blur"));
    return req.every($el => $el.hasClass("success"));
  }

  $(document).off("click.validateNext").on("click.validateNext", ".btn-next", async function (e) {
    const $page = $(this).closest(".page");
    if (!pageOk($page)) {
      e.preventDefault();
      e.stopImmediatePropagation(); // multipage.js 이동 막기
      await niceAlert("입력 형식을 다시 확인해주세요.");
    }
  });

  // ---------- 눈(보기/숨기기) ----------
  $(document).on("click", ".btn-eye", function (e) {
    e.preventDefault();
    const sel = $(this).data("target");
    const $i  = $(sel);
    const show = $i.attr("type") === "password";
    $i.attr("type", show ? "text" : "password");
    $(this).toggleClass("on", show)
           .attr("aria-pressed", show)
           .attr("aria-label", show ? "비밀번호 숨기기" : "비밀번호 보기");
  });
});
