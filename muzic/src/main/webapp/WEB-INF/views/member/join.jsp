	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	<!-- jQuery -->
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
	
	<!-- 다음 주소 API -->
	<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
		
<!-- CSS / JS -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
<script src="${pageContext.request.contextPath}/js/member-join.js"></script>

	
	<form action="/member/join" method="post" enctype="multipart/form-data" autocomplete="off" class="join-form">
	
	<div class="container w-600">
	
	    <!-- 1️⃣ 기본정보 -->
	    <div class="page" data-step="1">
	        <h2>1단계 : 기본정보 입력</h2>
	
	        <div class="cell">
	            <label>아이디 *</label>
	            <input type="text" name="memberId" class="field w-100" placeholder="영문+숫자 5~20자">
	            <div class="success-feedback">사용 가능한 아이디입니다.</div>
	            <div class="fail-feedback">형식이 올바르지 않습니다.</div>
	            <div class="fail2-feedback">이미 사용 중인 아이디입니다.</div>
	        </div>
	
	        <div class="cell">
	            <label>비밀번호 *</label>
	            <input type="password" name="memberPw" class="field w-100">
	            <div class="fail-feedback">영문, 숫자, 특수문자 포함 8~16자</div>
	        </div>
	
	        <div class="cell">
	            <label>비밀번호 확인 *</label>
	            <input type="password" id="password-check" class="field w-100">
	            <div class="fail-feedback">비밀번호가 일치하지 않습니다.</div>
	        </div>
	
	        <div class="cell">
	            <label>닉네임 *</label>
	            <input type="text" name="memberNickname" class="field w-100">
	            <div class="success-feedback">사용 가능한 닉네임입니다.</div>
	            <div class="fail-feedback">한글 또는 숫자 2~10자.</div>
	            <div class="fail2-feedback">이미 사용 중인 닉네임입니다.</div>
	        </div>
	
	        <div class="cell center">
	            <button type="button" class="btn btn-next">다음</button>
	        </div>
	    </div>
	
	    <!-- 2️⃣ 개인정보 -->
	    <div class="page" data-step="2" style="display:none;">
	        <h2>2단계 : 개인정보 입력</h2>
	
	        <div class="cell">
	            <label>이름 *</label>
	            <input type="text" name="memberName" class="field w-100">
	            <div class="fail-feedback">한글 2~6자만 입력 가능합니다.</div>
	        </div>
	
	        <div class="cell">
	            <label>생년월일 *</label>
	            <input type="date" name="memberBirth" class="field w-100">
	        </div>
	
	        <div class="cell">
	            <label>MBTI *</label>
	            <select name="memberMbti" class="field w-100">
	                <option value="">선택하세요</option>
	                <option>INTJ</option><option>INTP</option><option>ENTJ</option><option>ENTP</option>
	                <option>INFJ</option><option>INFP</option><option>ENFJ</option><option>ENFP</option>
	                <option>ISTJ</option><option>ISFJ</option><option>ESTJ</option><option>ESFJ</option>
	                <option>ISTP</option><option>ISFP</option><option>ESTP</option><option>ESFP</option>
	            </select>
	            <small>
	                <a href="https://www.16personalities.com/ko" target="_blank">MBTI를 모르시나요?</a>
	            </small>
	        </div>
	
	        <div class="cell">
	            <label>전화번호 *</label>
	            <input type="text" name="memberContact" class="field w-100" placeholder="010-1234-5678">
	            <div class="fail-feedback">010-XXXX-XXXX 형식으로 입력해주세요.</div>
	        </div>
	
	        <div class="cell flex-box between">
	            <button type="button" class="btn btn-prev">이전</button>
	            <button type="button" class="btn btn-next">다음</button>
	        </div>
	    </div>
	
	    <!-- 3️⃣ 이메일 인증 -->
	    <div class="page" data-step="3" style="display:none;">
	        <h2>3단계 : 이메일 인증</h2>
	
	        <div class="cell flex-box">
	            <input type="email" name="memberEmail" class="field w-100" placeholder="example@domain.com">
	            <button type="button" class="btn btn-neutral ms-10 btn-cert-send">
	                <i class="fa-solid fa-paper-plane"></i> 인증번호 보내기
	            </button>
	        </div>
	
	        <div class="cell cell-cert-input" style="display:none;">
	            <input type="text" class="field cert-input" placeholder="인증번호 입력 (6자리)">
	            <button type="button" class="btn btn-positive btn-cert-check">확인</button>
	        </div>
	
	        <div class="cell flex-box between">
	            <button type="button" class="btn btn-prev">이전</button>
	            <button type="button" class="btn btn-next">다음</button>
	        </div>
	    </div>
	
	    <!-- 4️⃣ 주소 입력 -->
	    <div class="page" data-step="4" style="display:none;">
	        <h2>4단계 : 주소 입력</h2>
	
	        <div class="cell">
	            <input type="text" name="memberPostcode" placeholder="우편번호" class="field" readonly>
	            <button type="button" class="btn btn-neutral btn-address-search">주소찾기</button>
	        </div>
	
	        <div class="cell">
	            <input type="text" name="memberAddress1" placeholder="기본주소" class="field w-100" readonly>
	        </div>
	
	        <div class="cell">
	            <input type="text" name="memberAddress2" placeholder="상세주소" class="field w-100">
	        </div>
	
	        <div class="cell flex-box between">
	            <button type="button" class="btn btn-prev">이전</button>
	            <button type="button" class="btn btn-next">다음</button>
	        </div>
	    </div>
	

<!-- 5️⃣ 프로필 업로드 -->
<div class="page" data-step="5" style="display:none;">
    <h2>5단계 : 프로필 이미지 업로드</h2>

    <div class="cell">
        <input type="file" name="attach" accept="image/*" class="field w-100">
        <img class="img-preview" src="${pageContext.request.contextPath}/images/error/no-image.png" width="200">
    </div>

    <div class="cell flex-box between">
        <button type="button" class="btn btn-prev">이전</button>
        <button type="submit" class="btn btn-positive">회원가입 완료</button>
    </div>
</div>
</form>
