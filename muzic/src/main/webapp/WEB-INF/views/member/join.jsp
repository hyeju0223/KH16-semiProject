<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입 | MUZIC</title>

<!-- ✅ jQuery -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<!-- ✅ 외부 CSS (경로 수정 완료) -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/member-join.css">

<!-- ✅ JS (순서 중요: member-join.js → multipage.js) -->
<script src="${pageContext.request.contextPath}/js/member-join.js"></script>
<script src="${pageContext.request.contextPath}/multipage/multipage.js"></script>

<!-- ✅ 다음 주소 API -->
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>

<body>
<div class="container">
  <h1>MUZIC 회원가입</h1>	
  
  <!-- ✅ 이 부분 추가 (progress bar) -->
  <div class="progressbar">
    <div class="guage" style="width:0%; height:6px; background:#111; border-radius:10px;"></div>
  </div>

  <form action="/member/join" method="post" enctype="multipart/form-data" autocomplete="off" class="check-form">

    <!-- ✅ 1단계: 기본정보 -->
    <div class="page" data-step="1">
      <h2>기본정보 입력</h2>

     <!-- ✅ 아이디 입력 -->
<label>아이디 *</label>
<input type="text" name="memberId" class="field" placeholder="영문 소문자 시작, 5~20자">
<div class="success-feedback">좋은 아이디입니다!</div>
<div class="fail-feedback">형식이 올바르지 않습니다.</div>
<div class="fail2-feedback">이미 사용 중인 아이디입니다.</div>


<!-- ✅ 비밀번호 입력 (이 부분 새로 추가) -->
<label>비밀번호 *</label>
<input type="password" name="memberPw" class="field" placeholder="영문+숫자+특수문자 8~16자">
<div class="fail-feedback">비밀번호 형식이 올바르지 않습니다.</div>

<!-- ✅ 비밀번호 확인 -->
<label>비밀번호 확인 *</label>
<input type="password" id="password-check" class="field" placeholder="비밀번호 재입력">
<div class="fail-feedback">비밀번호가 일치하지 않습니다.</div>


<!-- ✅ 닉네임 입력 -->
<label>닉네임 *</label>
<input type="text" name="memberNickname" class="field" placeholder="한글 또는 숫자 2~10자">
<div class="success-feedback">좋은 닉네임이에요!</div>
<div class="fail-feedback">형식이 올바르지 않습니다.</div>
<div class="fail2-feedback">이미 사용 중인 닉네임입니다.</div>

<button type="button" class="btn btn-next">다음</button>

    </div>

    <!-- ✅ 2단계: 개인정보 -->
    <div class="page" data-step="2" style="display:none;">
      <h2>개인정보 입력</h2>

      <label>이름 *</label>
      <input type="text" name="memberName" class="field" placeholder="예: 김민우">

      <label>생년월일 *</label>
      <input type="date" name="memberBirth" class="field">

      <label>MBTI *</label>
      <select name="memberMbti" class="field">
        <option value="">선택하세요</option>
        <option>INTJ</option><option>INTP</option><option>ENTJ</option><option>ENTP</option>
        <option>INFJ</option><option>INFP</option><option>ENFJ</option><option>ENFP</option>
        <option>ISTJ</option><option>ISFJ</option><option>ESTJ</option><option>ESFJ</option>
        <option>ISTP</option><option>ISFP</option><option>ESTP</option><option>ESFP</option>
      </select>

      <label>연락처 *</label>
      <input type="text" name="memberContact" class="field" placeholder="010-1234-5678">

      <div class="flex-box">
        <button type="button" class="btn btn-prev">이전</button>
        <button type="button" class="btn btn-next">다음</button>
      </div>
    </div>

    <!-- ✅ 3단계: 이메일 인증 -->
    <div class="page" data-step="3" style="display:none;">
      <h2>이메일 인증</h2>

      <div class="flex-box">
        <input type="email" name="memberEmail" class="field w-100" placeholder="example@domain.com">
        <button type="button" class="btn btn-neutral ms-10 btn-cert-send">인증번호 보내기</button>
      </div>

      <div class="cell cell-cert-input" style="display:none;">
        <input type="text" class="field cert-input" placeholder="인증번호 입력 (6자리)">
        <button type="button" class="btn btn-positive btn-cert-check">확인</button>
      </div>

      <div class="flex-box">
        <button type="button" class="btn btn-prev">이전</button>
        <button type="button" class="btn btn-next">다음</button>
      </div>
    </div>

    <!-- ✅ 4단계: 주소 입력 -->
    <div class="page" data-step="4" style="display:none;">
      <h2>주소 입력</h2>

      <div class="flex-box">
        <input type="text" name="memberPostcode" placeholder="우편번호" class="field" readonly>
        <button type="button" class="btn btn-neutral btn-address-search">주소찾기</button>
      </div>

      <input type="text" name="memberAddress1" placeholder="기본주소" class="field w-100" readonly>
      <input type="text" name="memberAddress2" placeholder="상세주소" class="field w-100">

      <div class="flex-box">
        <button type="button" class="btn btn-prev">이전</button>
        <button type="button" class="btn btn-next">다음</button>
      </div>
    </div>

    <!-- ✅ 5단계: 프로필 업로드 -->
    <div class="page" data-step="5" style="display:none;">
      <h2>프로필 이미지 업로드</h2>

      <input type="file" name="attach" accept="image/*" class="field w-100">
      <img class="img-preview" src="${pageContext.request.contextPath}/images/error/no-image.png" width="200">

      <div class="flex-box">
        <button type="button" class="btn btn-prev">이전</button>
        <button type="submit" class="btn btn-positive">회원가입 완료</button>
      </div>
    </div>

  </form>
</div>
</body>
</html>
