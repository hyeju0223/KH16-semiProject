<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>로그인 | MUZIC</title>

  <!-- CSS -->
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">

  <!-- jQuery -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

  <script>
    window.ctx = '${pageContext.request.contextPath}';

    $(function () {
      const $form = $("#loginForm");
      const $id   = $("input[name=memberId]");
      const $pw   = $("input[name=memberPw]");
      const $btn  = $(".btn-positive");
      const $msg  = $(".login-message");
      const $sub  = $(".login-submessage");
      const $caps = $(".caps-warning");

      // 초기 포커스
      $id.trigger("focus");

      function showErr(text) {
        $msg.text(text).css("color", "#d63031").show();
      }
      function clearMsgs() {
        $msg.hide().text("");
        $sub.hide().text("");
        $caps.hide().text("");
      }

      // CapsLock 경고
      $pw.on("keydown keyup", function (e) {
        if (typeof e.getModifierState === "function") {
          if (e.getModifierState("CapsLock")) $caps.text("Caps Lock이 켜져 있습니다.").show();
          else $caps.hide().text("");
        }
      });

      // Enter 제출
      $form.on("keypress", function (e) {
        if (e.which === 13) {
          e.preventDefault();
          $form.trigger("submit");
        }
      });

      // 제출(AJAX)
      $form.on("submit", function (e) {
        e.preventDefault();

        const memberId = $id.val().trim();
        const memberPw = $pw.val().trim();
        if (!memberId || !memberPw) {
          showErr("아이디와 비밀번호를 입력하세요.");
          return;
        }

        $btn.prop("disabled", true).text("로그인 중...");
        clearMsgs();

        $.ajax({
          url: ctx + "/rest/member/login",
          type: "POST",
          dataType: "json",
          data: { memberId, memberPw },

          // ★ 여기부터 success 콜백 교체본
          success: function (res) {
            if (res && res.ok) {
              // 성공 시 바로 이동
              location.href = ctx + "/";
              return;
            }

            // 실패 메시지 우선순위
            let text = "로그인에 실패했습니다.";
            if (res && res.msg) text = res.msg;
            else if (res && res.code === "bad_pw")  text = "비밀번호가 일치하지 않습니다.";
            else if (res && res.code === "no_user") text = "존재하지 않는 아이디입니다.";
            else if (res && res.code === "locked")  text = "5회 이상 실패로 계정이 잠겼습니다. 5분 후 다시 시도해주세요.";
            showErr(text);

            // ✅ 남은 시도는 '비밀번호 오답'일 때만 표시
            if (res && res.code === "bad_pw" && typeof res.remain === "number") {
              const remainNum = Number(res.remain);
              if (!Number.isNaN(remainNum) && remainNum >= 0) {
                $sub.text('남은 시도: ' + remainNum + '회').show();
              } else {
                $sub.hide().text('');
              }
            } else {
              $sub.hide().text('');
            }
          },
          // ★ 여기까지 success 콜백 교체

          error: function () {
            showErr("서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
          },
          complete: function () {
            $btn.prop("disabled", false).text("로그인");
          }
        });
      });
    });
  </script>
</head>

<body>
  <form id="loginForm" autocomplete="on">
    <div class="container login-container center">

      <!-- MUZIC 로고 -->
      <div class="cell center">
        <a href="${pageContext.request.contextPath}/" class="muzic-logo">MUZIC</a>
      </div>

      <!-- 아이디 -->
      <div class="cell center">
        <input type="text" name="memberId" placeholder="아이디" required class="field" autocomplete="username">
      </div>

      <!-- 비밀번호 -->
      <div class="cell center">
        <input type="password" name="memberPw" placeholder="비밀번호" required class="field" autocomplete="current-password">
      </div>

      <!-- 로그인 버튼 -->
      <div class="cell center">
        <button type="submit" class="btn-positive">로그인</button>
      </div>

      <!-- 메시지들 -->
      <div class="cell center">
        <div class="caps-warning"    style="margin-top:8px; font-size:13px; color:#ff9f1a; display:none;"></div>
        <div class="login-message"   style="margin-top:12px; font-size:14px; display:none;"></div>
        <div class="login-submessage"style="margin-top:6px;  font-size:13px; color:#777; display:none;"></div>
      </div>

      <!-- 하단 링크 -->
      <div class="cell center link-group">
        <a href="join">회원가입</a>
        <span class="divider">|</span>
        <a href="findMemberId">아이디 찾기</a>
        <span class="divider">|</span>
        <a href="findMemberPw">비밀번호 찾기</a>
      </div>

      <hr class="bottom-line">
    </div>
  </form>
</body>
</html>
