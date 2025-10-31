
/* ==================== 중앙 모달 알림 (alert 가로채기) ==================== */
(() => {
  let _open = false;
  window.niceAlert = function (message) {
    return new Promise((resolve) => {
      if (_open) return resolve();
      _open = true;

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
        _open = false;
        resolve();
      };
      wrap.querySelector(".na-btn").addEventListener("click", close);
      wrap.addEventListener("click", (e) => { if (e.target === e.currentTarget) close(); });
      document.addEventListener("keydown", function onEsc(e){
        if (e.key === "Escape") { document.removeEventListener("keydown", onEsc); close(); }
      });
    });
  };

  const _native = window.alert.bind(window);
  window.alert = function (msg) {
    try { return window.niceAlert(String(msg)); }
    catch { return _native(msg); }
  };
})();

/* ============================== 가입 폼 ============================== */
$(function () {
  const ctx = window.ctx || "";

  /* ---------- 유틸 ---------- */
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
  function isEmail(s){ return /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(s || ""); }

  /* ========== [1] 아이디 ========== */
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
      success(res) { setState($id, res ? "fail2" : "success"); }, // true=중복
      error() { setState($id, "success"); } // API 없으면 일단 통과
    });
  });

  /* ========== [2] 비밀번호/확인 ========== */
  const pwRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,16}$/;
  const $pw  = $("[name=memberPw]");
  const $pw2 = $("#password-check");

  $pw.off("blur input").on("blur", function () {
    const v = $pw.val().trim();
    if (!v) return setState($pw, null);
    setState($pw, pwRegex.test(v) ? "success" : "fail");
  }).on("input", function(){
    if ($pw2.val()) setState($pw2, null);
  });

  $pw2.off("blur").on("blur", function () {
    const v1 = $pw.val().trim();
    const v2 = $pw2.val().trim();
    if (!v2) return setState($pw2, null);
    if (!v1) return setState($pw2, "fail");
    setState($pw2, v1 === v2 ? "success" : "fail");
  });

  /* ========== [3] 닉네임 ========== */
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
      error() { setState($nick, "success"); }
    });
  });

  /* ========== [4] 이름 ========== */
  const $name = $("[name=memberName]");
  $name.off("blur").on("blur", function () {
    const v = $name.val().trim();
    if (!v) return setState($name, null);
    setState($name, /^[가-힣]{2,6}$/.test(v) ? "success" : "fail");
  });

  /* ========== [5] 생년월일 ========== */
  const $birth = $("[name=memberBirth]");
  $birth.off("change blur input").on("change blur input", function () {
    const v = $birth.val();
    if (!v) return setState($birth, null);
    const today = new Date().toISOString().slice(0, 10);
    if (!/^\d{4}-\d{2}-\d{2}$/.test(v)) return setState($birth, "fail");
    if (v > today) return setState($birth, "fail2");
    setState($birth, "success");
  });

  /* ========== [6] MBTI ========== */
  const $mbti = $("[name=memberMbti]");
  $mbti.off("change blur").on("change blur", function () {
    const v = ($mbti.val() || "").trim().toUpperCase();
    if (!v) return setState($mbti, null);
    setState($mbti, /^[IE][SN][FT][PJ]$/.test(v) ? "success" : "fail");
  });

  /* ========== [7] 연락처 ========== */
  const $contact = $("[name=memberContact]");
  $contact.off("blur").on("blur", function () {
    const v = $contact.val().trim();
    if (!v) return setState($contact, null);
    setState($contact, /^010-[1-9][0-9]{3}-[0-9]{4}$/.test(v) ? "success" : "fail");
  });

  /* ========== [8] 이메일 인증 (보내기/확인/타이머) ========== */

  (function () {
    const SEND_COOLDOWN_SEC = 60;
    const CERT_EXPIRE_SEC   = 300;

    let certDeadline = null;
    let certTick     = null;
    let certExpired  = false;

    function fmt(sec){ const m=Math.floor(sec/60),s=sec%60; return `${m}:${String(s).padStart(2,"0")}`; }
    function stopCertTimer(){ if (certTick) clearInterval(certTick); certTick=null; }
    function startCertTimer(totalSec = CERT_EXPIRE_SEC){
      stopCertTimer(); certExpired=false; certDeadline=Date.now()+totalSec*1000;
      const $box=$("#certTimer"), $txt=$("#certTimerText");
      $box.removeClass("expired").show(); $txt.text(fmt(totalSec));
      $(".btn-cert-check").prop("disabled", false);
      $(".cert-input").prop("readonly", false);

      certTick=setInterval(()=>{
        const left=Math.max(0, Math.ceil((certDeadline-Date.now())/1000));
        $txt.text(fmt(left));
        if (left<=0){
          stopCertTimer(); certExpired=true; $box.addClass("expired"); $txt.text("0:00");
          $(".btn-cert-check").prop("disabled",true);
          $(".cert-input").prop("readonly",true);
          $(".cert-feedback").removeClass("ok").addClass("error")
            .text("인증 유효시간이 만료되었습니다. ‘인증번호 보내기’를 눌러 재전송하세요.").show();
        }
      },250);
    }
    function startCooldown($btn){
      let left=SEND_COOLDOWN_SEC;
      $btn.prop("disabled",true).text(`재발송(${left}s)`);
      const t=setInterval(()=>{
        left--;
        if(left<=0){clearInterval(t);$btn.prop("disabled",false).text("인증번호 보내기");}
        else {$btn.text(`재발송(${left}s)`);}
      },1000);
    }
    function showEmailHint(msg, ok){ $("#emailHint").text(msg).removeClass("ok err").addClass(ok?"ok":"err").show(); }
    function resetEmailUIForResend(){
      $(".cert-input").val("").prop("readonly",false);
      $(".btn-cert-check").prop("disabled",false);
      $(".cert-feedback").hide().text("").removeClass("ok error");
      $("#certTimer").removeClass("expired").hide();
      $("#certTimerText").text("05:00");
      certExpired=false;
    }

    // ===== 요소 바인딩 =====
    const $email = $("[name=memberEmail]");

    // ===== 이메일 중복 체크 (blur 시) =====
    $email.off("blur").on("blur", function(){
      const v = $email.val().trim();
      if (!v) return setState($email, null);
      if (!isEmail(v)) return setState($email, "fail");

      $.ajax({
        url: `${ctx}/rest/member/checkMemberEmail`,
        method: "get",
        data: { memberEmail: v },
        dataType: "json",                       // 서버 응답: { exists: true/false }
        success(res){
          const dup = !!(res && (res.exists === true || res.exists === 1));
          setState($email, dup ? "fail2" : "success");
        },
        error(){ setState($email, "success"); } // 장애 시 일단 진행 가능
      });
    });

    // ===== [8-1] 인증번호 보내기 =====
    $(".btn-cert-send").off("click").on("click", function(){
      const email = $email.val().trim();
      if (!isEmail(email)) { setState($email, "fail"); return; }
      if ($email.hasClass("fail2")) { niceAlert("이미 사용 중인 이메일입니다. 다른 이메일을 입력해 주세요."); return; }

      const $btn=$(this);
      $.ajax({
        url: `${ctx}/rest/member/certSend`,
        method: "post",
        data: { certEmail: email },
        success(){
          $(".cell-cert-input").show();
          resetEmailUIForResend();
          startCooldown($btn);
          startCertTimer(CERT_EXPIRE_SEC);
          showEmailHint("인증번호를 발송했습니다. 메일함을 확인해 주세요.", true);
        },
        error(){ niceAlert("메일 발송 중 오류가 발생했습니다."); }
      });
    });

    // ===== [8-2] 인증번호 확인 =====
    $(".btn-cert-check").off("click").on("click", function () {
      const certEmail  = $email.val().trim();
      const certNumber = $(".cert-input").val().trim();

      if (!certEmail || !certNumber) { niceAlert("이메일과 인증번호를 입력해 주세요."); return; }
      if (certExpired) {
        $(".cert-feedback").removeClass("ok").addClass("error")
          .text("인증 유효시간이 만료되었습니다. ‘인증번호 보내기’를 눌러 재전송하세요.").show();
        return;
      }

      $.ajax({
        url: `${ctx}/rest/member/certCheck`,
        method: "post",
        data: { certEmail, certNumber },
        success(res) {
          const ok = (function parseOk(r){
            try{
              if (typeof r === "boolean") return r;
              if (typeof r === "number")  return r === 1;
              if (typeof r === "string"){
                const s=r.trim().toLowerCase();
                if (["true","1","ok","success"].includes(s)) return true;
                const j=JSON.parse(r); return !!(j?.result ?? j?.ok ?? j?.success);
              }
              if (r && typeof r === "object") return !!(r.result ?? r.ok ?? r.success);
            }catch(e){}
            return false;
          })(res);

          if (ok) {
            stopCertTimer(); $("#certTimer").hide();
            $email.prop("readonly", true);
            $(".cert-input").prop("readonly", true);
            $(".btn-cert-check").prop("disabled", true);
            setState($email, "success");
            $(".cert-feedback").removeClass("error").addClass("ok").text("인증번호 확인이 완료되었습니다.").show();
            $('input[name="certNumber"]').val(certNumber); // 컨트롤러 전달
          } else {
            setState($email, "fail");
            $(".cert-feedback").removeClass("ok").addClass("error").text("인증번호가 일치하지 않습니다.").show();
          }
        },
        error(){ niceAlert("인증 확인 요청 중 오류가 발생했습니다."); }
      });
    });
  })();



  /* ========== [9] 주소 검색 ========== */
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

  /* ========== [9.5] 프로필 이미지 미리보기 ========== */
  $(document).on("change", "input[name=attach]", function () {
    const f = this.files && this.files[0];
    const $img = $(".img-preview");
    if (!f) return;
    const reader = new FileReader();
    reader.onload = (e) => $img.attr("src", e.target.result);
    reader.readAsDataURL(f);
  });

  /* ========== [NEXT] 스텝별 선검사(Next 버튼) ========== */
  function pageOk($page){
    const step = Number($page.data("step"));
    let req = [];

    if (step === 1) {
      req = [
        $("[name=memberId]"),
        $("[name=memberPw]"),
        $("#password-check"),
        $("[name=memberNickname]")
      ];
      return req.every($el => $el.hasClass("success"));
    }
    else if (step === 2) {
      req = [
        $("[name=memberName]"),
        $("[name=memberBirth]"),
        $("[name=memberMbti]"),
        $("[name=memberContact]")
      ];
      return req.every($el => $el.hasClass("success"));
    }
    else if (step === 3) {
      // 이메일 단계도 1/2단계처럼 '통과 조건 충족 시에만 다음' 허용
      const $email = $("[name=memberEmail]");
      const verified =
        $email.hasClass("success") &&                 // 형식/중복 OK
        $email.prop("readonly") &&                    // 인증 성공 후 readonly
        $(".btn-cert-check").prop("disabled") &&      // 인증 버튼 비활성화
        (($('input[name="certNumber"]').val() || "").trim().length > 0); // 히든 필드 채움
      return verified;
    }

    // 그 외 스텝은 필요 시 추가
    return true;
  } // ← 반드시 함수 닫기

  // 문서 위임 핸들러(캡처 단계에서 next 클릭 차단)
  $(document).off("click.validateNext");
  document.addEventListener("click", async function(e){
    const btn = e.target.closest(".btn-next");
    if (!btn) return;

    const $page = $(btn).closest(".page");
    const step  = Number($page.data("step"));

    // Step4: 주소 3칸 all-or-none 즉시 검증
    if (step === 4) {
      const $post = $("[name=memberPostcode]");
      const $addr1= $("[name=memberAddress1]");
      const $addr2= $("[name=memberAddress2]");
      const filledCnt = [$post,$addr1,$addr2].filter($el => ($el.val()||"").trim().length>0).length;

      if (!(filledCnt === 0 || filledCnt === 3)) {
        e.preventDefault();
        e.stopImmediatePropagation();
        await niceAlert("주소는 ‘세 칸 모두 입력’하거나 ‘모두 비우기’만 허용됩니다.");
        return;
      }
    }

    // Step1~3: 형식검사 미통과 시 차단
    if (!pageOk($page)) {
      e.preventDefault();
      e.stopImmediatePropagation();
      await niceAlert("입력 형식을 다시 확인해주세요.");
      return;
    }
  }, true);

  /* ========== [10] 폼 제출 전 최종 검증 ========== */
  $(".check-form").off("submit").on("submit", async function () {
    // 1) 개별 blur 유도
    $([
      "[name=memberId]","[name=memberPw]","#password-check",
      "[name=memberNickname]","[name=memberName]","[name=memberBirth]",
      "[name=memberMbti]","[name=memberContact]","[name=memberEmail]"
    ].join(",")).each(function(){ $(this).triggerHandler("blur"); });

    // 2) 필수 성공 여부
    const mustBeSuccess = [
      $("[name=memberId]"), $("[name=memberPw]"), $("#password-check"),
      $("[name=memberNickname]"), $("[name=memberName]"), $("[name=memberBirth]"),
      $("[name=memberMbti]"), $("[name=memberContact]"), $("[name=memberEmail]")
    ];
    const allOk = mustBeSuccess.every($el => $el.hasClass("success"));
    if (!allOk) { await niceAlert("필수 정보를 정확히 입력해야 가입이 가능합니다."); return false; }

    // 3) 이메일 인증 성공 여부(= 읽기전용 + 체크버튼 비활성 + certNumber 히든 채움)
    const emailVerified = $("[name=memberEmail]").prop("readonly") && $(".btn-cert-check").prop("disabled");
    if (!emailVerified) { await niceAlert("이메일 인증을 완료해 주세요."); return false; }
    if (!$('input[name="certNumber"]').val()) { await niceAlert("인증번호를 확인 버튼으로 검증해 주세요."); return false; }

    // 4) 주소 3칸 all-or-none
    const $post=$("[name=memberPostcode]"), $addr1=$("[name=memberAddress1]"), $addr2=$("[name=memberAddress2]");
    const filledCnt = [$post,$addr1,$addr2].filter($el => ($el.val()||"").trim().length>0).length;
    if (!(filledCnt === 0 || filledCnt === 3)) {
      await niceAlert("주소는 ‘전부 비우거나 전부 채우기’로 입력해 주세요."); return false;
    }

    // 5) 아이디/닉네임/이메일 중복 최종 차단
    if ($("[name=memberId]").hasClass("fail2"))        { await niceAlert("이미 사용 중인 아이디입니다.");  return false; }
    if ($("[name=memberNickname]").hasClass("fail2"))  { await niceAlert("이미 사용 중인 닉네임입니다."); return false; }
    if ($("[name=memberEmail]").hasClass("fail2"))     { await niceAlert("이미 사용 중인 이메일입니다."); return false; }

    return true; // ✅ 제출 허용
  });

  /* ========== 비밀번호 보기/숨기기 ========== */
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

