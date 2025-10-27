<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원가입 | MUZIC</title>

  <!-- jQuery -->
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <!-- 다음 주소 API -->
  <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

  <!-- CSS -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/multipage/multipage.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/member-join.css">

  <!-- 전역 컨텍스트 경로 -->
  <script>
    window.ctx = '<c:out value="${pageContext.request.contextPath}"/>';
  </script>

  <!-- JS -->
  <script src="${pageContext.request.contextPath}/multipage/multipage.js"></script>
  <script src="${pageContext.request.contextPath}/js/member-join.js"></script>
</head>
<body>
  <div class="container w-600">
    <h1 class="center">MUZIC 회원가입</h1>

    <!-- 진행바 -->
    <div class="progressbar"><div class="guage"></div></div>

    <form action="${pageContext.request.contextPath}/member/join"
          method="post" enctype="multipart/form-data"
          autocomplete="off" class="join-form check-form"
          novalidate>

      <!-- 1️⃣ 기본정보 -->
      <div class="page" data-step="1">
        <h2>기본정보 입력</h2>

        <label>아이디 *</label>
        <input type="text" name="memberId" class="field" placeholder="영문 소문자 시작, 5~20자" required>
        <div class="success-feedback">좋은 아이디입니다!</div>
        <div class="fail-feedback">형식이 올바르지 않습니다.</div>
        <div class="fail2-feedback">이미 사용 중인 아이디입니다.</div>

        <label>비밀번호 *</label>
        <input type="password" name="memberPw" class="field" placeholder="영문+숫자+특수문자 8~16자" required>
        <div class="fail-feedback">비밀번호 형식이 올바르지 않습니다.</div>

        <label>비밀번호 확인 *</label>
        <input type="password" id="password-check" class="field" placeholder="비밀번호 재입력" required>
        <div class="fail-feedback">비밀번호가 일치하지 않습니다.</div>

        <label>닉네임 *</label>
        <input type="text" name="memberNickname" class="field" placeholder="한글 또는 숫자 2~10자" required>
        <div class="success-feedback">좋은 닉네임이에요!</div>
        <div class="fail-feedback">형식이 올바르지 않습니다.</div>
        <div class="fail2-feedback">이미 사용 중인 닉네임입니다.</div>

        <button type="button" class="btn btn-next">다음</button>
      </div>

      <!-- 2️⃣ 개인정보 -->
      <div class="page" data-step="2" style="display:none;">
        <h2>개인정보 입력</h2>

        <label>이름 *</label>
        <input type="text" name="memberName" class="field" placeholder="예: 김민우" required>
        <div class="fail-feedback">한글 2~6자만 입력 가능합니다.</div>

        <label>생년월일 *</label>
        <input type="date" name="memberBirth" class="field" required>
        <div class="fail-feedback">날짜 형식이 올바르지 않습니다.</div>
        <div class="fail2-feedback">미래 날짜는 입력할 수 없습니다.</div>

        <label>MBTI *</label>
        <select name="memberMbti" class="field" required>
          <option value="">선택하세요</option>
          <option>INTJ</option><option>INTP</option><option>ENTJ</option><option>ENTP</option>
          <option>INFJ</option><option>INFP</option><option>ENFJ</option><option>ENFP</option>
          <option>ISTJ</option><option>ISFJ</option><option>ESTJ</option><option>ESFJ</option>
          <option>ISTP</option><option>ISFP</option><option>ESTP</option><option>ESFP</option>
        </select>

        <label>전화번호 *</label>
        <input type="text" name="memberContact" class="field" placeholder="010-1234-5678" required>

        <div class="flex-box">
          <button type="button" class="btn btn-prev">이전</button>
          <button type="button" class="btn btn-next">다음</button>
        </div>
      </div>

      <!-- 3️⃣ 이메일 인증 -->
      <div class="page" data-step="3" style="display:none;">
        <h2>이메일 인증</h2>

        <div class="flex-box" style="gap:8px;">
          <input type="email" name="memberEmail" class="field flex-fill" placeholder="example@naver.com" required>
          <button type="button" class="btn btn-neutral btn-cert-send">
            <i class="fa-solid fa-paper-plane"></i> <span>인증번호 보내기</span>
          </button>
        </div>

        <div class="cell-cert-input" style="display:none;">
          <label>인증번호 입력</label>
          <div class="flex-box" style="gap:8px;">
            <input type="text" class="field cert-input" placeholder="6자리 숫자">
            <button type="button" class="btn btn-positive btn-cert-check">확인</button>
          </div>
        </div>

        <div class="flex-box">
          <button type="button" class="btn btn-prev">이전</button>
          <button type="button" class="btn btn-next">다음</button>
        </div>
      </div>

      <!-- 4️⃣ 주소 입력 -->
      <div class="page" data-step="4" style="display:none;">
        <h2>주소 입력</h2>

        <div class="flex-box" style="gap:8px;">
          <input type="text" name="memberPostcode" class="field" placeholder="우편번호" readonly>
          <button type="button" class="btn btn-neutral btn-address-search">주소 검색</button>
        </div>

        <input type="text" name="memberAddress1" class="field" placeholder="주소" readonly>
        <input type="text" name="memberAddress2" class="field" placeholder="상세주소">

        <div class="flex-box">
          <button type="button" class="btn btn-prev">이전</button>
          <button type="button" class="btn btn-next">다음</button>
        </div>
      </div>

      <!-- 5️⃣ 프로필 업로드 -->
      <div class="page" data-step="5" style="display:none;">
        <h2>프로필 이미지 업로드</h2>

        <label>프로필 이미지</label>
        <input type="file" name="attach" accept="image/*" class="field">
        <img class="img-preview" src="${pageContext.request.contextPath}/images/error/no-image.png" width="200" style="margin-top:10px; border-radius:8px;">

        <div class="flex-box" style="margin-top:20px;">
          <button type="button" class="btn btn-prev">이전</button>
          <button type="submit" class="btn btn-positive">회원가입 완료</button>
        </div>
      </div>

    </form>
  </div>
</body>
</html>
