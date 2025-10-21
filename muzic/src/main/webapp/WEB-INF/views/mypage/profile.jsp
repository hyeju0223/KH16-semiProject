<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2>${memberDto.memberNickname} (${memberDto.memberMbti})</h2>

<hr>

이메일 | ${memberDto.memberEmail} <br>
전화번호 | ${memberDto.memberContact} <br>
주소 | ${memberDto.memberPostcode} <br>
${memberDto.memberAddress1} ${memberDto.memberAddress2}<br>
<hr>
<div>프로필 수정</div>
<div><a href="password?memberId=${memberDto.memberId}">비밀번호 변경 ></a></div>
<div><a href="calendar/?memberId=${memberDto.memberId}">나의 일정 ></a></div>
<hr>
<div>달력1 / 달력 2</div>
<hr>
<h4>내가 등록한 음원 TRACKS</h4>
<table border="1">
	<c:forEach var="musicDto" items="${musicList}">
	<thead>
		<th>음원 제목</th>
		<th>가수명</th>
		<th>앨범</th>
		<th>재생수</th>
		<th>좋아요</th>
		<th>등록시간</th>
	</thead>
	<tbody>
		<td>${musicDto.musicTitle}</td>
		<td>${musicDto.musicArtist}</td>
		<td>${musicDto.musicAlbum}</td>
		<td>${musicDto.musicPlay}</td>
		<td>${musicDto.musicLike}</td>
		<td>${musicDto.musicUtime}</td>
	</tbody>
	</c:forEach>
</table>


<a href="withDraw?memberId=${memberDto.memberId}">탈퇴하기 ></a>

</body>
</html>