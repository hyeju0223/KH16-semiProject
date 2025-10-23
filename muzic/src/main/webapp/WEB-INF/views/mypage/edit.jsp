<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/fmt" %>

    <h1>프로필 수정 페이지</h1>
    
    <h3>프로필 이미지</h3> 
    <form action="edit" method="post">
    닉네임<input type="text" name="memberNickname" value="${memberDto.memberNickname}"> <br>
    이름<input type="text" name="memberName" value="${memberDto.memberName}"><br>
    이메일<input type="email" name="memberEmail" value="${memberDto.memberEmail}"><br>
    <select name="memberMbti">
    	<option value="ESTJ" ${memberDto.memberMbti == 'ESTJ' ? 'selected' : ''}>ESTJ</option>
    	<option value="ESTP" ${memberDto.memberMbti == 'ESTP' ? 'selected' : ''}>ESTP</option>
    	<option value="ESFJ" ${memberDto.memberMbti == 'ESFJ' ? 'selected' : ''}>ESFJ</option>
    	<option value="ESFP" ${memberDto.memberMbti == 'ESFP' ? 'selected' : ''}>ESFP</option>
    	<option value="ENTJ" ${memberDto.memberMbti == 'ENTJ' ? 'selected' : ''}>ENTJ</option>
    	<option value="ENTP" ${memberDto.memberMbti == 'ESTP' ? 'selected' : ''}>ENTP</option>
    	<option value="ENFJ" ${memberDto.memberMbti == 'ENFJ' ? 'selected' : ''}>ENFJ</option>
    	<option value="ENFP" ${memberDto.memberMbti == 'ENFP' ? 'selected' : ''}>ENFP</option>
    	<option value="ISTJ" ${memberDto.memberMbti == 'ISTJ' ? 'selected' : ''}>ISTJ</option>
    	<option value="ISTP" ${memberDto.memberMbti == 'ISTP' ? 'selected' : ''}>ISTP</option>
    	<option value="ISFJ" ${memberDto.memberMbti == 'ISFJ' ? 'selected' : ''}>ISFJ</option>
    	<option value="ISFP" ${memberDto.memberMbti == 'ISFP' ? 'selected' : ''}>ISFP</option>
    	<option value="INTJ" ${memberDto.memberMbti == 'INTJ' ? 'selected' : ''}>INTJ</option>
    	<option value="INTP" ${memberDto.memberMbti == 'INTP' ? 'selected' : ''}>INTP</option>
    	<option value="INFJ" ${memberDto.memberMbti == 'INFJ' ? 'selected' : ''}>INFJ</option>
    	<option value="INFP" ${memberDto.memberMbti == 'INFP' ? 'selected' : ''}>INFP</option>
    </select><br>
    <!-- 카카오맵 API 추가 필요... -->
    생년월일<input type="text" name="memberBirth" value="${memberDto.memberBirth}"><br>
    전화번호<input type="text" name="memberContact" value="${memberDto.memberContact}"><br>
    주소<input type="text" name="memberPostcode" value="${memberDto.memberPostcode}"><br>
    상세주소<input type="text" name="memberAddress1" value="${memberDto.memberAddress1}"><br>
    상세주소2<input type="text" name="memberAddress2" value="${memberDto.memberAddress2}"><br>
    <button type="submit">수정</button>
    </form>
    
    